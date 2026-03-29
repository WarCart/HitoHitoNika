package net.warcar.hito_hito_nika.renderers.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.warcar.hito_hito_nika.entities.LuffyBoss;
import xyz.pixelatedw.mineminenomi.abilities.haki.HakiHelper;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileRenderer;
import xyz.pixelatedw.mineminenomi.api.helpers.RendererHelper;
import xyz.pixelatedw.mineminenomi.init.ModRenderTypes;
import xyz.pixelatedw.mineminenomi.init.ModResources;

import java.util.ArrayList;
import java.util.List;

public class GomuProjectileRenderer<E extends NuProjectileEntity> extends NuProjectileRenderer<E, EntityModel<E>> {
    private final Model tipModel;
    private final Model stretchModel;
    private float internalStretchScaleX = 1.0F;
    private float internalStretchScaleY = 1.0F;
    private float internalStretchScaleZ = 1.0F;
    private LivingEntityRenderer<? super LivingEntity, ?> ownerRenderer;

    public GomuProjectileRenderer(EntityRendererProvider.Context renderManager, Model tipModel, Model stretchModel) {
        super(renderManager, null);
        this.tipModel = tipModel;
        this.stretchModel = stretchModel;
    }
    
    public void setStretchScale(double x, double y, double z) {
        this.internalStretchScaleX = (float) x;
        this.internalStretchScaleY = (float) y;
        this.internalStretchScaleZ = (float) z;
    }

    public void render(E entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        if (Minecraft.getInstance().getEntityRenderDispatcher().shouldRenderHitBoxes() && !entity.isInvisible() && !Minecraft.getInstance().showOnlyReducedInfo()) {
            this.renderDebugBox(matrixStack, buffer.getBuffer(RenderType.lines()), entity);
        }

        if (entity.getOwner() != null) {
            LivingEntity owner = entity.getOwner();
            if (this.ownerRenderer == null) {
                this.ownerRenderer = (LivingEntityRenderer<? super LivingEntity, ?>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity.getOwner());
            }
            List<ModelPart> tipParts = new ArrayList<>();
            List<ModelPart> stretchParts = new ArrayList<>();
            if (tipModel != null) {
                if (tipModel.leg) {
                    tipParts.addAll(RendererHelper.getLegPartsFrom(ownerRenderer.getModel()));
                } else {
                    tipParts.addAll(RendererHelper.getArmPartsFrom(ownerRenderer.getModel()));
                }
            }
            if (stretchModel != null) {
                if (stretchModel.leg) {
                    stretchParts.addAll(RendererHelper.getLegPartsFrom(ownerRenderer.getModel()));
                } else {
                    stretchParts.addAll(RendererHelper.getArmPartsFrom(ownerRenderer.getModel()));
                }
            }
            boolean advancedBuso = HakiHelper.hasAdvancedBusoActive(owner);
            boolean hardening = true || HakiHelper.hasHardeningActive(owner, false, true);
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
            VertexConsumer solid = buffer.getBuffer(type);
            VertexConsumer glow = buffer.getBuffer(ModRenderTypes.TRANSPARENT_COLOR);
            matrixStack.pushPose();
            matrixStack.mulPose(Axis.YP.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks - 180.0F));
            matrixStack.mulPose(Axis.XP.rotationDegrees(entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks));
            matrixStack.scale((float) this.getScale().x, (float) this.getScale().y, (float) this.getScale().z);
            //matrixStack.translate(this.translateX, this.translateY, this.translateZ);
            if (!tipParts.isEmpty()) {
                if (tipModel.leg) {
                    matrixStack.translate(0.1, 0.0, 1.0);
                } else {
                    matrixStack.translate(0.3, 0.0, 0.5);
                }
                matrixStack.mulPose(Axis.XN.rotationDegrees(90));
                renderToBuffer(tipParts, matrixStack, solid, packedLight, 1, 1, 1, 1);
                if (advancedBuso) {
                    matrixStack.pushPose();
                    matrixStack.scale(1.2f, 1.2f, 1.02f);
                    tipParts.get(0).render(matrixStack, glow, packedLight, OverlayTexture.NO_OVERLAY, 0.886f, 0.5f, 0.1f, 0.4f);
                    matrixStack.popPose();
                }
            }
            matrixStack.popPose();
            matrixStack.pushPose();
            matrixStack.mulPose(Axis.YP.rotationDegrees(entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks - 180.0F));
            matrixStack.mulPose(Axis.XP.rotationDegrees(entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks));
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
            if (!stretchParts.isEmpty()) {
                if (stretchModel.leg) {
                    matrixStack.translate(0.1, 0.0, 1.0);
                } else {
                    matrixStack.translate(0.3, 0.0, 0.5);
                }
                matrixStack.mulPose(Axis.XN.rotationDegrees(90));
                renderToBuffer(stretchParts, matrixStack, solid, packedLight, 1, 1, 1, 1);
                if (advancedBuso) {
                    matrixStack.pushPose();
                    matrixStack.scale(1.2f, 1.2f, 1.02f);
                    stretchParts.get(0).render(matrixStack, glow, packedLight, OverlayTexture.NO_OVERLAY, 0.886f, 0.5f, 0.1f, 0.4f);
                    matrixStack.popPose();
                }
            }
            matrixStack.popPose();
        }
    }

    private void renderDebugBox(PoseStack matrixStack, VertexConsumer vertexBuilder, E entity) {
        AABB axisalignedbb = new AABB(
                -entity.getBlockCollisionBox().getXsize() / 2.0D,
                -entity.getBlockCollisionBox().getYsize() / 2.0D,
                -entity.getBlockCollisionBox().getZsize() / 2.0D,
                entity.getBlockCollisionBox().getXsize() / 2.0D,
                entity.getBlockCollisionBox().getYsize() / 2.0D,
                entity.getBlockCollisionBox().getZsize() / 2.0D);

        LevelRenderer.renderLineBox(matrixStack, vertexBuilder, axisalignedbb, 1.0F, 0.0F, 0.0F, 1.0F);
    }

    private void renderToBuffer(List<ModelPart> limbs, PoseStack matrixStack, VertexConsumer skinVertex, int packedLight, float red, float green, float blue, float alpha) {
        for (ModelPart limb : limbs) {
            limb.resetPose();
            matrixStack.pushPose();
            limb.render(matrixStack, skinVertex, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
            matrixStack.popPose();
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
        protected Model stretchModel;
        protected Model tipModel;
        private double stretchScaleX;
        private double stretchScaleY;
        private double stretchScaleZ;

        public Factory(Model stretchModel) {
            this(null, stretchModel);
        }

        public Factory(Model tipModel, Model stretchModel) {
            this.stretchModel = stretchModel;
            this.tipModel = tipModel;
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
            EntityModel<T> model;
            if (this.model == null) {
                model = null;
            } else {
                model = this.model.apply(manager);
            }
            GomuProjectileRenderer<T> renderer = new GomuProjectileRenderer<>(manager, this.tipModel, this.stretchModel);
            renderer.setStretchScale(this.stretchScaleX, this.stretchScaleY, this.stretchScaleZ);
            renderer.setScale(this.scaleX, this.scaleY, this.scaleZ);
            renderer.setColor(this.colour);
            renderer.setUseArmSkin();
            return renderer;
        }
    }

    public record Model(boolean leg) {}
}
