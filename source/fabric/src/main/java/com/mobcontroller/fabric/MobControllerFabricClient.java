package com.mobcontroller.fabric;

import com.mobcontroller.Ver;
import com.mobcontroller.client.ClientBridge;
import com.mobcontroller.client.ClientCore;
import com.mobcontroller.client.PossessionHud;
import com.mobcontroller.net.PossessSyncS2CPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

/** Arranque de cliente de Fabric: teclas, HUD, receptor de cámara y tick de cliente. */
public class MobControllerFabricClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientCore.init();

		KeyBindingHelper.registerKeyBinding(ClientCore.UNPOSSESS_KEY);
		KeyBindingHelper.registerKeyBinding(ClientCore.POSE_KEY);

		ClientPlayNetworking.registerGlobalReceiver(PossessSyncS2CPayload.TYPE, (payload, context) ->
				ClientBridge.cameraSync.accept(payload.entityId()));

		if (Ver.LEVEL >= 10) {
			HudRenderCallback.EVENT.register(PossessionHud::render); // HUD de posesión: 1.0.0
		}
		ClientTickEvents.END_CLIENT_TICK.register(ClientCore::onClientTick);
	}
}
