package net.warcar.hito_hito_nika.mixins;

import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbilityModeSwitcher;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiInternalDestructionAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiHardeningAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiEmissionAbility;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.player.PlayerEntity;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;

@Mixin(BusoshokuHakiInternalDestructionAbility.class)
public abstract class BusoshokuDestructionMixin extends ContinuousAbilityMixin implements IAbilityModeSwitcher {
	@Inject(method = "onStartContinuityEvent", at = @At(value = "RETURN"), remap = false)
	private void start(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
		if (info.getReturnValue()) {
			BusoshokuHakiHardeningAbility hard = AbilityDataCapability.get(player).getUnlockedAbility(BusoshokuHakiHardeningAbility.INSTANCE);
			if (hard != null) {
				this.enableModes(player, hard);
			}
			BusoshokuHakiEmissionAbility emission = AbilityDataCapability.get(player).getUnlockedAbility(BusoshokuHakiEmissionAbility.INSTANCE);
			if (emission != null) {
				this.enableModes(player, emission);
			}
			this.enableModes(player, (BusoshokuHakiInternalDestructionAbility) (Object) this);
		}
	}

	@Override
	protected void continueStop(PlayerEntity player, CallbackInfo info) {
		BusoshokuHakiHardeningAbility hard = AbilityDataCapability.get(player).getUnlockedAbility(BusoshokuHakiHardeningAbility.INSTANCE);
		BusoshokuHakiEmissionAbility emission = AbilityDataCapability.get(player).getUnlockedAbility(BusoshokuHakiEmissionAbility.INSTANCE);
		this.disableModes(player, hard);
		this.disableModes(player, emission);
		this.disableModes(player, (BusoshokuHakiInternalDestructionAbility) (Object) this);
	}
}
