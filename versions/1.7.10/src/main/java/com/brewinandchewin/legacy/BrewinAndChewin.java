package com.brewinandchewin.legacy;
import net.minecraft.item.*;
import net.minecraft.block.Block;
import net.minecraft.potion.Potion;
import com.google.gson.*;
import vectorwing.farmersdelight.legacy.*;
import vectorwing.farmersdelight.legacy.api.*;
import cpw.mods.fml.common.Mod;
@Mod(modid="brewinandchewin",name="Brewin and Chewin Legacy",version=BuildVersion.VERSION,acceptedMinecraftVersions="[1.7.10]",dependencies="required-after:farmersdelight@[1.0.2,)")
public final class BrewinAndChewin {
 @Mod.Instance("brewinandchewin") public static BrewinAndChewin instance;
 @cpw.mods.fml.common.SidedProxy(clientSide="com.brewinandchewin.legacy.ClientProxy",serverSide="com.brewinandchewin.legacy.CommonProxy") public static CommonProxy proxy;
 public static final java.util.Map<String,Item> ITEMS=new java.util.LinkedHashMap<String,Item>();
 public static final java.util.Map<String,Block> BLOCKS=new java.util.LinkedHashMap<String,Block>();
 public static Potion tipsy,satisfaction,sweetHeart;
 public static final PotionIdRegistry POTIONS=new PotionIdRegistry("brewinandchewin");
 @Mod.EventHandler public void construct(cpw.mods.fml.common.event.FMLConstructionEvent event) { java.util.Map<String,Integer> names=new java.util.LinkedHashMap<String,Integer>();for(String name:new String[]{"tipsy","satisfaction","sweet_heart"}) names.put(name,-1);POTIONS.reserve(names); }
 @Mod.EventHandler public void complete(cpw.mods.fml.common.event.FMLLoadCompleteEvent event) { POTIONS.verify(); }
 @cpw.mods.fml.common.network.NetworkCheckHandler public boolean network(java.util.Map<String,String> remote,cpw.mods.fml.relauncher.Side side) { return remote.containsKey("brewinandchewin")?BuildVersion.VERSION.equals(remote.get("brewinandchewin")) && POTIONS.matches(remote):side==cpw.mods.fml.relauncher.Side.SERVER; }

 @Mod.EventHandler public void preInit(cpw.mods.fml.common.event.FMLPreInitializationEvent event) {
  POTIONS.allocate();net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(POTIONS);
  tipsy=effect("tipsy");satisfaction=effect("satisfaction");sweetHeart=effect("sweet_heart");
  add("tankard",new Item());
  try(java.io.InputStreamReader reader=new java.io.InputStreamReader(BrewinAndChewin.class.getResourceAsStream("/brewinandchewin/content.json"),java.nio.charset.StandardCharsets.UTF_8)) {
   Gson gson=new Gson();for(JsonObject entry:gson.fromJson(reader,JsonObject[].class)) if(entry.get("kind").getAsString().equals("food")) add(entry.get("id").getAsString(),new DrinkFood(gson.fromJson(entry,FoodDefinition.class),entry));
  } catch(java.io.IOException e) { throw new IllegalStateException("Cannot load registered food contract",e); }
  block("keg",new KegBlock(),1);block("item_mat",new MatBlock(),64);
  for(String type:new String[]{"flaxen","scarlet"}) { block("unripe_"+type+"_cheese_wheel",new AgingWheelBlock(type),16);block(type+"_cheese_wheel",new WheelBlock(type,false),16); }
  block("pizza",new WheelBlock("pizza",true),1);
  cpw.mods.fml.common.registry.GameRegistry.registerTileEntity(KegTile.class,"brewinandchewin:keg");cpw.mods.fml.common.registry.GameRegistry.registerTileEntity(MatTile.class,"brewinandchewin:item_mat");
  net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new EffectEvents());proxy.preInit();
  if(cpw.mods.fml.common.Loader.isModLoaded("AppleCore")) com.brewinandchewin.legacy.integration.AppleCoreCompat.register();
 }
 @Mod.EventHandler public void init(cpw.mods.fml.common.event.FMLInitializationEvent event) {
  SourceRecipes.addCatalog("brewinandchewin",BrewinAndChewin.class,"/brewinandchewin/source_recipes.json");SourceTags.addCatalog(BrewinAndChewin.class,"/brewinandchewin/source_tags.json");
  AddonRecipes.register("brewinandchewin",new String[0],"brewinandchewin:fermenting");
  for(String type:new String[]{"flaxen","scarlet"}) AddonRecipes.processing("brewinandchewin","brewinandchewin:unripe_"+type+"_cheese_wheel","brewinandchewin:"+type+"_cheese_wheel");
  cpw.mods.fml.common.network.NetworkRegistry.INSTANCE.registerGuiHandler(instance,proxy);
 }
 @Mod.EventHandler public void postInit(cpw.mods.fml.common.event.FMLPostInitializationEvent event) { Fermentation.register(); }
 private static Potion effect(String name) { Potion potion=new BCEffect(name);POTIONS.track(name,potion); LegacyFood.registerEffect("brewinandchewin:"+name,potion);return potion; }
 private static void add(String id,Item item) { item.setUnlocalizedName("brewinandchewin."+id).setTextureName("brewinandchewin:"+id).setCreativeTab(Content.TAB);cpw.mods.fml.common.registry.GameRegistry.registerItem(item,id); ITEMS.put(id,item); }
 private static void block(String id,Block block,int limit) { block.setBlockName("brewinandchewin."+id).setBlockTextureName("brewinandchewin:"+id).setCreativeTab(Content.TAB);cpw.mods.fml.common.registry.GameRegistry.registerBlock(block,id);ITEMS.put(id,Item.getItemFromBlock(block)); BLOCKS.put(id,block);ITEMS.get(id).setMaxStackSize(limit); }
}
