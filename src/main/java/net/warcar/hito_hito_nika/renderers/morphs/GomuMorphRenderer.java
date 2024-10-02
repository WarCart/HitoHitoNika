package net.warcar.hito_hito_nika.renderers.morphs;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.warcar.hito_hito_nika.renderers.layers.TrueGomuSmokeLayer;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;
import xyz.pixelatedw.mineminenomi.renderers.layers.BodyCoatingLayer;
import xyz.pixelatedw.mineminenomi.renderers.morphs.ZoanMorphRenderer;

public class GomuMorphRenderer<T extends AbstractClientPlayerEntity, M extends MorphModel<?>> extends ZoanMorphRenderer<T, M> {
    private final ResourceLocation[] textures;
    private Vector3f size = new Vector3f(0, 0, 0);
    public GomuMorphRenderer(EntityRendererManager rendererManager, MorphInfo info, boolean smallHands, ResourceLocation... overlayTextures) {
        super(rendererManager, info, smallHands);
        this.addLayer(new TrueGomuSmokeLayer<>(this));
        this.addLayer(new BodyCoatingLayer<>(this));
        this.textures = overlayTextures;
    }

    @Override
    protected void renderModel(AbstractClientPlayerEntity entity, MatrixStack matrixStack, int packedLight, IRenderTypeBuffer buffer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        super.renderModel(entity, matrixStack, packedLight, buffer, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        matrixStack.pushPose();
        for (ResourceLocation texture : this.textures) {
            this.getModel().renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entityTranslucent(texture)), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        }
        matrixStack.popPose();
    }

    @Override
    protected void scale(AbstractClientPlayerEntity p_225620_1_, MatrixStack p_225620_2_, float p_225620_3_) {
        p_225620_2_.scale(this.size.x(), this.size.y(), this.size.z());
    }

    public ResourceLocation getTextureLocation(AbstractClientPlayerEntity entity) {
        return entity.getSkinTextureLocation();
    }

    public static class Factory<T extends PlayerEntity> implements IRenderFactory<T> {
        private MorphInfo info;
        private boolean hasSmallHands;
        private Vector3f size = new Vector3f(1, 1, 1);
        private ResourceLocation[] overlays = new ResourceLocation[0];

        public Factory(MorphInfo info, boolean hasSmallHands) {
            this.info = info;
            this.hasSmallHands = hasSmallHands;
        }

        public Factory<T> setOverlays(ResourceLocation... overlays) {
            this.overlays = overlays;
            return this;
        }

        public EntityRenderer<? super T> createRenderFor(EntityRendererManager manager) {
            GomuMorphRenderer gomuMorphRenderer = new GomuMorphRenderer(manager, this.info, this.hasSmallHands, this.overlays);
            gomuMorphRenderer.size = size;
            return gomuMorphRenderer;
        }

        public Factory<T> setSize(Vector3f size) {
            this.size = size;
            return this;
        }
    }
}
