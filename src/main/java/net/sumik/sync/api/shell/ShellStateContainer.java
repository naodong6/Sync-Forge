package net.sumik.sync.api.shell;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

/**
 * A container that can store player's shell.
 */
public interface ShellStateContainer {
    /**
     * Attempts to retrieve a {@link ShellStateContainer} instance from a block in the world.
     *
     * @param world The world.
     * @param pos The position of the block.
     * @return The {@linkplain ShellStateContainer} available at the given position, if any; otherwise, null.
     */
    @Nullable
    static ShellStateContainer find(Level world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity != null) {
            return blockEntity instanceof ShellStateContainer container ? container : null;
        }
        return null;
    }

    /**
     * Attempts to retrieve a {@link ShellStateContainer} that contains a given {@link ShellState}.
     *
     * @param world The world.
     * @param state The {@linkplain ShellState}.
     * @return The {@linkplain ShellStateContainer} that contains the given {@linkplain ShellState}, if any; otherwise, null.
     */
    @Nullable
    static ShellStateContainer find(Level world, ShellState state) {
        ShellStateContainer container = find(world, state.getPos());
        if (container != null && container.getShellState() == state) {
            return container;
        }
        return null;
    }


    /**
     * Indicates whether {@link ShellState} that is stored in this container is available for remote use
     * (i.e., should be displayed in player's radial menu).
     *
     * @return true if the {@linkplain ShellState} that is stored in this container
     * is available for remote use and should be displayed in player's radial menu;
     * otherwise, false.
     */
    default boolean isRemotelyAccessible() {
        return true;
    }

    /**
     * @return {@link ShellState} that is currently stored in the container, if any;
     * otherwise, null.
     */
    @Nullable
    ShellState getShellState();

    /**
     * Stores the given {@link ShellState} in the container.
     * @param state The {@linkplain ShellState}.
     */
    void setShellState(@Nullable ShellState state);

    /**
     * @return Color of the container, if any; otherwise, null.
     */
    @Nullable
    default DyeColor getColor() {
        ShellState state = this.getShellState();
        return state == null ? null : state.getColor();
    }
}
