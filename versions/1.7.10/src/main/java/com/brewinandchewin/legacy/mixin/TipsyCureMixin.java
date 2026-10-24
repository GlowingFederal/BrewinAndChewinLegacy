package com.brewinandchewin.legacy.mixin;
import org.spongepowered.asm.mixin.*;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.entity.EntityLivingBase;import net.minecraft.potion.PotionEffect;import net.minecraft.item.ItemStack;import com.brewinandchewin.legacy.BrewinAndChewin;
@Mixin(EntityLivingBase.class) public abstract class TipsyCureMixin {
 @Shadow protected abstract void onChangedPotionEffect(PotionEffect effect,boolean reapply);
 @Unique private boolean brewinandchewin$demote(PotionEffect effect) {
  if(effect==null || BrewinAndChewin.tipsy==null || effect.getPotionID()!=BrewinAndChewin.tipsy.id || effect.getAmplifier()<=0 || ((EntityLivingBase)(Object)this).worldObj.isRemote) return false;
  ((PotionLevelAccessor)effect).brewinandchewin$amplifier(effect.getAmplifier()-1);onChangedPotionEffect(effect,true);return true;
 }
 @Redirect(method="curePotionEffects",at=@At(value="INVOKE",target="Lnet/minecraft/potion/PotionEffect;isCurativeItem(Lnet/minecraft/item/ItemStack;)Z",remap=false),remap=false)
 private boolean milk(PotionEffect effect,ItemStack stack) { return effect.isCurativeItem(stack) && !brewinandchewin$demote(effect); }
 @Inject(method="removePotionEffect",at=@At("HEAD"),cancellable=true) private void remove(int id,CallbackInfo ci) { if(BrewinAndChewin.tipsy!=null && id==BrewinAndChewin.tipsy.id && brewinandchewin$demote(((EntityLivingBase)(Object)this).getActivePotionEffect(BrewinAndChewin.tipsy))) ci.cancel(); }
}
