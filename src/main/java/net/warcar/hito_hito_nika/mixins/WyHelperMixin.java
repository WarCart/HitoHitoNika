package net.warcar.hito_hito_nika.mixins;

import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xyz.pixelatedw.mineminenomi.api.WyHelper;

import java.util.Map;

@Mixin(value = WyHelper.class, remap = false)
public abstract class WyHelperMixin {
    @Shadow public static  <K extends Comparable, V extends Comparable> Map<K, V> sortAlphabetically(Map<K, V> map){
        throw new IllegalStateException("Mixin not mixin-ing");
    }

    @ModifyVariable(method = "generateJSONLangs", at = @At("STORE"), remap = false, name = "sorted")
    private static Map<String, String> modifySorted(Map<String, String> value) {
        return sortAlphabetically(HitoHitoNoMiNikaMod.getLangMap());
    }
}
