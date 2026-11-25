package com.brewinandchewin.legacy.integration;
import codechicken.nei.api.*;
import com.brewinandchewin.legacy.AgingWheelBlock;
import net.minecraft.block.Block;
import java.util.List;
public final class NEIBrewinConfig implements IConfigureNEI {
 public static void registerHandlerInfo() {
  registerHandlerInfo(FermentingHandler.class, "brewinandchewin:keg");
  List<AgingWheelBlock> wheels = AgingWheelBlock.registeredWheels();
  if (!wheels.isEmpty()) {
   registerHandlerInfo(RipeningHandler.class, Block.blockRegistry.getNameForObject(wheels.get(0).getRipeBlock()));
  }
  net.minecraft.nbt.NBTTagCompound catalyst=new net.minecraft.nbt.NBTTagCompound();
  catalyst.setString("handlerID",FermentingHandler.class.getName());catalyst.setString("itemName","brewinandchewin:keg");
  cpw.mods.fml.common.event.FMLInterModComms.sendMessage("NotEnoughItems","registerCatalystInfo",catalyst);
 }
 private static void registerHandlerInfo(Class<?> handler, String item) {
  net.minecraft.nbt.NBTTagCompound tag=new net.minecraft.nbt.NBTTagCompound();
  tag.setString("handler",handler.getName());
  tag.setString("modName","Brewin and Chewin Legacy");tag.setString("modId","brewinandchewin");
  tag.setBoolean("modRequired",true);tag.setString("itemName",item);
  tag.setInteger("handlerWidth",166);tag.setInteger("handlerHeight",65);tag.setBoolean("multipleWidgetsAllowed",true);
  cpw.mods.fml.common.event.FMLInterModComms.sendMessage("NotEnoughItems","registerHandlerInfo",tag);
 }
 public void loadConfig() {
  API.registerRecipeHandler(new FermentingHandler());
  API.registerUsageHandler(new FermentingHandler());
  API.registerRecipeHandler(new RipeningHandler());
  API.registerUsageHandler(new RipeningHandler());
  API.registerGuiOverlay(com.brewinandchewin.legacy.KegGui.class,FermentingHandler.UID,5,11);
  API.registerGuiOverlayHandler(com.brewinandchewin.legacy.KegGui.class,new codechicken.nei.recipe.DefaultOverlayHandler(5,11),FermentingHandler.UID);
 }
 public String getName() { return "Brewin and Chewin Legacy"; }public String getVersion() { return com.brewinandchewin.legacy.BuildVersion.VERSION; }
}
