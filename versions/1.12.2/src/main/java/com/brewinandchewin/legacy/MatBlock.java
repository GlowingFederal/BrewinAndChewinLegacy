package com.brewinandchewin.legacy;
import net.minecraft.block.*;import net.minecraft.block.material.Material;import net.minecraft.block.state.*;import net.minecraft.block.properties.*;
import net.minecraft.util.*;import net.minecraft.util.math.*;import net.minecraft.world.*;
import net.minecraft.entity.player.EntityPlayer;import net.minecraft.item.ItemStack;import vectorwing.farmersdelight.legacy.*;
public final class MatBlock extends BlockContainer {
 public static final PropertyDirection FACING=BlockHorizontal.FACING;
 public MatBlock() { super(Material.CARPET);setHardness(.2F);setSoundType(SoundType.PLANT);setDefaultState(blockState.getBaseState().withProperty(FACING,EnumFacing.NORTH)); }
 public net.minecraft.tileentity.TileEntity createNewTileEntity(World world,int meta) { return new MatTile(); }
 public boolean isOpaqueCube(IBlockState state) { return false; }public boolean isFullCube(IBlockState state) { return false; }public EnumBlockRenderType getRenderType(IBlockState state) { return EnumBlockRenderType.MODEL; }
 public AxisAlignedBB getBoundingBox(IBlockState state,IBlockAccess world,BlockPos pos) { return new AxisAlignedBB(3D/16,0,3D/16,13D/16,1D/16,13D/16); }
 protected BlockStateContainer createBlockState() { return new BlockStateContainer(this,FACING); }
 public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(FACING,EnumFacing.byHorizontalIndex(meta&3)); }public int getMetaFromState(IBlockState state) { return state.getValue(FACING).getHorizontalIndex(); }
 public IBlockState getStateForPlacement(World world,BlockPos pos,EnumFacing side,float x,float y,float z,int meta,net.minecraft.entity.EntityLivingBase player) { return getDefaultState().withProperty(FACING,player.getHorizontalFacing().getOpposite()); }
 public boolean canPlaceBlockAt(World world,BlockPos pos) { return world.isSideSolid(pos.down(),EnumFacing.UP); }
 public void neighborChanged(IBlockState state,World world,BlockPos pos,Block block,BlockPos from) { if(!canPlaceBlockAt(world,pos)) { dropBlockAsItem(world,pos,state,0);world.setBlockToAir(pos); } }
 public boolean onBlockActivated(World world,BlockPos pos,IBlockState state,EntityPlayer player,EnumHand hand,EnumFacing side,float x,float y,float z) {
  if(!(world.getTileEntity(pos) instanceof MatTile)) return false;if(world.isRemote) return true;
  MatTile mat=(MatTile)world.getTileEntity(pos);ItemStack held=player.getHeldItem(hand);if(!Stacks.empty(held)) return mat.put(held,player.capabilities.isCreativeMode);
  ItemStack item=mat.take();if(!Stacks.empty(item) && !player.inventory.addItemStackToInventory(item)) spawnAsEntity(world,pos,item);return true;
 }
 public void breakBlock(World world,BlockPos pos,IBlockState state) { if(!world.isRemote && world.getTileEntity(pos) instanceof MatTile) { ItemStack item=((MatTile)world.getTileEntity(pos)).take();if(!Stacks.empty(item)) spawnAsEntity(world,pos,item); }super.breakBlock(world,pos,state); }
 public boolean hasComparatorInputOverride(IBlockState state) { return true; }public int getComparatorInputOverride(IBlockState state,World world,BlockPos pos) { return world.getTileEntity(pos) instanceof MatTile && !Stacks.empty(((MatTile)world.getTileEntity(pos)).getStackInSlot(0))?15:0; }
}
