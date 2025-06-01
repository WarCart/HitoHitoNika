package net.warcar.hito_hito_nika.mixins;

import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.init.ModAbilities;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

@Mixin(ModAbilities.class)
public abstract class ModAbilitiesMixin {
    @Inject(method = "registerFruit", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static <T extends AkumaNoMiItem> void notReg(T fruit, CallbackInfoReturnable<T> info) {
        if (fruit.getDevilFruitName().equals("Gomu Gomu no Mi")) {
            info.setReturnValue(fruit);
        }
    }

    @Shadow
    @Final
    public static final AkumaNoMiItem GOMU_GOMU_NO_MI = TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA;
}
