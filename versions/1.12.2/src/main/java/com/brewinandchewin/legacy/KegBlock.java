package com.brewinandchewin.legacy;
import vectorwing.farmersdelight.legacy.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public final class KegBlock extends BlockContainer {
    public static final PropertyDirection FACING = net.minecraft.block.BlockHorizontal.FACING;
    public static final net.minecraft.block.properties.PropertyBool VERTICAL=net.minecraft.block.properties.PropertyBool.create("vertical");
    private static final AxisAlignedBB SHAPE = new AxisAlignedBB(0.0625,0,0.0625,0.9375,1,0.9375);
    public KegBlock() {
        super(Material.WOOD); setTranslationKey("brewinandchewin.keg"); setCreativeTab(Content.TAB);
        setHardness(2F); setResistance(3F); setSoundType(SoundType.WOOD);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(VERTICAL,false));
    }
    public TileEntity createNewTileEntity(World world, int metadata) { return new KegTile(); }
    @Override public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean harvest) {
        return harvest || super.removedByPlayer(state, world, pos, player, false);
    }
    @Override public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity tile, ItemStack tool) {
        super.harvestBlock(world, player, pos, state, tile, tool); world.setBlockToAir(pos);
    }
    public boolean isOpaqueCube(IBlockState state) { return false; }
    public boolean isFullCube(IBlockState state) { return false; }
    public EnumBlockRenderType getRenderType(IBlockState state) { return EnumBlockRenderType.MODEL; }
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) { boolean vertical=state.getValue(VERTICAL),alongZ=state.getValue(FACING).getAxis()==EnumFacing.Axis.Z;return new AxisAlignedBB(vertical || alongZ ? .0625 : 0,0,vertical || !alongZ ? .0625 : 0,vertical || alongZ ? .9375 : 1,1,vertical || !alongZ ? .9375 : 1); }
    protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, FACING,VERTICAL); }
    public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3)).withProperty(VERTICAL,(meta&4)==0); }
    public int getMetaFromState(IBlockState state) { return state.getValue(FACING).getHorizontalIndex()|(state.getValue(VERTICAL)?0:4); }
    public void neighborChanged(IBlockState state,World world,BlockPos pos,Block block,BlockPos from) { if(world.getTileEntity(pos) instanceof KegTile) ((KegTile)world.getTileEntity(pos)).invalidateTemperature(); }
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing side, float x, float y, float z, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(VERTICAL,Math.abs(placer.rotationPitch)>45);
    }
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (world.getTileEntity(pos) instanceof KegTile) {
            KegTile pot = (KegTile) world.getTileEntity(pos); pot.restore(stack);
            if (stack.hasDisplayName()) pot.customName=stack.getDisplayName();
        }
    }
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float x, float y, float z) {
        if (!(world.getTileEntity(pos) instanceof KegTile)) return false;
        if (!world.isRemote) {
            KegTile tile = (KegTile) world.getTileEntity(pos);
            ItemStack served = tile.serve(player.getHeldItem(hand), player.capabilities.isCreativeMode);
            if (!Stacks.empty(served)) { if (!player.inventory.addItemStackToInventory(served)) tile.drop(served); }
            else player.openGui(BrewinAndChewin.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        TileEntity tile = world.getTileEntity(pos);
        drops.add(tile instanceof KegTile ? ((KegTile) tile).portable() : new ItemStack(this));
    }
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote && world.getTileEntity(pos) instanceof KegTile) {
            KegTile pot = (KegTile) world.getTileEntity(pos);
            for (int i = 0; i < 8; i++) if (i != 5) pot.drop(pot.decrStackSize(i, Stacks.size(pot.getStackInSlot(i))));
            pot.payExperience();
        }
        super.breakBlock(world, pos, state);
    }
    public boolean hasComparatorInputOverride(IBlockState state) { return true; }
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        return Math.min(15,net.minecraft.inventory.Container.calcRedstoneFromInventory((net.minecraft.inventory.IInventory) world.getTileEntity(pos)));
    }
}
