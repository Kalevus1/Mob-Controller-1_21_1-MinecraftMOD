package com.mobcontroller.client.mixin;

import com.mobcontroller.Ver;
import com.mobcontroller.client.ClientPossession;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Ajustes de cámara al poseer (1ª persona):
 *  - Rotación: se fuerza a la mirada del jugador cada fotograma, así la vista gira al instante
 *    (sin esperar a que el servidor rote el mob).
 *  - Posición: en cuadrúpedos/lobo el "ojo" cae en el centro del torso, así que adelantamos la
 *    cámara hasta la cabeza (sensación de estar en la cabeza y no en el estómago).
 */
@Mixin(Camera.class)
public abstract class CameraMixin {

	@Shadow
	public abstract Entity getEntity();

	@Shadow
	public abstract Vec3 getPosition();

	@Shadow
	protected abstract void setRotation(float yaw, float pitch);

	@Shadow
	protected abstract void setPosition(double x, double y, double z);

	@Inject(method = "setup", at = @At("RETURN"))
	private void mobcontroller$possessView(CallbackInfo ci) {
		if (Ver.LEVEL < 7) {
			return; // cámara instantánea (giro sin desfase) llegó en 0.7.0
		}
		Entity entity = getEntity();
		if (entity == null || !ClientPossession.isViewingThrough(entity)) {
			return;
		}
		var player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}
		// Giro instantáneo con el ratón.
		setRotation(player.getYRot(), player.getXRot());

		double yawRad = Math.toRadians(player.getYRot());
		double forwardX = -Math.sin(yawRad);
		double forwardZ = Math.cos(yawRad);

		// Oso de pie (pose con R): subir la cámara al pecho alzado y adelantar poco, para ver
		// las patas delanteras levantadas y algo de torso (el modelo se muestra entero).
		if (Ver.LEVEL >= 9 && ClientPossession.isRearedBear(entity)) {
			Vec3 p = getPosition();
			setPosition(p.x + forwardX * 0.3, p.y + 1.0, p.z + forwardZ * 0.3);
			return;
		}

		// Adelantar la cámara a la cabeza (solo cuadrúpedos/lobo).
		double offset = ClientPossession.cameraForwardOffset(entity);
		if (offset > 0.0) {
			Vec3 p = getPosition();
			setPosition(p.x + forwardX * offset, p.y, p.z + forwardZ * offset);
		}
	}
}
