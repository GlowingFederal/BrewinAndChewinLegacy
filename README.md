# Brewin' And Chewin'

<a href="https://www.curseforge.com/minecraft/mc-mods/brewin-and-chewin">
  <img src="http://cf.way2muchnoise.eu/full_637808_downloads.svg" alt="Curseforge Downloads">
</a>
<a href="https://discord.gg/M5AtJGPf">
  <img alt="Discord" src="https://img.shields.io/discord/855495317298741248?color=brightgreen&label=Discord">
</a>
<br>
<img src="https://i.imgur.com/EFkjwBq.png" width="50%">

### Overview

**Brewin' and Chewin'** is an addon mod for Farmer's Delight.

Using a keg, you can brew or ferment many new foods, including liquors, cheese, and fudge!


## Legacy Forge ports

Brewin' and Chewin' Legacy brings the original keg, fermented drinks and foods, cheese wheels, pizza, and item mat to Forge **1.7.10 and 1.12.2**. Install the matching **Farmer's Delight Legacy 1.0.2 or newer**. 1.7.10 also requires UniMixins 0.3.1+; 1.12.2 requires MixinBooter 11.18. NEI (1.7.10), JEI (1.12.2), and Modern Regen are optional. Each addon is independently installable.

The keg ferments four ingredients and an item-based liquid over the original recipe duration. Keep it in the recipe's frigid, cold, normal, warm, or hot temperature range, then supply the original serving container. Its interface shows progress, temperature, the internal batch, and served output. Hoppers insert ingredients and liquid from above; other sides accept serving containers and extract served output. The internal batch cannot be extracted directly. Breaking a keg preserves only its internal batch and serving requirement in the keg item; other inventory and accumulated experience drop separately. Processing progress persists in a placed keg across saves and chunk unloads.

Cheese ages naturally through two unripe stages. Cut a ripe wheel with a knife for four wedges, or take four slices from a placed pizza. Partially served wheels and pizzas drop only their remaining portions when broken, preventing replenishment by replacing the block. The item mat displays one stored item and returns it on removal or break.

### Original requirements and deferred recipes

**Keg crafting and both unripe cheese recipes require honeycomb, which is absent on both targets. Those recipes remain deferred for future UAB support.** No replacement ingredient has been invented. This also defers cheese-dependent pizza and sandwich paths until the real prerequisite exists. The functioning keg is available in creative mode for development and packs providing the original items. Otherwise-valid fermentation recipes retain their ingredients, liquid, serving container and duration; this does not make the keg obtainable in ordinary survival yet.

On 1.7.10, tankards also require the absent vanilla iron nugget, so tankard-dependent drinks are deferred. Recipes requiring kelp, honey bottles, sweet berries, sea pickles, turtle eggs, unavailable beetroot on 1.7.10, or Farmer's Respite tea are likewise deferred, including dependent alcohol chains. Full-content source catalogs are retained in the JAR; missing prerequisites are checked when the game initializes. Native cheese aging participates in that acquisition check when a real provider makes unripe cheese obtainable.

The three original effects are native: Satisfaction restores hunger without owning regeneration, Sweet Heart increases healing, and Tipsy stacks intoxication and suppresses vanilla food healing above its first level. Milk removes one Tipsy level at a time. Modern Regen remains responsible for its own regeneration implementation. Ordinary drinks cap Tipsy at level ten; Dread Nog retains its uncapped stacking but its unavailable turtle-egg recipe is deferred. Neither target has Bad Omen; 1.7.10 also lacks Glowing and Luck. Missing effects are omitted without invented equivalents. Both targets lack native waterlogging and blue ice. Commented-out upstream fondue content is not registered.

The port preserves the source's 12,000-tick default and accepts its generated `cookingtime` field alongside explicit `fermentingtime`. Temperature is enforced for normal recipes as well as specialized ones. Inventory, output identity/NBT, serving-container identity and capacity are validated before consumption. Server-owned processing uses cached recipe matching and bounded temperature refreshes; rendering and screens stay client-only.

### Build and layout

`src/` remains the original 1.18.2 implementation and assets; its build files are preserved under `reference/`. Native implementations live in `versions/1.7.10` and `versions/1.12.2`. `common/` contains the source-derived registered food contract. `version.properties` controls both native distributions. Build Farmer's Delight Legacy first in the adjacent checkout so its native development JARs are available. Set `JAVA8_HOME`, `JAVA21_HOME` and `JAVA25_HOME`, then use `scripts/run-gradle.ps1 -ProjectDirectory . -JavaVersion 21 buildAll`. The coordinator invokes the native toolchains independently and stages exactly:

- `build/release/brewinandchewin-1.0.0+mc1.7.10.jar`
- `build/release/brewinandchewin-1.0.0+mc1.12.2.jar`

The MIT-licensed upstream source and artwork retain their original license. Small workstation lifecycle adaptations use Farmer's Delight's MIT license, also included in the JAR. The native port does not bundle core or optional viewer classes.
