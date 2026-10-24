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
