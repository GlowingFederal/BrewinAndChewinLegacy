package com.brewinandchewin.legacy;
import net.minecraft.block.*;import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;import net.minecraft.entity.player.EntityPlayer;import net.minecraft.world.*;
import vectorwing.farmersdelight.legacy.*;
public final class AgingWheelBlock extends Block {
 private final String type;
 public AgingWheelBlock(String type) { super(Material.cake);this.type=type;setHardness(.5F);setStepSound(soundTypeCloth);setTickRandomly(true);setBlockBounds(.125F,0,.125F,.875F,.375F,.875F); }
 public boolean isOpaqueCube() { return false; }public boolean renderAsNormalBlock() { return false; }public int getRenderType() { return Foundation.proxy.modelRenderId(); }
 public void updateTick(World world,int x,int y,int z,java.util.Random random) { if(world.isRemote || random.nextFloat()>.1F) return;if((world.getBlockMetadata(x,y,z)&4)==0) world.setBlockMetadataWithNotify(x,y,z,4,3);else world.setBlock(x,y,z,BrewinAndChewin.BLOCKS.get(type+"_cheese_wheel"),0,3); }
 public int damageDropped(int meta) { return 0; }
 public boolean canPlaceBlockAt(World world,int x,int y,int z) { return !world.isAirBlock(x,y-1,z); }
 public void onNeighborBlockChange(World world,int x,int y,int z,Block block) { if(!canPlaceBlockAt(world,x,y,z)) { dropBlockAsItem(world,x,y,z,world.getBlockMetadata(x,y,z),0);world.setBlockToAir(x,y,z); } }
 public boolean hasComparatorInputOverride() { return true; }public int getComparatorInputOverride(World world,int x,int y,int z,int side) { return 2-((world.getBlockMetadata(x,y,z)>>2)&1); }
}
