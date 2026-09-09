package com.mobcontroller.net;

import com.mobcontroller.MobController;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Cliente -> servidor (Fase 3+). El jugador mantiene la tecla de "pose": el mob adopta una
 * postura agresiva/erguida ({@code setAggressive}). {@code active} = tecla pulsada.
 */
public record PoseC2SPayload(boolean active) implements CustomPacketPayload {

	public static final Type<PoseC2SPayload> TYPE =
			new Type<>(ResourceLocation.fromNamespaceAndPath(MobController.MOD_ID, "pose"));

	public static final StreamCodec<RegistryFriendlyByteBuf, PoseC2SPayload> CODEC =
			StreamCodec.composite(
					ByteBufCodecs.BOOL, PoseC2SPayload::active,
					PoseC2SPayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
