package com.mobcontroller.net;

import com.mobcontroller.MobController;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Servidor -> cliente. Indica que entidad debe pasar a ser la camara
 * (su id) o, con {@link #CLEAR}, restaurar la camara del propio jugador.
 */
public record PossessSyncS2CPayload(int entityId) implements CustomPacketPayload {

	/** Valor de {@code entityId} que significa "salir de la posesion". */
	public static final int CLEAR = -1;

	public static final Type<PossessSyncS2CPayload> TYPE =
			new Type<>(ResourceLocation.fromNamespaceAndPath(MobController.MOD_ID, "possess_sync"));

	public static final StreamCodec<RegistryFriendlyByteBuf, PossessSyncS2CPayload> CODEC =
			StreamCodec.composite(
					ByteBufCodecs.VAR_INT, PossessSyncS2CPayload::entityId,
					PossessSyncS2CPayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
