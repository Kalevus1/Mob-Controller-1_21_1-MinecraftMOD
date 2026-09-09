package com.mobcontroller;

import com.mobcontroller.net.DriveInputC2SPayload;
import com.mobcontroller.net.PoseC2SPayload;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Núcleo COMÚN de Mob Controller (independiente del loader). Cada loader (Fabric, NeoForge,
 * Forge) engancha sus eventos y su red a estos métodos. La red se envía a través de
 * {@link Services#network}.
 */
public final class MobController {

	public static final String MOD_ID = "mobcontroller";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private MobController() {
	}

	public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
		PossessCommands.register(dispatcher);
	}

	public static void onServerTick(MinecraftServer server) {
		PossessionManager.tick(server);
	}

	public static void onDisconnect(ServerPlayer player) {
		PossessionManager.onDisconnect(player);
	}

	/** Agacharse + click derecho sobre un mob = poseerlo. Devuelve true si se consumió. */
	public static boolean onEntityInteract(Player player, InteractionHand hand, Entity entity) {
		if (hand != InteractionHand.MAIN_HAND) {
			return false;
		}
		if (player.isShiftKeyDown()
				&& player instanceof ServerPlayer serverPlayer
				&& serverPlayer.hasPermissions(2)
				&& entity instanceof Mob mob) {
			return PossessionManager.possess(serverPlayer, mob);
		}
		return false;
	}

	// --- Handlers de paquetes C2S (los receptores de cada loader los llaman en el hilo servidor). ---

	public static void handleUnpossess(ServerPlayer player) {
		PossessionManager.unpossess(player, true);
	}

	public static void handleDriveInput(ServerPlayer player, DriveInputC2SPayload payload) {
		PossessionManager.updateInput(player, payload);
	}

	public static void handleAttack(ServerPlayer player) {
		PossessionManager.attack(player);
	}

	public static void handlePose(ServerPlayer player, PoseC2SPayload payload) {
		PossessionManager.setPose(player, payload.active());
	}
}
