package com.brewinandchewin.legacy.mixin;
import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(net.minecraft.client.renderer.RenderBlocks.class) public abstract class MatDamageMixin {
 @Inject(method="renderBlockUsingTexture",at=@At("HEAD"),cancellable=true,require=0)
 private void hide(net.minecraft.block.Block block,int x,int y,int z,net.minecraft.util.IIcon icon,CallbackInfo ci) { if(block instanceof com.brewinandchewin.legacy.MatBlock) ci.cancel(); }
}
