package net.warcar.hito_hito_nika.renderers.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.warcar.hito_hito_nika.projectiles.PythonProjectile;
import xyz.pixelatedw.mineminenomi.abilities.haki.HakiHelper;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileRenderer;
import xyz.pixelatedw.mineminenomi.api.helpers.RendererHelper;
import xyz.pixelatedw.mineminenomi.init.ModRenderTypes;
import xyz.pixelatedw.mineminenomi.init.ModResources;

import java.util.ArrayList;
import java.util.List;

public class PythonProjectileRenderer<E extends PythonProjectile> extends NuProjectileRenderer<E, EntityModel<E>> {
    private final boolean leg;
    private LivingEntityRenderer<? super LivingEntity, ?> ownerRenderer;
    public PythonProjectileRenderer(EntityRendererProvider.Context renderManager, boolean leg) {
        super(renderManager, null);
        this.leg = leg;
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
            if (this.ownerRenderer == null) {
                this.ownerRenderer = (LivingEntityRenderer<? super LivingEntity, ?>) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity.getOwner());
            }
            List<ModelPart> stretchParts = new ArrayList<>();
            if (leg) {
                stretchParts.addAll(RendererHelper.getLegPartsFrom(ownerRenderer.getModel()));
            } else {
                stretchParts.addAll(RendererHelper.getArmPartsFrom(ownerRenderer.getModel()));
            }
            if (!stretchParts.isEmpty()) {
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

                VertexConsumer solid = buffer.getBuffer(type);
                renderToBuffer(stretchParts, matrixStack, solid, packedLight, 1, 1, 1, 1);
                if (owner != null && (entity.isAffectedByHardening() || entity.isAffectedByImbuing())) {
                    if (HakiHelper.hasAdvancedBusoActive(owner)) {
                        matrixStack.pushPose();
                        matrixStack.scale(1.2f, 1.2f, 1.02f);
                        VertexConsumer glow = buffer.getBuffer(ModRenderTypes.TRANSPARENT_COLOR);
                        renderToBuffer(stretchParts, matrixStack, glow, packedLight, 0.886f, 0.5f, 0.1f, 0.4f);
                        matrixStack.popPose();
                    }
                }
                matrixStack.popPose();
            }
        }
    }

    private void renderToBuffer(List<ModelPart> limbs, PoseStack matrixStack, VertexConsumer skinVertex, int packedLight, float red, float green, float blue, float alpha) {
        for (ModelPart limb : limbs) {
            limb.resetPose();
            matrixStack.pushPose();
            limb.render(matrixStack, skinVertex, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
            matrixStack.popPose();
        }
    }

    public static class Factory<T extends PythonProjectile> extends NuProjectileRenderer.Factory<T> {
        private final boolean leg;

        public Factory(boolean leg) {
            this.leg = leg;
        }
        public EntityRenderer<T> create(EntityRendererProvider.Context manager) {
            PythonProjectileRenderer<T> renderer = new PythonProjectileRenderer<>(manager, leg);
            renderer.setScale(this.scaleX, this.scaleY, this.scaleZ);
            renderer.setColor(this.colour);
            return renderer;
        }
    }
}
