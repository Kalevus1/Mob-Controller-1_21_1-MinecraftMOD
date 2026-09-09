package com.mobcontroller.neoforge;

import com.mobcontroller.MobController;
import com.mobcontroller.Services;
import com.mobcontroller.client.ClientBridge;
import com.mobcontroller.net.DriveInputC2SPayload;
import com.mobcontroller.net.MobAttackC2SPayload;
import com.mobcontroller.net.PoseC2SPayload;
import com.mobcontroller.net.PossessSyncS2CPayload;
import com.mobcontroller.net.UnpossessC2SPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/** Arranque para NeoForge: engancha red y eventos al núcleo común. */
@Mod(MobController.MOD_ID)
public class MobControllerNeoForge {

	public MobControllerNeoForge(IEventBus modEventBus) {
		Services.network = new NeoForgeNetwork();

		modEventBus.addListener(this::onRegisterPayloads);

		NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
		NeoForge.EVENT_BUS.addListener(this::onServerTick);
		NeoForge.EVENT_BUS.addListener(this::onEntityInteract);
		NeoForge.EVENT_BUS.addListener(this::onLoggedOut);

		MobController.LOGGER.info("Mob Controller (NeoForge) inicializado.");
	}

	private void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("1");
		registrar.playToClient(PossessSyncS2CPayload.TYPE, PossessSyncS2CPayload.CODEC,
				(payload, context) -> context.enqueueWork(
						() -> ClientBridge.cameraSync.accept(payload.entityId())));
		registrar.playToServer(UnpossessC2SPayload.TYPE, UnpossessC2SPayload.CODEC,
				(payload, context) -> context.enqueueWork(
						() -> MobController.handleUnpossess((ServerPlayer) context.player())));
		registrar.playToServer(DriveInputC2SPayload.TYPE, DriveInputC2SPayload.CODEC,
				(payload, context) -> context.enqueueWork(
						() -> MobController.handleDriveInput((ServerPlayer) context.player(), payload)));
		registrar.playToServer(MobAttackC2SPayload.TYPE, MobAttackC2SPayload.CODEC,
				(payload, context) -> context.enqueueWork(
						() -> MobController.handleAttack((ServerPlayer) context.player())));
		registrar.playToServer(PoseC2SPayload.TYPE, PoseC2SPayload.CODEC,
				(payload, context) -> context.enqueueWork(
						() -> MobController.handlePose((ServerPlayer) context.player(), payload)));
	}

	private void onRegisterCommands(RegisterCommandsEvent event) {
		MobController.registerCommands(event.getDispatcher());
	}

	private void onServerTick(ServerTickEvent.Post event) {
		MobController.onServerTick(event.getServer());
	}

	private void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
		if (!event.getLevel().isClientSide()
				&& MobController.onEntityInteract(event.getEntity(), event.getHand(), event.getTarget())) {
			event.setCanceled(true);
			event.setCancellationResult(InteractionResult.SUCCESS);
		}
	}

	private void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
		if (event.getEntity() instanceof ServerPlayer player) {
			MobController.onDisconnect(player);
		}
	}
}
