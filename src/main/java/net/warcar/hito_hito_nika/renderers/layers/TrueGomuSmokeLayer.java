package net.warcar.hito_hito_nika.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.models.entities.AwakeningSmokeModel;

public class TrueGomuSmokeLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private AwakeningSmokeModel model;

    private static final ResourceLocation[] SMOKE_ANIM = new ResourceLocation[]{ResourceLocation.fromNamespaceAndPath("mineminenomi", "textures/models/morphs/awakening_smoke/smoke_0.png"),
            ResourceLocation.fromNamespaceAndPath("mineminenomi", "textures/models/morphs/awakening_smoke/smoke_1.png"), ResourceLocation.fromNamespaceAndPath("mineminenomi", "textures/models/morphs/awakening_smoke/smoke_2.png"),
            ResourceLocation.fromNamespaceAndPath("mineminenomi", "textures/models/morphs/awakening_smoke/smoke_3.png")};

    public TrueGomuSmokeLayer(EntityRendererProvider.Context ctx, RenderLayerParent<T, M> renderer) {
        super(renderer);
        model = new AwakeningSmokeModel(ctx.bakeLayer(AwakeningSmokeModel.LAYER_LOCATION));
    }

    public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, T entity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        IAbilityData abilityData = AbilityCapability.get(entity).get();
        if (TrueGomuHelper.hasGearFifthActive(abilityData) || TrueGomuHelper.hasGearFourthSnakemanActive(abilityData)) {
            float speed = 1000.0F;
            float anim = (float) Util.getMillis() % speed / (speed / (float)SMOKE_ANIM.length);
            matrixStack.pushPose();
            if (TrueGomuHelper.hasGearFifthActive(abilityData) && TrueGomuHelper.hasGearFourthBoundmanActive(abilityData)) {
                matrixStack.scale(2.5f, 2.5f, 2.5f);
                matrixStack.translate(0, -0.75, -0.2);
            } else if (TrueGomuHelper.hasGearFifthActive(abilityData)) {
                matrixStack.scale(1.3F, 1.3F, 1.3F);
            } else if (TrueGomuHelper.hasGearFourthSnakemanActive(abilityData)) {
                matrixStack.scale(1.5F, 1.5F, 1.5F);
            }
            VertexConsumer ivb = buffer.getBuffer(RenderType.entityTranslucent(SMOKE_ANIM[(int)Math.floor(anim)]));
            this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            this.model.renderToBuffer(matrixStack, ivb, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }
    }
}
