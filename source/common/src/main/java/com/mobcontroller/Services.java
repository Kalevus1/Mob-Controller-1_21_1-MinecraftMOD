package com.mobcontroller;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

/** Punto de acceso a las implementaciones que aporta cada loader. */
public final class Services {

	/** Red. Cada loader lo reemplaza en su arranque; por defecto no hace nada. */
	public static NetworkPlatform network = new NetworkPlatform() {
		@Override
		public void sendToClient(ServerPlayer player, CustomPacketPayload payload) {
		}

		@Override
		public void sendToServer(CustomPacketPayload payload) {
		}
	};

	private Services() {
	}
}
