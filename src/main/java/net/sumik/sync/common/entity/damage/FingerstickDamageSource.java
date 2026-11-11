package net.sumik.sync.common.entity.damage;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.sumik.sync.Sync;

public class FingerstickDamageSource {
    public static final ResourceKey<DamageType> FINGERSTICK = ResourceKey.create(
            Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(Sync.MOD_ID, "fingerstick")
    );

    public static DamageSource fingerstick(Level world) {
        return new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(FINGERSTICK));
    }

    public static DamageSource fingerstick(Entity entity) {
        return fingerstick(entity.level());
    }
}