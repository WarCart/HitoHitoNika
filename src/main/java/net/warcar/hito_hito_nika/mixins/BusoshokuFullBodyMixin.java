package net.warcar.hito_hito_nika.mixins;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiFullBodyHardeningAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiHardeningAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbilityModeSwitcher;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;

@Mixin(BusoshokuHakiFullBodyHardeningAbility.class)
public abstract class BusoshokuFullBodyMixin extends ContinuousAbilityMixin implements IAbilityModeSwitcher {
	@Inject(method = "onStartContinuityEvent", at = {@At(value = "RETURN")}, remap = false)
	private void start(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
		if (info.getReturnValue()) {
			BusoshokuHakiHardeningAbility hard = AbilityDataCapability.get(player).getUnlockedAbility(BusoshokuHakiHardeningAbility.INSTANCE);
			if (hard != null) {
				this.enableModes(player, hard);
			}
			this.enableModes(player, (BusoshokuHakiFullBodyHardeningAbility) (Object) this);
		}
	}

	@Override
	protected void continueStop(PlayerEntity player, CallbackInfo info) {
		BusoshokuHakiHardeningAbility hard = AbilityDataCapability.get(player).getUnlockedAbility(BusoshokuHakiHardeningAbility.INSTANCE);
		this.disableModes(player, hard);
		this.disableModes(player, (BusoshokuHakiFullBodyHardeningAbility) (Object) this);
	}
}
