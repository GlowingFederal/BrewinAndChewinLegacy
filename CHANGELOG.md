2026-10-23 17:18 — Initial native Forge 1.7.10 and 1.12.2 ports

- Register the original 33 items, seven blocks, foods, drinks and three native effects against Farmer's Delight Legacy 1.0.2. Preserve original resources and omit commented-out fondue experiments.
- Implement real temperature-sensitive fermentation, original input/liquid/container contracts, cached matching, bounded heat lookup, transactional output, serving, experience, portable batches, persistence, native screens, and sided automation including liquid input.
- Port cheese aging, four portions per ripe wheel/pizza, item-mat display and storage, Tipsy stacking and staged cures, dedicated-server-safe Satisfaction hunger restoration, Sweet Heart healing and vanilla food-healing interception. Keep Modern Regen optional and independently authoritative.
- Provide optional NEI/JEI fermentation recipes, usages and transfer mappings using actual processing inputs and output. Keep rendering and cosmetic break-overlay Mixins client-only.
- Defer original honeycomb, 1.7.10 tankard iron nuggets, kelp, honey, berries, pickle, turtle-egg and tea paths, plus dependent recipes, for their real providers and future UAB. Preserve exact requirements without replacement ingredients or modern content substitutes.
- Correct upstream normal-temperature bypass, recipe-duration field mismatch, partial-portion replacement duplication and double item removal. Skip intoxicated vanilla food healing at the actual call, preventing Sweet Heart from reviving a zero-amount heal. Document absent native effects and waterlogging.
- Add independent native builds and verified, versioned distributions while retaining the modern source as the behavioral reference.

2026-10-23 22:14 — Add optional UAB compatibility on Forge 1.7.10

- Use enabled UAB berries, honey, beetroot, fungi, flowers, iron nuggets and turtle eggs in original fermentation chains, preserving temperature, duration, quantities and serving containers. Reject unavailable liquids/containers; recognize enabled UAB kelp and pickle ingredients when provided, and defer missing content and tea providers.
- Follow the shared compatibility settings in farmersdelight.cfg, existing addon settings and UAB feature availability. Keep UAB optional and preserve saved registrations and other Minecraft implementations.

2026-11-02 01:08 — Expose native keg recipes to optional scripting

- Connect keg additions/removals to the shared version-specific scripting integration without requiring a scripting mod. Retain item liquids, NBT, servings, containers, temperatures, full-width durations and XP.
- Reject original 1.12.2 recipes whose required liquid or serving container is unresolved. Refresh cached keg recipes after script edits and document ecosystem UAB/scripting behavior.

2026-11-04 15:48 — Support AppleCore food regeneration on 1.7.10

- Apply the optional AppleCore event integration proposed in PR #1 by ailanlana. Skip the vanilla FoodStats redirect when AppleCore is present, avoiding the AppleCore Unofficial overwrite conflict during startup.
- Cancel food healing for Tipsy II and above while retaining regeneration exhaustion, without allowing Sweet Heart to revive a suppressed heal. Use the shared API supported by original AppleCore and the unofficial fork; keep AppleCore optional and unbundled.

2026-11-12 13:32 — Report fermentation coverage and unavailable keg progression

- Supply the live fermentation catalog to optional Farmer's Delight recipe diagnostics on both native targets, including exact missing input/liquid/container reasons and input assignment checks.
- Distinguish registered fermentation from survival keg acquisition when honeycomb is unavailable. Preserve original recipes, saved machines and the existing optional UAB relationship; diagnostics remain optional with older compatible Farmer's Delight releases.
- Render the native keg artwork, temperature gauge and progress on 1.12.2 JEI pages; register the real keg category icon and catalyst during 1.7.10 pre-initialization so both supported GTNH NEI builds show the workstation instead of fallback letters.

2026-11-12 14:05 — Increment patch version for release packaging

- Advance the mod patch version by 0.0.1 and refresh matching native artifact and mod metadata versions.

2026-11-25 02:23 — Restore legacy localization and show cheese ripening in recipe viewers

- Supply format 3 resource pack metadata on Forge 1.12.2 so its native lowercase language files load. Convert available source locales to each target's native filename and item/block key format.
- Add Ripening recipes and usages for Flaxen and Scarlet cheese in NEI and JEI. Use the actual registered aging blocks and display placement, both random-tick advances, and the per-tick chance.
- Localize the fermentation title, temperature and processing details and register the keg catalyst under the handler ID used by GTNH NEI. Clarify Egg Grog's Tipsy tooltip and retain custom keg names when packaging stored batches.
- Restore the original cheese knife prompt and cut sound. Use the native bottle-fill sound for hand-serving on 1.12.2 and draw its Item Mat in the cutout render layer.
