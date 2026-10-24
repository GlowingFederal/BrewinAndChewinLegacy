package com.brewinandchewin.legacy;
import net.minecraft.item.*;
import net.minecraft.block.Block;
import net.minecraft.potion.Potion;
import com.google.gson.*;
import vectorwing.farmersdelight.legacy.*;
import vectorwing.farmersdelight.legacy.api.*;
import net.minecraftforge.fml.common.Mod;
@Mod(modid="brewinandchewin",name="Brewin and Chewin Legacy",version=BuildVersion.VERSION,acceptedMinecraftVersions="[1.12.2]",dependencies="required-after:farmersdelight@[1.0.2,)")
public final class BrewinAndChewin {
 @Mod.Instance("brewinandchewin") public static BrewinAndChewin instance;
 @net.minecraftforge.fml.common.SidedProxy(clientSide="com.brewinandchewin.legacy.ClientProxy",serverSide="com.brewinandchewin.legacy.CommonProxy") public static CommonProxy proxy;
 public static final java.util.Map<String,Item> ITEMS=new java.util.LinkedHashMap<String,Item>();
 public static final java.util.Map<String,Block> BLOCKS=new java.util.LinkedHashMap<String,Block>();
 public static Potion tipsy,satisfaction,sweetHeart;
 
 @Mod.EventHandler public void preInit(net.minecraftforge.fml.common.event.FMLPreInitializationEvent event) {
  
  tipsy=effect("tipsy");satisfaction=effect("satisfaction");sweetHeart=effect("sweet_heart");
  add("tankard",new Item());
  try(java.io.InputStreamReader reader=new java.io.InputStreamReader(BrewinAndChewin.class.getResourceAsStream("/brewinandchewin/content.json"),java.nio.charset.StandardCharsets.UTF_8)) {
   Gson gson=new Gson();for(JsonObject entry:gson.fromJson(reader,JsonObject[].class)) if(entry.get("kind").getAsString().equals("food")) add(entry.get("id").getAsString(),new DrinkFood(gson.fromJson(entry,FoodDefinition.class),entry));
  } catch(java.io.IOException e) { throw new IllegalStateException("Cannot load registered food contract",e); }
  block("keg",new KegBlock(),1);block("item_mat",new MatBlock(),64);
  for(String type:new String[]{"flaxen","scarlet"}) { block("unripe_"+type+"_cheese_wheel",new AgingWheelBlock(type),16);block(type+"_cheese_wheel",new WheelBlock(type,false),16); }
  block("pizza",new WheelBlock("pizza",true),1);
  net.minecraftforge.fml.common.registry.GameRegistry.registerTileEntity(KegTile.class,new net.minecraft.util.ResourceLocation("brewinandchewin:keg"));net.minecraftforge.fml.common.registry.GameRegistry.registerTileEntity(MatTile.class,new net.minecraft.util.ResourceLocation("brewinandchewin:item_mat"));
  net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new EffectEvents());proxy.preInit();
 }
 @Mod.EventHandler public void init(net.minecraftforge.fml.common.event.FMLInitializationEvent event) {
  SourceRecipes.addCatalog("brewinandchewin",BrewinAndChewin.class,"/brewinandchewin/source_recipes.json");SourceTags.addCatalog(BrewinAndChewin.class,"/brewinandchewin/source_tags.json");
  AddonRecipes.register("brewinandchewin",new String[0],"brewinandchewin:fermenting");
  for(String type:new String[]{"flaxen","scarlet"}) AddonRecipes.processing("brewinandchewin","brewinandchewin:unripe_"+type+"_cheese_wheel","brewinandchewin:"+type+"_cheese_wheel");
  net.minecraftforge.fml.common.network.NetworkRegistry.INSTANCE.registerGuiHandler(instance,proxy);
 }
 @Mod.EventHandler public void postInit(net.minecraftforge.fml.common.event.FMLPostInitializationEvent event) { Fermentation.register(); }
 private static Potion effect(String name) { Potion potion=new BCEffect(name);net.minecraftforge.fml.common.registry.GameRegistry.findRegistry(Potion.class).register(potion); LegacyFood.registerEffect("brewinandchewin:"+name,potion);return potion; }
 private static void add(String id,Item item) { item.setTranslationKey("brewinandchewin."+id).setRegistryName("brewinandchewin",id);item.setCreativeTab(Content.TAB);net.minecraftforge.fml.common.registry.GameRegistry.findRegistry(Item.class).register(item); ITEMS.put(id,item); }
 private static void block(String id,Block block,int limit) { block.setTranslationKey("brewinandchewin."+id).setRegistryName("brewinandchewin",id);block.setCreativeTab(Content.TAB);net.minecraftforge.fml.common.registry.GameRegistry.findRegistry(Block.class).register(block);add(id,new ItemBlock(block)); BLOCKS.put(id,block);ITEMS.get(id).setMaxStackSize(limit); }
}
