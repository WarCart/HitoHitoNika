package net.warcar.hito_hito_nika.renderers.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.warcar.hito_hito_nika.entities.LuffyBoss;
import xyz.pixelatedw.mineminenomi.abilities.haki.HakiHelper;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileRenderer;
import xyz.pixelatedw.mineminenomi.init.ModRenderTypes;
import xyz.pixelatedw.mineminenomi.init.ModResources;

public class GomuProjectileRenderer<E extends NuProjectileEntity, M extends EntityModel<E>> extends NuProjectileRenderer<E, M> {
    protected M internalStretchingModel;
    private float internalStretchScaleX = 1.0F;
    private float internalStretchScaleY = 1.0F;
    private float internalStretchScaleZ = 1.0F;

    public GomuProjectileRenderer(EntityRendererProvider.Context renderManager, M model, M stretchModel) {
        super(renderManager, model);
        this.internalStretchingModel = stretchModel;
    }
    
    public void setStretchScale(double x, double y, double z){
        this.internalStretchScaleX = (float) x;
        this.internalStretchScaleY = (float) y;
        this.internalStretchScaleZ = (float) z;
    }

    public void render(E entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight){
        if (entity.getOwner() != null && (entity.isAffectedByImbuing() || entity.isAffectedByHardening())) {
            super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
            LivingEntity owner = entity.getOwner();
            if (HakiHelper.hasAdvancedBusoActive(owner)) {
                VertexConsumer ivertexbuilder = buffer.getBuffer(ModRenderTypes.TRANSPARENT_COLOR);
                matrixStack.pushPose();
                matrixStack.scale(1.2f, 1.2f, 1.02f);
                matrixStack.mulPose(Axis.YP.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks - 180.0F));
                matrixStack.mulPose(Axis.XP.rotationDegrees(entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks));
                matrixStack.scale((float) this.getScale().x, (float) this.getScale().y, (float) this.getScale().z);
                //matrixStack.translate(this.translateX, this.translateY, this.translateZ);
                if (this.model != null) {
                    this.model.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 0.886f, 0.5f, 0.1f, 0.4f);
                }
                matrixStack.popPose();
                matrixStack.pushPose();
                matrixStack.mulPose(Axis.YP.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks - 180.0F));
                matrixStack.mulPose(Axis.XP.rotationDegrees(entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks));
                matrixStack.scale(1.2f, 1.2f, 1.02f);
                Vec3 originPos = owner.position();
                Vec3 entityPos = new Vec3(Mth.lerp(partialTicks, entity.xo, entity.getX()), Mth.lerp(partialTicks, entity.yo, entity.getY()), Mth.lerp(partialTicks, entity.zo, entity.getZ()));
                Vec3 stretchVec = entityPos.subtract(originPos);
                matrixStack.mulPose(Axis.ZP.rotationDegrees(180));
                float modelLength = this.internalStretchScaleZ / 16.0F;
                float modelOffset = 0.25F;
                float stretchLength = (float)stretchVec.length();
                matrixStack.translate(0.0D, 0.0D, -modelOffset);
                matrixStack.scale(this.internalStretchScaleX, this.internalStretchScaleY, (stretchLength - 2.0F * modelOffset) / modelLength);
                matrixStack.translate(0.0D, 0.0D, modelOffset);
                this.internalStretchingModel.renderToBuffer(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 0.886f, 0.5f, 0.1f, 0.4f);
                matrixStack.popPose();
            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(NuProjectileEntity entity) {
        ResourceLocation textureLocation = super.getTextureLocation(entity);
        if (textureLocation != ModResources.BUSOSHOKU_HAKI_ARM && entity.getOwner() instanceof LuffyBoss luffy) {
            return luffy.getCurrentTexture();
        }
        return textureLocation;
    }

    public static class Factory<T extends NuProjectileEntity> extends NuProjectileRenderer.Factory<T> {
        protected EntityModel<T> internalStretchingModel;
        private double stretchScaleX;
        private double stretchScaleY;
        private double stretchScaleZ;

        public Factory(EntityModel<T> stretchModel) {
            this(null, stretchModel);
        }
        public Factory(EntityModel<T> tipModel, EntityModel stretchModel) {
            this.setModel(() -> tipModel);
            this.internalStretchingModel = stretchModel;
        }

        public Factory setStretchScale(double xy, double z) {
            this.stretchScaleX = xy;
            this.stretchScaleY = xy;
            this.stretchScaleZ = z;
            return this;
        }
        
        public Factory setStretchScale(double x, double y, double z) {
            this.stretchScaleX = x;
            this.stretchScaleY = y;
            this.stretchScaleZ = z;
            return this;
        }
        
        public EntityRenderer<T> create(Context manager) {
            GomuProjectileRenderer<T, ?> renderer = new GomuProjectileRenderer<>(manager, this.model.apply(manager), this.internalStretchingModel);
            renderer.setStretchScale(this.stretchScaleX, this.stretchScaleY, this.stretchScaleZ);
            renderer.setScale(this.scaleX, this.scaleY, this.scaleZ);
            renderer.setColor(this.colour);
            renderer.setUseArmSkin();
            return renderer;
        }
    }
}
