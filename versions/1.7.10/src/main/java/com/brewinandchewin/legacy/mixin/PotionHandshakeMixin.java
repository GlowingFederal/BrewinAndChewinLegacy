package com.brewinandchewin.legacy.mixin;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.brewinandchewin.legacy.BrewinAndChewin;
import java.util.List;
import java.util.Map;

/** Verify identities in Forge's handshake, before saved effect packets reach the player. */
@Mixin(value = FMLHandshakeMessage.ModList.class, remap = false)
public abstract class PotionHandshakeMixin {
    @Shadow private Map<String,String> modTags;

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("RETURN"), remap = false)
    private void brewinandchewin$includePotionIds(List<ModContainer> mods, CallbackInfo ci) {
        modTags.put(BrewinAndChewin.POTIONS.handshakeKey(), BrewinAndChewin.POTIONS.handshake());
    }
}
