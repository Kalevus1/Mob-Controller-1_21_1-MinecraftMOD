package com.mobcontroller.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/**
 * HUD que aparece mientras posees: corazones (vida del mob), hambre (del jugador) y burbujas
 * de aire (del mob) para no ahogarte sin darte cuenta. Se dibuja arriba y al centro.
 */
public final class PossessionHud {

	private PossessionHud() {
	}

	private static ResourceLocation vanilla(String path) {
		return ResourceLocation.withDefaultNamespace(path);
	}

	private static final ResourceLocation HEART_BG = vanilla("hud/heart/container");
	private static final ResourceLocation HEART_FULL = vanilla("hud/heart/full");
	private static final ResourceLocation HEART_HALF = vanilla("hud/heart/half");
	private static final ResourceLocation FOOD_BG = vanilla("hud/food_empty");
	private static final ResourceLocation FOOD_FULL = vanilla("hud/food_full");
	private static final ResourceLocation FOOD_HALF = vanilla("hud/food_half");
	private static final ResourceLocation AIR = vanilla("hud/air");

	private static final int ICON = 9;
	private static final int STEP = 8;
	private static final int SLOTS = 10;
	private static final int BAR_W = (SLOTS - 1) * STEP + ICON;

	public static void render(GuiGraphics graphics, DeltaTracker delta) {
		if (!ClientPossession.isActive()) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null || mc.options.hideGui) {
			return;
		}
		Entity entity = ClientPossession.getPossessedEntity();
		if (!(entity instanceof LivingEntity mob)) {
			return;
		}

		int cx = mc.getWindow().getGuiScaledWidth() / 2;
		int y = 4;

		// Corazones: vida del mob (barra proporcional de 10 corazones, con medios).
		float maxHp = Math.max(1.0F, mob.getMaxHealth());
		int hpHalves = Mth.clamp(Math.round(mob.getHealth() / maxHp * 20.0F), 0, 20);
		drawHalfBar(graphics, cx, y, hpHalves, HEART_BG, HEART_FULL, HEART_HALF);
		y += 11;

		// Hambre: nivel de comida del jugador.
		int foodHalves = Mth.clamp(mc.player.getFoodData().getFoodLevel(), 0, 20);
		drawHalfBar(graphics, cx, y, foodHalves, FOOD_BG, FOOD_FULL, FOOD_HALF);
		y += 11;

		// Aire: burbujas del mob, solo cuando esta perdiendo aire (bajo el agua).
		int air = mob.getAirSupply();
		int maxAir = Math.max(1, mob.getMaxAirSupply());
		if (air < maxAir) {
			int bubbles = Mth.clamp(Mth.ceil(air * (double) SLOTS / maxAir), 0, SLOTS);
			int x0 = cx - BAR_W / 2;
			for (int i = 0; i < bubbles; i++) {
				graphics.blitSprite(AIR, x0 + i * STEP, y, ICON, ICON);
			}
			y += 11;
		}

		// Numero de vida real del mob (por si tiene muchos corazones).
		String text = "❤ " + (int) mob.getHealth() + " / " + (int) mob.getMaxHealth();
		int tx = cx - mc.font.width(text) / 2;
		graphics.drawString(mc.font, text, tx, y, 0xFFFFFF);
	}

	/** Dibuja 10 iconos (fondo + lleno/medio) centrados en cx segun un valor en medios (0..20). */
	private static void drawHalfBar(GuiGraphics graphics, int cx, int y, int halves,
									ResourceLocation background, ResourceLocation full, ResourceLocation half) {
		int x0 = cx - BAR_W / 2;
		for (int i = 0; i < SLOTS; i++) {
			int x = x0 + i * STEP;
			graphics.blitSprite(background, x, y, ICON, ICON);
			int value = halves - i * 2;
			if (value >= 2) {
				graphics.blitSprite(full, x, y, ICON, ICON);
			} else if (value == 1) {
				graphics.blitSprite(half, x, y, ICON, ICON);
			}
		}
	}
}
