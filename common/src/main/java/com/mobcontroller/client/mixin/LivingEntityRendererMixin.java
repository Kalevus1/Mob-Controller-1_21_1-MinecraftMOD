package com.mobcontroller.client.mixin;

import com.mobcontroller.Ver;
import com.mobcontroller.client.ClientPossession;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Al renderizar el cuerpo del mob poseído en 1ª persona, la cabeza taparía la cámara (está
 * en los ojos). Para modelos humanoides ocultamos la cabeza durante ese render, así ves el
 * cuerpo/brazos/piernas sin que la cara bloquee la vista. Otros modelos se renderizan enteros.
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

	private static final String RENDER =
			"render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;"
					+ "Lnet/minecraft/client/renderer/MultiBufferSource;I)V";

	@Inject(method = RENDER, at = @At("HEAD"))
	private void mobcontroller$hideHeadStart(LivingEntity entity, float yaw, float partial,
											 PoseStack pose, MultiBufferSource buffer, int light,
											 CallbackInfo ci) {
		toggleHead(entity, false);
	}

	@Inject(method = RENDER, at = @At("RETURN"))
	private void mobcontroller$hideHeadEnd(LivingEntity entity, float yaw, float partial,
										   PoseStack pose, MultiBufferSource buffer, int light,
										   CallbackInfo ci) {
		toggleHead(entity, true);
	}

	private void toggleHead(LivingEntity entity, boolean visible) {
		if (!ClientPossession.rendersBodyFor(entity)) {
			return;
		}
		// Con el oso de pie la cabeza queda arriba (no tapa), asi que la mostramos (0.9.4).
		boolean show = visible || (Ver.LEVEL >= 9 && ClientPossession.isRearedBear(entity));
		EntityModel<?> model = ((LivingEntityRenderer<?, ?>) (Object) this).getModel();
		if (model instanceof HumanoidModel<?> humanoid) {
			// Humanoides: ocultar cabeza + capa exterior (el ojo esta en la cabeza).
			humanoid.head.visible = show;
			humanoid.hat.visible = show;
		} else if (model instanceof WolfModel<?> wolf) {
			// Lobo: ocultar cabeza + torso (rodean la camara) y dejar las patas visibles.
			WolfModelAccessor a = (WolfModelAccessor) wolf;
			a.mobcontroller$head().visible = show;
			a.mobcontroller$realHead().visible = show;
			a.mobcontroller$body().visible = show;
			a.mobcontroller$upperBody().visible = show;
		} else if (model instanceof QuadrupedModel<?> quadruped) {
			// Cuadrupedos (oso, vaca, cerdo...): ocultar cabeza + torso, dejar las patas.
			QuadrupedModelAccessor a = (QuadrupedModelAccessor) quadruped;
			a.mobcontroller$head().visible = show;
			a.mobcontroller$body().visible = show;
		}
	}
}
