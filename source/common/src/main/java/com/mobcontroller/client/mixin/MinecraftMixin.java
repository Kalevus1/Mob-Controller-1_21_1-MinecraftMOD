package com.mobcontroller.client.mixin;

import com.mobcontroller.client.ClientPossession;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mientras posees un mob, tu cuerpo invisible esta sobre el mob, asi que un ataque/uso
 * golpearia a tu propio jugador (el servidor te expulsa por "atacar entidad invalida").
 * Cancelamos el ataque/uso vanilla; el click izquierdo se reenvia como ataque DEL mob.
 */
@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
	private void mobcontroller$blockAttack(CallbackInfoReturnable<Boolean> cir) {
		if (ClientPossession.isActive()) {
			// Click izquierdo => el mob golpea lo que tiene delante.
			ClientPossession.requestMobAttack();
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
	private void mobcontroller$blockContinueAttack(boolean bl, CallbackInfo ci) {
		if (ClientPossession.isActive()) {
			ci.cancel();
		}
	}

	@Inject(method = "startUseItem", at = @At("HEAD"), cancellable = true)
	private void mobcontroller$blockUseItem(CallbackInfo ci) {
		if (ClientPossession.isActive()) {
			ci.cancel();
		}
	}
}
