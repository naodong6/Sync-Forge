package net.sumik.sync.common.item;

import net.sumik.sync.Sync;
import net.sumik.sync.common.block.SyncBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class SyncItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, Sync.MOD_ID);

    public static final DeferredHolder<Item, Item> SYNC_CORE = ITEMS.register("sync_core",
            () -> new Item(new Item.Properties().stacksTo(16)));

    public static final DeferredHolder<Item, Item> SHELL_CONSTRUCTOR = ITEMS.register("shell_constructor",
            () -> new ShellConstructorItem(SyncBlocks.SHELL_CONSTRUCTOR.get(), new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, Item> SHELL_STORAGE = ITEMS.register("shell_storage",
            () -> new ShellStorageItem(SyncBlocks.SHELL_STORAGE.get(), new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, Item> TREADMILL = ITEMS.register("treadmill",
            () -> new TreadmillItem(SyncBlocks.TREADMILL.get(), new Item.Properties().stacksTo(1)));
}
