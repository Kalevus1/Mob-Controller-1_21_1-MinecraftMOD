package com.mobcontroller.forge;

import com.mobcontroller.MobController;
import com.mobcontroller.client.ClientCore;
import com.mobcontroller.client.PossessionHud;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/** Cliente Forge: teclas, HUD y tick de cliente. */
@Mod.EventBusSubscriber(modid = MobController.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class MobControllerForgeClient {

	private MobControllerForgeClient() {
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		ClientCore.init();
		MinecraftForge.EVENT_BUS.addListener(MobControllerForgeClient::onClientTick);
	}

	private static void onClientTick(TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			ClientCore.onClientTick(Minecraft.getInstance());
		}
	}

	@SubscribeEvent
	public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
		event.register(ClientCore.UNPOSSESS_KEY);
		event.register(ClientCore.POSE_KEY);
	}

	@SubscribeEvent
	public static void onAddGuiLayers(AddGuiOverlayLayersEvent event) {
		event.getLayeredDraw().add(
				ResourceLocation.fromNamespaceAndPath(MobController.MOD_ID, "possession_hud"),
				PossessionHud::render);
	}
}
