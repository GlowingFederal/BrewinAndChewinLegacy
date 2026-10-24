package com.brewinandchewin.legacy;
import net.minecraft.block.*;import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;import net.minecraft.entity.player.EntityPlayer;import net.minecraft.world.*;
import vectorwing.farmersdelight.legacy.*;
import net.minecraft.util.math.*;import net.minecraft.block.state.*;import net.minecraft.block.properties.*;
public final class AgingWheelBlock extends Block {
 public static final PropertyInteger AGE=PropertyInteger.create("age",0,1);private final String type;
 public AgingWheelBlock(String type) { super(Material.CAKE);this.type=type;setHardness(.5F);setSoundType(SoundType.CLOTH);setTickRandomly(true);setDefaultState(blockState.getBaseState().withProperty(AGE,0)); }
 public boolean isOpaqueCube(IBlockState state) { return false; }public boolean isFullCube(IBlockState state) { return false; }
 public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess world,BlockPos pos) { return new AxisAlignedBB(.125,0,.125,.875,.375,.875); }
 protected BlockStateContainer createBlockState() { return new BlockStateContainer(this,AGE); }
 public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(AGE,(meta>>2)&1); }public int getMetaFromState(IBlockState state) { return state.getValue(AGE)<<2; }
 public void updateTick(World world,BlockPos pos,IBlockState state,java.util.Random random) { if(world.isRemote || random.nextFloat()>.1F) return;if(state.getValue(AGE)==0) world.setBlockState(pos,state.withProperty(AGE,1),3);else world.setBlockState(pos,BrewinAndChewin.BLOCKS.get(type+"_cheese_wheel").getDefaultState(),3); }
 public int damageDropped(IBlockState state) { return 0; }
 public boolean canPlaceBlockAt(World world,BlockPos pos) { return !world.isAirBlock(pos.down()); }public void neighborChanged(IBlockState state,World world,BlockPos pos,Block block,BlockPos from) { if(!canPlaceBlockAt(world,pos)) { dropBlockAsItem(world,pos,state,0);world.setBlockToAir(pos); } }
 public boolean hasComparatorInputOverride(IBlockState state) { return true; }public int getComparatorInputOverride(IBlockState state,World world,BlockPos pos) { return 2-state.getValue(AGE); }
}
