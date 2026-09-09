package com.mobcontroller.client;

import com.mobcontroller.net.MobAttackC2SPayload;
import com.mobcontroller.net.PossessSyncS2CPayload;
import com.mobcontroller.Services;
import com.mobcontroller.Ver;
import net.minecraft.client.CameraType;
import com.mobcontroller.client.mixin.QuadrupedModelAccessor;
import com.mobcontroller.client.mixin.WolfModelAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.PolarBear;

/**
 * Estado de posesion en el cliente: coloca la camara en los ojos del mob
 * ({@link Minecraft#setCameraEntity}) y la restaura al salir.
 */
public final class ClientPossession {

	private ClientPossession() {
	}

	private static boolean active = false;
	private static CameraType previousCameraType = CameraType.FIRST_PERSON;
	private static Entity possessed;

	public static boolean isActive() {
		return active;
	}

	/** La entidad que estamos poseyendo (o null). */
	public static Entity getPossessedEntity() {
		return possessed;
	}

	/**
	 * ¿Estamos viendo a través de esta entidad (posesión en 1ª persona)? Lo usa la cámara
	 * para orientarse al instante con el ratón, sea el mob que sea.
	 */
	public static boolean isViewingThrough(Entity entity) {
		return active
				&& entity != null
				&& entity == possessed
				&& Minecraft.getInstance().options.getCameraType().isFirstPerson();
	}

	/**
	 * ¿Hay que renderizar el cuerpo de esta entidad en 1ª persona? Solo para <b>humanoides</b>:
	 * su ojo está en la cabeza (que ocultamos), así ves tu cuerpo/brazos/piernas sin ver el
	 * interior. En cuadrúpedos el ojo cae dentro del torso, así que NO renderizamos su cuerpo
	 * (el juego lo oculta como entidad-cámara) y la vista queda limpia desde los ojos.
	 */
	public static boolean rendersBodyFor(Entity entity) {
		if (!isViewingThrough(entity)) {
			return false;
		}
		EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
		if (!(renderer instanceof LivingEntityRenderer<?, ?> living)) {
			return false;
		}
		// Modelos donde sabemos ocultar la cabeza dejando el cuerpo/patas visibles.
		Object model = living.getModel();
		if (model instanceof HumanoidModel) {
			return Ver.LEVEL >= 6; // cuerpo humanoide visible: 0.6.x
		}
		if (model instanceof WolfModel || model instanceof QuadrupedModel) {
			return Ver.LEVEL >= 8; // patas de cuadrúpedos/lobo: 0.8.0
		}
		return false;
	}

	/**
	 * Cuánto adelantar la cámara (hacia donde mira el mob) para colocarla en la cabeza. En
	 * cuadrúpedos/lobo el "ojo" cae en el centro del torso; los adelantamos hasta la cabeza.
	 * En humanoides devuelve 0 (el ojo ya está en la cabeza).
	 */
	public static double cameraForwardOffset(Entity entity) {
		if (Ver.LEVEL < 9) {
			return 0.0; // la cámara calibrada a la cabeza (head.z) llegó en 0.9.x
		}
		EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
		if (!(renderer instanceof LivingEntityRenderer<?, ?> living)) {
			return 0.0;
		}
		Object model = living.getModel();
		ModelPart head = null;
		if (model instanceof QuadrupedModel) {
			head = ((QuadrupedModelAccessor) model).mobcontroller$head();
		} else if (model instanceof WolfModel) {
			head = ((WolfModelAccessor) model).mobcontroller$head();
		}
		if (head == null) {
			return 0.0;
		}
		// La cabeza (su pivote) esta en z negativa (frente del modelo, en pixeles/16). La
		// convertimos a bloques y sumamos un margen para llegar al hocico. Asi cada mob
		// (oso, vaca, cerdo, oveja, lobo...) queda calibrado por su propio modelo.
		double headForward = Math.max(0.0, -head.z / 16.0);
		return headForward + 0.35;
	}

	/** ¿Es un oso poseído que está de pie (pose con R)? Se filma distinto (ver patas/torso). */
	public static boolean isRearedBear(Entity entity) {
		return entity instanceof PolarBear bear && bear.isStanding();
	}

	/**
	 * Pide al servidor que el mob poseido ataque hacia delante. Se llama desde el mixin;
	 * la lógica de red vive aquí (clase normal) para no meter Fabric API dentro del mixin.
	 */
	public static void requestMobAttack() {
		Services.network.sendToServer(MobAttackC2SPayload.INSTANCE);
	}

	/** Aplica la orden recibida del servidor. */
	public static void apply(Minecraft client, int entityId) {
		if (entityId == PossessSyncS2CPayload.CLEAR) {
			clear(client);
			return;
		}
		if (client.level == null || client.player == null) {
			return;
		}
		Entity target = client.level.getEntity(entityId);
		if (target == null) {
			// El mob aun no esta cargado en el cliente; no se puede fijar la camara.
			return;
		}
		if (!active) {
			previousCameraType = client.options.getCameraType();
		}
		client.options.setCameraType(CameraType.FIRST_PERSON);
		client.setCameraEntity(target);
		possessed = target;
		active = true;
	}

	/** Restaura la camara del propio jugador. */
	public static void clear(Minecraft client) {
		if (client.player != null) {
			client.setCameraEntity(client.player);
		}
		if (active) {
			client.options.setCameraType(previousCameraType);
		}
		active = false;
		possessed = null;
	}
}
