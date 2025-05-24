package net.warcar.hito_hito_nika.mixins;

import net.minecraft.entity.LivingEntity;
import net.warcar.hito_hito_nika.abilities.TrueGearFifthAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.abilities.gomu.GomuGomuNoDawnWhipAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityUseResult;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;

@Mixin(GomuGomuNoDawnWhipAbility.class)
public abstract class DawnWhipMixin {
    @Inject(method = "canUse", at = @At("HEAD"), remap = false, cancellable = true)
    private void newUse(LivingEntity entity, IAbility ability, CallbackInfoReturnable<AbilityUseResult> cir) {
        IAbilityData props = AbilityDataCapability.get(entity);
        TrueGearFifthAbility gearFifth = props.getEquippedAbility(TrueGearFifthAbility.INSTANCE);
        cir.setReturnValue(gearFifth != null && gearFifth.isContinuous() ? AbilityUseResult.success() : AbilityUseResult.fail(null));
    }
}
