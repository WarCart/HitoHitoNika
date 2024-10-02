package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.Cancelable;
import net.warcar.hito_hito_nika.config.CommonConfig;
import xyz.pixelatedw.mineminenomi.abilities.haki.*;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.helpers.AttributeHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.haki.HakiDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.haki.IHakiData;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.ModI18n;

public class TrueGearThirdAbility extends StatsChangeAbility implements IParallelContinuousAbility, IAbilityModeSwitcher, IExtraUpdateData, IAbilityMode {
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
	private boolean gigant = false;
	private boolean secondGearWas = false;
	private int smallFormCooldown = 0;

	public TrueGearThirdAbility(AbilityCore core) {
		super(core);
		this.setDisplayName("Gear Third");
		this.setCustomIcon("Gear Third");
		this.setDynamicModifiers();
		this.onStartContinuityEvent = this::onStartContinuityEvent;
		this.afterContinuityStopEvent = this::afterContinuityStopEvent;
		this.duringContinuityEvent = this::onTick;
	}

	protected boolean onStartContinuityEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		double time = EntityStatsCapability.get(player).getDoriki() * .01d;
		if (!TrueGomuHelper.canActivateGear(props, INSTANCE)) {
			player.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_GEAR_ACTIVE), Util.NIL_UUID);
			return false;
		} else if (!canUnlock(player)) {
			player.sendMessage(new TranslationTextComponent("text.mineminenomi.too_weak"), Util.NIL_UUID);
			return false;
		} else {
			if (TrueGomuHelper.hasGearSecondActive(props)) {
				props.getEquippedAbility(TrueGearSecondAbility.INSTANCE).setThirdGear(true);
				this.secondGearWas = true;
			}
			if (TrueGomuHelper.hasGearFifthActive(props) && player.isCrouching()) {
				this.gigant = true;
				time /= 4;
				this.addModifier(Attributes.MOVEMENT_SPEED, SPEED_MODIFIER);
				this.addModifier(ModAttributes.JUMP_HEIGHT, JUMP_MODIFIER);
				this.addModifier(Attributes.ARMOR, ARMOR_MODIFIER);
				this.addModifier(ModAttributes.PUNCH_DAMAGE, STRENGTH_MODIFIER);
				this.addModifier(ForgeMod.REACH_DISTANCE, REACH_MODIFIER);
				this.addModifier(ModAttributes.ATTACK_RANGE, REACH_MODIFIER);
				this.addModifier(ModAttributes.STEP_HEIGHT, STEP_HEIGHT);
				this.addModifier(Attributes.ATTACK_KNOCKBACK, KNOCKBACK_RESISTANCE);
				this.addModifier(ModAttributes.FALL_RESISTANCE, FALL_RESISTANCE_MODIFIER);
				this.addModifier(ModAttributes.TOUGHNESS, TOUGHNESS_MODIFIER);
				this.setCustomIcon("Gigant");
				this.setDisplayName("Gomu Gomu Gigant");
			}
			if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
				time /= 4;
			}
			this.enableModes(player, this);
			GomuFusenAbility fusen = props.getEquippedAbility(GomuFusenAbility.INSTANCE);
			((GomuMorphsAbility) props.getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
			if (fusen != null)
				fusen.tryStoppingContinuity(player);
			if (time >= 500) {
				this.setThreshold(0f);
			} else {
				this.setThreshold(time);
			}
			return true;
		}
	}

	protected void onTick(PlayerEntity player, int passiveTimer) {
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
		if (TrueGomuHelper.hasAbilityActive(props, BusoshokuHakiFullBodyHardeningAbility.INSTANCE) && this.gigant) {
			haki.alterHakiOveruse(2);
		}
	}

	protected void afterContinuityStopEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		if (this.gigant) {
			this.gigant = false;
			this.setCustomIcon("Gear Third");
			this.setDisplayName("Gear Third");
		}
		int cooldown = (int) Math.round(Math.sqrt(this.continueTime * 2));
		this.setMaxCooldown(2 + cooldown);
		if (this.secondGearWas && EntityStatsCapability.get(player).getDoriki() < 3500.0D) {
			player.addEffect(new EffectInstance(ModEffects.UNCONSCIOUS.get(), 300, 1, true, true));
		} else if (EntityStatsCapability.get(player).getDoriki() < 3000.0D) {
			player.addEffect(new EffectInstance(Effects.WEAKNESS, 300, 1, true, true));
			player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 300, 1, true, true));
			this.smallFormCooldown = 300;
		}
		this.disableModes(player, this);
		TrueGearSecondAbility secondGear = props.getEquippedAbility(TrueGearSecondAbility.INSTANCE);
		if (secondGear != null && secondGear.isContinuous()) {
			secondGear.tryStoppingContinuity(player);
		}
		this.setSecondGear(false);
		((GomuMorphsAbility) props.getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
	}

	public void setSecondGear(boolean newWas) {
		this.secondGearWas = newWas;
	}

	protected static boolean canUnlock(LivingEntity user) {
		return EntityStatsCapability.get(user).getDoriki() * .01d >= 20d;
	}

	public boolean isGigant() {
		return gigant;
	}

	public void setGigant(boolean gigant) {
		this.gigant = gigant;
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
		out.putBoolean("gigant", this.gigant);
		out.putInt("sfc", this.smallFormCooldown);
		return out;
	}

	@Override
	public void setExtraData(CompoundNBT compoundNBT) {
		this.gigant = compoundNBT.getBoolean("gigant");
		this.smallFormCooldown = compoundNBT.getInt("sfc");
	}

	@Override
	public AbilityCore[] getParents() {
		return new AbilityCore[]{TrueGearFourthAbility.INSTANCE};
	}

	@Override
	public void enableMode(Ability ability) {
		if (CommonConfig.INSTANCE.isNonCanon()) {
			this.setCustomIcon("Gear 4 overload");
			this.setDisplayName("Gear Fourth: Overload");
		}
	}

	@Override
	public void disableMode(Ability ability) {
		this.setDisplayName("Gear Third");
		this.setCustomIcon("Gear Third");
	}

	public int getSmallFormCooldown() {
		return this.smallFormCooldown;
	}

	public void tick(PlayerEntity player) {
		super.tick(player);
		if (this.smallFormCooldown == 1 || this.smallFormCooldown >= 299)
			((GomuMorphsAbility) AbilityDataCapability.get(player).getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
		if (this.smallFormCooldown > 0) --this.smallFormCooldown;
		else if (this.smallFormCooldown < 0) this.smallFormCooldown = 0;
	}
}
