package com.mobcontroller.neoforge;

import com.mobcontroller.MobController;
import com.mobcontroller.client.ClientCore;
import com.mobcontroller.client.PossessionHud;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

/** Cliente NeoForge: teclas, HUD y tick de cliente. */
@EventBusSubscriber(modid = MobController.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class MobControllerNeoForgeClient {

	private MobControllerNeoForgeClient() {
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		ClientCore.init();
		NeoForge.EVENT_BUS.addListener(MobControllerNeoForgeClient::onClientTick);
	}

	private static void onClientTick(ClientTickEvent.Post event) {
		ClientCore.onClientTick(Minecraft.getInstance());
	}

	@SubscribeEvent
	public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
		event.register(ClientCore.UNPOSSESS_KEY);
		event.register(ClientCore.POSE_KEY);
	}

	@SubscribeEvent
	public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
		event.registerAboveAll(
				ResourceLocation.fromNamespaceAndPath(MobController.MOD_ID, "possession_hud"),
				PossessionHud::render);
	}
}
