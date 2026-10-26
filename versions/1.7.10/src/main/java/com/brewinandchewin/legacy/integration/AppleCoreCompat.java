package com.brewinandchewin.legacy.integration;

import com.brewinandchewin.legacy.BrewinAndChewin;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import squeek.applecore.api.hunger.HealthRegenEvent;

/** Loaded only when AppleCore is installed; leaves other sources of healing alone. */
public final class AppleCoreCompat {
    private AppleCoreCompat() {}

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new AppleCoreCompat());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void regen(HealthRegenEvent.Regen event) {
        if (BrewinAndChewin.tipsy == null) return;
        PotionEffect effect = event.player.getActivePotionEffect(BrewinAndChewin.tipsy);
        if (effect != null && effect.getAmplifier() > 0) {
            // Skip heal entirely (rather than healing for zero, which other listeners
            // could increase), while preserving the normal regeneration exhaustion.
            event.setCanceled(true);
            event.player.getFoodStats().addExhaustion(event.deltaExhaustion);
        }
    }
}
