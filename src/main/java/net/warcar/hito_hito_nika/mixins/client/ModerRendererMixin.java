package net.warcar.hito_hito_nika.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.warcar.hito_hito_nika.renderers.IModelRendererMixin;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelPart.class)
public class ModerRendererMixin implements IModelRendererMixin {
    @Unique
    private float xScale = 1.0f;
    @Unique
    private float yScale = 1.0f;
    @Unique
    private float zScale = 1.0f;

    @Override
    public Vector3f getScale() {
        return new Vector3f(xScale, yScale, zScale);
    }

    @Override
    public void setXScale(float xScale) {
        this.xScale = xScale;
    }

    @Override
    public void setYScale(float yScale) {
        this.yScale = yScale;
    }

    @Override
    public void setZScale(float zScale) {
        this.zScale = zScale;
    }

    @Override
    public void setScale(Vector3f scale) {
        this.xScale = scale.x();
        this.yScale = scale.y();
        this.zScale = scale.z();
    }

    @Inject(method = "translateAndRotate", at = @At("TAIL"))
    private void preRender(PoseStack matrixStack, CallbackInfo ci) {
        matrixStack.scale(xScale, yScale, zScale);
    }

    @Inject(method = "copyFrom", at = @At("TAIL"))
    private void copySize(ModelPart other, CallbackInfo ci) {
        this.setScale(((IModelRendererMixin) other).getScale());
    }
}
