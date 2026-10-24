package com.brewinandchewin.legacy;
import net.minecraft.potion.PotionEffect;
public final class EffectEvents {
 @cpw.mods.fml.common.eventhandler.SubscribeEvent public void healing(net.minecraftforge.event.entity.living.LivingHealEvent event) {
  PotionEffect effect=event.entityLiving.getActivePotionEffect(BrewinAndChewin.sweetHeart);if(effect!=null && !event.entityLiving.worldObj.isRemote) event.amount+=.5F+.5F*effect.getAmplifier();
 }
 
}
