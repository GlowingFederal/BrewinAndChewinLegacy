package com.brewinandchewin.legacy;
import com.google.gson.*;
import java.util.*;
import net.minecraft.item.ItemStack;
import vectorwing.farmersdelight.legacy.Stacks;
import vectorwing.farmersdelight.legacy.api.SourceRecipes;
/** Immutable startup catalog. Matching assigns distinct inventory slots to duplicate ingredients. */
public final class Fermentation {
 private static final String[] TEMPERATURES = {"frigid", "cold", "normal", "warm", "hot"};
 public static final List<Recipe> ALL=new ArrayList<Recipe>();
 public static final Map<String,String> DEFERRED=new LinkedHashMap<String,String>();
 public static void register() {
  for(Map.Entry<String,JsonObject> entry:SourceRecipes.all().entrySet()) {
   JsonObject json=entry.getValue();if(!"brewinandchewin:fermenting".equals(json.get("type").getAsString())) continue;
   Recipe recipe=new Recipe(entry.getKey(),json);
   if(recipe.valid()) ALL.add(recipe);else DEFERRED.put(entry.getKey(),recipe.unavailable());
  }
  vectorwing.farmersdelight.legacy.ScriptRecipes.register("fermenting", ALL, Recipe::new, r -> r.id,
   r -> Collections.singletonList(r.result), () -> {});
  // Diagnostics are optional when using older compatible FD releases.
  try {
   Class.forName("vectorwing.farmersdelight.legacy.api.RecipeDiagnostics");
   vectorwing.farmersdelight.legacy.api.RecipeDiagnostics.register("brewinandchewin:fermenting", Fermentation::inspect);
  } catch(ClassNotFoundException absent) { /* Older FD still owns the same processing catalogs. */ }
 }
 /** Inspect the live catalog after scripts, without consuming items or changing a world. */
 private static Map<String,vectorwing.farmersdelight.legacy.api.RecipeDiagnostics.Entry> inspect() {
  Map<String,vectorwing.farmersdelight.legacy.api.RecipeDiagnostics.Entry> rows=new LinkedHashMap<>();
  boolean keg=false;
  for(vectorwing.farmersdelight.legacy.SourceCrafting recipe:vectorwing.farmersdelight.legacy.SourceCrafting.ALL)
   if(recipe.id.equals("brewinandchewin:keg") && recipe.enabled()) keg=true;
  for(Recipe recipe:ALL) {
   boolean valid=recipe.valid();
   KegTile tile=new KegTile();
   if(valid) {
    for(int i=0;i<recipe.ingredients.size();i++) tile.setInventorySlotContents(i,recipe.ingredients.get(i).get(0).copy());
    if(!recipe.liquid.isEmpty()) tile.setInventorySlotContents(4,recipe.liquid.get(0).copy());
    // Script predicates can require NBT/conditions beyond their viewer examples.
    boolean scripted=recipe.liquid instanceof vectorwing.farmersdelight.legacy.ScriptRecipes.Ingredient;
    for(List<ItemStack> input:recipe.ingredients) scripted|=input instanceof vectorwing.farmersdelight.legacy.ScriptRecipes.Ingredient;
    if(!scripted) valid=recipe.matches(tile);
   }
   rows.put(recipe.id,new vectorwing.farmersdelight.legacy.api.RecipeDiagnostics.Entry("fermenting",
    !valid?"invalid":keg?"registered":"registered_unreachable",
    !valid?recipe.unavailable():keg?"":"No survival keg crafting route; its original honeycomb ingredient requires an enabled provider"));
  }
  for(Map.Entry<String,String> entry:DEFERRED.entrySet()) if(!rows.containsKey(entry.getKey()))
   rows.put(entry.getKey(),new vectorwing.farmersdelight.legacy.api.RecipeDiagnostics.Entry("fermenting","unavailable",entry.getValue()));
  return rows;
 }
 public static final class Recipe {
  public final String id;public final List<List<ItemStack>> ingredients=new ArrayList<List<ItemStack>>();
  public final List<ItemStack> liquid;public final ItemStack result,container;public final int time,temperature;public final float experience;private final boolean requiresLiquid,requiresContainer;
  Recipe(vectorwing.farmersdelight.legacy.ScriptRecipes.Spec spec) {
   id=spec.id;ingredients.addAll(spec.inputs);liquid=spec.liquid;result=spec.outputs.get(0);container=spec.container;
   time=spec.time;temperature=spec.temperature;experience=spec.experience;
   requiresLiquid=!liquid.isEmpty();requiresContainer=!Stacks.empty(container);
  }
  Recipe(String id,JsonObject json) {
   this.id=id;for(JsonElement ingredient:json.getAsJsonArray("ingredients")) ingredients.add(Stacks.candidates(ingredient));
   liquid=json.has("liquid") && !json.getAsJsonObject("liquid").entrySet().isEmpty()?Stacks.candidates(json.get("liquid")):Collections.<ItemStack>emptyList();
   requiresLiquid=json.has("liquid") && !json.getAsJsonObject("liquid").entrySet().isEmpty();requiresContainer=json.has("container");
   result=Stacks.result(json.getAsJsonObject("result"));container=json.has("container")?Stacks.result(json.getAsJsonObject("container")):Stacks.none();
   time=json.has("fermentingtime")?json.get("fermentingtime").getAsInt():json.has("cookingtime")?json.get("cookingtime").getAsInt():12000;
   temperature=json.get("temperature").getAsInt();experience=json.has("experience")?json.get("experience").getAsFloat():0;
   if(time<=0 || temperature<1 || temperature>5 || ingredients.size()>4) throw new IllegalArgumentException("Invalid fermentation: "+id);
  }
  boolean valid() { if(Stacks.empty(result) || requiresLiquid && liquid.isEmpty() || requiresContainer && Stacks.empty(container)) return false;for(List<ItemStack> choices:ingredients) if(choices.isEmpty()) return false;return true; }
  String unavailable() {
   List<String> reasons=new ArrayList<>();
   JsonObject source=SourceRecipes.all().get(id);
   if(Stacks.empty(result)) reasons.add("output="+(source==null?"empty":source.get("result")));
   if(requiresLiquid && liquid.isEmpty()) reasons.add("liquid="+(source==null?"empty":source.get("liquid")));
   if(requiresContainer && Stacks.empty(container)) reasons.add("serving container="+(source==null?"empty":source.get("container")));
   for(int i=0;i<ingredients.size();i++) if(ingredients.get(i).isEmpty()) reasons.add("ingredient["+i+"]="+(source==null?"empty":source.getAsJsonArray("ingredients").get(i)));
   return reasons.isEmpty()?"Native fermentation input assignment failed":"Unavailable "+reasons+" in "+id;
  }
  public boolean matches(KegTile keg) {
   int count=0;for(int i=0;i<4;i++) if(!Stacks.empty(keg.getStackInSlot(i))) count++;
   return count==ingredients.size() && (liquid.isEmpty()?Stacks.empty(keg.getStackInSlot(4)):Stacks.matches(liquid,keg.getStackInSlot(4))) && assign(keg,0,0);
  }
  private boolean assign(KegTile keg,int at,int used) {
   if(at==ingredients.size()) return true;
   for(int i=0;i<4;i++) if((used&(1<<i))==0 && Stacks.matches(ingredients.get(at),keg.getStackInSlot(i)) && assign(keg,at+1,used|(1<<i))) return true;
   return false;
  }
 }
 public static Recipe find(KegTile tile) { for(Recipe r:ALL) if(r.matches(tile)) return r;return null; }
 public static int category(int heat) { return heat<=-9?1:heat<=-5?2:heat<=4?3:heat<=8?4:5; }
 public static String temperatureName(int category) { return TEMPERATURES[category - 1]; }
 public static boolean liquid(ItemStack stack) { for(Recipe r:ALL) if(Stacks.matches(r.liquid,stack)) return true;return false; }
 public static boolean container(ItemStack stack) { for(Recipe r:ALL) if(Stacks.same(r.container,stack)) return true;return false; }
 private Fermentation() {}
}
