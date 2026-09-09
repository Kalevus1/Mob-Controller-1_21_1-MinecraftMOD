package com.mobcontroller;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

/**
 * Envío de paquetes abstracto. Cada loader la implementa con su API de red (Fabric,
 * NeoForge, Forge) y la registra en {@link Services#network}.
 */
public interface NetworkPlatform {

	/** Servidor -> un cliente concreto. */
	void sendToClient(ServerPlayer player, CustomPacketPayload payload);

	/** Cliente -> servidor. */
	void sendToServer(CustomPacketPayload payload);
}
