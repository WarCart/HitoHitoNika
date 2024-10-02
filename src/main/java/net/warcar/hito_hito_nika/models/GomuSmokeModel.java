package net.warcar.hito_hito_nika.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class GomuSmokeModel extends EntityModel<Entity> {
    private final ModelRenderer smoke;
    private final ModelRenderer rightSmoke;
    private final ModelRenderer rightSmoke2;
    private final ModelRenderer rightSmoke3;
    private final ModelRenderer leftSmoke;
    private final ModelRenderer leftSmoke2;
    private final ModelRenderer leftSmoke3;
    private final ModelRenderer backSmoke;
    private final ModelRenderer backSmoke3;
    private final ModelRenderer backSmoke2;
    private final ModelRenderer backSmoke5;
    private final ModelRenderer backSmoke4;

    public GomuSmokeModel() {
        this.texWidth = 32;
        this.texHeight = 32;
        this.smoke = new ModelRenderer(this);
        this.smoke.setPos(0.0F, 0.0F, 0.5F);
        this.rightSmoke = new ModelRenderer(this);
        this.rightSmoke.setPos(-6.0F, -1.1156F, -2.112F);
        this.smoke.addChild(this.rightSmoke);
        this.setRotationAngle(this.rightSmoke, -0.5672F, 0.0F, 0.0F);
        this.rightSmoke.texOffs(0, 15).addBox(-2.0F, -1.3844F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        this.rightSmoke2 = new ModelRenderer(this);
        this.rightSmoke2.setPos(0.0F, 4.8844F, 0.888F);
        this.rightSmoke.addChild(this.rightSmoke2);
        this.setRotationAngle(this.rightSmoke2, 0.3491F, 0.0F, 0.0F);
        this.rightSmoke2.texOffs(0, 9).addBox(-2.0F, -2.5F, 0.0F, 4.0F, 5.0F, 0.0F, 0.0F, false);
        this.rightSmoke3 = new ModelRenderer(this);
        this.rightSmoke3.setPos(0.0F, 5.0F, 0.75F);
        this.rightSmoke2.addChild(this.rightSmoke3);
        this.setRotationAngle(this.rightSmoke3, 0.2618F, 0.0F, 0.0F);
        this.rightSmoke3.texOffs(0, 0).addBox(-2.0F, -2.6188F, -0.0759F, 4.0F, 8.0F, 0.0F, 0.0F, false);
        this.leftSmoke = new ModelRenderer(this);
        this.leftSmoke.setPos(6.0F, -1.1156F, -2.112F);
        this.smoke.addChild(this.leftSmoke);
        this.setRotationAngle(this.leftSmoke, -0.5672F, 0.0F, 0.0F);
        this.leftSmoke.texOffs(0, 15).addBox(-2.0F, -1.3844F, 0.0F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        this.leftSmoke2 = new ModelRenderer(this);
        this.leftSmoke2.setPos(0.0F, 4.8844F, 0.888F);
        this.leftSmoke.addChild(this.leftSmoke2);
        this.setRotationAngle(this.leftSmoke2, 0.3491F, 0.0F, 0.0F);
        this.leftSmoke2.texOffs(0, 9).addBox(-2.0F, -2.5F, 0.0F, 4.0F, 5.0F, 0.0F, 0.0F, true);
        this.leftSmoke3 = new ModelRenderer(this);
        this.leftSmoke3.setPos(0.0F, 5.0F, 0.75F);
        this.leftSmoke2.addChild(this.leftSmoke3);
        this.setRotationAngle(this.leftSmoke3, 0.2618F, 0.0F, 0.0F);
        this.leftSmoke3.texOffs(0, 0).addBox(-2.0F, -2.6188F, -0.0759F, 4.0F, 8.0F, 0.0F, 0.0F, true);
        this.backSmoke = new ModelRenderer(this);
        this.backSmoke.setPos(-6.5F, -3.065F, 1.0686F);
        this.smoke.addChild(this.backSmoke);
        this.setRotationAngle(this.backSmoke, -1.309F, 0.0F, 0.0F);
        this.backSmoke.texOffs(0, 20).addBox(-1.5F, -2.435F, 0.1314F, 4.0F, 5.0F, 0.0F, 0.0F, false);
        this.backSmoke3 = new ModelRenderer(this);
        this.backSmoke3.setPos(0.9475F, -6.5053F, -1.1009F);
        this.backSmoke.addChild(this.backSmoke3);
        this.setRotationAngle(this.backSmoke3, 0.5236F, 0.0F, 0.0F);
        this.backSmoke3.texOffs(0, 26).addBox(-2.5F, -3.5F, -0.25F, 16.0F, 5.0F, 0.0F, 0.0F, false);
        this.backSmoke2 = new ModelRenderer(this);
        this.backSmoke2.setPos(-0.0525F, -3.5053F, -0.1009F);
        this.backSmoke.addChild(this.backSmoke2);
        this.setRotationAngle(this.backSmoke2, 0.2618F, 0.0F, 0.0F);
        this.backSmoke2.texOffs(11, 0).addBox(-1.5F, -2.75F, -0.0977F, 4.0F, 4.0F, 0.0F, 0.0F, false);
        this.backSmoke5 = new ModelRenderer(this);
        this.backSmoke5.setPos(11.9475F, -3.5053F, -0.1009F);
        this.backSmoke.addChild(this.backSmoke5);
        this.setRotationAngle(this.backSmoke5, 0.2618F, 0.0F, 0.0F);
        this.backSmoke5.texOffs(11, 0).addBox(-1.5F, -2.75F, -0.0977F, 4.0F, 4.0F, 0.0F, 0.0F, true);
        this.backSmoke4 = new ModelRenderer(this);
        this.backSmoke4.setPos(12.0F, 0.0F, 0.0F);
        this.backSmoke.addChild(this.backSmoke4);
        this.backSmoke4.texOffs(0, 20).addBox(-1.5F, -2.435F, 0.1314F, 4.0F, 5.0F, 0.0F, 0.0F, true);
    }

    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.smoke.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
