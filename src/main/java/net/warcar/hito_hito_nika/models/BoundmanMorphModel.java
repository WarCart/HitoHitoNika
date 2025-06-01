package net.warcar.hito_hito_nika.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.warcar.hito_hito_nika.abilities.GomuBulletAbility;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.config.CommonConfig;
import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;

public class BoundmanMorphModel<T extends LivingEntity> extends MorphModel<T> implements IHasArm {
    private final ModelRenderer rightArm;
    private final ModelRenderer rightLeg;
    private final ModelRenderer head;
    private final ModelRenderer body;
    private final ModelRenderer leftArm;
    private final ModelRenderer leftLeg;
    private final boolean gomuAnimations;
    private boolean isFlying;

    public BoundmanMorphModel(boolean gomuAnimations) {
        super(-0.2F);
        this.texWidth = 64;
        this.texHeight = 64;
        this.gomuAnimations = gomuAnimations;
        this.rightArm = new ModelRenderer(this);
        this.rightArm.setPos(0F, -2.0F, 0.0F);
        this.rightArm.texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        this.rightLeg = new ModelRenderer(this);
        this.rightLeg.setPos(0F, 0.0F, 0.0F);
        this.rightLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        this.head = new ModelRenderer(this);
        this.head.setPos(0.0F, -5.4F, 0.0F);
        this.head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
        this.body = new ModelRenderer(this);
        this.body.setPos(0.0F, -5.4F, 0.0F);
        this.body.texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, 0.0F, false);
        this.leftArm = new ModelRenderer(this);
        this.leftArm.setPos(9.5F, -2.0F, 0.0F);
        this.leftArm.texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        this.leftLeg = new ModelRenderer(this);
        this.leftLeg.setPos(3.2F, 15.0F, 0.0F);
        this.leftLeg.texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
        super.body = this.body;
        super.head = this.head;
        super.rightArm = this.rightArm;
        super.leftArm = this.leftArm;
        super.rightLeg = this.rightLeg;
        super.leftLeg = this.leftLeg;
    }

    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float scale = 1.5F;
        matrixStack.pushPose();
        if (this.isFlying) matrixStack.mulPose(Vector3f.XP.rotationDegrees(90));
        matrixStack.scale(scale, scale, scale);
        matrixStack.translate(0.0D, -0.8D, 0.0D);
        matrixStack.pushPose();
        matrixStack.translate(0.0D, -0.21D, 0.0D);
        this.head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        if (this.gomuAnimations) {
            this.hat.copyFrom(this.head);
            this.hat.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }

        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.scale(2.0F, 1.7F, 3.0F);
        this.body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        matrixStack.popPose();
        PlayerEntity player = Minecraft.getInstance().player;
        if (this.gomuAnimations && !(EntityStatsCapability.get(player).isBlackLeg() && CommonConfig.INSTANCE.isLegAbilities())) {
            float time = 0.2F;
            GomuBulletAbility ability = AbilityDataCapability.get(player).getEquippedAbility(GomuBulletAbility.INSTANCE);
            if (ability != null && ability.isCharging()) {
                float maxChargeTime = ability.getMaxChargeTime();
                if (maxChargeTime == 0)
                    maxChargeTime = ability.getChargeTime();
                time += (ability.getChargeTime() / maxChargeTime) * 0.8F;
            }
            if (Float.isNaN(time))
                time = 0.2f;
            if (TrueGomuHelper.hasGearThirdActive(AbilityDataCapability.get(player))) {
                time += 1;
            }
            matrixStack.pushPose();
            matrixStack.translate(-.5d - 0.5d * time, -0.07D + 0.05D * (double) time, 0.0D);
            matrixStack.scale(1.75F * 5.0F * time, 1.75F * 5.0F * time, 1.75F * 5.0F * time);
        } else {
            matrixStack.pushPose();
            matrixStack.translate(-.5D, -0.07D, 0.0D);
            matrixStack.scale(1.75F, 1.75F, 1.75F);
        }

        this.rightArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.scale(1.75F, 1.75F, 1.75F);
        matrixStack.translate(-0.25D, -0.07D, 0.0D);
        this.leftArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        matrixStack.popPose();
        matrixStack.pushPose();
        if (this.gomuAnimations && EntityStatsCapability.get(player).isBlackLeg() && CommonConfig.INSTANCE.isLegAbilities()) {
            float time = 0.2F;
            GomuBulletAbility ability = AbilityDataCapability.get(player).getEquippedAbility(GomuBulletAbility.INSTANCE);
            if (ability != null && ability.isCharging()) {
                float maxChargeTime = (float) ability.getMaxChargeTime();
                if (maxChargeTime == 0)
                    maxChargeTime = ability.getChargeTime();
                time += (1 - (float) ability.getChargeTime() / maxChargeTime) * 0.8F;
            }
            if (Float.isNaN(time)) time = 0.2f;
            if (TrueGomuHelper.hasGearThirdActive(AbilityDataCapability.get(player))) {
                time += 1;
            }
            matrixStack.translate(-.13d - 0.6d * time, .5D + 0.15D * (double) time, 0.0D);
            matrixStack.scale(5.0F * time, 5.0F * time, 5.0F * time);
        } else {
            matrixStack.translate(-0.25D, 0.55D, 0.0D);
        }
        this.rightLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.translate(-0.05D, -0.4D, 0.0D);
        this.leftLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        matrixStack.popPose();
        matrixStack.popPose();
    }

    public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        boolean isMoving = Math.pow(entity.getDeltaMovement().x, 2) + Math.pow(entity.getDeltaMovement().z, 2) > 0.5;
        BlockPos pos = entity.blockPosition().below(2);
        boolean isInAir = entity.level.getBlockState(pos).getMaterial() == Material.AIR;
        boolean isFlying = isMoving && isInAir;
        if (this.gomuAnimations) {
            this.isFlying = isFlying;
            if (isFlying) {
                this.rightArm.zRot = (float)Math.toRadians(90.0D);
                this.leftArm.zRot = (float)Math.toRadians(-90.0D);
            }

            boolean flag = entity.getFallFlyingTicks() > 4;
            boolean flag1 = entity.isVisuallySwimming();
            this.head.yRot = netHeadYaw * 0.017453292F;
            if (flag) {
                this.head.xRot = -0.7853982F;
            } else if (this.swimAmount > 0.0F) {
                if (flag1) {
                    this.head.xRot = this.rotlerpRad(this.head.xRot, -0.7853982F, this.swimAmount);
                } else {
                    this.head.xRot = this.rotlerpRad(this.head.xRot, headPitch * 0.017453292F, this.swimAmount);
                }
            } else {
                this.head.xRot = headPitch * 0.017453292F;
                if ((double)this.head.xRot > 0.6D) {
                    this.head.xRot = 0.6F;
                }
            }

            float f = 1.0F;
            if (!isFlying) {
                this.rightArm.xRot = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 0.8F * limbSwingAmount * 0.5F / f;
                this.leftArm.xRot = MathHelper.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount * 0.5F / f;
            }

            float speed = 0.4F;
            if (entity.isSprinting()) {
                speed = 0.7F;
            }

            ModelRenderer var10000;
            if (!entity.getMainHandItem().isEmpty()) {
                var10000 = this.rightArm;
                var10000.xRot += -0.15F;
            }

            this.attackTime = entity.attackAnim;
            boolean isBlackLeg = EntityStatsCapability.get(entity).isBlackLeg();
            if (this.attackTime > 0.0F) {
                this.body.yRot = MathHelper.sin(MathHelper.sqrt(this.attackTime) * 6.2831855F) * 0.2F;
                float f1;
                float f2;
                float f3;
                f1 = 1.0F - this.attackTime;
                f1 *= f1;
                f1 *= f1;
                f1 = 1.0F - f1;
                f2 = MathHelper.sin(f1 * 3.1415927F);
                f3 = MathHelper.sin(this.attackTime * 3.1415927F) * -(this.head.xRot - 0.7F) * 0.75F;
                if (isBlackLeg) {
                    var10000 = this.rightLeg;
                    var10000.yRot += this.body.yRot;
                    var10000 = this.leftLeg;
                    var10000.yRot += this.body.yRot;
                    this.rightLeg.xRot = (float)((double)this.rightArm.xRot - ((double)f2 * 1.5D + (double)f3));
                    var10000 = this.rightLeg;
                    var10000.yRot += this.body.yRot * 2.0F;
                } else {
                    var10000 = this.rightArm;
                    var10000.yRot += this.body.yRot;
                    this.leftArm.z = -MathHelper.sin(this.body.yRot) * 5.0F;
                    var10000 = this.leftArm;
                    var10000.yRot -= this.body.yRot;
                    var10000 = this.leftArm;
                    var10000.xRot -= this.body.yRot;
                    this.rightArm.xRot = (float)((double)this.rightArm.xRot - ((double)f2 * 1.2D + (double)f3));
                    var10000 = this.rightArm;
                    var10000.yRot += this.body.yRot * 2.0F;
                    var10000 = this.rightArm;
                    var10000.zRot += MathHelper.sin(this.attackTime * 3.1415927F) * -0.4F;
                }
            }

            if (!isMoving) {
                this.rightArm.xRot = (float)Math.toRadians(-90.0D);
                this.leftArm.xRot = (float)Math.toRadians(-90.0D);
                this.leftArm.zRot = (float)Math.toRadians(10.0D);
                this.leftArm.yRot = (float)Math.toRadians(-5.0D);
                var10000 = this.leftArm;
                var10000.z += 4.0F;
            }
        } else if (isFlying && entity.isSprinting()) {
            this.rightArm.zRot = (float)Math.toRadians(90.0D);
            this.leftArm.zRot = (float)Math.toRadians(-90.0D);
        }
    }

    public void renderFirstPersonArm(MatrixStack matrixStack, IVertexBuilder vertex, int packedLight, int overlay, float red, float green, float blue, float alpha, HandSide side) {
    }

    public void renderFirstPersonLeg(MatrixStack matrixStack, IVertexBuilder vertex, int packedLight, int overlay, float red, float green, float blue, float alpha, HandSide side) {
    }
}
