package net.warcar.hito_hito_nika.mixins;

import net.minecraft.entity.LivingEntity;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.abilities.GomuMorphsAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.MorphComponent;
import xyz.pixelatedw.mineminenomi.api.helpers.MorphHelper;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.init.ModAbilityKeys;

@Mixin(MorphHelper.class)
public abstract class MorphHelperMixin {
    @Inject(method = "getZoanInfo", at = @At("HEAD"), cancellable = true, remap = false)
    private static void model(LivingEntity entity, CallbackInfoReturnable<MorphInfo> cir) {
        IAbilityData props = AbilityDataCapability.get(entity);
        GomuMorphsAbility ability = props.getPassiveAbility(GomuMorphsAbility.INSTANCE);
        if (ability != null && ability.getTransformation(entity) != null) {
            cir.setReturnValue(ability.getTransformation(entity));
        }
    }
}
