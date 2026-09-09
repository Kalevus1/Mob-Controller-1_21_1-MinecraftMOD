package com.mobcontroller.net;

import com.mobcontroller.MobController;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Cliente -> servidor (Fase 2). El input de conduccion del jugador que el servidor
 * aplica al mob poseido cada tick:
 *
 * <ul>
 *   <li>{@code forward} / {@code strafe}: impulso de avance y lateral (-1..1), de WASD.</li>
 *   <li>{@code jump} / {@code sneak}: teclas de saltar y agacharse.</li>
 *   <li>{@code yaw} / {@code pitch}: hacia donde mira el jugador (para orientar al mob).</li>
 * </ul>
 */
public record DriveInputC2SPayload(float forward, float strafe, boolean jump, boolean sneak,
                                   float yaw, float pitch) implements CustomPacketPayload {

	public static final Type<DriveInputC2SPayload> TYPE =
			new Type<>(ResourceLocation.fromNamespaceAndPath(MobController.MOD_ID, "drive_input"));

	public static final StreamCodec<RegistryFriendlyByteBuf, DriveInputC2SPayload> CODEC =
			StreamCodec.composite(
					ByteBufCodecs.FLOAT, DriveInputC2SPayload::forward,
					ByteBufCodecs.FLOAT, DriveInputC2SPayload::strafe,
					ByteBufCodecs.BOOL, DriveInputC2SPayload::jump,
					ByteBufCodecs.BOOL, DriveInputC2SPayload::sneak,
					ByteBufCodecs.FLOAT, DriveInputC2SPayload::yaw,
					ByteBufCodecs.FLOAT, DriveInputC2SPayload::pitch,
					DriveInputC2SPayload::new);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
