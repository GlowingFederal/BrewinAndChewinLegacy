package com.brewinandchewin.legacy.integration;

import codechicken.nei.PositionedStack;
import com.brewinandchewin.legacy.AgingWheelBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import vectorwing.farmersdelight.legacy.Stacks;
import vectorwing.farmersdelight.legacy.integration.nei.FDRecipeHandler;

public final class RipeningHandler extends FDRecipeHandler {
    public static final String UID = "brewinandchewin.ripening";
    private static final int INPUT_X = 30, OUTPUT_X = 120, SLOT_Y = 17;

    @Override public String getOverlayIdentifier() { return UID; }
    @Override public String getRecipeName() { return I18n.format(UID + ".title"); }
    @Override public String getGuiTexture() { return "brewinandchewin:textures/gui/keg.png"; }

    @Override public void loadCraftingRecipes(String id, Object... results) {
        if (UID.equals(id) || "all".equals(id)) {
            for (AgingWheelBlock wheel : AgingWheelBlock.registeredWheels()) arecipes.add(new Cached(wheel));
        } else {
            super.loadCraftingRecipes(id, results);
        }
    }

    @Override public void loadCraftingRecipes(ItemStack result) {
        for (AgingWheelBlock wheel : AgingWheelBlock.registeredWheels()) {
            if (Stacks.same(new ItemStack(wheel.getRipeBlock()), result)) arecipes.add(new Cached(wheel));
        }
    }

    @Override public void loadUsageRecipes(ItemStack ingredient) {
        for (AgingWheelBlock wheel : AgingWheelBlock.registeredWheels()) {
            if (Stacks.same(new ItemStack(wheel), ingredient)) arecipes.add(new Cached(wheel));
        }
    }

    @Override public void drawBackground(int index) {
        panel();
        slot(INPUT_X, SLOT_Y);
        slot(OUTPUT_X, SLOT_Y);
        arrow(57, 18, 52);
    }

    @Override public void drawExtras(int index) {
        centered(4, "place");
        centered(36, "stages");
        centered(46, "chance", Math.round(AgingWheelBlock.ADVANCE_CHANCE * 100));
        centered(56, "ticking");
    }

    private static void centered(int y, String key, Object... arguments) {
        String line = I18n.format(UID + "." + key, arguments);
        label(line, (166 - Minecraft.getMinecraft().fontRenderer.getStringWidth(line)) / 2, y);
    }

    private final class Cached extends CachedRecipe {
        private final PositionedStack input;
        private final PositionedStack output;

        Cached(AgingWheelBlock wheel) {
            input = new PositionedStack(new ItemStack(wheel), INPUT_X, SLOT_Y);
            output = new PositionedStack(new ItemStack(wheel.getRipeBlock()), OUTPUT_X, SLOT_Y);
        }

        @Override public PositionedStack getIngredient() { return input; }
        @Override public PositionedStack getResult() { return output; }
    }
}
