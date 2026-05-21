package net.warcar.hito_hito_nika.animations;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.pixelatedw.mineminenomi.api.animations.AnimationId;

@OnlyIn(Dist.CLIENT)
public class TrueGomuBulletAnimation extends TimedAnimation<LivingEntity, HumanoidModel<LivingEntity>> {
    public TrueGomuBulletAnimation(AnimationId<TrueGomuBulletAnimation> animId) {
        super(animId);
        this.setAnimationAngles(this::angles);
    }

    public void angles(LivingEntity player, HumanoidModel<LivingEntity> model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        ModelPart mainHand;
        if (player.getMainArm().equals(HumanoidArm.RIGHT)) {
            mainHand = model.rightArm;
        } else {
            mainHand = model.leftArm;
        }
        mainHand.yRot = 0.0F;
        mainHand.xRot = (float)Math.toRadians(90.0F);
        double percentage = getPercentage();
        double invPercent = 1.0D - percentage;
        if (percentage < 0.5D) {
            mainHand.yScale = (float) percentage * 22;
        } else {
            mainHand.yScale = (float) invPercent * 20 + 1;
        }
    }
}
