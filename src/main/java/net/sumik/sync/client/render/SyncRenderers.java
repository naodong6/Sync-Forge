package net.sumik.sync.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.sumik.sync.client.render.block.entity.ShellConstructorBlockEntityRenderer;
import net.sumik.sync.client.render.block.entity.ShellStorageBlockEntityRenderer;
import net.sumik.sync.client.render.block.entity.TreadmillBlockEntityRenderer;
import net.sumik.sync.common.block.entity.SyncBlockEntities;
import net.sumik.sync.common.block.SyncBlocks;
import net.sumik.sync.common.item.SyncItems;

@OnlyIn(Dist.CLIENT)
public final class SyncRenderers {
    public static void initClient() {
    }

    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(SyncBlockEntities.SHELL_STORAGE.get(), ShellStorageBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(SyncBlockEntities.SHELL_CONSTRUCTOR.get(), ShellConstructorBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(SyncBlockEntities.TREADMILL.get(), TreadmillBlockEntityRenderer::new);
    }

    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(createItemRenderer(SyncBlockEntities.SHELL_STORAGE.get(), SyncBlocks.SHELL_STORAGE.get()),
                SyncItems.SHELL_STORAGE.get());
        event.registerItem(createItemRenderer(SyncBlockEntities.SHELL_CONSTRUCTOR.get(), SyncBlocks.SHELL_CONSTRUCTOR.get()),
                SyncItems.SHELL_CONSTRUCTOR.get());
        event.registerItem(createItemRenderer(SyncBlockEntities.TREADMILL.get(), SyncBlocks.TREADMILL.get()),
                SyncItems.TREADMILL.get());
    }

    /**
     * Creates an IClientItemExtensions instance for blocks that need custom item rendering.
     */
    public static IClientItemExtensions createItemRenderer(BlockEntityType<?> blockEntityType, Block block) {
        return new IClientItemExtensions() {
            private BlockEntityWithoutLevelRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = new SyncBlockEntityItemRenderer(blockEntityType, block);
                }
                return renderer;
            }
        };
    }

    /**
     * Custom item renderer for block entities
     */
    @OnlyIn(Dist.CLIENT)
    private static class SyncBlockEntityItemRenderer extends BlockEntityWithoutLevelRenderer {
        private final BlockEntityType<?> blockEntityType;
        private final Block block;
        private BlockEntity renderEntity;

        public SyncBlockEntityItemRenderer(BlockEntityType<?> blockEntityType, Block block) {
            super(net.minecraft.client.Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                    net.minecraft.client.Minecraft.getInstance().getEntityModels());
            this.blockEntityType = blockEntityType;
            this.block = block;
        }

        @Override
        public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
            if (renderEntity == null) {
                renderEntity = blockEntityType.create(BlockPos.ZERO, block.defaultBlockState());
            }

            if (renderEntity != null) {
                net.minecraft.client.Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(renderEntity, poseStack, buffer, packedLight, packedOverlay);
            }
        }
    }
}
