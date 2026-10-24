package com.brewinandchewin.legacy.mixin;
import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.gen.Accessor;import net.minecraft.potion.PotionEffect;
@Mixin(PotionEffect.class) public interface PotionLevelAccessor { @Accessor("amplifier") void brewinandchewin$amplifier(int amplifier); }
