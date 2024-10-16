package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.abilities.gomu.GomuHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AnimeScreamComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.init.ModAbilityKeys;

public class GearSixthAbility extends Ability {
	public static final AbilityCore<GearSixthAbility> INSTANCE;
	private final ContinuousComponent continuousComponent;

	public GearSixthAbility(AbilityCore<GearSixthAbility> core) {
		super(core);
		this.isNew = true;
		this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
		continuousComponent = new ContinuousComponent(this, true);
		this.addUseEvent(this::onStartContinuity);
		this.addComponents(continuousComponent);
	}

	private void onStartContinuity(LivingEntity player, IAbility ability) {
		this.continuousComponent.startContinuity(player, -1);
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gear Sixth", AbilityCategory.DEVIL_FRUITS, GearSixthAbility::new)).build();
	}
}
