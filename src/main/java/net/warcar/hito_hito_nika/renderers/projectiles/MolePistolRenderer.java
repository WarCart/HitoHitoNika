package net.warcar.hito_hito_nika.renderers.projectiles;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.warcar.hito_hito_nika.projectiles.GomuGomuNoMoguraPistolProjectile;
import xyz.pixelatedw.mineminenomi.renderers.abilities.AbilityProjectileRenderer;

public class MolePistolRenderer extends AbilityProjectileRenderer<GomuGomuNoMoguraPistolProjectile, EntityModel<GomuGomuNoMoguraPistolProjectile>> {
    public MolePistolRenderer(EntityRendererManager renderManager) {
        super(renderManager, null, null);
    }

    @Override
    public void render(GomuGomuNoMoguraPistolProjectile entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        Minecraft mc = Minecraft.getInstance();
        Vector3d to = entity.getTargetPos();
        Vector3d from = entity.position().scale(partialTicks).add(new Vector3d(entity.xo, entity.yo, entity.zo).scale(1 - partialTicks));
        Vector3d stretchVec = from.vectorTo(to);
        BlockState block = entity.getOutBlock();
        for (int i = 0; i < 5; i++) {
            matrixStack.pushPose();
            float sPos = (float) i / 5;
            Vector3d sStretch = stretchVec.scale(sPos);
            matrixStack.translate(sStretch.x, sStretch.y, sStretch.z);
            float length = (float) stretchVec.length() / 5;
            matrixStack.scale(sPos * 3, length, sPos * 3);
            matrixStack.translate(-0.5, -1, -0.5);
            mc.getBlockRenderer().renderBlock(block, matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
            matrixStack.popPose();
        }
    }
}
