package net.warcar.hito_hito_nika.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.player.Player;

public class SmallMorphModel <T extends Player> extends PlayerModel<T> {
    public SmallMorphModel(ModelPart part, boolean isSlim) {
        super(part, isSlim);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        matrixStack.pushPose();
        matrixStack.translate(0, 0.6, 0);
        head.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        hat.render(matrixStack, buffer, packedLight, packedOverlay);
        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.scale(0.7f, 0.5f, 0.7f);
        matrixStack.translate(0, 1.5, 0);
        body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        jacket.render(matrixStack, buffer, packedLight, packedOverlay);

        leftArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leftSleeve.render(matrixStack, buffer, packedLight, packedOverlay);

        rightArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rightSleeve.render(matrixStack, buffer, packedLight, packedOverlay);

        leftLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        leftPants.render(matrixStack, buffer, packedLight, packedOverlay);

        rightLeg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        rightPants.render(matrixStack, buffer, packedLight, packedOverlay);
        matrixStack.popPose();
    }
}
