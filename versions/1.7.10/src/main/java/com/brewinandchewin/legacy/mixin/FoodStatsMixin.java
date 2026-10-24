package com.brewinandchewin.legacy.mixin;
import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.util.FoodStats;import net.minecraft.entity.player.EntityPlayer;
import com.brewinandchewin.legacy.BrewinAndChewin;
/** Skip vanilla food healing itself so healing listeners cannot turn a zero amount back into healing. */
@Mixin(FoodStats.class) public abstract class FoodStatsMixin {
 @Redirect(method="onUpdate",at=@At(value="INVOKE",target="Lnet/minecraft/entity/player/EntityPlayer;heal(F)V"),require=0)
 private void naturalHealing(EntityPlayer player,float amount) {
  if(BrewinAndChewin.tipsy!=null && player.isPotionActive(BrewinAndChewin.tipsy.id) && player.getActivePotionEffect(BrewinAndChewin.tipsy).getAmplifier()>0) return;
  player.heal(amount);
 }
}
