package net.warcar.hito_hito_nika.renderers.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.warcar.hito_hito_nika.projectiles.PythonProjectile;
import xyz.pixelatedw.mineminenomi.abilities.haki.HakiHelper;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileRenderer;
import xyz.pixelatedw.mineminenomi.init.ModRenderTypes;
import xyz.pixelatedw.mineminenomi.init.ModResources;

public class PythonProjectileRenderer<E extends PythonProjectile, M extends EntityModel<E>> extends NuProjectileRenderer<E, M> {
    protected M internalStretchingModel;
    public PythonProjectileRenderer(EntityRendererProvider.Context renderManager, M stretchModel) {
        super(renderManager, null);
        this.internalStretchingModel = stretchModel;
    }

    @Override
    public void render(E entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        LivingEntity owner = entity.getOwner();
        if (owner != null && owner.isAlive()) {
            boolean hardening = HakiHelper.hasHardeningActive(owner, false, true);
            Vec3 originPos = owner.position();
            if (entity.getPrev() != null) {
                originPos = entity.getPrev().position();
            }
            Vec3 entityPos = new Vec3(Mth.lerp(partialTicks, entity.xo, entity.getX()), Mth.lerp(partialTicks, entity.yo, entity.getY()), Mth.lerp(partialTicks, entity.zo, entity.getZ()));
            Vec3 stretchVec = entityPos.subtract(originPos);
            if (this.internalStretchingModel != null) {
                matrixStack.pushPose();
                matrixStack.mulPose(Axis.YP.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks - 180.0F));
                matrixStack.mulPose(Axis.XP.rotationDegrees(entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks));
                matrixStack.mulPose(Axis.ZP.rotationDegrees(180));
                float modelLength = (float) this.getScale().z / 16.0F;
                float modelOffset = -0.1F;
                float stretchLength = (float) stretchVec.length();
                matrixStack.translate(0.0D, 0.0D, -modelOffset);
                matrixStack.scale((float) this.getScale().x, (float) this.getScale().y, (stretchLength + 2.0F * modelOffset) / modelLength);
                matrixStack.translate(0.0D, 0.0D, modelOffset);
                ResourceLocation finalTexture;
                if (hardening) {
                    finalTexture = ModResources.BUSOSHOKU_HAKI_ARM;
                } else {
                    finalTexture = this.getTextureLocation(entity);
                }
                RenderType type;
                if (finalTexture == null) {
                    type = ModRenderTypes.TRANSPARENT_COLOR;
                } else {
                    type = RenderType.entityTranslucent(finalTexture);
                }

                VertexConsumer ivertexbuilder = buffer.getBuffer(type);
                this.internalStretchingModel.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, (float) this.getColor().getRed() / 255.0F, (float) this.getColor().getGreen() / 255.0F, (float) this.getColor().getBlue() / 255.0F, (float) this.getColor().getAlpha() / 255.0F);
                if (owner != null && (entity.isAffectedByHardening() || entity.isAffectedByImbuing())) {
                    if (HakiHelper.hasAdvancedBusoActive(owner)) {
                        matrixStack.pushPose();
                        matrixStack.scale(1.2f, 1.2f, 1.02f);
                        ivertexbuilder = buffer.getBuffer(ModRenderTypes.TRANSPARENT_COLOR);
                        this.internalStretchingModel.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 0.886f, 0.5f, 0.1f, 0.4f);
                        matrixStack.popPose();
                    }
                }
                matrixStack.popPose();
            }
        }
    }

    public static class Factory extends NuProjectileRenderer.Factory {
        protected EntityModel internalStretchingModel;
        public Factory(EntityModel stretchModel) {
            this.internalStretchingModel = stretchModel;
        }
        public EntityRenderer<? super NuProjectileEntity> createRenderFor(EntityRendererProvider.Context manager) {
            PythonProjectileRenderer renderer = new PythonProjectileRenderer(manager, this.internalStretchingModel);
            renderer.setScale(this.scaleX, this.scaleY, this.scaleZ);
            renderer.setColor(this.colour);
            return renderer;
        }
    }
}
