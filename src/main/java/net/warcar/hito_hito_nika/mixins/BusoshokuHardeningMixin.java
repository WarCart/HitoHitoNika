package net.warcar.hito_hito_nika.mixins;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiHardeningAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbilityModeSwitcher;

@Mixin(BusoshokuHakiHardeningAbility.class)
public abstract class BusoshokuHardeningMixin extends ContinuousAbilityMixin implements IAbilityModeSwitcher {
	@Inject(method = "onStartContinuityEvent", at = {@At(value = "RETURN")}, remap = false)
	private void start(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
		BusoshokuHakiHardeningAbility buso = (BusoshokuHakiHardeningAbility) ((Object) this);
		if (info.getReturnValue())
			this.enableModes(player, buso);
	}

	@Override
	protected void continueStop(PlayerEntity player, CallbackInfo info) {
		this.disableModes(player, (BusoshokuHakiHardeningAbility) ((Object) this));
	}
}
