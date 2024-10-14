package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AnimeScreamComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChangeStatsComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.init.*;
import xyz.pixelatedw.mineminenomi.particles.effects.ParticleEffect;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.Objects;
import java.util.UUID;

public class TrueGearSecondAbility extends Ability {
	public static final AbilityCore<TrueGearSecondAbility> INSTANCE;
	private static final AbilityAttributeModifier JUMP_HEIGHT;
	private static final AbilityAttributeModifier STRENGTH_MODIFIER;
	private static final AbilityAttributeModifier STEP_HEIGHT;
	private final ContinuousComponent continuousComponent;
	private final ChangeStatsComponent statsComponent;
	private final AnimeScreamComponent trueScreamComponent = new AnimeScreamComponent(this) {
		@Override
		public void setupDefaultScreams(IAbility ability) {
			ability.getComponent(ModAbilityKeys.CONTINUOUS).ifPresent(chargeComponent -> chargeComponent.addStartEvent((entity, iAbility) -> this.scream(entity, ability.getDisplayName().getString())));
		}
	};
	private boolean thirdGearWas = false;
	private boolean prevSprintValue = false;

	public TrueGearSecondAbility(AbilityCore core) {
		super(core);
		this.setCustomIcon("Gear Second");
		this.setDisplayName("Gear Second");
		this.isNew = true;
		this.continuousComponent = new ContinuousComponent(this, true);
		this.statsComponent = new ChangeStatsComponent(this);
		this.statsComponent.addAttributeModifier(ModAttributes.STEP_HEIGHT, STEP_HEIGHT);
		this.statsComponent.addAttributeModifier(Attributes.MOVEMENT_SPEED, STEP_HEIGHT);
		this.statsComponent.addAttributeModifier(ModAttributes.JUMP_HEIGHT, JUMP_HEIGHT);
		this.statsComponent.addAttributeModifier(Attributes.ATTACK_DAMAGE, STRENGTH_MODIFIER);
		this.statsComponent.addAttributeModifier(ModAttributes.PUNCH_DAMAGE, STRENGTH_MODIFIER);
		this.addUseEvent(this::onStartContinuity);
		this.continuousComponent.addTickEvent(this::duringContinuity);
		this.continuousComponent.addEndEvent(this::afterContinuityStopEvent);
		this.addComponents(this.continuousComponent, this.statsComponent, this.trueScreamComponent);
	}

	private void onStartContinuity(LivingEntity player, IAbility abl) {
		if (this.continuousComponent.isContinuous()) {
			this.continuousComponent.stopContinuity(player);
			return;
		}
		float time = (float) EntityStatsCapability.get(player).getDoriki() * .02f;
		IAbilityData props = AbilityDataCapability.get(player);
		if (!TrueGomuHelper.canActivateGear(props, INSTANCE)) {
			player.sendMessage(ModI18n.ABILITY_MESSAGE_GEAR_ACTIVE, Util.NIL_UUID);
		} else {
			if (TrueGomuHelper.hasGearThirdActive(props)) {
				props.getEquippedAbility(TrueGearThirdAbility.INSTANCE).setSecondGear(true);
				this.thirdGearWas = true;
			}
			if (time >= 500) {
				this.continuousComponent.startContinuity(player, -1);
			} else {
				this.continuousComponent.startContinuity(player, time * 20);
			}
			this.statsComponent.applyModifiers(player);
			player.level.playSound(null, player.blockPosition(), ModSounds.GEAR_SECOND_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
			this.prevSprintValue = player.isSprinting();
		}
	}

	private void duringContinuity(LivingEntity entity, IAbility abl) {
		if (this.continuousComponent.getContinueTime() % 10.0F == 0.0F) {
			WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), entity, entity.getX(), entity.getY(), entity.getZ());
		}

		if (AbilityHelper.canUseMomentumAbilities(entity)) {
			if (entity.isSprinting()) {
				if (!this.prevSprintValue) {
					entity.level.playSound(null, entity.blockPosition(), ModSounds.TELEPORT_SFX.get(), SoundCategory.PLAYERS, 2.0F, 1.0F);
				}

				float maxSpeed = 2.2F;
				Vector3d vec = entity.getLookAngle();
				double var10001;
				double var10003;
				if (entity.isOnGround()) {
					var10001 = vec.x * (double)maxSpeed;
					var10003 = vec.z * (double)maxSpeed;
					entity.setDeltaMovement(var10001, entity.getDeltaMovement().y, var10003);
				} else {
					var10001 = vec.x * (double)maxSpeed * 0.5D;
					var10003 = vec.z * (double)maxSpeed;
					entity.setDeltaMovement(var10001, entity.getDeltaMovement().y, var10003 * 0.5D);
				}

				this.prevSprintValue = entity.isSprinting();
				entity.hurtMarked = true;
			} else {
				this.prevSprintValue = false;
			}
		}
	}

	private void afterContinuityStopEvent(LivingEntity player, IAbility abl) {
		int cooldown = (int) Math.round(Math.sqrt(this.continuousComponent.getContinueTime()));
		this.cooldownComponent.startCooldown(player, 20 * (cooldown + 1));
		if (this.thirdGearWas && EntityStatsCapability.get(player).getDoriki() < 3500.0D) {
			player.addEffect(new EffectInstance(ModEffects.UNCONSCIOUS.get(), 300, 1, true, true));
		} else if ((double) this.continuousComponent.getContinueTime() > (double) this.continuousComponent.getThresholdTime() / 1.425D && EntityStatsCapability.get(player).getDoriki() < 2000.0D) {
			player.addEffect(new EffectInstance(Effects.HUNGER, 600, 3, true, true));
			player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 200, 1, true, true));
		}
		Objects.requireNonNull(player.getAttribute(ModAttributes.STEP_HEIGHT.get())).removeModifier(STEP_HEIGHT);
		Objects.requireNonNull(player.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(STEP_HEIGHT);
		Objects.requireNonNull(player.getAttribute(ModAttributes.JUMP_HEIGHT.get())).removeModifier(JUMP_HEIGHT);
		Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_DAMAGE)).removeModifier(STRENGTH_MODIFIER);
		Objects.requireNonNull(player.getAttribute(ModAttributes.PUNCH_DAMAGE.get())).removeModifier(STRENGTH_MODIFIER);
		TrueGearThirdAbility thirdGear = AbilityDataCapability.get(player).getEquippedAbility(TrueGearThirdAbility.INSTANCE);
		if (thirdGear != null && thirdGear.isContinuous()) {
			//thirdGear.tryStoppingContinuity(player);
		}
		this.setThirdGear(false);
		this.statsComponent.removeModifiers(player);
	}

	public void setThirdGear(boolean newWas) {
		this.thirdGearWas = newWas;
	}

	protected static boolean canUnlock(LivingEntity user) {
		return EntityStatsCapability.get(user).getDoriki() * .02d >= 20d && TrueGomuHelper.hasFruit(user, new ResourceLocation("mineminenomi", "gomu_gomu_no_mi"));
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gear Second", AbilityCategory.DEVIL_FRUITS, TrueGearSecondAbility::new)).addDescriptionLine("By speeding up their blood flow, the user gains strength, speed and mobility").setUnlockCheck(TrueGearSecondAbility::canUnlock).build();
		//OVERLAY = (new AbilityOverlay.Builder()).setColor(new Color(232, 54, 54, 74)).build();
		JUMP_HEIGHT = new AbilityAttributeModifier(UUID.fromString("a44a9644-369a-4e18-88d9-323727d3d85b"), INSTANCE, "Gear Second Jump Modifier", 5.0D, Operation.ADDITION);
		STRENGTH_MODIFIER = new AbilityAttributeModifier(UUID.fromString("a2337b58-7e6d-4361-a8ca-943feee4f906"), INSTANCE, "Gear Second Attack Damage Modifier", 4.0D, Operation.ADDITION);
		STEP_HEIGHT = new AbilityAttributeModifier(UUID.fromString("eab680cd-a6dc-438a-99d8-46f9eb53a950"), INSTANCE, "Gear Second Step Height Modifier", 1.0D, Operation.ADDITION);
	}
}
