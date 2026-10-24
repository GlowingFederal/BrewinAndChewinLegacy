package com.brewinandchewin.legacy.integration;
import java.util.*;import net.minecraft.item.ItemStack;import codechicken.nei.PositionedStack;
import vectorwing.farmersdelight.legacy.Stacks;import com.brewinandchewin.legacy.*;
public final class FermentingHandler extends vectorwing.farmersdelight.legacy.integration.nei.FDRecipeHandler {
 public static final String UID="brewinandchewin.fermenting";
 public String getOverlayIdentifier() { return UID; }public String getRecipeName() { return "Fermenting"; }public String getGuiTexture() { return "brewinandchewin:textures/gui/keg.png"; }
 public Class<? extends net.minecraft.client.gui.inventory.GuiContainer> getGuiClass() { return KegGui.class; }
 public void loadTransferRects() { transferRects.add(new RecipeTransferRect(new java.awt.Rectangle(72,33,27,9),UID)); }
 public void drawBackground(int index) { panel();codechicken.lib.gui.GuiDraw.changeTexture(getGuiTexture());codechicken.lib.gui.GuiDraw.drawTexturedModalRect(0,0,5,11,166,65); }
 public void drawExtras(int index) { Fermentation.Recipe recipe=((Cached)arecipes.get(index)).recipe;label(recipe.time/20F+" s / "+recipe.experience+" XP",5,56);label(new String[]{"Frigid","Cold","Normal","Warm","Hot"}[recipe.temperature-1],110,2); }
 public void loadCraftingRecipes(String id,Object... results) { if(id.equals(UID) || id.equals("all")) for(Fermentation.Recipe r:Fermentation.ALL) arecipes.add(new Cached(r));else super.loadCraftingRecipes(id,results); }
 public void loadCraftingRecipes(ItemStack result) { for(Fermentation.Recipe r:Fermentation.ALL) if(Stacks.same(r.result,result)) arecipes.add(new Cached(r)); }
 public void loadUsageRecipes(ItemStack used) {
  if(workstation(used,BrewinAndChewin.BLOCKS.get("keg"))) { loadCraftingRecipes(UID);return; }
  for(Fermentation.Recipe r:Fermentation.ALL) { boolean match=Stacks.same(r.container,used) || Stacks.matches(r.liquid,used);for(List<ItemStack> i:r.ingredients) match|=Stacks.matches(i,used);if(match) arecipes.add(new Cached(r)); }
 }
 public final class Cached extends CachedRecipe {
  final Fermentation.Recipe recipe;private final List<PositionedStack> inputs=new ArrayList<PositionedStack>();private final PositionedStack output;
  Cached(Fermentation.Recipe recipe) { this.recipe=recipe;for(int i=0;i<recipe.ingredients.size();i++) inputs.add(new PositionedStack(recipe.ingredients.get(i),28+i%2*18,17+i/2*18));if(!recipe.liquid.isEmpty()) inputs.add(new PositionedStack(recipe.liquid,80,7));if(!Stacks.empty(recipe.container)) inputs.add(new PositionedStack(recipe.container,85,44));output=new PositionedStack(recipe.result,117,44); }
  public List<PositionedStack> getIngredients() { return getCycledIngredients(cycleticks/20,inputs); }public PositionedStack getResult() { return output; }
 }
}
