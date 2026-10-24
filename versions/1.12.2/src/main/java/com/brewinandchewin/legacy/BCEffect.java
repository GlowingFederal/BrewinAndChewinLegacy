package com.brewinandchewin.legacy;
import net.minecraft.potion.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
public final class BCEffect extends Potion {
 private final String effect;
 public BCEffect(String name) { super(false,0); effect=name;setPotionName("effect.brewinandchewin."+name);setRegistryName("brewinandchewin",name); }
 public boolean isReady(int duration,int amplifier) { return effect.equals("tipsy") || effect.equals("satisfaction") && duration%Math.max(1,400>>Math.min(30,amplifier))==0; }
 public void performEffect(EntityLivingBase entity,int amplifier) {
  if(entity.world.isRemote) return;
  if(effect.equals("satisfaction") && entity instanceof EntityPlayer) { EntityPlayer player=(EntityPlayer)entity;if(player.getFoodStats().getFoodLevel()<20) player.getFoodStats().setFoodLevel(player.getFoodStats().getFoodLevel()+1); }
  if(effect.equals("tipsy")) {
   PotionEffect active=entity.getActivePotionEffect(BrewinAndChewin.tipsy);if(active==null) return;
   if(amplifier>1) apply(entity,net.minecraft.init.MobEffects.NAUSEA,active.getDuration());if(amplifier>8) apply(entity,net.minecraft.init.MobEffects.POISON,active.getDuration());
   if(amplifier>3) { java.util.Random random=entity.world.rand;double amount=random.nextFloat()*(.06+.01*amplifier);if(random.nextBoolean()) amount=-amount;if(random.nextBoolean()) entity.motionX+=amount;else entity.motionZ+=amount;entity.velocityChanged=true; }
  }
 }
 private void apply(EntityLivingBase entity,Potion potion,int duration) { PotionEffect old=entity.getActivePotionEffect(potion);if(old==null || old.getDuration()<duration) entity.addPotionEffect(new PotionEffect(potion,duration,0)); }
 @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT) public void renderInventoryEffect(int x,int y,PotionEffect active,net.minecraft.client.Minecraft mc) { mc.getTextureManager().bindTexture(new net.minecraft.util.ResourceLocation("brewinandchewin","textures/mob_effect/"+effect+".png"));org.lwjgl.opengl.GL11.glColor4f(1,1,1,1);net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(x+6,y+7,0,0,18,18,18,18); }
 @net.minecraftforge.fml.relauncher.SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT) public void renderHUDEffect(int x,int y,PotionEffect active,net.minecraft.client.Minecraft mc,float alpha) { mc.getTextureManager().bindTexture(new net.minecraft.util.ResourceLocation("brewinandchewin","textures/mob_effect/"+effect+".png"));org.lwjgl.opengl.GL11.glColor4f(1,1,1,alpha);net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture(x+3,y+3,0,0,18,18,18,18); }
}
