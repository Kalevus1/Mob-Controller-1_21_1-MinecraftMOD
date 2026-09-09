package com.mobcontroller.neoforge;

import com.mobcontroller.NetworkPlatform;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

/** Implementación de red para NeoForge. */
public final class NeoForgeNetwork implements NetworkPlatform {

	@Override
	public void sendToClient(ServerPlayer player, CustomPacketPayload payload) {
		PacketDistributor.sendToPlayer(player, payload);
	}

	@Override
	public void sendToServer(CustomPacketPayload payload) {
		PacketDistributor.sendToServer(payload);
	}
}
