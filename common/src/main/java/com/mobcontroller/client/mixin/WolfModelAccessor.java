package com.mobcontroller.client.mixin;

import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Acceso a las partes de cabeza/torso del lobo para ocultarlas dejando las patas. */
@Mixin(WolfModel.class)
public interface WolfModelAccessor {

	@Accessor("head")
	ModelPart mobcontroller$head();

	@Accessor("realHead")
	ModelPart mobcontroller$realHead();

	@Accessor("body")
	ModelPart mobcontroller$body();

	@Accessor("upperBody")
	ModelPart mobcontroller$upperBody();
}
