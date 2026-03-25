package net.warcar.hito_hito_nika.mixin;

import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.init.ModRegistry;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

import java.util.function.Supplier;

@Mixin(ModRegistry.class)
public class ModRegistryMixin {
    //@Inject(method = "registerFruitItem", at = @At("HEAD"), remap = false, cancellable = true)
    private static  <T extends AkumaNoMiItem> void noGomu(String localizedName, Supplier<T> item, CallbackInfoReturnable<RegistryObject<T>> cir) {
        if (localizedName.equalsIgnoreCase("Gomu Gomu no Mi")) {
            cir.setReturnValue(null);
        }
    }
}
