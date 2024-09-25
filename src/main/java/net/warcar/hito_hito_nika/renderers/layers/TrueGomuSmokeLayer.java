package net.warcar.hito_hito_nika.renderers.layers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.abilities.TrueGearFourthAbility;
import net.warcar.hito_hito_nika.abilities.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.models.abilities.GomuSmokeModel;

public class TrueGomuSmokeLayer<T extends LivingEntity, M extends EntityModel<T>> extends LayerRenderer<T, M> {
    private GomuSmokeModel model = new GomuSmokeModel();

    private static final ResourceLocation[] SMOKE_ANIM = new ResourceLocation[] {new ResourceLocation("mineminenomi", "textures/models/zoanmorph/g5/smoke_0.png"),
            new ResourceLocation("mineminenomi", "textures/models/zoanmorph/g5/smoke_1.png"), new ResourceLocation("mineminenomi", "textures/models/zoanmorph/g5/smoke_2.png"),
            new ResourceLocation("mineminenomi", "textures/models/zoanmorph/g5/smoke_3.png")};

    public TrueGomuSmokeLayer(IEntityRenderer<T, M> renderer) {
        super(renderer);
    }

    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, T entity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
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
            IVertexBuilder ivb = buffer.getBuffer(RenderType.entityTranslucent(SMOKE_ANIM[(int)Math.floor(anim)]));
            this.model.setupAnim(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            this.model.renderToBuffer(matrixStack, ivb, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.popPose();
        }
    }
}
