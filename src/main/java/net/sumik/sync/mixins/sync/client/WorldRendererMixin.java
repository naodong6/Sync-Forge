package net.sumik.sync.mixins.sync.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sumik.sync.client.render.MatrixStackStorage;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(value = LevelRenderer.class, priority = 1010)
abstract class WorldRendererMixin {
    @Shadow @Final
    private Minecraft minecraft;

    /**
     * This method forces renderer to render the player when they aren't a camera entity.
     */
    @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;getEntity()Lnet/minecraft/world/entity/Entity;", ordinal = 3), require = 1)
    private Entity getFocusedEntity(Camera camera) {
        LocalPlayer player = this.minecraft.player;
        if (player != null && player != this.minecraft.getCameraEntity() && !player.isSpectator()) {
            return player;
        }
        return camera.getEntity();
    }

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void render(PoseStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightmapTextureManager, Matrix4f projectionMatrix, CallbackInfo ci) {
        MatrixStackStorage.saveModelMatrixStack(matrices);
    }
}