package net.warcar.hito_hito_nika.renderers.projectiles;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.warcar.hito_hito_nika.abilities.TrueGomuHelper;
import net.warcar.hito_hito_nika.projectiles.PythonProjectile;
import xyz.pixelatedw.mineminenomi.api.helpers.HakiHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModRenderTypes;
import xyz.pixelatedw.mineminenomi.init.ModResources;
import xyz.pixelatedw.mineminenomi.renderers.abilities.AbilityProjectileRenderer;

public class PythonProjectileRenderer<E extends PythonProjectile, M extends EntityModel<E>> extends AbilityProjectileRenderer<E, M> {
    protected M internalStretchingModel;
    public PythonProjectileRenderer(EntityRendererManager renderManager, M stretchModel) {
        super(renderManager, null, null);
        this.internalStretchingModel = stretchModel;
    }

    @Override
    public void render(E entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        if (entity.getThrower() != null && entity.getThrower().isAlive()) {
            if (HakiHelper.hasHardeningActive(entity.getThrower(), false, true)) {
                this.setTexture(ModResources.BUSOSHOKU_HAKI_ARM);
                this.setPlayerTexture(false);
            }
            Vector3d originPos = entity.getThrower().position();
            if (entity.getPrev() != null) {
                originPos = entity.getPrev().position();
            }
            Vector3d entityPos = new Vector3d(MathHelper.lerp(partialTicks, entity.xo, entity.getX()), MathHelper.lerp(partialTicks, entity.yo, entity.getY()), MathHelper.lerp(partialTicks, entity.zo, entity.getZ()));
            Vector3d stretchVec = entityPos.subtract(originPos);
            if (this.internalStretchingModel != null) {
                matrixStack.pushPose();
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(entity.yRotO + (entity.yRot - entity.yRotO) * partialTicks - 180.0F));
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks));
                matrixStack.mulPose(new Quaternion(Vector3f.ZP, 180.0F, true));
                float modelLength = (float) this.getScale().z / 16.0F;
                float modelOffset = -0.1F;
                float stretchLength = (float) stretchVec.length();
                matrixStack.translate(0.0D, 0.0D, -modelOffset);
                matrixStack.scale((float) this.getScale().x, (float) this.getScale().y, (stretchLength + 2.0F * modelOffset) / modelLength);
                matrixStack.translate(0.0D, 0.0D, modelOffset);
                ResourceLocation finalTexture = this.getTextureLocation(entity);
                RenderType type;
                if (finalTexture == null) {
                    type = this.isGlowing() ? ModRenderTypes.getEnergyRenderType() : ModRenderTypes.TRANSPARENT_COLOR;
                } else {
                    type = RenderType.entityTranslucent(finalTexture);
                }

                IVertexBuilder ivertexbuilder = buffer.getBuffer(type);
                this.internalStretchingModel.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, (float) this.getColor().getRed() / 255.0F, (float) this.getColor().getGreen() / 255.0F, (float) this.getColor().getBlue() / 255.0F, (float) this.getColor().getAlpha() / 255.0F);
                if (entity.getThrower() != null && entity.isAffectedByHaki()) {
                    if (TrueGomuHelper.hasHakiEmissionActive(AbilityDataCapability.get(entity.getThrower()))) {
                        matrixStack.pushPose();
                        matrixStack.scale(1.2f, 1.2f, 1.02f);
                        ivertexbuilder = buffer.getBuffer(ModRenderTypes.TRANSPARENT_COLOR);
                        this.internalStretchingModel.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 0.886f, 0.5f, 0.1f, 0.4f);
                        matrixStack.popPose();
                    }
                }
                this.setPlayerTexture(true);
                matrixStack.popPose();
            }
        } else {
            entity.remove();
        }
    }

    public static class Factory extends AbilityProjectileRenderer.Factory{
        protected EntityModel internalStretchingModel;
        public Factory(EntityModel stretchModel) {
            super(stretchModel);
            this.internalStretchingModel = stretchModel;
        }
        public EntityRenderer<? super AbilityProjectileEntity> createRenderFor(EntityRendererManager manager) {
            PythonProjectileRenderer renderer = new PythonProjectileRenderer(manager, this.internalStretchingModel);
            renderer.setScale(this.scaleX, this.scaleY, this.scaleZ);
            renderer.setColor(this.colour);
            renderer.setPlayerTexture(true);
            renderer.setGlowing(this.isGlowing);
            return renderer;
        }
    }
}
