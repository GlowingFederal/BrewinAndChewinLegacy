package com.brewinandchewin.legacy;
import net.minecraft.entity.player.*;import net.minecraft.inventory.*;import net.minecraft.item.ItemStack;
import vectorwing.farmersdelight.legacy.Stacks;
public final class KegContainer extends Container {
 public final KegTile keg;private final int[] last={-1,-1,-1,-1,-100};
 public KegContainer(InventoryPlayer player,final KegTile keg) {
  this.keg=keg;
  for(int i=0;i<4;i++) addSlotToContainer(new Slot(keg,i,33+i%2*18,28+i/2*18));addSlotToContainer(new Slot(keg,4,85,18));
  addSlotToContainer(new Slot(keg,5,122,23) { public boolean isItemValid(ItemStack stack) { return false; }public boolean canTakeStack(EntityPlayer player) { return false; } });
  addSlotToContainer(new Slot(keg,6,90,55));addSlotToContainer(new Slot(keg,7,122,55) { public boolean isItemValid(ItemStack stack) { return false; }public void onPickupFromSlot(EntityPlayer player,ItemStack stack) { keg.payExperience();super.onPickupFromSlot(player,stack); } });
  for(int row=0;row<3;row++) for(int column=0;column<9;column++) addSlotToContainer(new Slot(player,9+row*9+column,8+column*18,84+row*18));
  for(int column=0;column<9;column++) addSlotToContainer(new Slot(player,column,8+column*18,142));
 }
 public boolean canInteractWith(EntityPlayer player) { return keg.isUseableByPlayer(player); }
 public boolean canDragIntoSlot(Slot slot) { return slot.slotNumber!=5 && slot.slotNumber!=7 && super.canDragIntoSlot(slot); }
 public ItemStack transferStackInSlot(EntityPlayer player,int index) {
  if(index<0 || index>=inventorySlots.size() || index==5) return Stacks.none();Slot slot=(Slot)inventorySlots.get(index);if(!slot.getHasStack()) return Stacks.none();
  ItemStack stack=slot.getStack(),before=stack.copy();boolean moved;
  if(index<8) moved=mergeItemStack(stack,8,44,true);
  else if(keg.isContainer(stack) || Fermentation.container(stack)) moved=mergeItemStack(stack,6,7,false);
  else if(Fermentation.liquid(stack)) moved=mergeItemStack(stack,4,5,false);
  else moved=mergeItemStack(stack,0,4,false);
  if(!moved || Stacks.size(stack)==Stacks.size(before)) return Stacks.none();if(Stacks.empty(stack)) slot.putStack(Stacks.none());else slot.onSlotChanged();slot.onPickupFromSlot(player,stack);return before;
 }
 private int[] values() { return new int[]{keg.progress&65535,(keg.progress>>>16)&65535,keg.total&65535,(keg.total>>>16)&65535,keg.temperature}; }
 public void detectAndSendChanges() {
  super.detectAndSendChanges();int[] values=values();for(Object entry:crafters) { ICrafting listener=(ICrafting)entry;for(int i=0;i<5;i++) if(last[i]!=values[i]) listener.sendProgressBarUpdate(this,i,values[i]); }System.arraycopy(values,0,last,0,5);
 }
 public void addCraftingToCrafters(ICrafting listener) { super.addCraftingToCrafters(listener);int[] values=values();for(int i=0;i<5;i++) listener.sendProgressBarUpdate(this,i,values[i]); }
 public void updateProgressBar(int id,int value) { int half=value&65535;if(id==0) keg.progress=(keg.progress&0xffff0000)|half;else if(id==1) keg.progress=(keg.progress&65535)|(half<<16);else if(id==2) keg.total=(keg.total&0xffff0000)|half;else if(id==3) keg.total=(keg.total&65535)|(half<<16);else if(id==4) keg.temperature=(short)value; }
}
