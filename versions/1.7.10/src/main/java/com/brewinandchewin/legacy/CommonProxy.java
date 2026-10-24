package com.brewinandchewin.legacy;
import net.minecraft.entity.player.EntityPlayer;import net.minecraft.world.World;
public class CommonProxy implements cpw.mods.fml.common.network.IGuiHandler {
 public void preInit() {}
 public Object getServerGuiElement(int id,EntityPlayer player,World world,int x,int y,int z) { net.minecraft.tileentity.TileEntity tile=world.getTileEntity(x,y,z);return id==0 && tile instanceof KegTile?new KegContainer(player.inventory,(KegTile)tile):null; }
 public Object getClientGuiElement(int id,EntityPlayer player,World world,int x,int y,int z) { return null; }
}
