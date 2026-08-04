package net.warcar.hito_hito_nika.renderers.projectiles;

import com.google.common.collect.ImmutableMap;
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
import net.warcar.hito_hito_nika.entities.LuffyBoss;
import net.warcar.hito_hito_nika.projectiles.TrueGomuProjectile;
import org.joml.Matrix4f;
import xyz.pixelatedw.mineminenomi.abilities.haki.HakiHelper;
import xyz.pixelatedw.mineminenomi.api.entities.NuLightningEntity;
import xyz.pixelatedw.mineminenomi.api.entities.NuLightningEntityRenderer;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileRenderer;
import xyz.pixelatedw.mineminenomi.api.entities.NuVerticalLightningEntity;
import xyz.pixelatedw.mineminenomi.init.ModRenderTypes;
import xyz.pixelatedw.mineminenomi.init.ModResources;

import java.util.Map;

public class GomuProjectileRenderer<M extends EntityModel<NuLightningEntity>> extends NuLightningEntityRenderer {
    private static final Map<Deformation, Float> FIRST_SIZE_MOD = ImmutableMap.<Deformation, Float>builder().put(Deformation.FLAT, 1f)
            .put(Deformation.ELEPHANT, (float) (3 / (1 + Math.exp(9)) + 0.2)).put(Deformation.GIANT, (float) (3 / (1 + Math.exp(2)))).build();
    public static final int MAX_DEPTH = 8;
    public static final float U_ARM_SIDE_MIN = 0.65f;
    public static final float U_ARM_SIDE_MAX = 0.68f;
    public static final float V_ARM_SIDE_MIN = 0.317f;
    public static final float V_ARM_SIDE_MAX = 0.5f;
    public static final float V_ARM_SIDE_DIFF = V_ARM_SIDE_MAX - V_ARM_SIDE_MIN;

    public static final float U_ARM_BACK_CAP_MIN = 0.687f;
    public static final float U_ARM_BACK_CAP_MAX = 0.718f;
    public static final float U_ARM_FRONT_CAP_MIN = 0.734f;
    public static final float U_ARM_FRONT_CAP_MAX = 0.781f;
    public static final float V_ARM_CAP_MIN = 0.25f;
    public static final float V_ARM_CAP_MAX = 0.312f;
    public static final float V_ARM_CAP_DIFF = V_ARM_CAP_MAX - V_ARM_CAP_MIN;


    public static final float U_LEG_SIDE_MIN = 0;
    public static final float U_LEG_SIDE_MAX = 0.0469f;
    public static final float V_LEG_SIDE_MIN = 0.317f;
    public static final float V_LEG_SIDE_MAX = 0.5f;
    public static final float V_LEG_SIDE_DIFF = V_LEG_SIDE_MAX - V_LEG_SIDE_MIN;

    public static final float U_LEG_BACK_CAP_MIN = 0.0625f;
    public static final float U_LEG_BACK_CAP_MAX = 0.109f;
    public static final float U_LEG_FRONT_CAP_MIN = 0.125f;
    public static final float U_LEG_FRONT_CAP_MAX = 0.781f;
    public static final float V_LEG_CAP_MIN = 0.25f;
    public static final float V_LEG_CAP_MAX = 0.172f;
    public static final float V_LEG_CAP_DIFF = V_LEG_CAP_MAX - V_LEG_CAP_MIN;

    public GomuProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);
    }

    public void render(NuLightningEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        if (entity.tickCount < 1 || entity.getSegments() < 0) {
            return;
        }
        if (!(entity instanceof TrueGomuProjectile)) {
            return;
        }
        TrueGomuProjectile gomu = (TrueGomuProjectile) entity;
        int layerAmount = HakiHelper.hasAdvancedBusoActive(entity.getOwner()) ? 3 :
                (HakiHelper.hasHardeningActive(entity.getOwner(), false, true) ? 2 : 1);

        entity.getRandom().setSeed(entity.seed);

        final int startDepth = entity.getDepth();

        int angle = entity.getAngle();
        int segments = entity.getSegments();

        if (segments == 0) {
            return;
        }

        int branches = entity.getBranches();

        float lengthFactor = Math.min((entity.tickCount + partialTicks) / 2.0F, 1.0F);
        float length = entity.getLength() * lengthFactor;
        float size = entity.getSize();
        float maxDistance = entity.getLength() / segments;
        float rotation = entity.getRotation();

        float[] segmentLengths = new float[segments];

        int targetNumber = (int) (segments * lengthFactor);

        float defAlpha = entity.getAlpha() / 255.0F;
        float fadeAlpha = entity.getFadeTime() / (float) entity.getStartFadeTime();
        float alpha = defAlpha;
        if (entity.getFadeTime() >= 0) {
            alpha = Math.min(fadeAlpha, alpha);
        }

        int segmentLength = entity.getSegmentLength();
        for (int segment = 0; segment < segments; ++segment) {
            if (segmentLength <= 0) {
                segmentLengths[segment] = segment == targetNumber ? length - maxDistance * segment : maxDistance;
            } else {
                segmentLengths[segment] = segmentLength;
            }
        }

        float[] offsetsX = new float[segments];
        float[] offsetsY = new float[segments];

//		entity.getRandom().setSeed(entity.tickCount);

        var body = this.getRenderType(entity);
        var haki = ModRenderTypes.getPosColorTexLightmap(ModResources.BUSOSHOKU_HAKI_ARM);
        var glow = ModRenderTypes.LIGHTNING;
        var layers = new RenderType[]{body, haki, glow};

        Matrix4f matrix4f = matrixStack.last().pose();

        matrixStack.mulPose(Axis.YN.rotationDegrees(entity.getYRot()));
        matrixStack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
        matrixStack.mulPose(Axis.ZP.rotationDegrees((float) (entity.tickCount * Math.PI * rotation)));
        if (entity instanceof NuVerticalLightningEntity verticalBeam) {
            matrixStack.translate(0, 0, -verticalBeam.getY());
        }

        float r = entity.getRed() / 255.0f;
        float g = entity.getGreen() / 255.0f;
        float b = entity.getBlue() / 255.0f;

        Deformation deformation = gomu.getDeformation();
        float prevSizeMod = FIRST_SIZE_MOD.get(deformation);

        int[] color = {entity.getRed(), entity.getGreen(), entity.getBlue(), 1};
        int[] glowColor = {225, 127, 25, 102};
        boolean leg = gomu.isLeg();

        for (int segmentIndex = 0; segmentIndex < segments; ++segmentIndex) {
            float y = offsetsY[segmentIndex];
            float x = offsetsX[segmentIndex];

            float sizeMod = getSizeMod(deformation, ((float) segmentIndex / segments));
            int trueAmount = layerAmount;
            if (segmentIndex < 1.5 && trueAmount > 1) {
                trueAmount = 1;
            } else if (segmentIndex < 4.5 && trueAmount > 2) {
                trueAmount = 2;
            }

            for (int layer = 0; layer < trueAmount; layer++) {
                float depth = (MAX_DEPTH - layer * layer - 1) * size;

                float endY = ((segmentIndex == segments - 1) ? y : offsetsY[segmentIndex + 1]);
                float endX = ((segmentIndex == segments - 1) ? x : offsetsX[segmentIndex + 1]);

                if (segmentIndex <= targetNumber) {
                    float addon = layer * size / 1.5f * segmentIndex;
                    float addon2 = layer * size / 1.5f * (segmentIndex - 1);
                    VertexConsumer vertex = buffer.getBuffer(layers[layer]);
                    int[] finalColor = layer == 2 ? glowColor : color;
                    this.drawSides(matrix4f, vertex, leg, y, x, segmentIndex, segments, endY, endX, finalColor[0], finalColor[1], finalColor[2], finalColor[3], (depth + addon) * sizeMod, (depth + addon2) * prevSizeMod, false, false, true, false, maxDistance, segmentLengths[segmentIndex], packedLight);
                    this.drawSides(matrix4f, vertex, leg, y, x, segmentIndex, segments, endY, endX, finalColor[0], finalColor[1], finalColor[2], finalColor[3], (depth + addon) * sizeMod, (depth + addon2) * prevSizeMod, true, false, true, true, maxDistance, segmentLengths[segmentIndex], packedLight);
                    this.drawSides(matrix4f, vertex, leg, y, x, segmentIndex, segments, endY, endX, finalColor[0], finalColor[1], finalColor[2], finalColor[3], (depth + addon) * sizeMod, (depth + addon2) * prevSizeMod, true, true, false, true, maxDistance, segmentLengths[segmentIndex], packedLight);
                    this.drawSides(matrix4f, vertex, leg, y, x, segmentIndex, segments, endY, endX, finalColor[0], finalColor[1], finalColor[2], finalColor[3], (depth + addon) * sizeMod, (depth + addon2) * prevSizeMod, false, true, false, false, maxDistance, segmentLengths[segmentIndex], packedLight);
                }
            }
            prevSizeMod = sizeMod;
        }

        this.drawCaps(deformation, matrix4f, buffer, leg, layers, layerAmount, offsetsX[0], offsetsY[0], prevSizeMod, size, length, r, g, b, alpha, packedLight, segments);
    }

    private static float getSizeMod(Deformation deformation, float segmentFloat) {
        switch (deformation) {
            case GIANT:
                return (float) (3 * (1 / (1 + Math.exp(-5 * segmentFloat + 2))));
            case ELEPHANT:
                return (float) (3 * (1 / (1 + Math.exp(-15 * segmentFloat + 9))) + 0.2);
            case FLAT:
            default:
                return 1;
        }
    }

    private void drawSides(Matrix4f matrix4f, VertexConsumer builder, boolean leg, float startY, float startX, int segmentIndex, int maxSegments, float endY, float endX, int r, int g, int b, float alpha, float firstOffset, float secondOffset, boolean negativeOffset, boolean bl2, boolean bl3, boolean bl4, float segmentLength, float segmentLengthAdded, int light) {
        float red = r / 255.0f;
        float green = g / 255.0f;
        float blue = b / 255.0f;
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

        if (leg) {
            u0 = U_LEG_SIDE_MIN;
            v0 = V_LEG_SIDE_MIN + (segmentFloat * V_LEG_SIDE_DIFF);
            u1 = U_LEG_SIDE_MAX;
            v1 = Math.min(V_LEG_SIDE_MAX, v0 + (segmentFloat * V_LEG_SIDE_DIFF));
        }

        builder.vertex(matrix4f, x1, y1, z1).color(red, green, blue, alpha).uv(u0, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
        builder.vertex(matrix4f, x2, y2, z2).color(red, green, blue, alpha).uv(u0, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
        builder.vertex(matrix4f, x3, y3, z2).color(red, green, blue, alpha).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
        builder.vertex(matrix4f, x4, y4, z1).color(red, green, blue, alpha).uv(u1, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
    }

    private void drawCaps(Deformation deformation, Matrix4f matrix4f, MultiBufferSource source, boolean leg, RenderType[] layers, int layerAmount, float startX, float startY, float lastMod, float size, float length, float r, float g, float b, float alpha, int light, int maxSegments) {
        float u0 = U_ARM_BACK_CAP_MIN;
        float v0 = V_ARM_CAP_MIN + V_ARM_CAP_DIFF;
        float u1 = U_ARM_BACK_CAP_MAX;
        float v1 = Math.min(V_ARM_CAP_MAX, v0 + V_ARM_CAP_DIFF);

        if (leg) {
            u0 = U_LEG_BACK_CAP_MIN;
            v0 = V_LEG_CAP_MIN + V_LEG_CAP_DIFF;
            u1 = U_LEG_BACK_CAP_MAX;
            v1 = Math.min(V_LEG_CAP_MAX, v0 + V_LEG_CAP_DIFF);
        }

        float depth = MAX_DEPTH - 1;
        float x0 = startX - (depth * size * FIRST_SIZE_MOD.get(deformation));
        float y0 = startY - (depth * size * FIRST_SIZE_MOD.get(deformation));
        float x1 = x0 + (depth * size * FIRST_SIZE_MOD.get(deformation)) * 2.0f;
        float y1 = y0 + (depth * size * FIRST_SIZE_MOD.get(deformation)) * 2.0f;
        VertexConsumer builder = source.getBuffer(layers[0]);
        builder.vertex(matrix4f, x0, y0, 0).color(r, g, b, alpha).uv(u0, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
        builder.vertex(matrix4f, x0, y1, 0).color(r, g, b, alpha).uv(u0, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
        builder.vertex(matrix4f, x1, y1, 0).color(r, g, b, alpha).uv(u1, v1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();
        builder.vertex(matrix4f, x1, y0, 0).color(r, g, b, alpha).uv(u1, v0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).endVertex();

        if (!leg) {
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
            x0 = startX - ((depth + addon) * size * lastMod) / 2.0f;
            y0 = startY - ((depth + addon) * size * lastMod) / 2.0f;
            x1 = x0 + ((depth + addon) * size * lastMod);
            y1 = y0 + ((depth + addon) * size * lastMod);
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
    public ResourceLocation getTextureLocation(NuLightningEntity lightning) {
        if (lightning.getOwner() instanceof LuffyBoss luffy) {
            return luffy.getCurrentTexture();
        }
        return super.getTextureLocation(lightning);
    }

    public static class Factory extends NuProjectileRenderer.Factory<NuLightningEntity> {
        public Factory setDeformation(Deformation deformation) {
            return this;
        }
        
        public EntityRenderer<NuLightningEntity> create(Context manager) {
            EntityModel<NuLightningEntity> model;
            if (this.model == null) {
                model = null;
            } else {
                model = this.model.apply(manager);
            }
            GomuProjectileRenderer<?> renderer = new GomuProjectileRenderer<>(manager);
            renderer.setUseArmSkin();
            return renderer;
        }
    }

    public enum Deformation {
        FLAT,
        GIANT,
        ELEPHANT
    }
}
