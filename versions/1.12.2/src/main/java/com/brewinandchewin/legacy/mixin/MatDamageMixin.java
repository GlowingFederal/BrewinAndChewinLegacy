package com.brewinandchewin.legacy.mixin;
import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.*;import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(net.minecraft.client.renderer.BlockRendererDispatcher.class) public abstract class MatDamageMixin {
 @Inject(method="renderBlockDamage",at=@At("HEAD"),cancellable=true,require=0)
 private void hide(net.minecraft.block.state.IBlockState state,net.minecraft.util.math.BlockPos pos,net.minecraft.client.renderer.texture.TextureAtlasSprite texture,net.minecraft.world.IBlockAccess world,CallbackInfo ci) { if(state.getBlock() instanceof com.brewinandchewin.legacy.MatBlock) ci.cancel(); }
}
