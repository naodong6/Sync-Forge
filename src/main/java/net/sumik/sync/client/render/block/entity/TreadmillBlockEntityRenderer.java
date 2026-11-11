package net.sumik.sync.client.render.block.entity;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sumik.sync.Sync;
import net.sumik.sync.client.model.DoubleBlockModel;
import net.sumik.sync.client.model.TreadmillModel;
import net.sumik.sync.common.block.SyncBlocks;
import net.sumik.sync.common.block.TreadmillBlock;
import net.sumik.sync.common.block.entity.TreadmillBlockEntity;

@OnlyIn(Dist.CLIENT)
public class TreadmillBlockEntityRenderer extends DoubleBlockEntityRenderer<TreadmillBlockEntity> {
    private static final ResourceLocation TREADMILL_TEXTURE_ID = ResourceLocation.fromNamespaceAndPath(Sync.MOD_ID, "textures/block/treadmill.png");
    private static final BlockState DEFAULT_STATE = SyncBlocks.TREADMILL.get().defaultBlockState()
            .setValue(TreadmillBlock.PART, TreadmillBlock.Part.FRONT)
            .setValue(TreadmillBlock.FACING, Direction.SOUTH);

    private final DoubleBlockModel model;

    public TreadmillBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.model = new TreadmillModel();
    }

    @Override
    protected DoubleBlockModel getModel(TreadmillBlockEntity blockEntity, BlockState blockState, float tickDelta) {
        return this.model;
    }

    @Override
    protected BlockState getDefaultState() {
        return DEFAULT_STATE;
    }

    @Override
    protected ResourceLocation getTextureId() {
        return TREADMILL_TEXTURE_ID;
    }
}