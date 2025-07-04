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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.warcar.hito_hito_nika.renderers.layers.TrueGomuSmokeLayer;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;
import xyz.pixelatedw.mineminenomi.renderers.layers.BodyCoatingLayer;
import xyz.pixelatedw.mineminenomi.renderers.layers.abilities.GomuDawnWhipLayer;
import xyz.pixelatedw.mineminenomi.renderers.morphs.ZoanMorphRenderer;
import xyz.pixelatedw.mineminenomi.renderers.morphs.ZoanMorphRenderer2;

public class GomuMorphRenderer<T extends LivingEntity, M extends MorphModel<T>> extends ZoanMorphRenderer2<T, M> {
    private final ResourceLocation[] textures;
    public GomuMorphRenderer(EntityRendererManager rendererManager, MorphInfo info, ResourceLocation... overlayTextures) {
        super(rendererManager, info);
        if (info.getModel() == null) {
            this.addLayer(new BipedArmorLayer<>(this, new BipedModel<>(0.5F), new BipedModel<>(1.0F)));
        }
        this.addLayer(new TrueGomuSmokeLayer<>(this));
        this.addLayer(new GomuDawnWhipLayer<>(this));
        this.addLayer(new BodyCoatingLayer<>(this));
        this.textures = overlayTextures;
    }

    @Override
    protected void renderModel(T entity, MatrixStack matrixStack, int packedLight, IRenderTypeBuffer buffer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        super.renderModel(entity, matrixStack, packedLight, buffer, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        matrixStack.pushPose();
        for (ResourceLocation texture : this.textures) {
            this.getModel().renderToBuffer(matrixStack, buffer.getBuffer(RenderType.entityTranslucent(texture)), packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        }
        matrixStack.popPose();
    }

    public ResourceLocation getTextureLocation(T entity) {
        if (entity instanceof AbstractClientPlayerEntity) {
            return ((AbstractClientPlayerEntity) entity).getSkinTextureLocation();
        }
        return this.getOriginalRenderer().getTextureLocation(entity);
    }

    public static class Factory<T extends PlayerEntity> implements IRenderFactory<T> {
        private MorphInfo info;
        private ResourceLocation[] overlays = new ResourceLocation[0];

        public Factory(MorphInfo info) {
            this.info = info;
        }

        public Factory<T> setOverlays(ResourceLocation... overlays) {
            this.overlays = overlays;
            return this;
        }

        public EntityRenderer<? super T> createRenderFor(EntityRendererManager manager) {
            return new GomuMorphRenderer(manager, this.info, this.overlays);
        }
    }
}
