package net.warcar.hito_hito_nika.renderers.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.warcar.hito_hito_nika.entities.LuffyBoss;
import net.warcar.hito_hito_nika.projectiles.PythonProjectile;
import org.joml.Matrix4f;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileRenderer;
import xyz.pixelatedw.mineminenomi.init.ModRenderTypes;

import java.util.List;

import static net.warcar.hito_hito_nika.renderers.projectiles.GomuProjectileRenderer.*;

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
        if (entity.tickCount < 1 || owner == null) {
            return;
        }

        List<Vec3> points = entity.getAllTurns(partialTicks);
        int segments = points.size() - 1;

        if (segments == 0) {
            return;
        }


        float[] segmentLengths = new float[segments];

        Vec3 prev = points.get(0);
        for (int segment = 0; segment < segments; ++segment) {
            Vec3 cur = points.get(segment + 1);
            segmentLengths[segment] = (float) prev.distanceTo(cur);
            prev = cur;
        }

        float[] offsetsX = new float[segments];
        float[] offsetsY = new float[segments];

        var body = this.getRenderType(entity);

        matrixStack.pushPose();


        matrixStack.pushPose();
        prev = points.get(0);
        for (int segmentIndex = 0; segmentIndex < segments; ++segmentIndex) {
            float y = offsetsY[segmentIndex];
            float x = offsetsX[segmentIndex];

            float depth = 1;

            float endY = ((segmentIndex == segments - 1) ? y : offsetsY[segmentIndex + 1]);
            float endX = ((segmentIndex == segments - 1) ? x : offsetsX[segmentIndex + 1]);
            Vec3 cur = points.get(segmentIndex + 1);
            var vec = prev.vectorTo(cur).normalize();
            var yaw = Mth.atan2(vec.x, vec.z);
            var pitch = Mth.atan2(vec.y, vec.horizontalDistance());
            matrixStack.pushPose();
            var toOwner = entity.getPosition(partialTicks).vectorTo(prev);
            matrixStack.translate(toOwner.x, toOwner.y, toOwner.z);
            matrixStack.mulPose(Axis.YP.rotation((float) yaw));
            matrixStack.mulPose(Axis.XN.rotation((float) pitch));
            VertexConsumer vertex = buffer.getBuffer(body);
            Matrix4f matrix4f = matrixStack.last().pose();
            float length = segmentLengths[segmentIndex];
            this.drawSides(matrix4f, vertex, y, x, segmentIndex, segments, endY, endX, 1, 1, 1, 1, depth, depth, false, false, true, false, 0, length, packedLight);
            this.drawSides(matrix4f, vertex, y, x, segmentIndex, segments, endY, endX, 1, 1, 1, 1, depth, depth, true, false, true, true, 0, length, packedLight);
            this.drawSides(matrix4f, vertex, y, x, segmentIndex, segments, endY, endX, 1, 1, 1, 1, depth, depth, true, true, false, true, 0, length, packedLight);
            this.drawSides(matrix4f, vertex, y, x, segmentIndex, segments, endY, endX, 1, 1, 1, 1, depth, depth, false, true, false, false, 0, length, packedLight);
            matrixStack.popPose();
            prev = cur;
        }
        matrixStack.popPose();

        //this.drawCaps(matrixStack.last().pose(), buffer, layers, layerAmount, offsetsX[0], offsetsY[0], 1, 1, 1, 1, 1, 1, packedLight, segments);
        matrixStack.popPose();
    }

    private void drawSides(Matrix4f matrix4f, VertexConsumer builder, float startY, float startX, int segmentIndex, int maxSegments, float endY, float endX, int r, int g, int b, float alpha, float firstOffset, float secondOffset, boolean negativeOffset, boolean bl2, boolean bl3, boolean bl4, float segmentLength, float segmentLengthAdded, int light) {
        alpha = Mth.clamp(alpha, 0.0f, 1.0f);

        float x1 = startX + (bl2 ? secondOffset : -secondOffset);
        float y1 = startY + (negativeOffset ? secondOffset : -secondOffset);
        float x2 = endX + (bl2 ? firstOffset : -firstOffset);
        float y2 = endY + (negativeOffset ? firstOffset : -firstOffset);
        float x3 = endX + (bl4 ? firstOffset : -firstOffset);
        float y3 = endY + (bl3 ? firstOffset : -firstOffset);
        float x4 = startX + (bl4 ? secondOffset : -secondOffset);
        float y4 = startY + (bl3 ? secondOffset : -secondOffset);
        float z1 = segmentIndex * segmentLength;
        float z2 = z1 + segmentLengthAdded;

        float segmentFloat = segmentIndex / (float)maxSegments;

        float u0 = U_ARM_SIDE_MIN;
        float v0 = V_ARM_SIDE_MIN + (segmentFloat * V_ARM_SIDE_DIFF);
        float u1 = U_ARM_SIDE_MAX;
        float v1 = Math.min(V_ARM_SIDE_MAX, v0 + (segmentFloat * V_ARM_SIDE_DIFF));

        if (this.leg) {
            u0 = U_LEG_SIDE_MIN;
            v0 = V_LEG_SIDE_MIN + (segmentFloat * V_LEG_SIDE_DIFF);
            u1 = U_LEG_SIDE_MAX;
            v1 = Math.min(V_LEG_SIDE_MAX, v0 + (segmentFloat * V_LEG_SIDE_DIFF));
        }

        builder.vertex(matrix4f, x1, y1, z1).color(r, g, b, alpha).uv(u0, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
        builder.vertex(matrix4f, x2, y2, z2).color(r, g, b, alpha).uv(u0, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
        builder.vertex(matrix4f, x3, y3, z2).color(r, g, b, alpha).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
        builder.vertex(matrix4f, x4, y4, z1).color(r, g, b, alpha).uv(u1, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
    }

    private void drawCaps(Matrix4f matrix4f, MultiBufferSource source, RenderType[] layers, int layerAmount, float startX, float startY, float size, float length, float r, float g, float b, float alpha, int light, int maxSegments) {
        float u0 = U_ARM_BACK_CAP_MIN;
        float v0 = V_ARM_CAP_MIN + V_ARM_CAP_DIFF;
        float u1 = U_ARM_BACK_CAP_MAX;
        float v1 = Math.min(V_ARM_CAP_MAX, v0 + V_ARM_CAP_DIFF);

        if (this.leg) {
            u0 = U_LEG_BACK_CAP_MIN;
            v0 = V_LEG_CAP_MIN + V_LEG_CAP_DIFF;
            u1 = U_LEG_BACK_CAP_MAX;
            v1 = Math.min(V_LEG_CAP_MAX, v0 + V_LEG_CAP_DIFF);
        }

        float depth = MAX_DEPTH - 1;
        float x0 = startX - (depth * size);
        float y0 = startY - (depth * size);
        float x1 = x0 + (depth * size) * 2.0f;
        float y1 = y0 + (depth * size) * 2.0f;
        VertexConsumer builder = source.getBuffer(layers[0]);
        builder.vertex(matrix4f, x0, y0, 0).color(r, g, b, alpha).uv(u0, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
        builder.vertex(matrix4f, x0, y1, 0).color(r, g, b, alpha).uv(u0, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
        builder.vertex(matrix4f, x1, y1, 0).color(r, g, b, alpha).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
        builder.vertex(matrix4f, x1, y0, 0).color(r, g, b, alpha).uv(u1, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();

        if (!this.leg) {
            u0 = U_ARM_FRONT_CAP_MIN;
            v0 = V_ARM_CAP_MIN + V_ARM_CAP_DIFF;
            u1 = U_ARM_FRONT_CAP_MAX;
            v1 = Math.min(V_ARM_CAP_MAX, v0 + V_ARM_CAP_DIFF);
        } else {
            u0 = U_LEG_FRONT_CAP_MIN;
            v0 = V_LEG_CAP_MIN + V_LEG_CAP_DIFF;
            u1 = U_LEG_FRONT_CAP_MAX;
            v1 = Math.min(V_LEG_CAP_MAX, v0 + V_LEG_CAP_DIFF);
        }

        for (int i = 0; i < layerAmount; i++) {
            depth = (MAX_DEPTH - i * i - 1);
            float addon = i * maxSegments;
            x0 = startX - ((depth + addon) * size);
            y0 = startY - ((depth + addon) * size);
            x1 = x0 + ((depth + addon) * size) * 2.0f;
            y1 = y0 + ((depth + addon) * size) * 2.0f;
            builder = source.getBuffer(layers[i]);
            float offset = (layerAmount - i - 1) / 100f;
            if (i == 2) {
                r = 0.886f;
                g = 0.5f;
                b = 0.1f;
                alpha = 0.4f;
            }
            builder.vertex(matrix4f, x0, y0, length - offset).color(r, g, b, alpha).uv(u0, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
            builder.vertex(matrix4f, x0, y1, length - offset).color(r, g, b, alpha).uv(u0, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
            builder.vertex(matrix4f, x1, y1, length - offset).color(r, g, b, alpha).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
            builder.vertex(matrix4f, x1, y0, length - offset).color(r, g, b, alpha).uv(u1, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
        }
    }

    @Override
    public RenderType getRenderType(NuProjectileEntity entity) {
        return ModRenderTypes.getPosColorTexLightmap(this.getTexture(entity));
    }

    public ResourceLocation getTexture(NuProjectileEntity entity) {
        if (entity.getOwner() instanceof LuffyBoss luffy) {
            return luffy.getCurrentTexture();
        }
        if (entity.getOwner() != null && entity.getOwner() instanceof AbstractClientPlayer player) {
            return player.getSkinTextureLocation();
        }
        else {
            return DefaultPlayerSkin.getDefaultSkin(entity.getUUID());
        }
    }

    @Override
    public boolean shouldRender(E pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
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
