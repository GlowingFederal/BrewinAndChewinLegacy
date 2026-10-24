package com.brewinandchewin.legacy;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;import net.minecraft.item.ItemStack;import vectorwing.farmersdelight.legacy.Stacks;import org.lwjgl.opengl.GL11;
public final class MatRenderer extends TileEntitySpecialRenderer<MatTile> {
 public void render(MatTile tile,double x,double y,double z,float partial,int destroyStage,float alpha) {
  if(!(tile instanceof MatTile)) return;ItemStack stack=((MatTile)tile).getStackInSlot(0);if(Stacks.empty(stack)) return;
  GL11.glPushMatrix();try { GL11.glTranslated(x+.5,y+.4+Math.sin(((tile.getWorld().getTotalWorldTime()%100000)+partial)/200D)/20D,z+.5);GL11.glRotatef(((tile.getWorld().getTotalWorldTime()%100000)+partial)*.16F,0,1,0);GL11.glScalef(.5F,.5F,.5F);net.minecraft.client.Minecraft.getMinecraft().getRenderItem().renderItem(stack,net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType.FIXED); }finally { GL11.glPopMatrix(); }
 }
}
