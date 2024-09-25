package net.warcar.hito_hito_nika.models;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.matrix.MatrixStack;

import javax.annotation.ParametersAreNonnullByDefault;

public class KingBajrangGunModel extends EntityModel<Entity> {
	private final ModelRenderer bone;

	public KingBajrangGunModel() {
		this.texHeight = 64;
		this.texWidth = 64;
		bone = new ModelRenderer(this);
		bone.setPos(0.0F, 24.5F, 0.0F);
		bone.texOffs(0, 17).addBox(-4.0F, -15.5F, -2.0F, 8.0F, 17.0F, 3.0F, 0.0F, false);
		bone.texOffs(22, 24).addBox(-4.0F, -15.5F, 1.0F, 8.0F, 13.0F, 1.0F, 0.0F, false);
		bone.texOffs(42, 24).addBox(-4.0F, -15.5F, 2.0F, 8.0F, 11.0F, 1.0F, 0.0F, false);
		bone.texOffs(22, 17).addBox(-4.0F, -16.5F, -2.0F, 8.0F, 1.0F, 6.0F, 0.0F, false);
		bone.texOffs(34, 0).addBox(-4.0F, -15.5F, 4.0F, 8.0F, 9.0F, 2.0F, 0.0F, false);
		bone.texOffs(44, 11).addBox(-4.0F, -14.5F, 6.0F, 8.0F, 8.0F, 1.0F, 0.0F, false);
		bone.texOffs(44, 20).addBox(-4.0F, -6.5F, 4.0F, 8.0F, 1.0F, 2.0F, 0.0F, false);
		bone.texOffs(14, 38).addBox(-4.0F, -15.5F, 3.0F, 8.0F, 11.0F, 1.0F, 0.0F, false);
		bone.texOffs(50, 52).addBox(5.0F, -11.5F, 0.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
		bone.texOffs(60, 23).addBox(5.0F, -7.5F, 1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		bone.texOffs(14, 60).addBox(5.0F, -12.5F, 1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		bone.texOffs(60, 29).addBox(5.0F, -10.5F, 4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		bone.texOffs(60, 26).addBox(5.0F, -10.5F, -1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		bone.texOffs(32, 56).addBox(-6.0F, -10.5F, 4.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		bone.texOffs(60, 5).addBox(-6.0F, -7.5F, 1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		bone.texOffs(58, 8).addBox(-6.0F, -12.5F, 1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		bone.texOffs(52, 40).addBox(-6.0F, -11.5F, 0.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
		bone.texOffs(52, 40).addBox(-6.0F, -10.5F, -1.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		bone.texOffs(27, 56).addBox(-7.0F, -11.0F, 0.5F, 1.0F, 3.0F, 3.0F, 0.0F, false);
		bone.texOffs(19, 56).addBox(6.0F, -11.0F, 0.5F, 1.0F, 3.0F, 3.0F, 0.0F, false);
		bone.texOffs(0, 37).addBox(4.0F, -15.5F, -2.0F, 1.0F, 10.0F, 6.0F, 0.0F, false);
		bone.texOffs(4, 59).addBox(4.0F, -15.5F, 4.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);
		bone.texOffs(34, 32).addBox(-5.0F, -15.5F, -2.0F, 1.0F, 10.0F, 6.0F, 0.0F, false);
		bone.texOffs(0, 59).addBox(-5.0F, -15.5F, 4.0F, 1.0F, 8.0F, 1.0F, 0.0F, false);
		bone.texOffs(26, 48).addBox(-1.0F, -10.0F, -7.75F, 2.0F, 2.0F, 6.0F, -0.2F, false);
		bone.texOffs(52, 0).addBox(-4.0F, -8.0F, -3.0F, 8.0F, 1.0F, 1.0F, -0.9F, false);
		bone.texOffs(58, 58).addBox(-2.75F, -13.25F, -4.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		bone.texOffs(44, 50).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 1.0F, 1.0F, -0.9F, false);
		bone.texOffs(0, 0).addBox(-5.0F, -15.5F, 7.0F, 10.0F, 10.0F, 7.0F, 0.0F, false);
		bone.texOffs(34, 11).addBox(-1.0F, -5.0F, -4.75F, 2.0F, 2.0F, 3.0F, 0.0F, false);
		bone.texOffs(16, 50).addBox(-3.0F, -5.0F, -6.75F, 4.0F, 2.0F, 2.0F, 0.0F, false);
		bone.texOffs(0, 53).addBox(-3.0F, -7.0F, -8.75F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		bone.texOffs(41, 41).addBox(-1.0F, -7.0F, -14.75F, 2.0F, 2.0F, 7.0F, 0.0F, false);
		ModelRenderer cube_r1 = new ModelRenderer(this);
		cube_r1.setPos(-1.0F, -3.6716F, -2.4853F);
		bone.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.7854F, 0.0F, 0.0F);
		cube_r1.texOffs(0, 37).addBox(-3.0F, 4.0F, 7.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
		cube_r1.texOffs(0, 0).addBox(-2.0F, 4.0F, 7.0F, 2.0F, 6.0F, 1.0F, 0.0F, false);
		cube_r1.texOffs(54, 2).addBox(0.0F, 4.0F, 7.0F, 2.0F, 7.0F, 1.0F, 0.0F, false);
		cube_r1.texOffs(27, 0).addBox(2.0F, 4.0F, 7.0F, 2.0F, 6.0F, 1.0F, 0.0F, false);
		cube_r1.texOffs(8, 37).addBox(4.0F, 4.0F, 7.0F, 1.0F, 5.0F, 1.0F, 0.0F, false);
		ModelRenderer cube_r2 = new ModelRenderer(this);
		cube_r2.setPos(2.5F, -12.75F, -6.5F);
		bone.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, -1.5708F);
		cube_r2.texOffs(44, 52).addBox(-1.5F, -1.75F, 2.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		cube_r2.texOffs(22, 20).addBox(-1.5F, -0.75F, 1.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		cube_r2.texOffs(19, 17).addBox(-1.5F, -1.75F, -1.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);
		cube_r2.texOffs(36, 49).addBox(-1.5F, -0.75F, 0.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		cube_r2.texOffs(56, 52).addBox(-0.5F, -0.75F, -4.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		cube_r2.texOffs(8, 53).addBox(-0.5F, -1.75F, -2.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		cube_r2.texOffs(24, 56).addBox(-1.5F, -1.75F, -0.5F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		ModelRenderer cube_r3 = new ModelRenderer(this);
		cube_r3.setPos(-2.25F, -12.75F, -7.25F);
		bone.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, 0.0F, -3.1416F);
		cube_r3.texOffs(35, 59).addBox(-1.5F, -1.5F, 0.25F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		cube_r3.texOffs(41, 59).addBox(-0.5F, -1.5F, -1.75F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		cube_r3.texOffs(58, 40).addBox(-0.5F, -0.5F, -3.75F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		cube_r3.texOffs(60, 2).addBox(-0.5F, -1.5F, 1.25F, 2.0F, 2.0F, 1.0F, 0.0F, false);
		cube_r3.texOffs(8, 59).addBox(-1.5F, -1.5F, -0.75F, 3.0F, 2.0F, 1.0F, 0.0F, false);
		cube_r3.texOffs(26, 50).addBox(-0.5F, -0.5F, 2.25F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		ModelRenderer cube_r4 = new ModelRenderer(this);
		cube_r4.setPos(0.0F, -0.5F, 0.0F);
		bone.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, 0.0F, 1.5708F);
		cube_r4.texOffs(42, 36).addBox(-7.5F, 2.2F, -3.0F, 9.0F, 1.0F, 1.0F, -0.9F, false);
		cube_r4.texOffs(48, 38).addBox(-7.5F, -3.2F, -3.0F, 9.0F, 1.0F, 1.0F, -0.9F, false);
		ModelRenderer cube_r5 = new ModelRenderer(this);
		cube_r5.setPos(0.0F, -0.5F, 0.0F);
		bone.addChild(cube_r5);
		setRotationAngle(cube_r5, -0.7854F, 0.0F, 0.0F);
		cube_r5.texOffs(47, 59).addBox(-0.5F, -15.75F, -10.75F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		ModelRenderer cube_r6 = new ModelRenderer(this);
		cube_r6.setPos(-3.2731F, 11.037F, 0.0F);
		bone.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.0F, 0.0F, 0.3927F);
		cube_r6.texOffs(8, 50).addBox(-8.0F, -15.0F, -2.0F, 1.0F, 3.0F, 6.0F, 0.0F, false);
		ModelRenderer cube_r7 = new ModelRenderer(this);
		cube_r7.setPos(-4.1836F, 8.7137F, 0.0F);
		bone.addChild(cube_r7);
		setRotationAngle(cube_r7, 0.0F, 0.0F, -0.3927F);
		cube_r7.texOffs(36, 50).addBox(13.0F, -10.0F, -2.0F, 1.0F, 3.0F, 6.0F, 0.0F, false);
	}

	@ParametersAreNonnullByDefault
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@ParametersAreNonnullByDefault
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}
