package net.warcar.hito_hito_nika.animations;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.pixelatedw.mineminenomi.api.animations.AnimationId;

@OnlyIn(Dist.CLIENT)
public class TrueGomuBazookaAnimation extends TimedAnimation<LivingEntity, HumanoidModel<LivingEntity>> {
    public TrueGomuBazookaAnimation(AnimationId<TrueGomuBazookaAnimation> animId) {
        super(animId);
        this.setAnimationAngles(this::angles);
    }

    public void angles(LivingEntity player, HumanoidModel<LivingEntity> model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        model.rightArm.yRot = 0.0F;
        model.leftArm.yRot = 0.0F;
        model.rightArm.xRot = (float)Math.toRadians(90.0F);
        model.leftArm.xRot = (float)Math.toRadians(90.0F);
        double percentage = getPercentage();
        double invPercent = 1.0D - percentage;
        if (percentage < 0.5D) {
            model.rightArm.yScale = (float) percentage * 22;
            model.leftArm.yScale = (float) percentage * 22;
        } else {
            model.rightArm.yScale = (float) invPercent * 20 + 1;
            model.leftArm.yScale = (float) invPercent * 20 + 1;
        }
    }
}
