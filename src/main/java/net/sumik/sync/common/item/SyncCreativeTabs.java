package net.sumik.sync.common.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.sumik.sync.Sync;

public class SyncCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Sync.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SYNC_TAB = CREATIVE_MODE_TABS.register("sync_tab",
            () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                    .icon(() -> new ItemStack(SyncItems.SYNC_CORE.get()))
                    .title(Component.translatable("creativetab.sync"))
                    .displayItems((parameters, output) -> {
                        output.accept(SyncItems.SYNC_CORE.get());
                        output.accept(SyncItems.SHELL_STORAGE.get());
                        output.accept(SyncItems.SHELL_CONSTRUCTOR.get());
                        output.accept(SyncItems.TREADMILL.get());
                    }).build()
    );
}
