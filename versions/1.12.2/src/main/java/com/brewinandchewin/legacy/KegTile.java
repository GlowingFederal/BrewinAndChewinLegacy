package com.brewinandchewin.legacy;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vectorwing.farmersdelight.legacy.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
public final class KegTile extends InventoryTile implements net.minecraft.util.ITickable {
 public int progress,total=12000,temperature;public String customName="";
 private String recipeId="";private Fermentation.Recipe cached;private boolean dirty=true;
 private int temperatureTicks;private float experience;
 private ItemStack servingContainer=Stacks.none();
 public KegTile() { super(8); }
 @Override public void markDirty() { dirty=true;super.markDirty(); }
 public void update() {
  if(world==null || world.isRemote) return;
  if(temperatureTicks--<=0) { temperatureTicks=20;updateTemperature(); }
  if(dirty) {
   Fermentation.Recipe next=Fermentation.find(this);String nextId=next==null?"":next.id;
   if(!recipeId.equals(nextId)) progress=0;
   recipeId=nextId;cached=next;dirty=false;
  }
  boolean inputs=false;for(int i=0;i<5;i++) inputs|=!Stacks.empty(inventory[i]);
  if(!inputs) progress=Math.max(0,progress-2);
  else if(cached!=null && Fermentation.category(temperature)==cached.temperature && canFinish(cached)) {
   total=cached.time;progress++;if(progress>=total) finish(cached);
  } else progress=0;
  serveStored();super.markDirty();
 }
 public void invalidateTemperature() { temperatureTicks=0; }
 public void updateTemperature() {
  int value=0;
  for(int x=-1;x<=1;x++) for(int y=-1;y<=1;y++) for(int z=-1;z<=1;z++) {
   net.minecraft.util.math.BlockPos at=pos.add(x,y,z);net.minecraft.block.Block block=world.getBlockState(at).getBlock();if(Heat.heated(world,at.up(),true)) value++;if(block==net.minecraft.init.Blocks.ICE || block==net.minecraft.init.Blocks.PACKED_ICE || block==net.minecraft.init.Blocks.SNOW) value--;
  }
  if(temperature!=value) { temperature=value;changed(); }
 }
 private boolean canFinish(Fermentation.Recipe recipe) {
  return recipe.matches(this) && (Stacks.empty(inventory[5]) || (Stacks.same(inventory[5],recipe.result) && (Stacks.empty(servingContainer) && Stacks.empty(recipe.container) || Stacks.same(servingContainer,recipe.container))))
   && Stacks.size(inventory[5])+Stacks.size(recipe.result)<=64;
 }
 private void finish(Fermentation.Recipe recipe) {
  if(!canFinish(recipe)) { progress=0;return; }
  for(int i=0;i<5;i++) if(!Stacks.empty(inventory[i])) {
   ItemStack consumed=Stacks.copy(inventory[i],1);Stacks.shrink(inventory[i],1);
   if(consumed.getItem().hasContainerItem(consumed)) drop(consumed.getItem().getContainerItem(consumed));
  }
  if(Stacks.empty(inventory[5])) inventory[5]=recipe.result.copy();else Stacks.count(inventory[5],Stacks.size(inventory[5])+Stacks.size(recipe.result));
  servingContainer=Stacks.copy(recipe.container,1);experience+=recipe.experience;progress=0;changed();
 }
 public boolean isContainer(ItemStack stack) { return !Stacks.empty(servingContainer) && Stacks.same(servingContainer,stack); }
 private void serveStored() {
  if(Stacks.empty(inventory[5])) return;
  if(!Stacks.empty(inventory[7]) && !Stacks.same(inventory[5],inventory[7])) return;
  int amount=Math.min(Stacks.size(inventory[5]),inventory[5].getMaxStackSize()-Stacks.size(inventory[7]));
  if(!Stacks.empty(servingContainer)) { if(!isContainer(inventory[6])) return;amount=Math.min(amount,Stacks.size(inventory[6])); }
  if(amount<=0) return;
  if(Stacks.empty(inventory[7])) inventory[7]=Stacks.copy(inventory[5],amount);else Stacks.count(inventory[7],Stacks.size(inventory[7])+amount);
  Stacks.shrink(inventory[5],amount);if(!Stacks.empty(servingContainer)) Stacks.shrink(inventory[6],amount);changed();
 }
 public ItemStack serve(ItemStack held,boolean creative) {
  if(!isContainer(held) || Stacks.empty(inventory[5])) return Stacks.none();
  ItemStack result=Stacks.copy(inventory[5],1);Stacks.shrink(inventory[5],1);if(!creative) Stacks.shrink(held,1);changed();return result;
 }
 @Override public boolean isItemValidForSlot(int slot,ItemStack stack) { return slot>=0 && slot<5 || slot==6 && (isContainer(stack) || Fermentation.container(stack)); }
 public int[] getSlotsForFace(net.minecraft.util.EnumFacing side) { return side==net.minecraft.util.EnumFacing.UP?new int[]{0,1,2,3,4}:new int[]{6,7}; }
 public boolean canInsertItem(int slot,ItemStack stack,net.minecraft.util.EnumFacing side) { return side==net.minecraft.util.EnumFacing.UP?slot<5:slot==6 && isItemValidForSlot(slot,stack); }
 public boolean canExtractItem(int slot,ItemStack stack,net.minecraft.util.EnumFacing side) { return side==net.minecraft.util.EnumFacing.UP?slot<5:slot==7; }
 public void drop(ItemStack stack) { if(!Stacks.empty(stack) && world!=null && !world.isRemote) world.spawnEntity(new EntityItem(world,pos.getX()+.5,pos.getY()+.5,pos.getZ()+.5,stack)); }
 public void payExperience() {
  if(world==null || world.isRemote) return;int amount=(int)experience;if(world.rand.nextFloat()<experience-amount) amount++;experience=0;
  while(amount>0) { int split=EntityXPOrb.getXPSplit(amount);amount-=split;world.spawnEntity(new EntityXPOrb(world,pos.getX()+.5,pos.getY()+.5,pos.getZ()+.5,split)); }super.markDirty();
 }
 public ItemStack portable() {
  ItemStack stack=Stacks.resolve("brewinandchewin:keg");NBTTagCompound tag=new NBTTagCompound();
  if(!Stacks.empty(inventory[5])) { NBTTagCompound meal=new NBTTagCompound();inventory[5].writeToNBT(meal);tag.setTag("Meal",meal); }
  if(!Stacks.empty(servingContainer)) { NBTTagCompound container=new NBTTagCompound();servingContainer.writeToNBT(container);tag.setTag("Container",container); }
  if(!customName.isEmpty()) stack.setStackDisplayName(customName);stack.setTagCompound(tag);return stack;
 }
 public void restore(ItemStack stack) {
  if(stack.hasTagCompound()) { NBTTagCompound tag=stack.getTagCompound();if(tag.hasKey("Meal")) inventory[5]=new ItemStack(tag.getCompoundTag("Meal"));if(tag.hasKey("Container")) servingContainer=new ItemStack(tag.getCompoundTag("Container")); }
  if(stack.hasDisplayName()) customName=stack.getDisplayName();changed();
 }
 @Override public void readFromNBT(NBTTagCompound tag) {
  super.readFromNBT(tag);progress=Math.max(0,tag.getInteger("Progress"));total=Math.max(1,tag.getInteger("Total"));temperature=tag.getInteger("Temperature");recipeId=tag.getString("Recipe");customName=tag.getString("CustomName");experience=Math.max(0,tag.getFloat("Experience"));
  servingContainer=tag.hasKey("Container")?new ItemStack(tag.getCompoundTag("Container")):Stacks.none();dirty=true;temperatureTicks=0;
 }
 @Override public NBTTagCompound writeToNBT(NBTTagCompound tag) {
  super.writeToNBT(tag);tag.setInteger("Progress",progress);tag.setInteger("Total",total);tag.setInteger("Temperature",temperature);tag.setString("Recipe",recipeId);tag.setString("CustomName",customName);tag.setFloat("Experience",experience);
  NBTTagCompound container=new NBTTagCompound();if(!Stacks.empty(servingContainer)) servingContainer.writeToNBT(container);tag.setTag("Container",container);return tag;
 }
 public String getName() { return customName.isEmpty()?"brewinandchewin.container.keg":customName; }
 public boolean hasCustomName() { return !customName.isEmpty(); }
 public net.minecraft.util.text.ITextComponent getDisplayName() { return customName.isEmpty()?new net.minecraft.util.text.TextComponentTranslation(getName()):new net.minecraft.util.text.TextComponentString(customName); }
}
