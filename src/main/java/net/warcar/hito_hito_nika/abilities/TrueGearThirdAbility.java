package net.warcar.hito_hito_nika.abilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.warcar.hito_hito_nika.config.CommonConfig;
import net.warcar.hito_hito_nika.helpers.EquationHelper;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.abilities.haki.*;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AltModeComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChangeStatsComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.helpers.AttributeHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.data.entity.haki.HakiCapability;
import xyz.pixelatedw.mineminenomi.data.entity.haki.IHakiData;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.init.ModAbilityComponents;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.i18n.ModI18nAbilities;

import java.util.HashMap;

public class TrueGearThirdAbility extends Ability {
	private static final Component[] DESCRIPTION = TrueGomuHelper.registerDescriptionText("gear_third", new Pair[]{ImmutablePair.of("By blowing air and inflating their body, the user's attacks get bigger and gain incredible strength.", (Object)null)});
	public static final AbilityCore<TrueGearThirdAbility> INSTANCE = (new AbilityCore.Builder<>("gear_third", "Gear Third", AbilityCategory.DEVIL_FRUITS, TrueGearThirdAbility::new))
			.addDescriptionLine(DESCRIPTION).setUnlockCheck(TrueGearThirdAbility::canUnlock).build();
	private static final AbilityAttributeModifier SPEED_MODIFIER;
	private static final AbilityAttributeModifier JUMP_MODIFIER;
	private static final AbilityAttributeModifier ARMOR_MODIFIER;
	private static final AbilityAttributeModifier STRENGTH_MODIFIER;
	private static final AbilityAttributeModifier REACH_MODIFIER;
	private static final AbilityAttributeModifier STEP_HEIGHT;
	private static final AbilityAttributeModifier KNOCKBACK_RESISTANCE;
	private static final AbilityAttributeModifier FALL_RESISTANCE_MODIFIER;
	private static final AbilityAttributeModifier TOUGHNESS_MODIFIER;
	private final ContinuousComponent continuousComponent;
	private final ChangeStatsComponent statsComponent;
	private final AltModeComponent<Mode> modeComponent;
	private boolean secondGearWas = false;
	private int smallFormCooldown = 0;

	public TrueGearThirdAbility(AbilityCore<TrueGearThirdAbility> core) {
		super(core);
		this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "gear_third"));
		modeComponent = new AltModeComponent<>(this, Mode.class, Mode.NORMAL);
		this.modeComponent.addChangeModeEvent(this::onModeChange);
		this.continuousComponent = new ContinuousComponent(this, true);
		this.statsComponent = new ChangeStatsComponent(this);
		this.continuousComponent.addTickEvent(this::onTick);
		this.addUseEvent(this::onStartContinuityEvent);
		this.continuousComponent.addStartEvent(this::afterStart);
		continuousComponent.addStartEvent(TrueGomuHelper.basicGearStuff());
		this.continuousComponent.addEndEvent(this::afterContinuityStopEvent);
		this.addTickEvent(this::smallTick);
		this.addComponents(continuousComponent, statsComponent, modeComponent);
	}

	private boolean onModeChange(LivingEntity entity, IAbility ability, Mode mode) {
        return TrueGomuHelper.hasGearFifthActive(AbilityCapability.get(entity).get()) || mode != Mode.GIANT;
    }

	private void afterStart(LivingEntity entity, IAbility ability) {
		GomuMorphsAbility morphs = AbilityCapability.get(entity).get().getPassiveAbility(GomuMorphsAbility.INSTANCE);
		if (morphs != null) morphs.updateModes(entity);
	}

	protected void onStartContinuityEvent(LivingEntity player, IAbility abl) {
		if (this.continuousComponent.isContinuous()) {
			this.continuousComponent.stopContinuity(player);
			return;
		}
		IAbilityData props = AbilityCapability.get(player).get();
		double time = EquationHelper.parseEquation(CommonConfig.INSTANCE.getG3Length(), player, new HashMap<>()).getValue();
		if (!TrueGomuHelper.canActivateGear(props, this)) {
			player.sendSystemMessage(ModI18nAbilities.MESSAGE_GEAR_ACTIVE);
		} else {
			if (this.isGiant()) {
				time /= 4;
				this.statsComponent.addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_MODIFIER);
				this.statsComponent.addAttributeModifier(ModAttributes.JUMP_HEIGHT, JUMP_MODIFIER);
				this.statsComponent.addAttributeModifier(Attributes.ARMOR, ARMOR_MODIFIER);
				this.statsComponent.addAttributeModifier(ModAttributes.PUNCH_DAMAGE, STRENGTH_MODIFIER);
				this.statsComponent.addAttributeModifier(ForgeMod.BLOCK_REACH, REACH_MODIFIER);
				this.statsComponent.addAttributeModifier(ForgeMod.ENTITY_REACH, REACH_MODIFIER);
				this.statsComponent.addAttributeModifier(ForgeMod.STEP_HEIGHT_ADDITION, STEP_HEIGHT);
				this.statsComponent.addAttributeModifier(Attributes.ATTACK_KNOCKBACK, KNOCKBACK_RESISTANCE);
				this.statsComponent.addAttributeModifier(ModAttributes.FALL_RESISTANCE, FALL_RESISTANCE_MODIFIER);
				this.statsComponent.addAttributeModifier(ModAttributes.TOUGHNESS, TOUGHNESS_MODIFIER);
				this.statsComponent.applyModifiers(player);
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
				time /= 4;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				props.getEquippedAbility(TrueGearSecondAbility.INSTANCE).setThirdGear(true);
				this.secondGearWas = true;
			}
			if (time >= 500) {
				this.continuousComponent.startContinuity(player, -1);
			} else {
				this.continuousComponent.startContinuity(player, (float) time * 20);
			}
		}
	}

	protected void onTick(LivingEntity player, IAbility abl) {
		IAbilityData props = AbilityCapability.get(player).get();
		IHakiData haki = HakiCapability.get(player).get();
		if (TrueGomuHelper.hasAbilityActive(props, BusoshokuHakiHardeningAbility.INSTANCE)) {
			haki.alterHakiOveruse(1);
		}
		if (TrueGomuHelper.hasAbilityActive(props, BusoshokuHakiEmissionAbility.INSTANCE)) {
			haki.alterHakiOveruse(2);
		}
		if (TrueGomuHelper.hasAbilityActive(props, BusoshokuHakiInternalDestructionAbility.INSTANCE)) {
			haki.alterHakiOveruse(4);
		}
		if (TrueGomuHelper.hasAbilityActive(props, HaoshokuHakiInfusionAbility.INSTANCE)) {
			haki.alterHakiOveruse(12);
		}
		if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
			haki.alterHakiOveruse(15);
		}
		if (TrueGomuHelper.hasAbilityActive(props, BusoshokuHakiFullBodyHardeningAbility.INSTANCE) && this.isGiant()) {
			haki.alterHakiOveruse(2);
		}
	}

	protected void afterContinuityStopEvent(LivingEntity player, IAbility abl) {
		IAbilityData props = AbilityCapability.get(player).get();
		if (this.isGiant()) {
			this.modeComponent.revertToDefault(player);
			this.statsComponent.removeModifiers(player);
			this.statsComponent.clearAttributeModifiers();
		}
		this.cooldownComponent.startCooldown(player, (float) EquationHelper.parseEquation(CommonConfig.INSTANCE.getG3Cooldown(), player, TrueGomuHelper.getBasicBonusData(this.continuousComponent.getContinueTime())).getValue());
		if (this.secondGearWas && EntityStatsCapability.get(player).get().getDoriki() < 3500.0D) {
			player.addEffect(new MobEffectInstance(ModEffects.UNCONSCIOUS.get(), 300, 1, true, true));
		} else if (EntityStatsCapability.get(player).get().getDoriki() < 3000.0D) {
			player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 300, 1, true, true));
			player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 1, true, true));
			this.smallFormCooldown = 300;
		}
		TrueGearSecondAbility secondGear = props.getEquippedAbility(TrueGearSecondAbility.INSTANCE);
		if (secondGear != null && secondGear.isContinuous() && this.secondGearWas) {
			this.setSecondGear(false);
			secondGear.getComponent(ModAbilityComponents.CONTINUOUS.get()).ifPresent(c -> c.stopContinuity(player));
		}
		this.setSecondGear(false);
		GomuMorphsAbility morphs = props.getPassiveAbility(GomuMorphsAbility.INSTANCE);
		if (morphs != null)
			morphs.updateModes(player);
	}

	public void setSecondGear(boolean newWas) {
		this.secondGearWas = newWas;
	}

	protected static boolean canUnlock(LivingEntity user) {
		IDevilFruit fruit = DevilFruitCapability.get(user).get();
		return EntityStatsCapability.get(user).get().getDoriki() >= 2000d && fruit.hasDevilFruit(TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA);
	}

	public boolean isGiant() {
		return this.modeComponent.isMode(Mode.GIANT);
	}

	static {
        SPEED_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_MOVEMENT_SPEED_UUID, INSTANCE, "Mega Mega Speed Modifier", 1.0199999809265137D, Operation.MULTIPLY_BASE);
		JUMP_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_JUMP_BOOST_UUID, INSTANCE, "Mega Mega Jump Modifier", 2.0D, Operation.ADDITION);
		ARMOR_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_ARMOR_UUID, INSTANCE, "Mega Mega Armor Modifier", 5.0D, Operation.ADDITION);
		STRENGTH_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_STRENGTH_UUID, INSTANCE, "Mega Mega Strength Modifier", 3.0D, Operation.ADDITION);
		REACH_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_ATTACK_REACH_UUID, INSTANCE, "Mega Mega Reach Modifier", 5.0D, Operation.ADDITION);
		STEP_HEIGHT = new AbilityAttributeModifier(AttributeHelper.MORPH_STEP_HEIGHT_UUID, INSTANCE, "Mega Mega Step Height Modifier", 1.5D, Operation.ADDITION);
		KNOCKBACK_RESISTANCE = new AbilityAttributeModifier(AttributeHelper.MORPH_KNOCKBACK_RESISTANCE_UUID, INSTANCE, "Mega Mega Knockback Resistance Modifier", 1.0D, Operation.ADDITION);
		FALL_RESISTANCE_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_FALL_RESISTANCE_UUID, INSTANCE, "Mega Mega Fall Resistance Modifier", 10.0D, Operation.ADDITION);
		TOUGHNESS_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_TOUGHNESS_UUID, INSTANCE, "Mega Mega Toughness Modifier", 4.0D, Operation.ADDITION);
	}

	@Override
	public void saveAdditional(CompoundTag out) {
		out.putInt("sfc", this.smallFormCooldown);
	}

	@Override
	public void loadAdditional(CompoundTag compoundNBT) {
		this.smallFormCooldown = compoundNBT.getInt("sfc");
	}

	public int getSmallFormCooldown() {
		return this.smallFormCooldown;
	}

	public void smallTick(LivingEntity player, IAbility abl) {
		if (this.smallFormCooldown == 1 || this.smallFormCooldown >= 299)
			AbilityCapability.get(player).get().getPassiveAbility(GomuMorphsAbility.INSTANCE).updateModes(player);
		if (this.smallFormCooldown > 0) --this.smallFormCooldown;
		else if (this.smallFormCooldown < 0) this.smallFormCooldown = 0;
	}

	public enum Mode {
		NORMAL,
		GIANT
	}
}
