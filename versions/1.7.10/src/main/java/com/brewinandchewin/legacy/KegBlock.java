package com.brewinandchewin.legacy;
import vectorwing.farmersdelight.legacy.*;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public final class KegBlock extends BlockContainer {
    public KegBlock() {
        super(Material.wood); setBlockName("brewinandchewin.keg"); setCreativeTab(Content.TAB);
        setHardness(2F); setResistance(3F); setStepSound(soundTypeWood);
        setBlockTextureName("brewinandchewin:keg_side"); setBlockBounds(0.0625F,0,0.0625F,0.9375F,1,0.9375F);
    }
    public void setBlockBoundsBasedOnState(net.minecraft.world.IBlockAccess world,int x,int y,int z) {
        int meta=world.getBlockMetadata(x,y,z);boolean vertical=(meta&4)==0,alongZ=(meta&1)==0;
        setBlockBounds(vertical || alongZ ? .0625F : 0,0,vertical || !alongZ ? .0625F : 0,vertical || alongZ ? .9375F : 1,1,vertical || !alongZ ? .9375F : 1);
    }
    public TileEntity createNewTileEntity(World world, int meta) { return new KegTile(); }
    public int onBlockPlaced(World world,int x,int y,int z,int side,float hx,float hy,float hz,int meta) { return side<=1?0:4; }
    public void onNeighborBlockChange(World world,int x,int y,int z,Block block) { if(world.getTileEntity(x,y,z) instanceof KegTile) ((KegTile)world.getTileEntity(x,y,z)).invalidateTemperature(); }
    @Override public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean harvest) {
        return harvest || super.removedByPlayer(world, player, x, y, z, false);
    }
    @Override public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
        super.harvestBlock(world, player, x, y, z, meta); world.setBlockToAir(x, y, z);
    }
    public boolean isOpaqueCube() { return false; }
    public boolean renderAsNormalBlock() { return false; }
    public int getRenderType() { return Foundation.proxy.modelRenderId(); }
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        world.setBlockMetadataWithNotify(x, y, z, (Math.abs(placer.rotationPitch)>45 ? 0 : 4)|(MathHelper.floor_double(placer.rotationYaw * 4 / 360 + 0.5) & 3), 2);
        if (world.getTileEntity(x, y, z) instanceof KegTile) {
            KegTile pot = (KegTile) world.getTileEntity(x, y, z); pot.restore(stack);
            if (stack.hasDisplayName()) pot.customName=stack.getDisplayName();
        }
    }
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hx, float hy, float hz) {
        if (!(world.getTileEntity(x, y, z) instanceof KegTile)) return false;
        if (!world.isRemote) {
            KegTile tile = (KegTile) world.getTileEntity(x, y, z);
            ItemStack served = tile.serve(player.getHeldItem(), player.capabilities.isCreativeMode);
            if (!Stacks.empty(served)) { if (!player.inventory.addItemStackToInventory(served)) tile.drop(served); }
            else player.openGui(BrewinAndChewin.instance, 0, world, x, y, z);
        }
        return true;
    }
    @Override public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        TileEntity tile = world.getTileEntity(x, y, z);
        drops.add(tile instanceof KegTile ? ((KegTile) tile).portable() : new ItemStack(this)); return drops;
    }
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        if (!world.isRemote && world.getTileEntity(x, y, z) instanceof KegTile) {
            KegTile pot = (KegTile) world.getTileEntity(x, y, z);
            for (int i = 0; i < 8; i++) if (i != 5) pot.drop(pot.decrStackSize(i, Stacks.size(pot.getStackInSlot(i))));
            pot.payExperience();
        }
        super.breakBlock(world, x, y, z, block, meta);
    }
    public boolean hasComparatorInputOverride() { return true; }
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        return Math.min(15,net.minecraft.inventory.Container.calcRedstoneFromInventory((net.minecraft.inventory.IInventory) world.getTileEntity(x, y, z)));
    }
}
