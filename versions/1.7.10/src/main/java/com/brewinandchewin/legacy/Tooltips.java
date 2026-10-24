package com.brewinandchewin.legacy;
import vectorwing.farmersdelight.legacy.Stacks;import net.minecraft.item.ItemStack;
public final class Tooltips {
 @cpw.mods.fml.common.eventhandler.SubscribeEvent public void tooltip(net.minecraftforge.event.entity.player.ItemTooltipEvent event) {
  ItemStack stack=event.itemStack;java.util.List<String> lines=event.toolTip;if(Stacks.empty(stack)) return;
  if(stack.getItem() instanceof DrinkFood) { DrinkFood drink=(DrinkFood)stack.getItem();if(drink.potency>0) lines.add("\u00a7c"+net.minecraft.client.resources.I18n.format("brewinandchewin.tooltip.tipsy"+drink.potency,drink.boozeDuration)); }
  if(stack.getItem()==BrewinAndChewin.ITEMS.get("keg")) {
   ItemStack meal=stack.hasTagCompound()?ItemStack.loadItemStackFromNBT(stack.getTagCompound().getCompoundTag("Meal")):Stacks.none();
   if(Stacks.empty(meal)) lines.add("\u00a77"+net.minecraft.client.resources.I18n.format("brewinandchewin.tooltip.keg.empty"));
   else { lines.add("\u00a77"+net.minecraft.client.resources.I18n.format("brewinandchewin.tooltip.keg."+(Stacks.size(meal)==1?"single_serving":"many_servings"),Stacks.size(meal)));lines.add("\u00a79"+meal.getDisplayName()); }
  }
 }
}
