package com.brewinandchewin.legacy;
public final class ClientProxy extends CommonProxy {
 public void preInit() {
  net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(new Tooltips());
  net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
  net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer(MatTile.class,new MatRenderer());
 }
 public Object getClientGuiElement(int id,net.minecraft.entity.player.EntityPlayer player,net.minecraft.world.World world,int x,int y,int z) { net.minecraft.tileentity.TileEntity tile=world.getTileEntity(new net.minecraft.util.math.BlockPos(x,y,z));return id==0 && tile instanceof KegTile?new KegGui(player.inventory,(KegTile)tile):null; }
 @net.minecraftforge.fml.common.eventhandler.SubscribeEvent public void models(net.minecraftforge.client.event.ModelRegistryEvent event) { for(java.util.Map.Entry<String,net.minecraft.item.Item> entry:BrewinAndChewin.ITEMS.entrySet()) net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(entry.getValue(),0,new net.minecraft.client.renderer.block.model.ModelResourceLocation("brewinandchewin:"+entry.getKey(),"inventory")); }
}
