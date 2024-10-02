package net.warcar.hito_hito_nika.mixins;

import xyz.pixelatedw.mineminenomi.api.abilities.IAbilityModeSwitcher;
import xyz.pixelatedw.mineminenomi.abilities.haki.HaoshokuHakiInfusionAbility;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.player.PlayerEntity;
import xyz.pixelatedw.mineminenomi.api.abilities.IHakiAbility;
import xyz.pixelatedw.mineminenomi.api.enums.HakiType;

@Mixin(HaoshokuHakiInfusionAbility.class)
public abstract class HaoshokuInfusionMixin extends ContinuousAbilityMixin implements IAbilityModeSwitcher, IHakiAbility {
	@Inject(method = "onStartContinuityEvent", at = {@At(value = "RETURN")}, cancellable = true, remap = false)
	private void start(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
		HaoshokuHakiInfusionAbility hao = (HaoshokuHakiInfusionAbility) ((Object) this);
		if (info.getReturnValue())
			this.enableModes(player, hao);
	}

	@Override
	protected void continueStop(PlayerEntity player, CallbackInfo info) {
		this.disableModes(player, (HaoshokuHakiInfusionAbility) ((Object) this));
	}

	@Override
	public HakiType getType() {
		return HakiType.HAOSHOKU;
	}
}
