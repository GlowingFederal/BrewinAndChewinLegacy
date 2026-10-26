package com.brewinandchewin.legacy.mixin;

import java.util.List;
import java.util.Set;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import net.minecraft.launchwrapper.Launch;

public final class BrewinMixinPlugin implements IMixinConfigPlugin {
    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // Do not load AppleCore's API classes while Mixin is transforming FoodStats.
        return !mixinClassName.endsWith(".FoodStatsMixin")
            || Launch.classLoader.getResource("squeek/applecore/api/hunger/HealthRegenEvent$AllowRegen.class") == null;
    }

    @Override public void onLoad(String mixinPackage) {}
    @Override public String getRefMapperConfig() { return null; }
    @Override public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}
    @Override public List<String> getMixins() { return null; }
    @Override public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
    @Override public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
