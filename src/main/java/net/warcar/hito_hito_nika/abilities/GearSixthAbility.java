package net.warcar.hito_hito_nika.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;

public class GearSixthAbility extends Ability {
	public static final RegistryObject<AbilityCore<GearSixthAbility>> INSTANCE = TrueGomuGomuNoMi.registerSixthGear(new AbilityCore.Builder<>("gear_sixth", "Gear Sixth", AbilityCategory.DEVIL_FRUITS, GearSixthAbility::new));
	public static final Component NAME = TrueGomuHelper.getName("Gomu Gomu no Strange Pistol", "gear_sixth");
	private final ContinuousComponent continuousComponent;

	public GearSixthAbility(AbilityCore<GearSixthAbility> core) {
		super(core);
		this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
		this.setDisplayName(NAME);
		continuousComponent = new ContinuousComponent(this, true);
		this.addUseEvent(this::onStartContinuity);
		this.addComponents(continuousComponent);
		continuousComponent.addStartEvent(TrueGomuHelper.basicGearStuff());
	}

	private void onStartContinuity(LivingEntity player, IAbility ability) {
		this.continuousComponent.startContinuity(player, -1);
	}

}
