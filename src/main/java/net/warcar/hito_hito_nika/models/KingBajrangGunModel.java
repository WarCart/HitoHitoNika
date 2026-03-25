package net.warcar.hito_hito_nika.models;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;

public class KingBajrangGunModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(HitoHitoNoMiNikaMod.MOD_ID, "king_bajrang_gun"), "main");
	private final ModelPart bone;

	public KingBajrangGunModel(ModelPart root) {
		this.bone = root.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 17).addBox(-4.0F, -15.5F, -2.0F, 8.0F, 17.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(22, 24).addBox(-4.0F, -15.5F, 1.0F, 8.0F, 13.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(42, 24).addBox(-4.0F, -15.5F, 2.0F, 8.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(22, 17).addBox(-4.0F, -16.5F, -2.0F, 8.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(34, 0).addBox(-4.0F, -15.5F, 4.0F, 8.0F, 9.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(44, 11).addBox(-4.0F, -14.5F, 6.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(44, 20).addBox(-4.0F, -6.5F, 4.0F, 8.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(14, 38).addBox(-4.0F, -15.5F, 3.0F, 8.0F, 11.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(50, 52).addBox(5.0F, -11.5F, 0.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(60, 23).addBox(5.0F, -7.5F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(14, 60).addBox(5.0F, -12.5F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(60, 29).addBox(5.0F, -10.5F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(60, 26).addBox(5.0F, -10.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(32, 56).addBox(-6.0F, -10.5F, 4.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(60, 5).addBox(-6.0F, -7.5F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(58, 8).addBox(-6.0F, -12.5F, 1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(52, 40).addBox(-6.0F, -11.5F, 0.0F, 1.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(52, 40).addBox(-6.0F, -10.5F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(27, 56).addBox(-7.0F, -11.0F, 0.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(19, 56).addBox(6.0F, -11.0F, 0.5F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 37).addBox(4.0F, -15.5F, -2.0F, 1.0F, 10.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(4, 59).addBox(4.0F, -15.5F, 4.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 32).addBox(-5.0F, -15.5F, -2.0F, 1.0F, 10.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(0, 59).addBox(-5.0F, -15.5F, 4.0F, 1.0F, 8.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(26, 48).addBox(-1.0F, -10.0F, -7.75F, 2.0F, 2.0F, 6.0F, new CubeDeformation(-0.2F))
		.texOffs(52, 0).addBox(-4.0F, -8.0F, -3.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(-0.9F))
		.texOffs(58, 58).addBox(-2.75F, -13.25F, -4.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(44, 50).addBox(-4.0F, 0.0F, -3.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(-0.9F))
		.texOffs(0, 0).addBox(-5.0F, -15.5F, 7.0F, 10.0F, 10.0F, 7.0F, new CubeDeformation(0.0F))
		.texOffs(34, 11).addBox(-1.0F, -5.0F, -4.75F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(16, 50).addBox(-3.0F, -5.0F, -6.75F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 53).addBox(-3.0F, -7.0F, -8.75F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(41, 41).addBox(-1.0F, -7.0F, -14.75F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.5F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}