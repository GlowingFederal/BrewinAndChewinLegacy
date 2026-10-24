package com.brewinandchewin.legacy;
import net.minecraft.block.*;import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;import net.minecraft.entity.player.EntityPlayer;import net.minecraft.world.*;
import vectorwing.farmersdelight.legacy.*;
import net.minecraft.util.*;import net.minecraft.util.math.*;import net.minecraft.block.state.*;import net.minecraft.block.properties.*;
public final class WheelBlock extends Block {
 private final String type;private final boolean pizza;
 public static final PropertyInteger SERVINGS=PropertyInteger.create("servings",0,3);
 public WheelBlock(String type,boolean pizza) { super(Material.CAKE);this.type=type;this.pizza=pizza;setHardness(.5F);setSoundType(SoundType.CLOTH);setDefaultState(blockState.getBaseState().withProperty(SERVINGS,3)); }
 public ItemStack portion() { return Stacks.resolve("brewinandchewin:"+(pizza?"pizza_slice":type+"_cheese_wedge")); }
 public boolean isOpaqueCube(IBlockState state) { return false; }public boolean isFullCube(IBlockState state) { return false; }
 public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess world,BlockPos pos) { int remaining=state.getValue(SERVINGS);double min=pizza?0:.125,max=pizza?1:.875;return new AxisAlignedBB(min,0,min,remaining==0?.5:max,pizza?.125:.375,remaining<=1?.5:max); }
 protected BlockStateContainer createBlockState() { return new BlockStateContainer(this,SERVINGS); }
 public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(SERVINGS,3-((meta>>2)&3)); }public int getMetaFromState(IBlockState state) { return (3-state.getValue(SERVINGS))<<2; }
 public void addCollisionBoxToList(IBlockState state,World world,BlockPos pos,AxisAlignedBB mask,java.util.List<AxisAlignedBB> boxes,net.minecraft.entity.Entity entity,boolean actual) {
  if(state.getValue(SERVINGS)!=2) { addCollisionBoxToList(pos,mask,boxes,getBoundingBox(state,world,pos));return; }
  double min=pizza?0:.125,max=pizza?1:.875,height=pizza?.125:.375;
  addCollisionBoxToList(pos,mask,boxes,new AxisAlignedBB(min,0,min,max,height,.5));addCollisionBoxToList(pos,mask,boxes,new AxisAlignedBB(min,0,.5,.5,height,max));
 }
 public boolean onBlockActivated(World world,BlockPos pos,IBlockState state,EntityPlayer player,EnumHand hand,EnumFacing side,float x,float y,float z) {
  if(!pizza && !Ingredients.isKnife(player.getHeldItem(hand))) return true;
  if(!world.isRemote) { ItemStack portion=portion();if(!pizza || !player.inventory.addItemStackToInventory(portion)) spawnAsEntity(world,pos,portion);int remaining=state.getValue(SERVINGS);if(remaining==0) world.setBlockToAir(pos);else world.setBlockState(pos,state.withProperty(SERVINGS,remaining-1),3); }return true;
 }
 public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops,IBlockAccess world,BlockPos pos,IBlockState state,int fortune) { if(state.getValue(SERVINGS)==3) drops.add(new ItemStack(this));else drops.add(Stacks.copy(portion(),state.getValue(SERVINGS)+1)); }
 public boolean hasComparatorInputOverride(IBlockState state) { return true; }public int getComparatorInputOverride(IBlockState state,World world,BlockPos pos) { return state.getValue(SERVINGS); }
 public boolean canPlaceBlockAt(World world,BlockPos pos) { return !world.isAirBlock(pos.down()); }public void neighborChanged(IBlockState state,World world,BlockPos pos,Block block,BlockPos from) { if(!canPlaceBlockAt(world,pos)) { dropBlockAsItem(world,pos,state,0);world.setBlockToAir(pos); } }
}
