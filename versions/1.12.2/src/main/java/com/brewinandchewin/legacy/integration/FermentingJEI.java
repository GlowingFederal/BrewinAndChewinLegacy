package com.brewinandchewin.legacy.integration;
import com.brewinandchewin.legacy.*;

import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import vectorwing.farmersdelight.legacy.Stacks;

@mezz.jei.api.JEIPlugin
public final class FermentingJEI implements mezz.jei.api.IModPlugin {
    static final String UID = "brewinandchewin.fermenting";
    private static final net.minecraft.util.ResourceLocation TEXTURE = new net.minecraft.util.ResourceLocation("brewinandchewin", "textures/gui/keg.png");
    public void registerCategories(IRecipeCategoryRegistration registration) {
        List<AgingWheelBlock> wheels = AgingWheelBlock.registeredWheels();
        if (!wheels.isEmpty()) {
            registration.addRecipeCategories(new RipeningCategory(registration.getJeiHelpers().getGuiHelper(), wheels.get(0)));
        }
        final IDrawable background = registration.getJeiHelpers().getGuiHelper().createDrawable(TEXTURE, 5, 11, 166, 65);
        final IDrawable icon = registration.getJeiHelpers().getGuiHelper().createDrawableIngredient(new ItemStack(BrewinAndChewin.BLOCKS.get("keg")));
        registration.addRecipeCategories(new IRecipeCategory<Wrapper>() {
            public String getUid() { return UID; }
            public String getTitle() { return I18n.format("brewinandchewin.jei.fermenting"); }
            public String getModName() { return "Brewin and Chewin Legacy"; }
            public IDrawable getBackground() { return background; }
            public IDrawable getIcon() { return icon; }
            public void setRecipe(IRecipeLayout layout, Wrapper wrapper, IIngredients ingredients) {
                for(int i=0;i<4;i++) layout.getItemStacks().init(i,true,27+i%2*18,16+i/2*18);layout.getItemStacks().init(4,true,79,6);layout.getItemStacks().init(5,true,84,43);
                layout.getItemStacks().init(6,false,116,43);
                layout.getItemStacks().set(ingredients);
            }
        });
    }
    public void register(IModRegistry registry) {
        List<RipeningCategory.Recipe> ripening = new ArrayList<>();
        for (AgingWheelBlock wheel : AgingWheelBlock.registeredWheels()) ripening.add(new RipeningCategory.Recipe(wheel));
        if (!ripening.isEmpty()) registry.addRecipes(ripening, RipeningCategory.UID);
        List<Wrapper> recipes = new ArrayList<Wrapper>();
        for (Fermentation.Recipe recipe : Fermentation.ALL) recipes.add(new Wrapper(recipe));
        registry.addRecipes(recipes, UID); registry.addRecipeCatalyst(new ItemStack(BrewinAndChewin.BLOCKS.get("keg")), UID);
        registry.addRecipeClickArea(KegGui.class, 77,44,27,9, UID);
        registry.getRecipeTransferRegistry().addRecipeTransferHandler(new mezz.jei.api.recipe.transfer.IRecipeTransferInfo<KegContainer>() {
            public Class<KegContainer> getContainerClass() { return KegContainer.class; }
            public String getRecipeCategoryUid() { return UID; }
            public boolean canHandle(KegContainer container) { return true; }
            public List<net.minecraft.inventory.Slot> getRecipeSlots(KegContainer container) {
                List<net.minecraft.inventory.Slot> slots=new ArrayList<net.minecraft.inventory.Slot>();
                for(int i=0;i<5;i++) slots.add(container.getSlot(i));
                slots.add(container.getSlot(6)); return slots;
            }
            public List<net.minecraft.inventory.Slot> getInventorySlots(KegContainer container) {
                return new ArrayList<net.minecraft.inventory.Slot>(container.inventorySlots.subList(8,44));
            }
        });
    }
    static final class Wrapper implements IRecipeWrapper {
        final Fermentation.Recipe recipe;
        Wrapper(Fermentation.Recipe recipe) { this.recipe = recipe; }
        public void getIngredients(IIngredients ingredients) {
            List<List<ItemStack>> inputs = new ArrayList<List<ItemStack>>(recipe.ingredients);
            while(inputs.size()<4) inputs.add(java.util.Collections.<ItemStack>emptyList());
            inputs.add(recipe.liquid);
            inputs.add(Stacks.empty(recipe.container) ? java.util.Collections.<ItemStack>emptyList() : java.util.Collections.singletonList(recipe.container));
            ingredients.setInputLists(VanillaTypes.ITEM, inputs); ingredients.setOutput(VanillaTypes.ITEM, recipe.result);
        }
        public void drawInfo(Minecraft minecraft, int width, int height, int mouseX, int mouseY) {
            net.minecraft.client.renderer.GlStateManager.color(1,1,1,1);
            minecraft.getTextureManager().bindTexture(TEXTURE);
            int fill=(int)(Minecraft.getSystemTime()/50%80)*26/80;
            net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(72,33,176,28,fill+1,9,256,256);
            if(recipe.temperature<=2) net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(78,28,182,0,7,4,256,256);
            if(recipe.temperature==1) net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(72,28,176,0,6,4,256,256);
            if(recipe.temperature>=4) net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(91,28,195,0,7,4,256,256);
            if(recipe.temperature==5) net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(98,28,202,0,7,4,256,256);
            minecraft.fontRenderer.drawString(I18n.format("brewinandchewin.fermenting.stats", recipe.time / 20F, recipe.experience), 5, 56, 0x404040);
            minecraft.fontRenderer.drawString(I18n.format("brewinandchewin.fermenting.temperature." + Fermentation.temperatureName(recipe.temperature)), 110, 2, 0x404040);
        }
    }
}
