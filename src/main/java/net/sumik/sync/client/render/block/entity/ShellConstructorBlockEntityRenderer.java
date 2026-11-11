package net.sumik.sync.client.render.block.entity;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sumik.sync.Sync;
import net.sumik.sync.api.shell.ShellState;
import net.sumik.sync.client.model.AbstractShellContainerModel;
import net.sumik.sync.client.model.ShellConstructorModel;
import net.sumik.sync.common.block.AbstractShellContainerBlock;
import net.sumik.sync.common.block.SyncBlocks;
import net.sumik.sync.common.block.entity.ShellConstructorBlockEntity;
import net.sumik.sync.common.block.entity.ShellEntity;

@OnlyIn(Dist.CLIENT)
public class ShellConstructorBlockEntityRenderer extends AbstractShellContainerBlockEntityRenderer<ShellConstructorBlockEntity> {
    private static final ResourceLocation SHELL_CONSTRUCTOR_TEXTURE_ID = ResourceLocation.fromNamespaceAndPath(Sync.MOD_ID, "textures/block/shell_constructor.png");
    private static final BlockState DEFAULT_STATE = SyncBlocks.SHELL_CONSTRUCTOR.get().defaultBlockState()
            .setValue(AbstractShellContainerBlock.HALF, DoubleBlockHalf.LOWER)
            .setValue(AbstractShellContainerBlock.FACING, Direction.SOUTH);

    private final ShellConstructorModel model;

    public ShellConstructorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.model = new ShellConstructorModel();
    }

    @Override
    protected AbstractShellContainerModel getShellContainerModel(ShellConstructorBlockEntity blockEntity, BlockState blockState, float tickDelta) {
        this.model.buildProgress = blockEntity.getShellState() == null ? 0F : blockEntity.getShellState().getProgress();
        this.model.showInnerParts = blockEntity.hasLevel();
        return this.model;
    }

    @Override
    protected ShellEntity createEntity(ShellState shellState, ShellConstructorBlockEntity blockEntity, float tickDelta) {
        ShellEntity entity = shellState.asEntity();
        entity.isActive = false;
        entity.pitchProgress = 0;
        return entity;
    }

    @Override
    protected BlockState getDefaultState() {
        return DEFAULT_STATE;
    }

    @Override
    protected ResourceLocation getTextureId() {
        return SHELL_CONSTRUCTOR_TEXTURE_ID;
    }
}