package com.brewinandchewin.legacy.integration;

import com.brewinandchewin.legacy.BrewinAndChewin;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.MinecraftForge;
import squeek.applecore.api.hunger.HealthRegenEvent;

/** Optional integration shared by original AppleCore and the unofficial 1.7.10 fork. */
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
            // Cancel the call itself: Sweet Heart can turn a zero heal into healing.
            // Keep the cost, as the vanilla redirect does. Already canceled events
            // are not delivered here, so another mod's cancellation costs nothing.
            event.setCanceled(true);
            event.player.getFoodStats().addExhaustion(event.deltaExhaustion);
        }
    }
}
