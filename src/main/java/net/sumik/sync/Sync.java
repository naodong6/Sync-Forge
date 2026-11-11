package net.sumik.sync;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.sumik.sync.client.render.CustomGameRenderer;
import net.sumik.sync.client.render.SyncRenderers;
import net.sumik.sync.common.block.SyncBlocks;
import net.sumik.sync.common.block.entity.SyncBlockEntities;
import net.sumik.sync.common.command.SyncCommands;
import net.sumik.sync.common.config.SyncConfig;
import net.sumik.sync.common.item.SyncCreativeTabs;
import net.sumik.sync.common.item.SyncItems;
import net.sumik.sync.networking.SyncPackets;

@Mod(Sync.MOD_ID)
public class Sync {
    public static final String MOD_ID = "sync";
    public static final String NAME = "Sync";

    public Sync(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, SyncConfig.SPEC, "sync-common.toml");
        SyncBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        SyncCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        SyncBlocks.BLOCKS.register(modEventBus);
        SyncItems.ITEMS.register(modEventBus);
        SyncCommands.init();
        modEventBus.addListener(SyncPackets::register);
        modEventBus.addListener(Sync::registerCapabilities);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(this::onClientSetup);
            modEventBus.addListener(SyncRenderers::registerBlockEntityRenderers);
            modEventBus.addListener(SyncRenderers::registerClientExtensions);
        }
    }

    private static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, SyncBlockEntities.SHELL_STORAGE.get(),
                (blockEntity, side) -> blockEntity.canReceive() ? blockEntity : null);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, SyncBlockEntities.SHELL_CONSTRUCTOR.get(),
                (blockEntity, side) -> blockEntity.canReceive() ? blockEntity : null);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, SyncBlockEntities.TREADMILL.get(),
                (blockEntity, side) -> blockEntity.canExtract() ? blockEntity : null);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        CustomGameRenderer.initClient();
        SyncRenderers.initClient();
    }

    public static ResourceLocation locate(String location) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, location);
    }
}
