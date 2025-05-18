package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Util;
import net.minecraftforge.common.ForgeMod;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import xyz.pixelatedw.mineminenomi.abilities.haki.*;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AltModeComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AnimeScreamComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChangeStatsComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.helpers.AttributeHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.haki.HakiDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.haki.IHakiData;
import xyz.pixelatedw.mineminenomi.init.*;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

public class TrueGearThirdAbility extends Ability implements IExtraUpdateData {
	public static final AbilityCore<TrueGearThirdAbility> INSTANCE;
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
	private final AnimeScreamComponent trueScreamComponent = new AnimeScreamComponent(this) {
		@Override
		public void setupDefaultScreams(IAbility ability) {
			ability.getComponent(ModAbilityKeys.CONTINUOUS).ifPresent(chargeComponent -> chargeComponent.addStartEvent((entity, iAbility) -> this.scream(entity, ability.getDisplayName().getString())));
		}
	};
	private boolean secondGearWas = false;
	private int smallFormCooldown = 0;

	public TrueGearThirdAbility(AbilityCore<TrueGearThirdAbility> core) {
		super(core);
		this.isNew = true;
		modeComponent = new AltModeComponent<>(this, Mode.class, Mode.Normal);
		this.modeComponent.addChangeModeEvent(this::onModeChange);
		this.continuousComponent = new ContinuousComponent(this, true);
		this.statsComponent = new ChangeStatsComponent(this);
		this.continuousComponent.addTickEvent(this::onTick);
		this.addUseEvent(this::onStartContinuityEvent);
		this.continuousComponent.addStartEvent(this::afterStart);
		this.continuousComponent.addEndEvent(this::afterContinuityStopEvent);
		this.addTickEvent(this::smallTick);
		this.addComponents(continuousComponent, statsComponent, modeComponent, trueScreamComponent);
	}

	private void onModeChange(LivingEntity entity, IAbility ability, Mode mode) {
		if (!TrueGomuHelper.hasGearFifthActive(AbilityDataCapability.get(entity))) {
			throw new IllegalStateException();
		}
	}

	private void afterStart(LivingEntity entity, IAbility ability) {
		GomuMorphsAbility morphs = AbilityDataCapability.get(entity).getPassiveAbility(GomuMorphsAbility.INSTANCE);
		if (morphs != null) morphs.updateModes();
	}

	protected void onStartContinuityEvent(LivingEntity player, IAbility abl) {
		if (this.continuousComponent.isContinuous()) {
			this.continuousComponent.stopContinuity(player);
			return;
		}
		IAbilityData props = AbilityDataCapability.get(player);
		double time = EntityStatsCapability.get(player).getDoriki() * .01d;
		if (!TrueGomuHelper.canActivateGear(props, INSTANCE)) {
			player.sendMessage(ModI18n.ABILITY_MESSAGE_GEAR_ACTIVE, Util.NIL_UUID);
		} else {
			if (this.isGigant()) {
				time /= 4;
				this.statsComponent.addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_MODIFIER);
				this.statsComponent.addAttributeModifier(ModAttributes.JUMP_HEIGHT, JUMP_MODIFIER);
				this.statsComponent.addAttributeModifier(Attributes.ARMOR, ARMOR_MODIFIER);
				this.statsComponent.addAttributeModifier(ModAttributes.PUNCH_DAMAGE, STRENGTH_MODIFIER);
				this.statsComponent.addAttributeModifier(ForgeMod.REACH_DISTANCE, REACH_MODIFIER);
				this.statsComponent.addAttributeModifier(ModAttributes.ATTACK_RANGE, REACH_MODIFIER);
				this.statsComponent.addAttributeModifier(ModAttributes.STEP_HEIGHT, STEP_HEIGHT);
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
		IAbilityData props = AbilityDataCapability.get(player);
		IHakiData haki = HakiDataCapability.get(player);
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
		if (TrueGomuHelper.hasAbilityActive(props, BusoshokuHakiFullBodyHardeningAbility.INSTANCE) && this.isGigant()) {
			haki.alterHakiOveruse(2);
		}
	}

	protected void afterContinuityStopEvent(LivingEntity player, IAbility abl) {
		IAbilityData props = AbilityDataCapability.get(player);
		if (this.isGigant()) {
			this.modeComponent.revertToDefault(player);
			this.statsComponent.removeModifiers(player);
			this.statsComponent.clearAttributeModifiers();
		}
		int cooldown = (int) Math.round(Math.sqrt(this.continuousComponent.getContinueTime() * 2));
		this.cooldownComponent.startCooldown(player, 20 * (2 + cooldown));
		if (this.secondGearWas && EntityStatsCapability.get(player).getDoriki() < 3500.0D) {
			player.addEffect(new EffectInstance(ModEffects.UNCONSCIOUS.get(), 300, 1, true, true));
		} else if (EntityStatsCapability.get(player).getDoriki() < 3000.0D) {
			player.addEffect(new EffectInstance(Effects.WEAKNESS, 300, 1, true, true));
			player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 300, 1, true, true));
			this.smallFormCooldown = 300;
		}
		this.setSecondGear(false);
		GomuMorphsAbility morphs = props.getPassiveAbility(GomuMorphsAbility.INSTANCE);
		if (morphs != null)
			morphs.updateModes();
	}

	public void setSecondGear(boolean newWas) {
		this.secondGearWas = newWas;
	}

	protected static boolean canUnlock(LivingEntity user) {
		IDevilFruit fruit = DevilFruitCapability.get(user);
		return (EntityStatsCapability.get(user).getDoriki() * .01d >= 20d && fruit.hasDevilFruit(TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA))
				|| (WyHelper.isAprilFirst() && fruit.hasDevilFruit(ModAbilities.MOCHI_MOCHI_NO_MI));
	}

	public boolean isGigant() {
		return this.modeComponent.isMode(Mode.Gigant);
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gear Third", AbilityCategory.DEVIL_FRUITS, TrueGearThirdAbility::new)).addDescriptionLine("By blowing air and inflating their body, the user's attacks get bigger and gain incredible strength")
				.setUnlockCheck(TrueGearThirdAbility::canUnlock).build();
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
	public CompoundNBT getExtraData() {
		CompoundNBT out = new CompoundNBT();
		out.putInt("sfc", this.smallFormCooldown);
		return out;
	}

	@Override
	public void setExtraData(CompoundNBT compoundNBT) {
		this.smallFormCooldown = compoundNBT.getInt("sfc");
	}

	public int getSmallFormCooldown() {
		return this.smallFormCooldown;
	}

	public void smallTick(LivingEntity player, IAbility abl) {
		if (this.smallFormCooldown == 1 || this.smallFormCooldown >= 299)
			AbilityDataCapability.get(player).getPassiveAbility(GomuMorphsAbility.INSTANCE).updateModes();
		if (this.smallFormCooldown > 0) --this.smallFormCooldown;
		else if (this.smallFormCooldown < 0) this.smallFormCooldown = 0;
	}

	enum Mode {
		Normal,
		Gigant
	}
}
