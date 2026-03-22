package net.warcar.hito_hito_nika.mixins;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.warcar.hito_hito_nika.renderers.IModelRendererMixin;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.pixelatedw.mineminenomi.api.helpers.RendererHelper;

@Mixin(RendererHelper.class)
public class RendererHelperMixin {
    @Inject(method = "resetModelToDefaultPivots", at = @At("TAIL"), remap = false)
    private static <M extends EntityModel<?>> void resetHumanoidModelToDefaultPivots(M model, CallbackInfo ci) {
        if (model instanceof HumanoidModel<?>) {
            HumanoidModel<?> biped = (HumanoidModel<?>)model;
            ((IModelRendererMixin) biped.leftArm).setScale(new Vector3f(1, 1, 1));
            ((IModelRendererMixin) biped.rightArm).setScale(new Vector3f(1, 1, 1));
            ((IModelRendererMixin) biped.leftLeg).setScale(new Vector3f(1, 1, 1));
            ((IModelRendererMixin) biped.rightLeg).setScale(new Vector3f(1, 1, 1));
            ((IModelRendererMixin) biped.head).setScale(new Vector3f(1, 1, 1));
            ((IModelRendererMixin) biped.body).setScale(new Vector3f(1, 1, 1));
        }
    }
}
