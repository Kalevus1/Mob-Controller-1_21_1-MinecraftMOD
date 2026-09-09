package com.mobcontroller;

import com.mobcontroller.mixin.MobAmbientSoundAccessor;
import com.mobcontroller.net.DriveInputC2SPayload;
import com.mobcontroller.net.PossessSyncS2CPayload;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Estado de posesion por jugador y su mantenimiento (lado servidor).
 *
 * <p>El jugador ve por los ojos del mob (camara puesta por el cliente) y lo <b>conduce</b>.
 * Para que el mob siga cargado y el movimiento sea suave, el jugador <b>monta al mob de forma
 * invisible</b> (jinete), en vez de teletransportar su cuerpo encima cada tick. Cada tick el
 * servidor orienta y mueve el mob con el ultimo input recibido; su IA se apaga
 * ({@code setNoAi}) para que no pelee con los controles. Al salir, el jugador se desmonta y
 * queda <b>junto al animal</b>.
 */
public final class PossessionManager {

	private PossessionManager() {
	}

	/** Salto vertical inicial (igual que un jugador/mob normal). */
	private static final double JUMP_VELOCITY = 0.42;
	/** Gravedad por tick y rozamiento del aire (como un jugador). */
	private static final double GRAVITY = 0.08;
	private static final double AIR_DRAG = 0.98;
	/** Velocidad horizontal (bloques/tick) acotada a un rango cómodo de control. */
	private static final double MIN_SPEED = 0.12;
	private static final double MAX_SPEED = 0.30;
	/** Alcance del ataque del mob (bloques) y enfriamiento entre golpes (ticks). */
	private static final double ATTACK_RANGE = 4.0;
	private static final long ATTACK_COOLDOWN = 10L;

	/** Datos guardados en el momento de poseer, para restaurar al jugador/mob al salir. */
	private static final class State {
		final Mob mob;
		final boolean prevInvisible;
		final boolean prevInvulnerable;
		final boolean prevMobNoAi;
		/** Tipo de locomoción del mob, decidido al poseer. */
		final boolean flyer;
		final boolean swimmer;

		volatile DriveInputC2SPayload lastInput;
		long lastAttackTick;

		State(Mob mob, ServerPlayer player) {
			this.mob = mob;
			this.prevInvisible = player.isInvisible();
			this.prevInvulnerable = player.isInvulnerable();
			this.prevMobNoAi = mob.isNoAi();
			this.flyer = mob instanceof FlyingAnimal
					|| mob.getNavigation() instanceof FlyingPathNavigation;
			this.swimmer = mob instanceof WaterAnimal
					|| mob.getNavigation() instanceof WaterBoundPathNavigation
					|| mob.getNavigation() instanceof AmphibiousPathNavigation;
			this.lastInput = new DriveInputC2SPayload(0.0F, 0.0F, false, false,
					mob.getYRot(), mob.getXRot());
			this.lastAttackTick = 0L;
		}

		/** ¿El mob puede moverse libre en 3D ahora mismo? (volar siempre, o nadar en agua). */
		boolean freeFlight(Mob mob) {
			if (Ver.LEVEL < 4) {
				return false; // vuelo/nado llegó en 0.4.0
			}
			if (Ver.LEVEL >= 9) {
				return flyer || mob.isInWater(); // 0.9.x: nado para todos los mobs
			}
			return flyer || (swimmer && mob.isInWater()); // 0.4.0: solo voladores y acuáticos
		}

		String modeLabel() {
			if (flyer) {
				return "vuelo";
			}
			return swimmer ? "nado" : "terrestre";
		}
	}

	private static final Map<UUID, State> ACTIVE = new HashMap<>();

	public static boolean isPossessing(ServerPlayer player) {
		return ACTIVE.containsKey(player.getUUID());
	}

	/** Empieza a poseer el mob. Devuelve true si se activo. */
	public static boolean possess(ServerPlayer player, Mob mob) {
		if (mob == null || !mob.isAlive() || mob == player.getVehicle()) {
			return false;
		}
		// Solo mobs en la misma dimension que el jugador.
		if (mob.level() != player.level()) {
			return false;
		}
		// Si ya poseia otro mob, salir de aquel primero (sin aviso).
		if (isPossessing(player)) {
			unpossess(player, false);
		}

		State state = new State(mob, player);

		// Desde 0.2.0 se controla el mob: montarlo (invisible desde 0.5.0) y apagar su IA.
		// En 0.1.0 es solo POV: se ve por sus ojos sin conducirlo (mantiene su IA).
		if (Ver.LEVEL >= 2) {
			if (!player.startRiding(mob, true)) {
				return false;
			}
		}

		ACTIVE.put(player.getUUID(), state);
		mob.setPersistenceRequired();
		if (Ver.LEVEL >= 2) {
			mob.setNoAi(true);
			player.setInvisible(true);
			player.setInvulnerable(true);
		}

		// Avisar al cliente para poner la camara en los ojos del mob.
		Services.network.sendToClient(player, new PossessSyncS2CPayload(mob.getId()));

		String help = Ver.LEVEL >= 4
				? "  ·  WASD/raton · click ataca · R pose · V para salir"
				: (Ver.LEVEL >= 2 ? "  ·  WASD/raton · V para salir" : "  ·  V para salir");
		player.displayClientMessage(Component.literal(
				"Poseyendo a " + mob.getName().getString() + " (" + state.modeLabel() + ")" + help), true);
		return true;
	}

	/** Guarda el ultimo input de conduccion recibido del cliente. */
	public static void updateInput(ServerPlayer player, DriveInputC2SPayload input) {
		State state = ACTIVE.get(player.getUUID());
		if (state != null) {
			state.lastInput = input;
		}
	}

	/**
	 * Pose de "preparado" (tecla R). El oso se para en dos patas; el resto adopta postura
	 * agresiva. Al activarla suena un aviso (rugido/gruñido/sonido del mob).
	 */
	public static void setPose(ServerPlayer player, boolean active) {
		State state = ACTIVE.get(player.getUUID());
		if (state == null || state.mob == null || !state.mob.isAlive()) {
			return;
		}
		if (Ver.LEVEL < 4) {
			return; // la pose (R) llegó en 0.4.0
		}
		Mob mob = state.mob;
		if (Ver.LEVEL >= 8 && mob instanceof PolarBear bear) {
			bear.setStanding(active); // el oso se yergue: 0.8.0
		} else {
			mob.setAggressive(active);
		}
		if (active && Ver.LEVEL >= 8) {
			playPoseSound(mob); // sonidos de pose: 0.8.0
		}
	}

	/** Sonido de aviso al posar: rugido del oso, gruñido del lobo, o el sonido ambiente del mob. */
	private static void playPoseSound(Mob mob) {
		SoundEvent sound;
		if (mob instanceof PolarBear) {
			sound = SoundEvents.POLAR_BEAR_WARNING;
		} else if (mob instanceof Wolf) {
			sound = SoundEvents.WOLF_GROWL;
		} else {
			sound = ((MobAmbientSoundAccessor) mob).mobcontroller$getAmbientSound();
		}
		if (sound != null) {
			mob.level().playSound(null, mob.getX(), mob.getY(), mob.getZ(),
					sound, SoundSource.NEUTRAL, 1.2F, 1.0F);
		}
	}

	/** Termina la posesion y restaura al jugador (queda junto al animal). */
	public static boolean unpossess(ServerPlayer player, boolean notify) {
		State state = ACTIVE.remove(player.getUUID());
		if (state == null) {
			if (notify) {
				player.displayClientMessage(Component.literal("No estabas poseyendo ningun mob."), true);
			}
			return false;
		}

		restore(player, state);
		Services.network.sendToClient(player, new PossessSyncS2CPayload(PossessSyncS2CPayload.CLEAR));

		if (notify) {
			player.displayClientMessage(Component.literal("Has salido de la posesion."), true);
		}
		return true;
	}

	/** Desmonta al jugador (queda junto al mob) y restaura sus banderas y la IA del mob. */
	private static void restore(ServerPlayer player, State state) {
		if (player.isPassenger()) {
			player.stopRiding();
		}
		player.setInvisible(state.prevInvisible);
		player.setInvulnerable(state.prevInvulnerable);
		player.setDeltaMovement(Vec3.ZERO);
		player.fallDistance = 0.0F;
		if (state.mob != null && !state.mob.isRemoved()) {
			state.mob.setNoAi(state.prevMobNoAi);
			state.mob.setAggressive(false);
			if (state.mob instanceof PolarBear bear) {
				bear.setStanding(false);
			}
		}
	}

	/** Mantenimiento por tick del servidor: conducir el mob (el jugador va montado en el). */
	public static void tick(MinecraftServer server) {
		if (Ver.LEVEL < 2) {
			return; // 0.1.0: solo POV, no se conduce el mob
		}
		if (ACTIVE.isEmpty()) {
			return;
		}
		Iterator<Map.Entry<UUID, State>> it = ACTIVE.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<UUID, State> entry = it.next();
			ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());
			State state = entry.getValue();

			// Jugador desconectado: se limpia en onDisconnect; solo lo quitamos del mapa.
			if (player == null) {
				it.remove();
				continue;
			}

			Mob mob = state.mob;

			// El mob murio o dejo de existir: salir de la posesion.
			if (mob == null || !mob.isAlive() || mob.isRemoved()) {
				it.remove();
				restore(player, state);
				Services.network.sendToClient(player, new PossessSyncS2CPayload(PossessSyncS2CPayload.CLEAR));
				player.displayClientMessage(Component.literal("El mob poseido ya no existe."), true);
				continue;
			}

			// Si por lo que sea el jugador dejo de montar el mob, re-montarlo o salir.
			if (player.getVehicle() != mob) {
				if (!player.startRiding(mob, true)) {
					it.remove();
					restore(player, state);
					Services.network.sendToClient(player, new PossessSyncS2CPayload(PossessSyncS2CPayload.CLEAR));
					continue;
				}
			}

			player.setInvisible(true);
			drive(mob, state);
		}
	}

	/** Conduce el mob con fisica manual (IA apagada) segun el modo (terrestre o vuelo/nado). */
	private static void drive(Mob mob, State state) {
		DriveInputC2SPayload in = state.lastInput;

		if (!mob.isNoAi()) {
			mob.setNoAi(true);
		}
		mob.fallDistance = 0.0F;

		// Orientar al mob hacia donde mira el jugador.
		mob.setYRot(in.yaw());
		mob.setYBodyRot(in.yaw());
		mob.setYHeadRot(in.yaw());
		mob.setXRot(in.pitch());

		double speed = Mth.clamp(mob.getAttributeValue(Attributes.MOVEMENT_SPEED), MIN_SPEED, MAX_SPEED);

		if (state.freeFlight(mob)) {
			driveFly(mob, in, speed);
		} else {
			driveGround(mob, in, speed);
		}
		mob.hasImpulse = true;
	}

	/** Locomoción terrestre: control horizontal + gravedad + salto. */
	private static void driveGround(Mob mob, DriveInputC2SPayload in, double speed) {
		double yawRad = Math.toRadians(in.yaw());
		double sin = Math.sin(yawRad);
		double cos = Math.cos(yawRad);
		double wishX = -sin * in.forward() + cos * in.strafe();
		double wishZ = cos * in.forward() + sin * in.strafe();
		double len = Math.sqrt(wishX * wishX + wishZ * wishZ);
		if (len > 1.0) {
			wishX /= len;
			wishZ /= len;
		}
		double vx = wishX * speed;
		double vz = wishZ * speed;

		double vy;
		if (mob.onGround()) {
			vy = in.jump() ? JUMP_VELOCITY : -0.02;
		} else {
			vy = (mob.getDeltaMovement().y - GRAVITY) * AIR_DRAG;
		}

		Vec3 delta = new Vec3(vx, vy, vz);
		mob.move(MoverType.SELF, delta);

		double keptY = (mob.onGround() || mob.verticalCollision) ? 0.0 : vy;
		mob.setDeltaMovement(0.0, keptY, 0.0);
	}

	/**
	 * Locomoción libre en 3D (voladores, o acuáticos dentro del agua). Sin gravedad:
	 * avance en la dirección de la mirada, laterales horizontales, y espacio/agacharse
	 * para subir/bajar.
	 */
	private static void driveFly(Mob mob, DriveInputC2SPayload in, double speed) {
		double yawRad = Math.toRadians(in.yaw());
		double pitchRad = Math.toRadians(in.pitch());
		double sinYaw = Math.sin(yawRad);
		double cosYaw = Math.cos(yawRad);
		double cosPitch = Math.cos(pitchRad);
		double sinPitch = Math.sin(pitchRad);

		double lookX = -sinYaw * cosPitch;
		double lookY = -sinPitch;
		double lookZ = cosYaw * cosPitch;
		double leftX = cosYaw;
		double leftZ = sinYaw;
		double vertical = (in.jump() ? 1.0 : 0.0) - (in.sneak() ? 1.0 : 0.0);

		double wishX = lookX * in.forward() + leftX * in.strafe();
		double wishY = lookY * in.forward() + vertical;
		double wishZ = lookZ * in.forward() + leftZ * in.strafe();
		double len = Math.sqrt(wishX * wishX + wishY * wishY + wishZ * wishZ);
		if (len > 1.0) {
			wishX /= len;
			wishY /= len;
			wishZ /= len;
		}

		Vec3 delta = new Vec3(wishX * speed, wishY * speed, wishZ * speed);
		mob.move(MoverType.SELF, delta);
		mob.setDeltaMovement(Vec3.ZERO);
	}

	/** El mob golpea lo que tiene delante (raycast desde sus ojos). Llamado al pulsar atacar. */
	public static void attack(ServerPlayer player) {
		if (Ver.LEVEL < 4) {
			return; // atacar siendo el mob llegó en 0.4.0
		}
		State state = ACTIVE.get(player.getUUID());
		if (state == null) {
			return;
		}
		Mob mob = state.mob;
		if (mob == null || !mob.isAlive()) {
			return;
		}
		long now = mob.level().getGameTime();
		if (now - state.lastAttackTick < ATTACK_COOLDOWN) {
			return;
		}
		state.lastAttackTick = now;

		Vec3 eye = mob.getEyePosition();
		Vec3 view = mob.getViewVector(1.0F);
		Vec3 end = eye.add(view.scale(ATTACK_RANGE));
		AABB box = mob.getBoundingBox().expandTowards(view.scale(ATTACK_RANGE)).inflate(1.0);

		EntityHitResult hit = ProjectileUtil.getEntityHitResult(
				mob, eye, end, box,
				e -> e instanceof LivingEntity && e.isAlive() && e != player,
				ATTACK_RANGE * ATTACK_RANGE);

		mob.swing(InteractionHand.MAIN_HAND);
		// Sonido de reaccion al atacar (rugido/gruñido/voz del mob), pegue o no: llegó en 0.9.4.
		if (Ver.LEVEL >= 9) {
			playPoseSound(mob);
		}
		if (hit != null && hit.getEntity() instanceof LivingEntity target) {
			mob.doHurtTarget(target);
		}
	}

	/** Al desconectar, restaurar el estado del jugador para que no quede invisible guardado. */
	public static void onDisconnect(ServerPlayer player) {
		State state = ACTIVE.remove(player.getUUID());
		if (state != null) {
			restore(player, state);
		}
	}
}
