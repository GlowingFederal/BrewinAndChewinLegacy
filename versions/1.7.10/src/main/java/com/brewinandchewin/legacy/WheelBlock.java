package com.brewinandchewin.legacy;
import net.minecraft.block.*;import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;import net.minecraft.entity.player.EntityPlayer;import net.minecraft.world.*;
import vectorwing.farmersdelight.legacy.*;
public final class WheelBlock extends Block {
 private final String type;private final boolean pizza;
 
 public WheelBlock(String type,boolean pizza) { super(Material.cake);this.type=type;this.pizza=pizza;setHardness(.5F);setStepSound(soundTypeCloth);setBlockBounds(pizza?0:.125F,0,pizza?0:.125F,pizza?1:.875F,pizza?.125F:.375F,pizza?1:.875F); }
 public ItemStack portion() { return Stacks.resolve("brewinandchewin:"+(pizza?"pizza_slice":type+"_cheese_wedge")); }
 public boolean isOpaqueCube() { return false; }public boolean renderAsNormalBlock() { return false; }public int getRenderType() { return Foundation.proxy.modelRenderId(); }
 public void setBlockBoundsBasedOnState(IBlockAccess world,int x,int y,int z) {
  int remaining=3-((world.getBlockMetadata(x,y,z)>>2)&3);float min=pizza?0:.125F,max=pizza?1:.875F;
  setBlockBounds(min,0,min,remaining==0?.5F:max,pizza?.125F:.375F,remaining<=1?.5F:max);
 }
 public void addCollisionBoxesToList(World world,int x,int y,int z,net.minecraft.util.AxisAlignedBB mask,java.util.List boxes,net.minecraft.entity.Entity entity) {
  setBlockBoundsBasedOnState(world,x,y,z);int remaining=3-((world.getBlockMetadata(x,y,z)>>2)&3);
  if(remaining!=2) { super.addCollisionBoxesToList(world,x,y,z,mask,boxes,entity);return; }
  double min=pizza?0:.125,max=pizza?1:.875,height=pizza?.125:.375;
  net.minecraft.util.AxisAlignedBB front=net.minecraft.util.AxisAlignedBB.getBoundingBox(x+min,y,z+min,x+max,y+height,z+.5);
  net.minecraft.util.AxisAlignedBB rear=net.minecraft.util.AxisAlignedBB.getBoundingBox(x+min,y,z+.5,x+.5,y+height,z+max);
  if(mask.intersectsWith(front)) boxes.add(front);if(mask.intersectsWith(rear)) boxes.add(rear);
 }
 public boolean onBlockActivated(World world,int x,int y,int z,EntityPlayer player,int side,float hx,float hy,float hz) {
   if(!pizza && !Ingredients.isKnife(player.getHeldItem())) return true;
   if(!world.isRemote) { int bites=(world.getBlockMetadata(x,y,z)>>2)&3;ItemStack portion=portion();
    if(!pizza || !player.inventory.addItemStackToInventory(portion)) world.spawnEntityInWorld(new net.minecraft.entity.item.EntityItem(world,x+.5,y+.5,z+.5,portion));
    if(bites==3) world.setBlockToAir(x,y,z);else world.setBlockMetadataWithNotify(x,y,z,(bites+1)<<2,3);
   }return true;
  }
 public java.util.ArrayList<ItemStack> getDrops(World world,int x,int y,int z,int meta,int fortune) { java.util.ArrayList<ItemStack> drops=new java.util.ArrayList<ItemStack>();int bites=(meta>>2)&3;if(bites==0) drops.add(new ItemStack(this));else drops.add(Stacks.copy(portion(),4-bites));return drops; }
 public boolean hasComparatorInputOverride() { return true; }public int getComparatorInputOverride(World world,int x,int y,int z,int side) { return 3-((world.getBlockMetadata(x,y,z)>>2)&3); }
 public boolean canPlaceBlockAt(World world,int x,int y,int z) { return !world.isAirBlock(x,y-1,z); }public void onNeighborBlockChange(World world,int x,int y,int z,Block block) { if(!canPlaceBlockAt(world,x,y,z)) { dropBlockAsItem(world,x,y,z,world.getBlockMetadata(x,y,z),0);world.setBlockToAir(x,y,z); } }
}
