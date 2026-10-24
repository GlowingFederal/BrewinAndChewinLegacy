package com.brewinandchewin.legacy;
import net.minecraft.potion.PotionEffect;
public final class EffectEvents {
 @net.minecraftforge.fml.common.eventhandler.SubscribeEvent public void healing(net.minecraftforge.event.entity.living.LivingHealEvent event) {
  PotionEffect effect=event.getEntityLiving().getActivePotionEffect(BrewinAndChewin.sweetHeart);if(effect!=null && !event.getEntityLiving().world.isRemote) event.setAmount(event.getAmount()+.5F+.5F*effect.getAmplifier());
 }
 private static final ThreadLocal<Boolean> CURING=new ThreadLocal<Boolean>();
 @net.minecraftforge.fml.common.eventhandler.SubscribeEvent public void cure(net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent event) {
  if(Boolean.TRUE.equals(CURING.get()) || event.getEntityLiving().world.isRemote || event.getPotion()!=BrewinAndChewin.tipsy) return;
  PotionEffect old=event.getEntityLiving().getActivePotionEffect(BrewinAndChewin.tipsy);if(old==null || old.getAmplifier()==0) return;
  event.setCanceled(true);CURING.set(true);
  try { event.getEntityLiving().removePotionEffect(BrewinAndChewin.tipsy);PotionEffect lower=new PotionEffect(BrewinAndChewin.tipsy,old.getDuration(),old.getAmplifier()-1,old.getIsAmbient(),old.doesShowParticles());lower.setCurativeItems(old.getCurativeItems());event.getEntityLiving().addPotionEffect(lower); }
  finally { CURING.remove(); }
 }

}
