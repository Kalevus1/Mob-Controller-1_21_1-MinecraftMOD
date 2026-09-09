package com.mobcontroller.forge;

import com.mobcontroller.MobController;
import com.mobcontroller.NetworkPlatform;
import com.mobcontroller.client.ClientBridge;
import com.mobcontroller.net.DriveInputC2SPayload;
import com.mobcontroller.net.MobAttackC2SPayload;
import com.mobcontroller.net.PoseC2SPayload;
import com.mobcontroller.net.PossessSyncS2CPayload;
import com.mobcontroller.net.UnpossessC2SPayload;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;

/** Implementación de red para Forge (canal de payloads + PacketDistributor). */
public final class ForgeNetwork implements NetworkPlatform {

	/** Registra el canal y sus payloads. Se llama una vez al arrancar. */
	public static void register() {
		ChannelBuilder.named(ResourceLocation.fromNamespaceAndPath(MobController.MOD_ID, "main"))
				.networkProtocolVersion(1)
				.optional()
				.payloadChannel()
				.play()
				.clientbound()
				.add(PossessSyncS2CPayload.TYPE, PossessSyncS2CPayload.CODEC,
						(msg, ctx) -> ctx.enqueueWork(() -> ClientBridge.cameraSync.accept(msg.entityId())))
				.serverbound()
				.add(UnpossessC2SPayload.TYPE, UnpossessC2SPayload.CODEC,
						(msg, ctx) -> ctx.enqueueWork(() -> MobController.handleUnpossess(ctx.getSender())))
				.add(DriveInputC2SPayload.TYPE, DriveInputC2SPayload.CODEC,
						(msg, ctx) -> ctx.enqueueWork(() -> MobController.handleDriveInput(ctx.getSender(), msg)))
				.add(MobAttackC2SPayload.TYPE, MobAttackC2SPayload.CODEC,
						(msg, ctx) -> ctx.enqueueWork(() -> MobController.handleAttack(ctx.getSender())))
				.add(PoseC2SPayload.TYPE, PoseC2SPayload.CODEC,
						(msg, ctx) -> ctx.enqueueWork(() -> MobController.handlePose(ctx.getSender(), msg)))
				.build();
	}

	@Override
	public void sendToClient(ServerPlayer player, CustomPacketPayload payload) {
		PacketDistributor.PLAYER.with(player).send(new ClientboundCustomPayloadPacket(payload));
	}

	@Override
	public void sendToServer(CustomPacketPayload payload) {
		PacketDistributor.SERVER.noArg().send(new ServerboundCustomPayloadPacket(payload));
	}
}
