package net.warcar.hito_hito_nika.renderers.projectiles;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.HakiHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModRenderTypes;
import xyz.pixelatedw.mineminenomi.init.ModResources;
import xyz.pixelatedw.mineminenomi.renderers.abilities.StretchingProjectileRenderer;

public class GomuProjectileRenderer<E extends AbilityProjectileEntity, M extends EntityModel<E>> extends StretchingProjectileRenderer<E, M> {
    protected M internalStretchingModel;
    private float internalStretchScaleX = 1.0F;
    private float internalStretchScaleY = 1.0F;
    private float internalStretchScaleZ = 1.0F;

    public GomuProjectileRenderer(EntityRendererManager renderManager, M model, M stretchModel) {
        super(renderManager, model, stretchModel);
        this.internalStretchingModel = stretchModel;
    }

    @Override
    public void setStretchScale(double x, double y, double z){
        super.setStretchScale(x, y, z);
        this.internalStretchScaleX = (float) x;
        this.internalStretchScaleY = (float) y;
        this.internalStretchScaleZ = (float) z;
    }

    public void render(E entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight){
        if (entity.getThrower() != null && entity.isAffectedByHaki()) {
            if (HakiHelper.hasHardeningActive(entity.getThrower(), false, true)) {
                this.setTexture(ModResources.BUSOSHOKU_HAKI_ARM);
                this.setPlayerTexture(false);
            }
            super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
            if (TrueGomuHelper.hasHakiEmissionActive(AbilityDataCapability.get(entity.getThrower()))) {
                IVertexBuilder ivertexbuilder = buffer.getBuffer(ModRenderTypes.TRANSPARENT_COLOR2);
                matrixStack.pushPose();
                matrixStack.scale(1.2f, 1.2f, 1.02f);
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(entity.yRotO + (entity.yRot - entity.yRotO) * partialTicks - 180.0F));
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks));
                matrixStack.scale((float) this.getScale().x, (float) this.getScale().y, (float) this.getScale().z);
                matrixStack.translate(this.translateX, this.translateY, this.translateZ);
                if (this.model != null) {
                    this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 0.886f, 0.5f, 0.1f, 0.4f);
                }
                matrixStack.popPose();
                matrixStack.pushPose();
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(entity.yRotO + (entity.yRot - entity.yRotO) * partialTicks - 180.0F));
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(entity.xRotO + (entity.xRot - entity.xRotO) * partialTicks));
                matrixStack.scale(1.2f, 1.2f, 1.02f);
                Vector3d originPos = entity.getThrower().position();
                Vector3d entityPos = new Vector3d(MathHelper.lerp(partialTicks, entity.xo, entity.getX()), MathHelper.lerp(partialTicks, entity.yo, entity.getY()), MathHelper.lerp(partialTicks, entity.zo, entity.getZ()));
                Vector3d stretchVec = entityPos.subtract(originPos);
                matrixStack.mulPose(new Quaternion(Vector3f.ZP, 180.0F, true));
                float modelLength = this.internalStretchScaleZ / 16.0F;
                float modelOffset = 0.25F;
                float stretchLength = (float)stretchVec.length();
                matrixStack.translate(0.0D, 0.0D, -modelOffset);
                matrixStack.scale(this.internalStretchScaleX, this.internalStretchScaleY, (stretchLength - 2.0F * modelOffset) / modelLength);
                matrixStack.translate(0.0D, 0.0D, modelOffset);
                this.internalStretchingModel.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 0.886f, 0.5f, 0.1f, 0.4f);
                matrixStack.popPose();
            }
            this.setPlayerTexture(true);
        }
    }
    public static class Factory extends StretchingProjectileRenderer.Factory{
        protected EntityModel internalStretchingModel;
        public Factory(EntityModel stretchModel) {
            this(null, stretchModel);
        }
        public Factory(EntityModel tipModel, EntityModel stretchModel) {
            super(tipModel, stretchModel);
            this.internalStretchingModel = stretchModel;
        }
        public EntityRenderer<? super AbilityProjectileEntity> createRenderFor(EntityRendererManager manager) {
            GomuProjectileRenderer renderer = new GomuProjectileRenderer(manager, this.model, this.internalStretchingModel);
            renderer.setStretchScale(this.stretchScaleX, this.stretchScaleY, this.stretchScaleZ);
            renderer.setScale(this.scaleX, this.scaleY, this.scaleZ);
            renderer.setColor(this.colour);
            renderer.setPlayerTexture(true);
            renderer.setGlowing(this.isGlowing);
            return renderer;
        }
    }
}
