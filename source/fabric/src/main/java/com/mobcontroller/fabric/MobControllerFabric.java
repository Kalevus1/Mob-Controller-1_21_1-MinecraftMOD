package com.mobcontroller.fabric;

import com.mobcontroller.MobController;
import com.mobcontroller.Services;
import com.mobcontroller.net.DriveInputC2SPayload;
import com.mobcontroller.net.MobAttackC2SPayload;
import com.mobcontroller.net.PoseC2SPayload;
import com.mobcontroller.net.PossessSyncS2CPayload;
import com.mobcontroller.net.UnpossessC2SPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;

/** Arranque de Fabric: engancha la red y los eventos de Fabric al núcleo común. */
public class MobControllerFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		Services.network = new FabricNetwork();

		// Tipos de paquete (en ambos lados).
		PayloadTypeRegistry.playS2C().register(PossessSyncS2CPayload.TYPE, PossessSyncS2CPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(UnpossessC2SPayload.TYPE, UnpossessC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(DriveInputC2SPayload.TYPE, DriveInputC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(MobAttackC2SPayload.TYPE, MobAttackC2SPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(PoseC2SPayload.TYPE, PoseC2SPayload.CODEC);

		// Comandos, tick, interacción y desconexión.
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				MobController.registerCommands(dispatcher));
		ServerTickEvents.END_SERVER_TICK.register(MobController::onServerTick);
		UseEntityCallback.EVENT.register((player, level, hand, entity, hitResult) -> {
			if (!level.isClientSide && MobController.onEntityInteract(player, hand, entity)) {
				return InteractionResult.SUCCESS;
			}
			return InteractionResult.PASS;
		});
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
				MobController.onDisconnect(handler.player));

		// Receptores de paquetes C2S (en el hilo del servidor).
		ServerPlayNetworking.registerGlobalReceiver(UnpossessC2SPayload.TYPE, (payload, context) ->
				runOnServer(context.player(), () -> MobController.handleUnpossess(context.player())));
		ServerPlayNetworking.registerGlobalReceiver(DriveInputC2SPayload.TYPE, (payload, context) ->
				runOnServer(context.player(), () -> MobController.handleDriveInput(context.player(), payload)));
		ServerPlayNetworking.registerGlobalReceiver(MobAttackC2SPayload.TYPE, (payload, context) ->
				runOnServer(context.player(), () -> MobController.handleAttack(context.player())));
		ServerPlayNetworking.registerGlobalReceiver(PoseC2SPayload.TYPE, (payload, context) ->
				runOnServer(context.player(), () -> MobController.handlePose(context.player(), payload)));

		MobController.LOGGER.info("Mob Controller (Fabric) inicializado.");
	}

	private static void runOnServer(ServerPlayer player, Runnable task) {
		MinecraftServer server = player.getServer();
		if (server != null) {
			server.execute(task);
		}
	}
}
