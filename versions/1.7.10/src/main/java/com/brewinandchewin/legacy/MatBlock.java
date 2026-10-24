package com.brewinandchewin.legacy;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vectorwing.farmersdelight.legacy.*;
public final class MatBlock extends BlockContainer {
 public MatBlock() { super(Material.carpet);setHardness(.2F);setStepSound(soundTypeGrass);setBlockBounds(3F/16,0,3F/16,13F/16,1F/16,13F/16); }
 public net.minecraft.tileentity.TileEntity createNewTileEntity(World world,int meta) { return new MatTile(); }
 public boolean isOpaqueCube() { return false; }public boolean renderAsNormalBlock() { return false; }public int getRenderType() { return Foundation.proxy.modelRenderId(); }
 public void onBlockPlacedBy(World world,int x,int y,int z,net.minecraft.entity.EntityLivingBase player,ItemStack stack) { world.setBlockMetadataWithNotify(x,y,z,(net.minecraft.util.MathHelper.floor_double(player.rotationYaw*4/360+.5)&3),2); }
 public boolean canPlaceBlockAt(World world,int x,int y,int z) { return world.isSideSolid(x,y-1,z,net.minecraftforge.common.util.ForgeDirection.UP); }
 public void onNeighborBlockChange(World world,int x,int y,int z,Block block) { if(!canPlaceBlockAt(world,x,y,z)) { dropBlockAsItem(world,x,y,z,world.getBlockMetadata(x,y,z),0);world.setBlockToAir(x,y,z); } }
 public boolean onBlockActivated(World world,int x,int y,int z,EntityPlayer player,int side,float hx,float hy,float hz) {
  if(!(world.getTileEntity(x,y,z) instanceof MatTile)) return false;if(world.isRemote) return true;
  MatTile mat=(MatTile)world.getTileEntity(x,y,z);ItemStack held=player.getHeldItem();if(!Stacks.empty(held)) return mat.put(held,player.capabilities.isCreativeMode);
  ItemStack item=mat.take();if(!Stacks.empty(item) && !player.inventory.addItemStackToInventory(item)) world.spawnEntityInWorld(new net.minecraft.entity.item.EntityItem(world,x+.5,y+.5,z+.5,item));return true;
 }
 public void breakBlock(World world,int x,int y,int z,Block block,int meta) { if(!world.isRemote && world.getTileEntity(x,y,z) instanceof MatTile) { ItemStack item=((MatTile)world.getTileEntity(x,y,z)).take();if(!Stacks.empty(item)) world.spawnEntityInWorld(new net.minecraft.entity.item.EntityItem(world,x+.5,y+.5,z+.5,item)); }super.breakBlock(world,x,y,z,block,meta); }
 public boolean hasComparatorInputOverride() { return true; }public int getComparatorInputOverride(World world,int x,int y,int z,int side) { return world.getTileEntity(x,y,z) instanceof MatTile && !Stacks.empty(((MatTile)world.getTileEntity(x,y,z)).getStackInSlot(0))?15:0; }
}
