package net.sumik.sync.common.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.sumik.sync.Sync;
import net.sumik.sync.common.block.SyncBlocks;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.function.BiFunction;

public class SyncBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Sync.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ShellStorageBlockEntity>> SHELL_STORAGE =
            BLOCK_ENTITIES.register("shell_storage",
                    () -> create(ShellStorageBlockEntity::new, SyncBlocks.SHELL_STORAGE.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ShellConstructorBlockEntity>> SHELL_CONSTRUCTOR =
            BLOCK_ENTITIES.register("shell_constructor",
                    () -> create(ShellConstructorBlockEntity::new, SyncBlocks.SHELL_CONSTRUCTOR.get()));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TreadmillBlockEntity>> TREADMILL =
            BLOCK_ENTITIES.register("treadmill",
                    () -> create(TreadmillBlockEntity::new, SyncBlocks.TREADMILL.get()));

    @SuppressWarnings("unchecked")
    private static <T extends BlockEntity> BlockEntityType<T> create(BiFunction<BlockPos, BlockState, T> factory, Block... blocks) {
        try {
            Class<?> supplierType = Class.forName("net.minecraft.world.level.block.entity.BlockEntityType$BlockEntitySupplier");
            Object supplier = Proxy.newProxyInstance(supplierType.getClassLoader(), new Class<?>[] {supplierType},
                    (proxy, method, args) -> method.getName().equals("create") ? factory.apply((BlockPos) args[0], (BlockState) args[1]) : null);
            Constructor<BlockEntityType> constructor = BlockEntityType.class.getConstructor(supplierType, Set.class, com.mojang.datafixers.types.Type.class);
            return (BlockEntityType<T>) constructor.newInstance(supplier, Set.of(blocks), null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to create block entity type", e);
        }
    }
}
