package net.warcar.hito_hito_nika.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.config.CommonConfig;
import net.warcar.hito_hito_nika.helpers.EquationHelper;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChangeStatsComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;

import java.util.HashMap;
import java.util.UUID;

public class GomuFusenAbility extends Ability {
	private static final Component[] DESCRIPTION = TrueGomuHelper.registerDescriptionText("gomu_gomu_no_fusen", ImmutablePair.of("By inhaling a lot of air user inflates their chest to gain invulnerability to cannon balls", null));
 	public static final RegistryObject<AbilityCore<GomuFusenAbility>> INSTANCE = TrueGomuGomuNoMi.registerAbility(new AbilityCore.Builder<>("gomu_gomu_no_fusen", "Gomu Gomu no Fusen", AbilityCategory.DEVIL_FRUITS, GomuFusenAbility::new)
			.addDescriptionLine(DESCRIPTION).addAdvancedDescriptionLine(ChangeStatsComponent.getTooltip()));
	public static final Component NAME = TrueGomuHelper.getName(ModMain.PROJECT_ID, "Gomu Gomu no Fusen", "gomu_gomu_no_fusen");
	private final ContinuousComponent continuousComponent;
	private final ChangeStatsComponent statsComponent;

	public GomuFusenAbility(AbilityCore<GomuFusenAbility> core) {
		super(core);
		this.setDisplayIcon(TrueGomuHelper.getIcon("Gomu Gomu no Fusen"));
		continuousComponent = new ContinuousComponent(this, true);
		this.continuousComponent.addEndEvent(this::afterContinuityStopEvent);
		this.continuousComponent.addStartEvent(this::onStartContinuityEvent);
		statsComponent = new ChangeStatsComponent(this);
		this.statsComponent.addAttributeModifier(Attributes.MOVEMENT_SPEED, new AbilityAttributeModifier(UUID.fromString("0b034de2-6e61-4c55-b259-050dae546a48"), INSTANCE, "Fusen Speed Modifier", -0.5d, AttributeModifier.Operation.MULTIPLY_TOTAL));
		this.statsComponent.addAttributeModifier(ModAttributes.JUMP_HEIGHT, new AbilityAttributeModifier(UUID.fromString("2a0bf1c6-0873-11ef-91ae-325096b39f47"), INSTANCE, "Fusen Jump Modifier", -0.5d, AttributeModifier.Operation.MULTIPLY_TOTAL));
		this.statsComponent.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, new AbilityAttributeModifier(UUID.fromString("2a0bf504-0873-11ef-8291-325096b39f47"), INSTANCE, "Fusen Knockback Resistance Modifier", 1, AttributeModifier.Operation.ADDITION));
		this.addComponents(continuousComponent, statsComponent);
		this.addUseEvent(this::start);
	}

	private void afterContinuityStopEvent(LivingEntity entity, IAbility ability) {
		this.cooldownComponent.startCooldown(entity, (float) EquationHelper.parseEquation(CommonConfig.INSTANCE.getFusenCooldown(), entity, TrueGomuHelper.getBasicBonusData(this.continuousComponent.getContinueTime())).getValue());
		IAbilityData props = AbilityCapability.get(entity).get();
		GomuMorphsAbility morphs = props.getPassiveAbility(GomuMorphsAbility.INSTANCE.get());
		if (morphs != null)
			morphs.updateModes(entity);
		TrueGearFourthAbility g4 = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE.get());
		if (g4 != null) {
			g4.setBoundman(entity);
			if (g4.isContinuous()) {
				g4.stopContinuity(entity);
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
		IAbilityData props = AbilityCapability.get(player).get();
		GomuMorphsAbility morphs = props.getPassiveAbility(GomuMorphsAbility.INSTANCE.get());
		this.statsComponent.addAttributeModifier(ModAttributes.TOUGHNESS, new AbilityAttributeModifier(UUID.fromString("2a0bf464-0873-11ef-b635-325096b39f47"), INSTANCE, "Fusen Resistance Damage Modifier", TrueGomuHelper.hasGearThirdActive(props) ? 10 : 3, AttributeModifier.Operation.ADDITION));
		if (morphs != null)
			morphs.updateModes(player);
		TrueGearFourthAbility g4 = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE.get());
		if (g4 != null) {
			g4.setTankman(player);
		}
	}

}
