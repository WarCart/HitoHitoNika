package net.warcar.hito_hito_nika.abilities;

import xyz.pixelatedw.mineminenomi.api.abilities.IParallelContinuousAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbilityModeSwitcher;
import xyz.pixelatedw.mineminenomi.api.abilities.ContinuousAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;

import net.minecraft.entity.player.PlayerEntity;

public class GearSixthAbility extends ContinuousAbility implements IParallelContinuousAbility, IAbilityModeSwitcher {
	public static final AbilityCore<GearSixthAbility> INSTANCE;

	public GearSixthAbility(AbilityCore core) {
		super(core);
		this.setCustomIcon("Gomu Gomu no Pistol");
		this.onStartContinuityEvent = this::onStartContinuity;
		this.afterContinuityStopEvent = this::afterContinuity;
	}

	private boolean onStartContinuity(PlayerEntity player) {
		this.enableModes(player, this);
		return true;
	}

	private void afterContinuity(PlayerEntity player) {
		this.disableModes(player, this);
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gear Sixth", AbilityCategory.DEVIL_FRUITS, GearSixthAbility::new)).setHidden().build();
	}
}
