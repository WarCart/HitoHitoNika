package net.warcar.hito_hito_nika.renderers.morphs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.warcar.hito_hito_nika.renderers.layers.TrueGomuSmokeLayer;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityOverlay;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.renderers.layers.BodyCoatingLayer;
import xyz.pixelatedw.mineminenomi.renderers.layers.abilities.GomuDawnWhipLayer;
import xyz.pixelatedw.mineminenomi.renderers.morphs.MorphRenderer;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class GomuMorphRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends MorphRenderer<T, M> {
    public GomuMorphRenderer(EntityRendererProvider.Context ctx, MorphInfo info, M model) {
        super(ctx, info, model);
        this.addLayer(new TrueGomuSmokeLayer<>(ctx, this));
        this.addLayer(new GomuDawnWhipLayer<>(ctx, this));
        this.addLayer(new BodyCoatingLayer<>(this));
    }

    public GomuMorphRenderer<T, M> addLayer(ResourceLocation layer) {
        return this.addLayer(RenderType.entityTranslucent(layer));
    }

    public GomuMorphRenderer<T, M> addLayer(RenderType layer) {
        this.addLayer(new Layer(this, layer));
        return this;
    }

    public ResourceLocation getTextureLocation(T entity) {
        if (entity instanceof AbstractClientPlayer) {
            return ((AbstractClientPlayer) entity).getSkinTextureLocation();
        }
        return this.getOriginalRenderer().getTextureLocation(entity);
    }

    @Override
    public void renderFirstPersonLimb(T entity, ItemStack itemStack, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, HumanoidArm side, Optional<AbilityOverlay> overlay, float swingProgress, float equipProgress, boolean isLeg) {
    }//Causes crash for some reason idk and idc

    private class Layer extends RenderLayer<T, M> {
        private final RenderType texture;
        public Layer(RenderLayerParent<T, M> pRenderer, RenderType texture) {
            super(pRenderer);
            this.texture = texture;
        }

        @Override
        public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            VertexConsumer consumer = pBuffer.getBuffer(this.texture);
            pPoseStack.pushPose();
            this.getParentModel().renderToBuffer(pPoseStack, consumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            pPoseStack.popPose();
        }
    }
}
