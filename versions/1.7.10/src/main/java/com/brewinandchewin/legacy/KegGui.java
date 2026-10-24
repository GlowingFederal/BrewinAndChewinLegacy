package com.brewinandchewin.legacy;
import net.minecraft.client.gui.inventory.GuiContainer;import net.minecraft.entity.player.InventoryPlayer;import net.minecraft.util.ResourceLocation;
public final class KegGui extends GuiContainer {
 private static final ResourceLocation TEXTURE=new ResourceLocation("brewinandchewin","textures/gui/keg.png");private final KegTile keg;
 public KegGui(InventoryPlayer player,KegTile keg) { super(new KegContainer(player,keg));this.keg=keg;xSize=176;ySize=166; }
 protected void drawGuiContainerForegroundLayer(int x,int y) { fontRendererObj.drawString(keg.customName.isEmpty()?net.minecraft.client.resources.I18n.format("brewinandchewin.container.keg"):keg.customName,8,6,0x404040);fontRendererObj.drawString(net.minecraft.client.resources.I18n.format("container.inventory"),8,73,0x404040); }
 protected void drawGuiContainerBackgroundLayer(float partial,int mouseX,int mouseY) {
  org.lwjgl.opengl.GL11.glColor4f(1,1,1,1);mc.getTextureManager().bindTexture(TEXTURE);int x=guiLeft,y=guiTop;drawTexturedModalRect(x,y,0,0,xSize,ySize);
  int progress=keg.total>0?(int)Math.min(26,(long)keg.progress*26/keg.total):0;drawTexturedModalRect(x+77,y+44,176,28,progress+1,9);
  if(keg.temperature<=-5) drawTexturedModalRect(x+83,y+39,182,0,7,4);if(keg.temperature<=-9) drawTexturedModalRect(x+77,y+39,176,0,6,4);
  if(keg.temperature>=5) drawTexturedModalRect(x+96,y+39,195,0,7,4);if(keg.temperature>=9) drawTexturedModalRect(x+103,y+39,202,0,7,4);
  if(keg.progress>0) { int height=24-new int[]{24,20,16,12,8,4,0}[keg.progress/5%7];drawTexturedModalRect(x+74,y+14,176,4,9,height);drawTexturedModalRect(x+103,y+14,186,4,9,height); }
 }
 public void drawScreen(int x,int y,float partial) {  super.drawScreen(x,y,partial);
  if(x>=guiLeft+77 && x<guiLeft+110 && y>=guiTop+39 && y<guiTop+43) drawHoveringText(java.util.Collections.singletonList(net.minecraft.client.resources.I18n.format("brewinandchewin.container.keg."+new String[]{"frigid","cold","normal","warm","hot"}[Fermentation.category(keg.temperature)-1])),x,y ,fontRendererObj);
 }
}
