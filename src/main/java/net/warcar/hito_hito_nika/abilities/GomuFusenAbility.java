package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.hito_hito_nika.config.CommonConfig;
import net.warcar.hito_hito_nika.helpers.EquationHelper;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AnimeScreamComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChangeStatsComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.init.ModAbilityKeys;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;

import java.util.HashMap;
import java.util.UUID;

public class GomuFusenAbility extends Ability {
	public static final AbilityCore<GomuFusenAbility> INSTANCE;
	public static final TranslationTextComponent NAME = TrueGomuHelper.getName(ModMain.PROJECT_ID, "Gomu Gomu no Fusen", "gomu_gomu_no_fusen");
	private final ContinuousComponent continuousComponent;
	private final ChangeStatsComponent statsComponent;
	private final AnimeScreamComponent trueScreamComponent = new AnimeScreamComponent(this) {
		@Override
		public void setupDefaultScreams(IAbility ability) {
			ability.getComponent(ModAbilityKeys.CONTINUOUS).ifPresent(chargeComponent -> chargeComponent.addStartEvent((entity, iAbility) -> this.scream(entity, ability.getDisplayName().getString())));
		}
	};

	public GomuFusenAbility(AbilityCore<GomuFusenAbility> core) {
		super(core);
		this.isNew = true;
		this.setDisplayIcon(TrueGomuHelper.getIcon("Gomu Gomu no Fusen"));
		continuousComponent = new ContinuousComponent(this, true);
		this.continuousComponent.addEndEvent(this::afterContinuityStopEvent);
		this.continuousComponent.addStartEvent(this::onStartContinuityEvent);
		statsComponent = new ChangeStatsComponent(this);
		this.statsComponent.addAttributeModifier(Attributes.MOVEMENT_SPEED, new AbilityAttributeModifier(UUID.fromString("0b034de2-6e61-4c55-b259-050dae546a48"), INSTANCE, "Fusen Speed Modifier", -0.5d, Operation.MULTIPLY_TOTAL));
		this.statsComponent.addAttributeModifier(ModAttributes.JUMP_HEIGHT, new AbilityAttributeModifier(UUID.fromString("2a0bf1c6-0873-11ef-91ae-325096b39f47"), INSTANCE, "Fusen Jump Modifier", -0.5d, Operation.MULTIPLY_TOTAL));
		this.statsComponent.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, new AbilityAttributeModifier(UUID.fromString("2a0bf504-0873-11ef-8291-325096b39f47"), INSTANCE, "Fusen Knockback Resistance Modifier", 1, Operation.ADDITION));
		this.addComponents(continuousComponent, statsComponent, trueScreamComponent);
		this.addUseEvent(this::start);
	}

	private void afterContinuityStopEvent(LivingEntity player, IAbility ability) {
		this.cooldownComponent.startCooldown(player, (float) EquationHelper.parseEquation(CommonConfig.INSTANCE.getFusenCooldown(), player, TrueGomuHelper.getBasicBonusData(this.continuousComponent.getContinueTime())).getValue());
		IAbilityData props = AbilityDataCapability.get(player);
		GomuMorphsAbility morphs = props.getPassiveAbility(GomuMorphsAbility.INSTANCE);
		if (morphs != null)
			morphs.updateModes();
		TrueGearFourthAbility g4 = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE);
		if (g4 != null) {
			g4.setBoundman(player);
			if (g4.isContinuous()) {
				g4.stopContinuity(player);
			}
		}
	}

	public void stopContinuity(LivingEntity player) {
		this.continuousComponent.stopContinuity(player);
	}

	private void start(LivingEntity entity, IAbility ability) {
		float time = (float) EquationHelper.parseEquation(CommonConfig.INSTANCE.getFusenLength(), entity, new HashMap<>()).getValue();
		if (time > 500)
			time = -1;
		this.continuousComponent.triggerContinuity(entity, time * 20);
	}

	private void onStartContinuityEvent(LivingEntity player, IAbility ability) {
		IAbilityData props = AbilityDataCapability.get(player);
		GomuMorphsAbility morphs = props.getPassiveAbility(GomuMorphsAbility.INSTANCE);
		this.statsComponent.addAttributeModifier(ModAttributes.DAMAGE_REDUCTION, new AbilityAttributeModifier(UUID.fromString("2a0bf464-0873-11ef-b635-325096b39f47"), INSTANCE, "Fusen Resistance Damage Modifier", TrueGomuHelper.hasGearThirdActive(props) ? 0.5 : 0.25, Operation.ADDITION));
		if (morphs != null)
			morphs.updateModes();
		TrueGearFourthAbility g4 = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE);
		if (g4 != null) {
			g4.setTankman(player);
		}
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gomu Gomu no Fusen", AbilityCategory.DEVIL_FRUITS, GomuFusenAbility::new)).addDescriptionLine("").build();
	}
}
