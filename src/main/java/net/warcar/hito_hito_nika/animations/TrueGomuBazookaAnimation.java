package net.warcar.hito_hito_nika.animations;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.warcar.hito_hito_nika.renderers.IModelRendererMixin;
import xyz.pixelatedw.mineminenomi.api.animations.AnimationId;

public class TrueGomuBazookaAnimation extends TimedAnimation<LivingEntity, BipedModel<LivingEntity>> {
    public TrueGomuBazookaAnimation(AnimationId<TrueGomuBazookaAnimation> animId) {
        super(animId);
        this.setAnimationAngles(this::angles);
    }

    public void angles(LivingEntity player, BipedModel model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        model.rightArm.yRot = 0.0F;
        model.leftArm.yRot = 0.0F;
        model.rightArm.xRot = (float)Math.toRadians(90.0F);
        model.leftArm.xRot = (float)Math.toRadians(90.0F);
        double percentage = getPercentage();
        double invPercent = 1.0D - percentage;
        if (percentage < 0.5D) {
            ((IModelRendererMixin) model.rightArm).setYScale((float) percentage * 22);
            ((IModelRendererMixin) model.leftArm).setYScale((float) percentage * 22);
        } else {
            ((IModelRendererMixin) model.rightArm).setYScale((float) invPercent * 20 + 1);
            ((IModelRendererMixin) model.leftArm).setYScale((float) invPercent * 20 + 1);
        }
    }
}
