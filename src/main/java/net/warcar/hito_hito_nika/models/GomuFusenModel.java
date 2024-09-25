package net.warcar.hito_hito_nika.models;

import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;

import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.HandSide;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.model.ModelRenderer;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

import javax.annotation.ParametersAreNonnullByDefault;

public class GomuFusenModel<T extends LivingEntity> extends MorphModel<T> {
	private final ModelRenderer Head;
	private final ModelRenderer Body;
	private final ModelRenderer RightArm;
	private final ModelRenderer LeftArm;
	private final ModelRenderer RightLeg;
	private final ModelRenderer LeftLeg;

	public GomuFusenModel() {
		super(0);
		this.texHeight = 64;
		this.texWidth = 64;
		Head = new ModelRenderer(this);
		Head.setPos(0.0F, -16.0F, 0.0F);
		Head.texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		Body = new ModelRenderer(this);
		Body.setPos(0.0F, 0.0F, 0.0F);
		Body.texOffs(16, 16).addBox(-4.0F, -8.0F, -2.0F, 8.0F, 12.0F, 4.0F, 8.0F, false);
		RightArm = new ModelRenderer(this);
		RightArm.setPos(-5.0F, -9.0F, 0.0F);
		RightArm.texOffs(40, 16).addBox(-11.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		LeftArm = new ModelRenderer(this);
		LeftArm.setPos(5.0F, -9.0F, 0.0F);
		LeftArm.texOffs(32, 48).addBox(7.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		RightLeg = new ModelRenderer(this);
		RightLeg.setPos(-1.9F, 12.0F, 0.0F);
		RightLeg.texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
		LeftLeg = new ModelRenderer(this);
		LeftLeg.setPos(1.9F, 12.0F, 0.0F);
		LeftLeg.texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		Body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		RightArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		RightLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
		LeftLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void renderFirstPersonArm(MatrixStack matrixStack, IVertexBuilder vertex, int packedLight, int overlay, float red, float green, float blue, float alpha, HandSide side) {
		if (side == HandSide.RIGHT) {
			matrixStack.translate(0.2D, 0.3D, 0.0D);
			this.RightArm.render(matrixStack, vertex, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 0.7F, 0.0F, 1.0F);
		} else {
			matrixStack.translate(-0.2D, 0.3D, 0.0D);
			this.LeftArm.render(matrixStack, vertex, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 0.7F, 0.0F, 1.0F);
		}
	}

	@ParametersAreNonnullByDefault
	public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.RightArm.xRot = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * limbSwingAmount;
		this.LeftLeg.xRot = MathHelper.cos(limbSwing) * -1.0F * limbSwingAmount;
		this.Head.yRot = netHeadYaw / (180F / (float) Math.PI);
		this.Head.xRot = headPitch / (180F / (float) Math.PI);
		this.LeftArm.xRot = MathHelper.cos(limbSwing * 0.6662F) * limbSwingAmount;
		this.RightLeg.xRot = MathHelper.cos(limbSwing) * 1.0F * limbSwingAmount;
	}

	public void renderFirstPersonLeg(MatrixStack matrixStack, IVertexBuilder vertex, int packedLight, int overlay, float red, float green, float blue, float alpha, HandSide side) {
		if (side == HandSide.RIGHT) {
			matrixStack.translate(0.0D, -1.2D, 0.3D);
			matrixStack.scale(1.5F, 1.5F, 1.5F);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(-60.0F));
			this.RightLeg.render(matrixStack, vertex, packedLight, overlay, red, green, blue, alpha);
		} else {
			matrixStack.translate(0.0D, -1.2D, 0.3D);
			matrixStack.scale(1.5F, 1.5F, 1.5F);
			matrixStack.mulPose(Vector3f.YP.rotationDegrees(60.0F));
			this.LeftLeg.render(matrixStack, vertex, packedLight, overlay, red, green, blue, alpha);
		}
	}
}
