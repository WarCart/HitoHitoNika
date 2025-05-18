package net.warcar.hito_hito_nika.mixins;

import net.warcar.hito_hito_nika.abilities.TrueGomuHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.abilities.gomu.GomuHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;

@Mixin(GomuHelper.class)
public class GomuHelperMixin {
    @Inject(method = "hasGearFifthActive", at = @At("HEAD"), remap = false, cancellable = true)
    private static void hasGearFifthActive(IAbilityData p, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(TrueGomuHelper.hasGearFifthActive(p));
    }
}
