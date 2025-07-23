package net.warcar.hito_hito_nika.animations;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.HandSide;
import net.warcar.hito_hito_nika.renderers.IModelRendererMixin;
import xyz.pixelatedw.mineminenomi.api.animations.AnimationId;

public class TrueGomuBulletAnimation extends TimedAnimation<LivingEntity, BipedModel<LivingEntity>> {
    public TrueGomuBulletAnimation(AnimationId<TrueGomuBulletAnimation> animId) {
        super(animId);
        this.setAnimationAngles(this::angles);
    }

    public void angles(LivingEntity player, BipedModel model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        ModelRenderer mainHand;
        if (player.getMainArm().equals(HandSide.RIGHT)) {
            mainHand = model.rightArm;
        } else {
            mainHand = model.leftArm;
        }
        mainHand.yRot = 0.0F;
        mainHand.xRot = (float)Math.toRadians(90.0F);
        double percentage = getPercentage();
        double invPercent = 1.0D - percentage;
        if (percentage < 0.5D) {
            ((IModelRendererMixin) mainHand).setYScale((float) percentage * 22);
        } else {
            ((IModelRendererMixin) mainHand).setYScale((float) invPercent * 20 + 1);
        }
    }
}
