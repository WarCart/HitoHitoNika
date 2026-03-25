package net.warcar.hito_hito_nika.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.config.CommonConfig;
import net.warcar.hito_hito_nika.helpers.EquationHelper;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChangeStatsComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.SkinOverlayComponent;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.init.*;
import xyz.pixelatedw.mineminenomi.init.i18n.ModI18nAbilities;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

public class TrueGearSecondAbility extends Ability {
	private static final Component[] DESCRIPTION = TrueGomuHelper.registerDescriptionText(HitoHitoNoMiNikaMod.MOD_ID, "gear_second",
			ImmutablePair.of("By speeding up their blood flow, the user gains strength, speed and mobility.", null));
	public static final RegistryObject<AbilityCore<TrueGearSecondAbility>> INSTANCE = TrueGomuGomuNoMi.registerAbility(new AbilityCore.Builder<>("gear_second", "Gear Second", AbilityCategory.DEVIL_FRUITS, TrueGearSecondAbility::new)
			.addDescriptionLine(DESCRIPTION).addAdvancedDescriptionLine(AbilityDescriptionLine.NEW_LINE, ChangeStatsComponent.getTooltip())
			.setUnlockCheck(TrueGearSecondAbility::canUnlock));
	private static final AbilityAttributeModifier JUMP_HEIGHT = new AbilityAttributeModifier(UUID.fromString("a44a9644-369a-4e18-88d9-323727d3d85b"), INSTANCE, "Gear Second Jump Modifier", 5, Operation.ADDITION);
	private static final AbilityAttributeModifier STRENGTH_MODIFIER = new AbilityAttributeModifier(UUID.fromString("a2337b58-7e6d-4361-a8ca-943feee4f906"), INSTANCE, "Gear Second Attack Damage Modifier", 4, Operation.ADDITION);
	private static final AbilityAttributeModifier ATTACK_SPEED_MODIFIER = new AbilityAttributeModifier(UUID.fromString("c495cf01-f3ff-4933-9805-5bb1ed9d27b0"), INSTANCE, "Gear Second Attack Speed Modifier", 4, Operation.ADDITION);
	private static final AbilityAttributeModifier STEP_HEIGHT = new AbilityAttributeModifier(UUID.fromString("eab680cd-a6dc-438a-99d8-46f9eb53a950"), INSTANCE, "Gear Second Step Height Modifier", 1, Operation.ADDITION);
	private final ContinuousComponent continuousComponent;
	private final ChangeStatsComponent statsComponent;
	private final SkinOverlayComponent overlayComponent = new SkinOverlayComponent(this, new AbilityOverlay.Builder().setColor(new Color(232, 54, 54, 74)).build());
	private boolean thirdGearWas = false;
	private boolean prevSprintValue = false;

	public TrueGearSecondAbility(AbilityCore<TrueGearSecondAbility> core) {
		super(core);
		this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "gear_second"));
		this.continuousComponent = new ContinuousComponent(this, true);
		this.statsComponent = new ChangeStatsComponent(this);
		this.statsComponent.addAttributeModifier(ModAttributes.JUMP_HEIGHT.get(), JUMP_HEIGHT);
		this.statsComponent.addAttributeModifier(ForgeMod.STEP_HEIGHT_ADDITION.get(), STEP_HEIGHT);
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
		IAbilityData props = AbilityCapability.get(player).get();
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
		if (this.thirdGearWas && EntityStatsCapability.get(player).get().getDoriki() < 3500.0D) {
			player.addEffect(new MobEffectInstance(ModEffects.UNCONSCIOUS.get(), 300, 1, true, true));
		} else if ((double) this.continuousComponent.getContinueTime() > (double) this.continuousComponent.getThresholdTime() / 1.425D && EntityStatsCapability.get(player).get().getDoriki() < 2000.0D) {
			player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 600, 3, true, true));
			player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1, true, true));
		}
		TrueGearThirdAbility thirdGear = AbilityCapability.getEquippedAbility(player, TrueGearThirdAbility.INSTANCE.get());
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
		return EntityStatsCapability.get(user).get().getDoriki() >= 1500d && DevilFruitCapability.get(user).get().hasDevilFruit(TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA);
	}
}
