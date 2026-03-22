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
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;

public class TrueGomuSmokeLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private GomuSmokeModel model = new GomuSmokeModel();

    private static final ResourceLocation[] SMOKE_ANIM = new ResourceLocation[] {new ResourceLocation("mineminenomi", "textures/models/zoanmorph/g5/smoke_0.png"),
            new ResourceLocation("mineminenomi", "textures/models/zoanmorph/g5/smoke_1.png"), new ResourceLocation("mineminenomi", "textures/models/zoanmorph/g5/smoke_2.png"),
            new ResourceLocation("mineminenomi", "textures/models/zoanmorph/g5/smoke_3.png")};

    public TrueGomuSmokeLayer(EntityRendererProvider.Context ctx, RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    public void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, T entity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        IAbilityData abilityData = AbilityDataCapability.get(entity);
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
