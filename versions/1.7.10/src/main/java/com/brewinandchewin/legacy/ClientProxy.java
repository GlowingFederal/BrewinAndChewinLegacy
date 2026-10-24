package com.brewinandchewin.legacy;
public final class ClientProxy extends CommonProxy {
 public void preInit() {
  net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new Tooltips());
  vectorwing.farmersdelight.legacy.CommonProxy core=vectorwing.farmersdelight.legacy.Foundation.proxy;
  int[] keg=new int[16],slice=new int[16],age=new int[16];for(int i=0;i<16;i++) { keg[i]=(i&4)==0?1:0;slice[i]=(i>>2)&3;age[i]=(i>>2)&1; }
  core.registerMetadataModels(BrewinAndChewin.BLOCKS.get("keg"),"brewinandchewin",new String[]{"keg","vertical_keg"},keg);
  core.registerMetadataModels(BrewinAndChewin.BLOCKS.get("item_mat"),"brewinandchewin",new String[]{"item_mat"},new int[16]);
  for(String type:new String[]{"flaxen","scarlet"}) {
   core.registerMetadataModels(BrewinAndChewin.BLOCKS.get("unripe_"+type+"_cheese_wheel"),"brewinandchewin",new String[]{"unripe_"+type+"_cheese_wheel","semiripe_"+type+"_cheese_wheel"},age);
   core.registerMetadataModels(BrewinAndChewin.BLOCKS.get(type+"_cheese_wheel"),"brewinandchewin",new String[]{type+"_cheese_wheel",type+"_cheese_wheel1",type+"_cheese_wheel2",type+"_cheese_wheel3"},slice);
  }
  core.registerMetadataModels(BrewinAndChewin.BLOCKS.get("pizza"),"brewinandchewin",new String[]{"pizza","pizza_slice1","pizza_slice2","pizza_slice3"},slice);
  cpw.mods.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(MatTile.class,new MatRenderer());
 }
 public Object getClientGuiElement(int id,net.minecraft.entity.player.EntityPlayer player,net.minecraft.world.World world,int x,int y,int z) { net.minecraft.tileentity.TileEntity tile=world.getTileEntity(x,y,z);return id==0 && tile instanceof KegTile?new KegGui(player.inventory,(KegTile)tile):null; }
 
}
