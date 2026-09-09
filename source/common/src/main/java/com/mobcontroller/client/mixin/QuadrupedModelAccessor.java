package com.mobcontroller.client.mixin;

import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Acceso a la cabeza y el torso de los cuadrúpedos (oso, vaca, cerdo…) para ocultarlos. */
@Mixin(QuadrupedModel.class)
public interface QuadrupedModelAccessor {

	@Accessor("head")
	ModelPart mobcontroller$head();

	@Accessor("body")
	ModelPart mobcontroller$body();
}
