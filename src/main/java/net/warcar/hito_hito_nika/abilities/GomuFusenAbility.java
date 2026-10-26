package net.warcar.hito_hito_nika.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.warcar.hito_hito_nika.config.CommonConfig;
import net.warcar.hito_hito_nika.helpers.EquationHelper;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.MineMineNoMi;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChangeStatsComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.data.entity.AbilityData;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;

import java.util.HashMap;

public class GomuFusenAbility extends Ability {
	private static final Component[] DESCRIPTION = TrueGomuHelper.registerDescriptionText("gomu_gomu_no_fusen", ImmutablePair.of("By inhaling a lot of air user inflates their chest to gain invulnerability to cannon balls", null));
 	public static final DeferredHolder<AbilityCore<?>, AbilityCore<GomuFusenAbility>> INSTANCE = TrueGomuGomuNoMi.registerAbility(new AbilityCore.Builder<>("gomu_gomu_no_fusen", "Gomu Gomu no Fusen", AbilityCategory.DEVIL_FRUITS, GomuFusenAbility::new)
			.addDescriptionLine(DESCRIPTION).addAdvancedDescriptionLine(ChangeStatsComponent.getTooltip()));
	public static final Component NAME = TrueGomuHelper.getName(MineMineNoMi.PROJECT_ID, "Gomu Gomu no Fusen", "gomu_gomu_no_fusen");
	private final ContinuousComponent continuousComponent;
	private final ChangeStatsComponent statsComponent;

	public GomuFusenAbility(AbilityCore<GomuFusenAbility> core) {
		super(core);
		this.setDisplayIcon(TrueGomuHelper.getIcon("Gomu Gomu no Fusen"));
		continuousComponent = new ContinuousComponent(this, true);
		this.continuousComponent.addEndEvent(this::afterContinuityStopEvent);
		this.continuousComponent.addStartEvent(this::onStartContinuityEvent);
		statsComponent = new ChangeStatsComponent(this);
		this.statsComponent.addAttributeModifier(Attributes.MOVEMENT_SPEED, new AttributeModifier(INSTANCE.getId(), -0.5d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		this.statsComponent.addAttributeModifier(ModAttributes.JUMP_HEIGHT, new AttributeModifier(INSTANCE.getId(), -0.5d, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
		this.statsComponent.addAttributeModifier(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(INSTANCE.getId(), 1, AttributeModifier.Operation.ADD_VALUE));
		this.addComponents(continuousComponent, statsComponent);
		this.addUseEvent(this::start);
	}

	private void afterContinuityStopEvent(LivingEntity entity, IAbility ability) {
		this.cooldownComponent.startCooldown(entity, (float) EquationHelper.parseEquation(CommonConfig.INSTANCE.getFusenCooldown(), entity, TrueGomuHelper.getBasicBonusData(this.continuousComponent.getContinueTime())).getValue());
		AbilityData props = AbilityData.get(entity).get();
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
		AbilityData props = AbilityData.get(player).get();
		GomuMorphsAbility morphs = props.getPassiveAbility(GomuMorphsAbility.INSTANCE.get());
		this.statsComponent.addAttributeModifier(ModAttributes.TOUGHNESS, new AttributeModifier(INSTANCE.getId(), TrueGomuHelper.hasGearThirdActive(props) ? 10 : 3, AttributeModifier.Operation.ADD_VALUE));
		if (morphs != null)
			morphs.updateModes(player);
		TrueGearFourthAbility g4 = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE.get());
		if (g4 != null) {
			g4.setTankman(player);
		}
	}

}
