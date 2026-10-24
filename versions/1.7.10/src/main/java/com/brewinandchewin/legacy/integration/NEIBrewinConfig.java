package com.brewinandchewin.legacy.integration;
import codechicken.nei.api.*;
public final class NEIBrewinConfig implements IConfigureNEI {
 public void loadConfig() { API.registerRecipeHandler(new FermentingHandler());API.registerUsageHandler(new FermentingHandler());API.registerGuiOverlay(com.brewinandchewin.legacy.KegGui.class,FermentingHandler.UID,5,11);API.registerGuiOverlayHandler(com.brewinandchewin.legacy.KegGui.class,new codechicken.nei.recipe.DefaultOverlayHandler(5,11),FermentingHandler.UID); }
 public String getName() { return "Brewin and Chewin Legacy"; }public String getVersion() { return com.brewinandchewin.legacy.BuildVersion.VERSION; }
}
