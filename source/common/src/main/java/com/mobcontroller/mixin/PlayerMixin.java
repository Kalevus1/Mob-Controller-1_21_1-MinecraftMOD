package com.mobcontroller.mixin;

import com.mobcontroller.PossessionManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mientras posees un mob vas montado en el (invisible). En vanilla, agacharse hace que
 * {@code Player.rideTick()} te desmonte ({@code wantsToStopRiding()} = estar agachado).
 * Aqui usamos agacharse para ir despacio / bajar, asi que mientras posees devolvemos
 * {@code false} para no desmontar. La salida es con la tecla V o /unpossess.
 */
@Mixin(Player.class)
public class PlayerMixin {

	@Inject(method = "wantsToStopRiding", at = @At("HEAD"), cancellable = true)
	private void mobcontroller$keepRidingWhilePossessing(CallbackInfoReturnable<Boolean> cir) {
		if ((Object) this instanceof ServerPlayer serverPlayer
				&& PossessionManager.isPossessing(serverPlayer)) {
			cir.setReturnValue(false);
		}
	}
}
