package com.mobcontroller.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/** Acceso al sonido ambiente (protegido) de un mob, para usarlo como aviso al posar. */
@Mixin(Mob.class)
public interface MobAmbientSoundAccessor {

	@Invoker("getAmbientSound")
	SoundEvent mobcontroller$getAmbientSound();
}
