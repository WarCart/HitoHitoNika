package net.warcar.hito_hito_nika.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.config.CommonConfig;
import net.warcar.hito_hito_nika.helpers.EquationHelper;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.MineMineNoMi;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChangeStatsComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.SkinOverlayComponent;
import xyz.pixelatedw.mineminenomi.data.entity.AbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.DevilFruitData;
import xyz.pixelatedw.mineminenomi.data.entity.EntityStatsData;
import xyz.pixelatedw.mineminenomi.init.*;
import xyz.pixelatedw.mineminenomi.init.i18n.ModI18nAbilities;

import java.awt.*;
import java.util.HashMap;

public class TrueGearSecondAbility extends Ability {
	private static final Component[] DESCRIPTION = TrueGomuHelper.registerDescriptionText(HitoHitoNoMiNikaMod.MOD_ID, "gear_second",
			ImmutablePair.of("By speeding up their blood flow, the user gains strength, speed and mobility.", null));
	public static final DeferredHolder<AbilityCore<?>, AbilityCore<TrueGearSecondAbility>> INSTANCE = TrueGomuGomuNoMi.registerAbility(new AbilityCore.Builder<>("gear_second", "Gear Second", AbilityCategory.DEVIL_FRUITS, TrueGearSecondAbility::new)
			.addDescriptionLine(DESCRIPTION).addAdvancedDescriptionLine(AbilityDescriptionLine.NEW_LINE, ChangeStatsComponent.getTooltip())
			.setUnlockCheck(TrueGearSecondAbility::canUnlock));
	private static final AttributeModifier JUMP_HEIGHT = new AttributeModifier(INSTANCE.getId(), 5, Operation.ADD_VALUE);
	private static final AttributeModifier STRENGTH_MODIFIER = new AttributeModifier(INSTANCE.getId(), 4, Operation.ADD_VALUE);
	private static final AttributeModifier ATTACK_SPEED_MODIFIER = new AttributeModifier(INSTANCE.getId(), 4, Operation.ADD_VALUE);
	private static final AttributeModifier STEP_HEIGHT = new AttributeModifier(INSTANCE.getId(), 1, Operation.ADD_VALUE);
	private final ContinuousComponent continuousComponent;
	private final ChangeStatsComponent statsComponent;
	private final SkinOverlayComponent overlayComponent = new SkinOverlayComponent(this, new AbilityOverlay.Builder().setColor(new Color(232, 54, 54, 74)).build());
	private boolean thirdGearWas = false;
	private boolean prevSprintValue = false;

	public TrueGearSecondAbility(AbilityCore<TrueGearSecondAbility> core) {
		super(core);
		this.setDisplayIcon(TrueGomuHelper.getIcon(MineMineNoMi.PROJECT_ID, "gear_second"));
		this.continuousComponent = new ContinuousComponent(this, true);
		this.statsComponent = new ChangeStatsComponent(this);
		this.statsComponent.addAttributeModifier(ModAttributes.JUMP_HEIGHT, JUMP_HEIGHT);
		this.statsComponent.addAttributeModifier(Attributes.STEP_HEIGHT, STEP_HEIGHT);
		this.statsComponent.addAttributeModifier(Attributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER);
		this.statsComponent.addAttributeModifier(ModAttributes.PUNCH_DAMAGE, STRENGTH_MODIFIER);
		this.addUseEvent(this::onStartContinuity);
		continuousComponent.addStartEvent(TrueGomuHelper.basicGearStuff());
		this.continuousComponent.addTickEvent(this::duringContinuity);
		this.continuousComponent.addTickEvent(TrueGomuHelper.getSpeedEvent(1.75f));
		this.continuousComponent.addEndEvent(this::afterContinuityStopEvent);
		this.addComponents(this.continuousComponent, this.statsComponent, this.overlayComponent);
	}

	private void onStartContinuity(LivingEntity player, IAbility abl) {
		if (this.continuousComponent.isContinuous()) {
			this.continuousComponent.stopContinuity(player);
			return;
		}
		float time = (float) EquationHelper.parseEquation(CommonConfig.INSTANCE.getG2Length(), player, new HashMap<>()).getValue();
		AbilityData props = AbilityData.get(player).get();
		if (!TrueGomuHelper.canActivateGear(props, this)) {
			player.sendSystemMessage(ModI18nAbilities.MESSAGE_GEAR_ACTIVE);
		} else {
			if (!this.prevSprintValue && player.isSprinting()) {
				player.level().playSound(null, player.blockPosition(), ModSounds.TELEPORT_SFX.get(), SoundSource.PLAYERS, 2.0F, 1.0F);
			}
			if (TrueGomuHelper.hasGearThirdActive(props)) {
				props.getEquippedAbility(TrueGearThirdAbility.INSTANCE.get()).setSecondGear(true);
				this.thirdGearWas = true;
			}
			this.overlayComponent.showAll(player);
			if (time >= 500) {
				this.continuousComponent.startContinuity(player, -1);
			} else {
				this.continuousComponent.startContinuity(player, time * 20);
			}
			this.statsComponent.applyModifiers(player);
			player.level().playSound(null, player.blockPosition(), ModSounds.GEAR_SECOND_SFX.get(), SoundSource.PLAYERS, 0.5F, 1.0F);
			this.prevSprintValue = player.isSprinting();
		}
	}

	private void duringContinuity(LivingEntity entity, IAbility abl) {
		if (this.continuousComponent.getContinueTime() % 10 == 0) {
			WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), entity, entity.getX(), entity.getY(), entity.getZ());
		}
	}

	private void afterContinuityStopEvent(LivingEntity player, IAbility abl) {
		this.cooldownComponent.startCooldown(player, (float) EquationHelper.parseEquation(CommonConfig.INSTANCE.getG2Cooldown(), player, TrueGomuHelper.getBasicBonusData(this.continuousComponent.getContinueTime())).getValue());
		this.overlayComponent.hideAll(player);
		if (this.thirdGearWas && EntityStatsData.get(player).get().getDoriki() < 3500.0D) {
			player.addEffect(new MobEffectInstance(ModEffects.UNCONSCIOUS, 300, 1, true, true));
		} else if ((double) this.continuousComponent.getContinueTime() > (double) this.continuousComponent.getThresholdTime() / 1.425D && EntityStatsData.get(player).get().getDoriki() < 2000.0D) {
			player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 3, true, true));
			player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1, true, true));
		}
		TrueGearThirdAbility thirdGear = AbilityData.getEquippedAbility(player, TrueGearThirdAbility.INSTANCE.get());
		if (thirdGear != null && thirdGear.isContinuous() && thirdGearWas) {
			this.setThirdGear(false);
			thirdGear.getComponent(ModAbilityComponents.CONTINUOUS.get()).ifPresent(c -> c.stopContinuity(player));
		}
		this.setThirdGear(false);
		this.statsComponent.removeModifiers(player);
	}

	public void setThirdGear(boolean newWas) {
		this.thirdGearWas = newWas;
	}

	protected static boolean canUnlock(LivingEntity user) {
		return EntityStatsData.get(user).get().getDoriki() >= 1500d && DevilFruitData.get(user).get().hasDevilFruit(TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA);
	}
}
