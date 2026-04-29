package net.warcar.hito_hito_nika.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.abilities.gomu.GearFifthAbility;

@Mixin(GearFifthAbility.class)
public class OldGear5Mixin {
    @Inject(at = @At("HEAD"), method = "canUnlock", remap = false, cancellable = true)
    private static void noUnlock(LivingEntity e, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
