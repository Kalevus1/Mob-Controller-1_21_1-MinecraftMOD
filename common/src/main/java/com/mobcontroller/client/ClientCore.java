package com.mobcontroller.client;

import com.mobcontroller.Services;
import com.mobcontroller.Ver;
import com.mobcontroller.net.DriveInputC2SPayload;
import com.mobcontroller.net.PoseC2SPayload;
import com.mobcontroller.net.UnpossessC2SPayload;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.lwjgl.glfw.GLFW;

/**
 * Lógica de CLIENTE común (independiente del loader). Las teclas se crean aquí y las registra
 * cada loader; {@link #onClientTick} se llama al final de cada tick de cliente.
 */
public final class ClientCore {

	public static final KeyMapping UNPOSSESS_KEY = new KeyMapping(
			"key.mobcontroller.unpossess", GLFW.GLFW_KEY_V, "key.categories.mobcontroller");
	public static final KeyMapping POSE_KEY = new KeyMapping(
			"key.mobcontroller.pose", GLFW.GLFW_KEY_R, "key.categories.mobcontroller");

	private static boolean lastPoseSent;

	private ClientCore() {
	}

	/** El arranque de cliente de cada loader llama a esto una vez. */
	public static void init() {
		ClientBridge.cameraSync = entityId -> {
			Minecraft mc = Minecraft.getInstance();
			mc.execute(() -> ClientPossession.apply(mc, entityId));
		};
	}

	/** Llamar al final de cada tick de cliente. */
	public static void onClientTick(Minecraft client) {
		while (UNPOSSESS_KEY.consumeClick()) {
			if (ClientPossession.isActive()) {
				Services.network.sendToServer(UnpossessC2SPayload.INSTANCE);
			}
		}

		boolean active = ClientPossession.isActive() && client.player != null;

		if (active) {
			var input = client.player.input;
			float yaw = client.player.getYRot();
			float pitch = client.player.getXRot();
			Services.network.sendToServer(new DriveInputC2SPayload(
					input.forwardImpulse, input.leftImpulse,
					input.jumping, input.shiftKeyDown, yaw, pitch));

			// Alinear el cuerpo del mob AL INSTANTE con la mirada (evita el desfase de red): 0.9.1.
			Entity possessed = Ver.LEVEL >= 9 ? ClientPossession.getPossessedEntity() : null;
			if (possessed instanceof LivingEntity mob) {
				mob.setYRot(yaw);
				mob.yRotO = yaw;
				mob.setXRot(pitch);
				mob.xRotO = pitch;
				mob.yBodyRot = yaw;
				mob.yBodyRotO = yaw;
				mob.yHeadRot = yaw;
				mob.yHeadRotO = yaw;
			}
		}

		boolean poseNow = active && POSE_KEY.isDown();
		if (poseNow != lastPoseSent) {
			if (active) {
				Services.network.sendToServer(new PoseC2SPayload(poseNow));
			}
			lastPoseSent = poseNow;
		}
	}
}
