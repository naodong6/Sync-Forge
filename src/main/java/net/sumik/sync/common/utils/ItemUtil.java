package net.sumik.sync.common.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.core.registries.BuiltInRegistries;
import net.sumik.sync.common.config.SyncConfig;

public final class ItemUtil {
    private static final TagKey<Item> WRENCHES = TagKey.create(Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath("c", "tools/wrenches"));

    public static boolean isWrench(ItemStack itemStack) {
        if (itemStack.is(WRENCHES)) {
            return true;
        }

        SyncConfig config = SyncConfig.getInstance();
        ResourceLocation wrenchId = config.wrench() == null || config.wrench().isBlank() ? null : ResourceLocation.tryParse(config.wrench());
        if (wrenchId == null) {
            return false;
        }
        Item wrench = BuiltInRegistries.ITEM.get(wrenchId);
        return wrench != null && wrench != Items.AIR && itemStack.is(wrench);
    }

    public static boolean isWrench(ItemLike item) {
        return isWrench(new ItemStack(item));
    }

    public static boolean isArmor(ItemStack itemStack) {
        return isArmor(itemStack.getItem());
    }

    public static boolean isArmor(ItemLike item) {
        return item.asItem() instanceof ArmorItem;
    }

    public static EquipmentSlot getPreferredEquipmentSlot(ItemStack itemStack) {
        return itemStack.getItem() instanceof ArmorItem armorItem
                ? armorItem.getEquipmentSlot()
                : EquipmentSlot.MAINHAND;
    }

    public static EquipmentSlot getPreferredEquipmentSlot(ItemLike item) {
        return getPreferredEquipmentSlot(new ItemStack(item));
    }

    private ItemUtil() {
    }
}
