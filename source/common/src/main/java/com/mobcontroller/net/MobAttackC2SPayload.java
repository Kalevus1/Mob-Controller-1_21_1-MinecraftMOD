package com.mobcontroller.net;

import com.mobcontroller.MobController;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Cliente -> servidor (Fase 3). El jugador pulsa atacar mientras posee: el servidor hace
 * que el mob golpee lo que tiene delante (raycast desde sus ojos). No lleva datos.
 */
public record MobAttackC2SPayload() implements CustomPacketPayload {

	public static final MobAttackC2SPayload INSTANCE = new MobAttackC2SPayload();

	public static final Type<MobAttackC2SPayload> TYPE =
			new Type<>(ResourceLocation.fromNamespaceAndPath(MobController.MOD_ID, "mob_attack"));

	public static final StreamCodec<RegistryFriendlyByteBuf, MobAttackC2SPayload> CODEC =
			StreamCodec.unit(INSTANCE);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
