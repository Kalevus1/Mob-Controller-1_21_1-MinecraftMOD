package com.mobcontroller.fabric;

import com.mobcontroller.NetworkPlatform;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

/** Implementación de red para Fabric. */
public final class FabricNetwork implements NetworkPlatform {

	@Override
	public void sendToClient(ServerPlayer player, CustomPacketPayload payload) {
		ServerPlayNetworking.send(player, payload);
	}

	@Override
	public void sendToServer(CustomPacketPayload payload) {
		// Solo se invoca en el cliente (ClientPlayNetworking se resuelve de forma perezosa).
		ClientPlayNetworking.send(payload);
	}
}
