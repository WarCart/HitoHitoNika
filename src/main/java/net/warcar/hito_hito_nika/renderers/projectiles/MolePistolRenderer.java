package net.warcar.hito_hito_nika.renderers.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.data.ModelData;
import net.warcar.hito_hito_nika.projectiles.GomuGomuNoMoguraPistolProjectile;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileRenderer;

public class MolePistolRenderer extends NuProjectileRenderer<GomuGomuNoMoguraPistolProjectile, EntityModel<GomuGomuNoMoguraPistolProjectile>> {
    public MolePistolRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, null);
    }

    @Override
    public void render(GomuGomuNoMoguraPistolProjectile entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        Minecraft mc = Minecraft.getInstance();
        Vec3 to = entity.getTargetPos();
        Vec3 from = entity.position().scale(partialTicks).add(new Vec3(entity.xo, entity.yo, entity.zo).scale(1 - partialTicks));
        Vec3 stretchVec = from.vectorTo(to);
        BlockState block = entity.getOutBlock();
        for (int i = 0; i < 5; i++) {
            matrixStack.pushPose();
            float sPos = (float) i / 5;
            Vec3 sStretch = stretchVec.scale(sPos);
            matrixStack.translate(sStretch.x, sStretch.y, sStretch.z);
            float length = (float) stretchVec.length() / 5;
            matrixStack.scale(sPos * 3, length, sPos * 3);
            matrixStack.translate(-0.5, -1, -0.5);
            mc.getBlockRenderer().renderSingleBlock(block, matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutout());
            matrixStack.popPose();
        }
    }
}
