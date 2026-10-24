package com.brewinandchewin.legacy;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import com.google.gson.JsonObject;
import vectorwing.farmersdelight.legacy.LegacyFood;
import vectorwing.farmersdelight.legacy.api.FoodDefinition;
public final class DrinkFood extends LegacyFood {
 public final int potency,boozeDuration;
 public DrinkFood(FoodDefinition definition,JsonObject entry) { super(definition);potency=entry.has("potency")?entry.get("potency").getAsInt():0;boozeDuration=entry.has("boozeDuration")?entry.get("boozeDuration").getAsInt():0; }
 public void intoxicate(EntityPlayer player) {
  if(potency==0) return;PotionEffect previous=player.getActivePotionEffect(BrewinAndChewin.tipsy);
  int amp=previous==null?potency-1:Math.min(9,previous.getAmplifier()+potency);
  if(definition.id.equals("dread_nog") && previous!=null) amp=previous.getAmplifier()+potency;
  int duration=previous==null?boozeDuration*1200:previous.getDuration()+boozeDuration*600;
  if(duration>0) player.addPotionEffect(new PotionEffect(BrewinAndChewin.tipsy,duration,amp));
 }
 public ItemStack onItemUseFinish(ItemStack stack,World world,net.minecraft.entity.EntityLivingBase player) { if(!world.isRemote && player instanceof EntityPlayer) intoxicate((EntityPlayer)player);return super.onItemUseFinish(stack,world,player); }
}
