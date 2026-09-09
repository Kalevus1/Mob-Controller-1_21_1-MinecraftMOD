package com.mobcontroller.client.mixin;

import com.mobcontroller.client.ClientPossession;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * En 1ª persona, {@code LevelRenderer} se salta la entidad-cámara (por eso no ves tu cuerpo,
 * ni con Steve). La comparación es {@code entity == camera.getEntity()}. Mientras posees,
 * redirigimos esa primera llamada a {@code getEntity()} para que devuelva null: así el mob
 * poseído deja de contar como entidad-cámara y SÍ se renderiza (le ves las patas al mirar
 * abajo y los brazos al atacar).
 */
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	@Redirect(
			method = "renderLevel",
			at = @At(value = "INVOKE",
					target = "Lnet/minecraft/client/Camera;getEntity()Lnet/minecraft/world/entity/Entity;",
					ordinal = 0))
	private Entity mobcontroller$renderPossessedBody(Camera camera) {
		Entity real = camera.getEntity();
		if (ClientPossession.rendersBodyFor(real)) {
			return null;
		}
		return real;
	}
}
