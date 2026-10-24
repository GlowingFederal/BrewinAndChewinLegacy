package com.brewinandchewin.legacy;
import vectorwing.farmersdelight.legacy.*;
import net.minecraft.item.ItemStack;
public final class MatTile extends InventoryTile {
 public MatTile() { super(1); }
 public int getInventoryStackLimit() { return 1; }
 public boolean isItemValidForSlot(int slot,ItemStack stack) { return slot==0 && Stacks.empty(inventory[0]); }
 public ItemStack take() { ItemStack stack=inventory[0];inventory[0]=Stacks.none();changed();return stack; }
 public boolean put(ItemStack held,boolean creative) { if(Stacks.empty(held) || !Stacks.empty(inventory[0])) return false;inventory[0]=Stacks.copy(held,1);if(!creative) Stacks.shrink(held,1);changed();return true; }
 public boolean canUpdate() { return false; }
}
