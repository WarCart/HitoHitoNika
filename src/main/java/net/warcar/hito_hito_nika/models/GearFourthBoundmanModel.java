package net.warcar.hito_hito_nika.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.api.helpers.RendererHelper;
import xyz.pixelatedw.mineminenomi.api.morph.HumanoidMorphModel;

public class GearFourthBoundmanModel<T extends LivingEntity> extends HumanoidMorphModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ModMain.PROJECT_ID, "gear_fourth"), "main");

	public static final ResourceLocation OVERLAY = ResourceLocation.fromNamespaceAndPath(ModMain.PROJECT_ID, "textures/models/morphs/g4_overlay.png");
	private boolean gomuAnimations = true;
	private boolean isFlying = false;

	public GearFourthBoundmanModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidMorphModel.createMesh(CubeDeformation.NONE, 0.0f);

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		float scale = 1.5f;
		matrixStack.pushPose();
		matrixStack.scale(scale, scale, scale);
		if (isFlying) {
			matrixStack.mulPose(Axis.XP.rotationDegrees(90));
		}
		matrixStack.translate(0, -0.8, 0);

		this.head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		if (this.gomuAnimations) {
			this.hat.copyFrom(this.head);
			this.hat.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		}

		matrixStack.pushPose();
		this.body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		matrixStack.popPose();

		if (this.gomuAnimations) {
			float time = 0.2F;
			matrixStack.pushPose();
			matrixStack.scale(1.75f * (5 * time), 1.75f * (5f * time), 1.75f * (5f * time));
		}
		else {
			matrixStack.pushPose();
			matrixStack.scale(1.75F, 1.75F, 1.75F);
			matrixStack.translate(0.25, -0.07, 0);
		}

		this.rightArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		matrixStack.popPose();

		matrixStack.pushPose();
		matrixStack.scale(1.75F, 1.75F, 1.75F);
		this.leftArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		matrixStack.popPose();

		this.rightLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		this.leftLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		matrixStack.popPose();
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		RendererHelper.resetModelToDefaultPivots(this);
		this.body.xScale = 2;
		this.body.yScale = 1.7f;
		this.body.zScale = 3;

		isFlying = Math.sqrt(entity.getDeltaMovement().x * entity.getDeltaMovement().x + entity.getDeltaMovement().z * entity.getDeltaMovement().z) * entity.zza > 0.05f;

		if (this.gomuAnimations) {
			// Handles the gear 4 posture while flying
			if (isFlying) {
				this.rightArm.zRot = (float) Math.toRadians(90);
				this.leftArm.zRot = (float) Math.toRadians(-90);
			}

			// Handles the head movement when following the mouse or when swimming
			this.setupHeadRotation(entity, headPitch, netHeadYaw);

			// Handles the arm and leg movement
			float f = 1.0F;
			if (!isFlying) {
				this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 0.8F * limbSwingAmount * 0.5F / f;
				this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount * 0.5F / f;
			}

			float speed = 0.4F;
			if (entity.isSprinting()) {
				speed = 0.7F;
			}

			this.rightLeg.y += -2 + Mth.cos(ageInTicks * speed) * 2F;
			this.leftLeg.y += -2 + Mth.cos(ageInTicks * speed) * 2F;
			if (!entity.getMainHandItem().isEmpty()) {
				this.rightArm.xRot += -0.15F;
			}

			// Handles the punch and item use animations of the model
			this.setupAttackAnimation(entity, ageInTicks);

			// Handles the gear 4 posture when idling
			if (!isFlying) {
				this.rightArm.xRot = (float) Math.toRadians(-90);
				this.leftArm.xRot = (float) Math.toRadians(-90);
				this.leftArm.zRot = (float) Math.toRadians(10);
				this.leftArm.yRot = (float) Math.toRadians(-5);
				this.leftArm.z += 3;
			}

			this.rightLeg.y += 0.5f;
			this.leftLeg.y += 0.5f;
			this.rightLeg.x -= 0.5f;
			this.leftLeg.x += 0.5f;
		}
		else if (isFlying && entity.isSprinting()) {
			// Handles the gear 4 posture while flying
			this.rightArm.zRot = (float) Math.toRadians(90);
			this.leftArm.zRot = (float) Math.toRadians(-90);
		}
	}

	@Override
	public void renderFirstPersonArm(PoseStack matrixStack, VertexConsumer vertex, int packedLight, int overlay, float red, float green, float blue, float alpha, HumanoidArm side, boolean isLeg) {
		/*if (!isLeg) {
			if (side == HumanoidArm.RIGHT) {
				matrixStack.translate(0.2, 0.3, 0);
				this.rightArm.render(matrixStack, vertex, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 0.7f, 0, 1);
			}
			else {
				matrixStack.translate(-0.2, 0.3, 0);
				this.leftArm.render(matrixStack, vertex, packedLight, OverlayTexture.NO_OVERLAY, 1.0f, 0.7f, 0, 1);
			}
		}
		else if (side == HumanoidArm.RIGHT) {
			matrixStack.translate(0.0, -1.2, 0.3);
			matrixStack.scale(1.5f, 1.5f, 1.5f);
			matrixStack.mulPose(Axis.YP.rotationDegrees(-60));
			this.rightLeg.render(matrixStack, vertex, packedLight, overlay, red, green, blue, alpha);
		}
		else {
			matrixStack.translate(0.0, -1.2, 0.3);
			matrixStack.scale(1.5f, 1.5f, 1.5f);
			matrixStack.mulPose(Axis.YP.rotationDegrees(60));
			this.leftLeg.render(matrixStack, vertex, packedLight, overlay, red, green, blue, alpha);
		}*/
	}

	@Override
	public void translateToHand(HumanoidArm side, PoseStack matrixStack) {
		super.translateToHand(side, matrixStack);
		matrixStack.translate(side == HumanoidArm.RIGHT ? -0.6 : 0.6, -0.5, -0.2);
	}
}
