package com.mobcontroller;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Comandos de posesion:
 *
 *   /possess                 -> posee el mob que estas mirando (raycast, 64 bloques)
 *   /possess <selector>      -> posee el mob indicado (por ejemplo @e[tag=oso,limit=1])
 *   /unpossess               -> sales de la posesion
 */
public final class PossessCommands {

	private PossessCommands() {
	}

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(
				Commands.literal("possess")
						.requires(source -> source.hasPermission(2))
						.executes(PossessCommands::possessLooking)
						.then(Commands.argument("mob", EntityArgument.entity())
								.executes(PossessCommands::possessSelector)));

		dispatcher.register(
				Commands.literal("unpossess")
						.requires(source -> source.hasPermission(2))
						.executes(PossessCommands::unpossess));
	}

	/** Posee el mob que el jugador esta mirando (raycast desde los ojos). */
	private static int possessLooking(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer player = ctx.getSource().getPlayerOrException();

		final double range = 64.0;
		Vec3 eye = player.getEyePosition();
		Vec3 view = player.getViewVector(1.0F);
		Vec3 end = eye.add(view.scale(range));
		AABB box = player.getBoundingBox().expandTowards(view.scale(range)).inflate(1.0);

		EntityHitResult hit = ProjectileUtil.getEntityHitResult(
				player, eye, end, box,
				e -> e instanceof Mob && e.isAlive(), range * range);

		if (hit == null || !(hit.getEntity() instanceof Mob mob)) {
			ctx.getSource().sendFailure(Component.literal(
					"No estas mirando a ningun mob (alcance " + (int) range + " bloques)."));
			return 0;
		}
		return doPossess(player, mob, ctx);
	}

	/** Posee el mob indicado por un selector de entidad. */
	private static int possessSelector(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer player = ctx.getSource().getPlayerOrException();
		Entity target = EntityArgument.getEntity(ctx, "mob");

		if (!(target instanceof Mob mob)) {
			ctx.getSource().sendFailure(Component.literal("El objetivo debe ser un mob."));
			return 0;
		}
		return doPossess(player, mob, ctx);
	}

	private static int doPossess(ServerPlayer player, Mob mob, CommandContext<CommandSourceStack> ctx) {
		if (PossessionManager.possess(player, mob)) {
			final String name = mob.getName().getString();
			ctx.getSource().sendSuccess(() -> Component.literal("Poseyendo a " + name), false);
			return 1;
		}
		ctx.getSource().sendFailure(Component.literal(
				"No se pudo poseer ese mob (debe estar vivo y en tu misma dimension)."));
		return 0;
	}

	private static int unpossess(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		ServerPlayer player = ctx.getSource().getPlayerOrException();
		PossessionManager.unpossess(player, true);
		return 1;
	}
}
