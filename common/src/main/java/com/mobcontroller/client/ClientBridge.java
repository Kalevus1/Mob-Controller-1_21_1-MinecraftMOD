package com.mobcontroller.client;

import java.util.function.IntConsumer;

/**
 * Puente hacia la lógica de cliente, para que el código común/servidor NO referencie clases
 * de cliente (net.minecraft.client). El arranque de cliente de cada loader rellena estos
 * campos; en el servidor quedan como no-op.
 */
public final class ClientBridge {

	/** Aplica la orden de cámara recibida del servidor (id del mob, o -1 para salir). */
	public static IntConsumer cameraSync = id -> {
	};

	private ClientBridge() {
	}
}
