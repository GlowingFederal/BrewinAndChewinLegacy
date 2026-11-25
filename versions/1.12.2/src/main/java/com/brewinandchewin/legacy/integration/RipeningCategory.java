package com.brewinandchewin.legacy.integration;

import com.brewinandchewin.legacy.AgingWheelBlock;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

final class RipeningCategory implements IRecipeCategory<RipeningCategory.Recipe> {
    static final String UID = "brewinandchewin.ripening";
    private static final int WIDTH = 166, HEIGHT = 65;
    private static final int INPUT_X = 30, OUTPUT_X = 120, SLOT_Y = 17;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slot;

    RipeningCategory(IGuiHelper gui, AgingWheelBlock wheel) {
        background = gui.createBlankDrawable(WIDTH, HEIGHT);
        slot = gui.getSlotDrawable();
        icon = gui.createDrawableIngredient(new ItemStack(wheel.getRipeBlock()));
    }

    @Override public String getUid() { return UID; }
    @Override public String getTitle() { return I18n.format(UID + ".title"); }
    @Override public String getModName() { return "Brewin and Chewin Legacy"; }
    @Override public IDrawable getBackground() { return background; }
    @Override public IDrawable getIcon() { return icon; }

    @Override public void setRecipe(IRecipeLayout layout, Recipe recipe, IIngredients ingredients) {
        layout.getItemStacks().init(0, true, INPUT_X - 1, SLOT_Y - 1);
        layout.getItemStacks().init(1, false, OUTPUT_X - 1, SLOT_Y - 1);
        layout.getItemStacks().set(ingredients);
    }

    @Override public void drawExtras(Minecraft minecraft) {
        Gui.drawRect(0, 0, WIDTH, HEIGHT, 0xffc6c6c6);
        slot.draw(minecraft, INPUT_X - 1, SLOT_Y - 1);
        slot.draw(minecraft, OUTPUT_X - 1, SLOT_Y - 1);
        Gui.drawRect(57, 23, 103, 26, 0xff8b8b8b);
        for (int i = 0; i < 7; i++) Gui.drawRect(102 + i, 18 + i, 103 + i, 31 - i, 0xff8b8b8b);
        centered(minecraft, 4, "place");
        centered(minecraft, 36, "stages");
        centered(minecraft, 46, "chance", Math.round(AgingWheelBlock.ADVANCE_CHANCE * 100));
        centered(minecraft, 56, "ticking");
    }

    private static void centered(Minecraft minecraft, int y, String key, Object... arguments) {
        String line = I18n.format(UID + "." + key, arguments);
        minecraft.fontRenderer.drawString(line, (WIDTH - minecraft.fontRenderer.getStringWidth(line)) / 2, y, 0x404040);
    }

    static final class Recipe implements IRecipeWrapper {
        private final AgingWheelBlock wheel;

        Recipe(AgingWheelBlock wheel) {
            this.wheel = wheel;
        }

        @Override public void getIngredients(IIngredients ingredients) {
            ingredients.setInput(VanillaTypes.ITEM, new ItemStack(wheel));
            ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(wheel.getRipeBlock()));
        }
    }
}
