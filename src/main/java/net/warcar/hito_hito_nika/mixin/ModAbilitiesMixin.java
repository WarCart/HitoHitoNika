package net.warcar.hito_hito_nika.mixin;

import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.pixelatedw.mineminenomi.init.ModFruits;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

@Mixin(value = ModFruits.class, remap = false)
public abstract class ModAbilitiesMixin {
    /*@Inject(method = "registerFruit", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static <T extends AkumaNoMiItem> void notReg(T fruit, CallbackInfoReturnable<T> info) {
        if (fruit.getDevilFruitName().equals("Gomu Gomu no Mi")) {
            info.setReturnValue(fruit);
        }
    }*/

    @Shadow
    @Final
    public static final RegistryObject<AkumaNoMiItem> GOMU_GOMU_NO_MI = TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA;
}
