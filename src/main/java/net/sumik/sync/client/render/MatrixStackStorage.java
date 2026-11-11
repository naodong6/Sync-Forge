package net.sumik.sync.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class MatrixStackStorage {
    private static PoseStack modelMatrixStack;

    public static void saveModelMatrixStack(PoseStack matrixStack) {
        modelMatrixStack = matrixStack;
    }

    public static PoseStack getModelMatrixStack() {
        return modelMatrixStack;
    }
}