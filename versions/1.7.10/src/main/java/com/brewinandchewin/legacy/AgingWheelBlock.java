package com.brewinandchewin.legacy;
import net.minecraft.block.*;import net.minecraft.block.material.Material;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.item.Item;
import net.minecraft.world.*;
import vectorwing.farmersdelight.legacy.Foundation;
public final class AgingWheelBlock extends Block {
 public static final float ADVANCE_CHANCE = 0.1F;
 private static final int SEMIRIPE = 4;
 private final Block ripeBlock;

 public AgingWheelBlock(Block ripeBlock) {
  super(Material.cake);
  this.ripeBlock = ripeBlock;
  setHardness(.5F);
  setStepSound(soundTypeCloth);
  setTickRandomly(true);
  setBlockBounds(.125F, 0, .125F, .875F, .375F, .875F);
 }

 public Block getRipeBlock() {
  return ripeBlock;
 }

 public static List<AgingWheelBlock> registeredWheels() {
  List<AgingWheelBlock> wheels = new ArrayList<>();
  for (Block block : BrewinAndChewin.BLOCKS.values()) {
   if (block instanceof AgingWheelBlock) {
    AgingWheelBlock wheel = (AgingWheelBlock) block;
    if (Item.getItemFromBlock(wheel) != null && Item.getItemFromBlock(wheel.ripeBlock) != null) wheels.add(wheel);
   }
  }
  return wheels;
 }
 public boolean isOpaqueCube() { return false; }public boolean renderAsNormalBlock() { return false; }public int getRenderType() { return Foundation.proxy.modelRenderId(); }
 public void updateTick(World world, int x, int y, int z, Random random) {
  if (world.isRemote || random.nextFloat() > ADVANCE_CHANCE) return;
  if ((world.getBlockMetadata(x, y, z) & SEMIRIPE) == 0) world.setBlockMetadataWithNotify(x, y, z, SEMIRIPE, 3);
  else world.setBlock(x, y, z, ripeBlock, 0, 3);
 }
 public int damageDropped(int meta) { return 0; }
 public boolean canPlaceBlockAt(World world,int x,int y,int z) { return !world.isAirBlock(x,y-1,z); }
 public void onNeighborBlockChange(World world,int x,int y,int z,Block block) { if(!canPlaceBlockAt(world,x,y,z)) { dropBlockAsItem(world,x,y,z,world.getBlockMetadata(x,y,z),0);world.setBlockToAir(x,y,z); } }
 public boolean hasComparatorInputOverride() { return true; }public int getComparatorInputOverride(World world,int x,int y,int z,int side) { return 2-((world.getBlockMetadata(x,y,z)>>2)&1); }
}
