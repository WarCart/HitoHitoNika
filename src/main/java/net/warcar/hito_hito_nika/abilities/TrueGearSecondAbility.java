package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.init.*;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.awt.*;
import java.util.Objects;
import java.util.UUID;

public class TrueGearSecondAbility extends StatsChangeAbility implements IBodyOverlayAbility, IParallelContinuousAbility, IAbilityModeSwitcher {
	public static final AbilityCore<TrueGearSecondAbility> INSTANCE;
	private static final AbilityOverlay OVERLAY;
	private static final AbilityAttributeModifier JUMP_HEIGHT;
	private static final AbilityAttributeModifier STRENGTH_MODIFIER;
	private static final AbilityAttributeModifier STEP_HEIGHT;
	private boolean thirdGearWas = false;
	private boolean prevSprintValue = false;

	public TrueGearSecondAbility(AbilityCore core) {
		super(core);
		this.setCustomIcon("Gear Second");
		this.setDisplayName("Gear Second");
		this.addModifier(ModAttributes.STEP_HEIGHT, STEP_HEIGHT);
		this.addModifier(Attributes.MOVEMENT_SPEED, STEP_HEIGHT);
		this.addModifier(ModAttributes.JUMP_HEIGHT, JUMP_HEIGHT);
		this.addModifier(Attributes.ATTACK_DAMAGE, STRENGTH_MODIFIER);
		this.addModifier(ModAttributes.PUNCH_DAMAGE, STRENGTH_MODIFIER);
		this.onStartContinuityEvent = this::onStartContinuity;
		this.duringContinuityEvent = this::duringContinuity;
		this.afterContinuityStopEvent = this::afterContinuityStopEvent;
	}

	private boolean onStartContinuity(PlayerEntity player) {
		double time = EntityStatsCapability.get(player).getDoriki() * .02d;
		IAbilityData props = AbilityDataCapability.get(player);
		if (!TrueGomuHelper.canActivateGear(props, INSTANCE)) {
			player.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_GEAR_ACTIVE), Util.NIL_UUID);
			return false;
		} else if (!canUnlock(player)) {
			player.sendMessage(new TranslationTextComponent("text.mineminenomi.too_weak"), Util.NIL_UUID);
			return false;
		} else {
			if (TrueGomuHelper.hasGearThirdActive(props)) {
				props.getEquippedAbility(TrueGearThirdAbility.INSTANCE).setSecondGear(true);
				this.thirdGearWas = true;
			}
			if (time >= 500) {
				this.setThreshold(0f);
			} else {
				this.setThreshold(time);
			}
			this.enableModes(player, this);
			player.level.playSound(null, player.blockPosition(), ModSounds.GEAR_SECOND_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
			this.prevSprintValue = player.isSprinting();
			return true;
		}
	}

	private void duringContinuity(PlayerEntity player, int passiveTimer) {
		if (passiveTimer % 10 == 0) {
			WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), player, player.getX(), player.getY(), player.getZ());
		}
		if (player.isSprinting()) {
			if (!this.prevSprintValue) {
				player.level.playSound(null, player.blockPosition(), ModSounds.TELEPORT_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
			}
			float maxSpeed = 2.2F;
			Vector3d vec = player.getDeltaMovement();
			double var10001;
			double var10003;
			if (player.isSprinting()) {
				var10001 = vec.x * (double) maxSpeed;
				var10003 = vec.z * (double) maxSpeed;
				player.setDeltaMovement(var10001, player.getDeltaMovement().y, var10003);
			} else {
				var10001 = vec.x * (double) maxSpeed * 0.5D;
				var10003 = vec.z * (double) maxSpeed;
				player.setDeltaMovement(var10001, player.getDeltaMovement().y, var10003 * 0.5D);
			}
			this.prevSprintValue = player.isSprinting();
			player.hurtMarked = true;
		} else {
			this.prevSprintValue = false;
		}
	}

	private void afterContinuityStopEvent(PlayerEntity player) {
		int cooldown = (int) Math.round(Math.sqrt(this.continueTime));
		this.setMaxCooldown(1 + cooldown);
		if (this.thirdGearWas && EntityStatsCapability.get(player).getDoriki() < 3500.0D) {
			player.addEffect(new EffectInstance(ModEffects.UNCONSCIOUS.get(), 300, 1, true, true));
		} else if ((double) this.continueTime > (double) this.getThreshold() / 1.425D && EntityStatsCapability.get(player).getDoriki() < 2000.0D) {
			player.addEffect(new EffectInstance(Effects.HUNGER, 600, 3, true, true));
			player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200, 1, true, true));
		}
		this.disableModes(player, this);
		Objects.requireNonNull(player.getAttribute(ModAttributes.STEP_HEIGHT.get())).removeModifier(STEP_HEIGHT);
		Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(STEP_HEIGHT);
		Objects.requireNonNull(player.getAttribute(ModAttributes.JUMP_HEIGHT.get())).removeModifier(JUMP_HEIGHT);
		Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_DAMAGE)).removeModifier(STRENGTH_MODIFIER);
		Objects.requireNonNull(player.getAttribute(ModAttributes.PUNCH_DAMAGE.get())).removeModifier(STRENGTH_MODIFIER);
		TrueGearThirdAbility thirdGear = AbilityDataCapability.get(player).getEquippedAbility(TrueGearThirdAbility.INSTANCE);
		if(thirdGear != null && thirdGear.isContinuous()) {
			thirdGear.tryStoppingContinuity(player);
		}
		this.setThirdGear(false);
	}

	public AbilityOverlay getBodyOverlay(LivingEntity entity) {
		return OVERLAY;
	}

	public void setThirdGear(boolean newWas) {
		this.thirdGearWas = newWas;
	}

	protected static boolean canUnlock(LivingEntity user) {
		return EntityStatsCapability.get(user).getDoriki() * .02d >= 20d;
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gear Second", AbilityCategory.DEVIL_FRUITS, TrueGearSecondAbility::new)).addDescriptionLine("By speeding up their blood flow, the user gains strength, speed and mobility").setUnlockCheck(TrueGearSecondAbility::canUnlock).build();
		OVERLAY = (new AbilityOverlay.Builder()).setColor(new Color(232, 54, 54, 74)).build();
		JUMP_HEIGHT = new AbilityAttributeModifier(UUID.fromString("a44a9644-369a-4e18-88d9-323727d3d85b"), INSTANCE, "Gear Second Jump Modifier", 5.0D, Operation.ADDITION);
		STRENGTH_MODIFIER = new AbilityAttributeModifier(UUID.fromString("a2337b58-7e6d-4361-a8ca-943feee4f906"), INSTANCE, "Gear Second Attack Damage Modifier", 4.0D, Operation.ADDITION);
		STEP_HEIGHT = new AbilityAttributeModifier(UUID.fromString("eab680cd-a6dc-438a-99d8-46f9eb53a950"), INSTANCE, "Gear Second Step Height Modifier", 1.0D, Operation.ADDITION);
	}
}
