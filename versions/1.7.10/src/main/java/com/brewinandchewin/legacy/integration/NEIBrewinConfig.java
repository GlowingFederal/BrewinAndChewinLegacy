package com.brewinandchewin.legacy.integration;
import codechicken.nei.api.*;
public final class NEIBrewinConfig implements IConfigureNEI {
 public static void registerHandlerInfo() {
  net.minecraft.nbt.NBTTagCompound tag=new net.minecraft.nbt.NBTTagCompound();
  tag.setString("handler","com.brewinandchewin.legacy.integration.FermentingHandler");
  tag.setString("modName","Brewin and Chewin Legacy");tag.setString("modId","brewinandchewin");
  tag.setBoolean("modRequired",true);tag.setString("itemName","brewinandchewin:keg");
  tag.setInteger("handlerWidth",166);tag.setInteger("handlerHeight",65);tag.setBoolean("multipleWidgetsAllowed",true);
  cpw.mods.fml.common.event.FMLInterModComms.sendMessage("NotEnoughItems","registerHandlerInfo",tag);
  net.minecraft.nbt.NBTTagCompound catalyst=new net.minecraft.nbt.NBTTagCompound();
  catalyst.setString("handlerID",FermentingHandler.UID);catalyst.setString("itemName","brewinandchewin:keg");
  cpw.mods.fml.common.event.FMLInterModComms.sendMessage("NotEnoughItems","registerCatalystInfo",catalyst);
 }
 public void loadConfig() { API.registerRecipeHandler(new FermentingHandler());API.registerUsageHandler(new FermentingHandler());API.registerGuiOverlay(com.brewinandchewin.legacy.KegGui.class,FermentingHandler.UID,5,11);API.registerGuiOverlayHandler(com.brewinandchewin.legacy.KegGui.class,new codechicken.nei.recipe.DefaultOverlayHandler(5,11),FermentingHandler.UID); }
 public String getName() { return "Brewin and Chewin Legacy"; }public String getVersion() { return com.brewinandchewin.legacy.BuildVersion.VERSION; }
}
