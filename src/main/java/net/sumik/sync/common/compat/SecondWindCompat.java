package net.sumik.sync.common.compat;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Pose;
import net.neoforged.fml.ModList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

/**
 * Optional reflection bridge for Second Wind.
 */
public final class SecondWindCompat {
    private static final String MOD_ID = "secondwind";
    private static final String SERVICE_CLASS = "com.jvn.secondwind.state.SecondWindService";
    private static final String STATE_CLASS = "com.jvn.secondwind.state.SecondWindPlayerState";
    private static final Logger LOGGER = LogManager.getLogger();

    private static boolean initialized;
    private static boolean available;
    private static @Nullable Method getStateMethod;
    private static @Nullable Method canTriggerMethod;
    private static @Nullable Method hasForcedCrawlingPoseMethod;
    private static @Nullable Method clearDownedRuntimeMethod;
    private static @Nullable Method resetCooldownAfterDeathMethod;
    private static @Nullable Method setForcedPoseMethod;

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }

    public static boolean canTriggerSecondWind(ServerPlayer player, DamageSource source) {
        initialize();
        if (!available || canTriggerMethod == null) {
            return false;
        }

        return invokeBoolean(canTriggerMethod, null, player, source);
    }

    public static void resetAfterSync(ServerPlayer player) {
        initialize();
        if (!available) {
            return;
        }

        Object state = getState(player);
        if (state != null) {
            if (invokeBoolean(hasForcedCrawlingPoseMethod, state)) {
                invoke(setForcedPoseMethod, player, (Object)null);
            }
            invoke(clearDownedRuntimeMethod, state);
        }

        invoke(resetCooldownAfterDeathMethod, null, player);
    }

    private static @Nullable Object getState(ServerPlayer player) {
        if (!available || getStateMethod == null) {
            return null;
        }
        return invoke(getStateMethod, null, player);
    }

    private static synchronized void initialize() {
        if (initialized) {
            return;
        }
        initialized = true;

        if (!isLoaded()) {
            return;
        }

        try {
            Class<?> serviceClass = Class.forName(SERVICE_CLASS);
            Class<?> stateClass = Class.forName(STATE_CLASS);
            getStateMethod = serviceClass.getMethod("getState", ServerPlayer.class);
            canTriggerMethod = serviceClass.getMethod("canTriggerSecondWind", ServerPlayer.class, DamageSource.class);
            hasForcedCrawlingPoseMethod = stateClass.getMethod("hasForcedCrawlingPoseApplied");
            clearDownedRuntimeMethod = stateClass.getMethod("clearDownedRuntime");
            resetCooldownAfterDeathMethod = serviceClass.getMethod("resetCooldownAfterDeath", ServerPlayer.class);
            try {
                setForcedPoseMethod = ServerPlayer.class.getMethod("setForcedPose", Pose.class);
            } catch (NoSuchMethodException exception) {
                LOGGER.debug("Second Wind forced-pose hook is not available.");
            }
            available = true;
        } catch (ReflectiveOperationException | LinkageError exception) {
            LOGGER.warn("Second Wind was detected, but its compatibility bridge could not be initialized.", exception);
        }
    }

    private static boolean invokeBoolean(@Nullable Method method, @Nullable Object target, Object... arguments) {
        return Boolean.TRUE.equals(invoke(method, target, arguments));
    }

    private static @Nullable Object invoke(@Nullable Method method, @Nullable Object target, Object... arguments) {
        if (method == null) {
            return null;
        }

        try {
            return method.invoke(target, arguments);
        } catch (ReflectiveOperationException | LinkageError exception) {
            LOGGER.warn("Second Wind compatibility call failed: {}", method.getName(), exception);
            return null;
        }
    }

    private SecondWindCompat() {
    }
}
