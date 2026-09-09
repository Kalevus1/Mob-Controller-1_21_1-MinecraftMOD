package com.mobcontroller.forge;

import com.mobcontroller.MobController;
import com.mobcontroller.Services;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Arranque para Forge: red y eventos de Forge enganchados al núcleo común. */
@Mod(MobController.MOD_ID)
public class MobControllerForge {

	public MobControllerForge() {
		Services.network = new ForgeNetwork();
		ForgeNetwork.register();
		MinecraftForge.EVENT_BUS.register(this);
		MobController.LOGGER.info("Mob Controller (Forge) inicializado.");
	}

	@SubscribeEvent
	public void onRegisterCommands(RegisterCommandsEvent event) {
		MobController.registerCommands(event.getDispatcher());
	}

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			MobController.onServerTick(event.getServer());
		}
	}

	@SubscribeEvent
	public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
		if (!event.getLevel().isClientSide()
				&& MobController.onEntityInteract(event.getEntity(), event.getHand(), event.getTarget())) {
			event.setCanceled(true);
			event.setCancellationResult(InteractionResult.SUCCESS);
		}
	}

	@SubscribeEvent
	public void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			MobController.onDisconnect(player);
		}
	}
}
