package com.mobcontroller.net;

import com.mobcontroller.MobController;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Cliente -> servidor. El jugador pide salir de la posesion (por ejemplo
 * al pulsar la tecla dedicada). No lleva datos.
 */
public record UnpossessC2SPayload() implements CustomPacketPayload {

	public static final UnpossessC2SPayload INSTANCE = new UnpossessC2SPayload();

	public static final Type<UnpossessC2SPayload> TYPE =
			new Type<>(ResourceLocation.fromNamespaceAndPath(MobController.MOD_ID, "unpossess"));

	public static final StreamCodec<RegistryFriendlyByteBuf, UnpossessC2SPayload> CODEC =
			StreamCodec.unit(INSTANCE);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
