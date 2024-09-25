package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.effects.GomuReviveEffect;
import xyz.pixelatedw.mineminenomi.abilities.haki.HaoshokuHakiInfusionAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AltModeComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AnimeScreamComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChangeStatsComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.enums.HakiType;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.AttributeHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.HakiHelper;
import xyz.pixelatedw.mineminenomi.config.CommonConfig;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.haki.HakiDataCapability;
import xyz.pixelatedw.mineminenomi.init.*;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncAbilityDataPacket;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

public class TrueGearFourthAbility extends Ability implements IExtraUpdateData {
	public static final AbilityCore<TrueGearFourthAbility> INSTANCE;
	private static final AbilityAttributeModifier ARMOR_MODIFIER;
	private static final AbilityAttributeModifier STRENGTH_MODIFIER;
	private static final AbilityAttributeModifier DAMAGE_REDUCTION_MODIFIER;
	private final AltModeComponent<Mode> mode;
	private final ContinuousComponent continuousComponent;
	private final ChangeStatsComponent statsComponent;
	private final AnimeScreamComponent trueScreamComponent = new AnimeScreamComponent(this) {
		@Override
		public void setupDefaultScreams(IAbility ability) {
			ability.getComponent(ModAbilityKeys.CONTINUOUS).ifPresent(chargeComponent -> chargeComponent.addStartEvent((entity, iAbility) -> this.scream(entity, ability.getDisplayName().getString())));
		}
	};
	protected int targetedTime = 30;
	protected boolean onTargetedTime = false;
	protected boolean isBonusTime = false;

	public TrueGearFourthAbility(AbilityCore<TrueGearFourthAbility> core) {
		super(core);
		this.isNew = true;
		this.setCustomIcon("Gear Fourth");
		this.setDisplayName(new TranslationTextComponent("ability.mineminenomi.gear_fourth"));
		statsComponent = new ChangeStatsComponent(this);
		mode = new AltModeComponent<>(this, Mode.class, Mode.BOUNDMAN);
		continuousComponent = new ContinuousComponent(this, true);
		this.addComponents(continuousComponent, statsComponent, mode, trueScreamComponent);
		this.statsComponent.addAttributeModifier(Attributes.ARMOR, ARMOR_MODIFIER);
		this.statsComponent.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, ARMOR_MODIFIER);
		this.statsComponent.addAttributeModifier(ModAttributes.PUNCH_DAMAGE, STRENGTH_MODIFIER);
		this.statsComponent.addAttributeModifier(ModAttributes.DAMAGE_REDUCTION, DAMAGE_REDUCTION_MODIFIER);
		this.continuousComponent.addTickEvent(this::duringContinuity);
		this.continuousComponent.addEndEvent(this::beforeContinuityStopEvent);
		this.continuousComponent.addStartEvent(this::afterStart);
		this.addUseEvent(this::onStartContinuity);
		this.addTickEvent(this::onTick);
		this.getComponent(ModAbilityKeys.SLOT_DECORATION).ifPresent(component -> component.addPreRenderEvent(30, ((livingEntity, minecraft, matrixStack, x, y, partialTicks) ->  {
			if (this.isOnTargetedTime()) {
				component.setSlotColor(0, 1, 0);
				component.setMaxValue(1);
				component.setCurrentValue(1);
				component.setDisplayText(" ");
			}
		})));
	}

	private void onTick(LivingEntity player, TrueGearFourthAbility ability) {
		if (ability.onTargetedTime) {
			HitoHitoNoMiNikaMod.LOGGER.info(this.targetedTime);
			if (ability.targetedTime <= 0) {
				ability.afterContinuityStopEvent(player);
				return;
			}
			for (int i = 0; i < 3; i++) {
				WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), player, player.getX(), player.getY() + 1.0D, player.getZ());
			}
			--ability.targetedTime;
		}
	}

	private void afterStart(LivingEntity entity, IAbility ability) {
		GomuMorphsAbility morphs = AbilityDataCapability.get(entity).getPassiveAbility(GomuMorphsAbility.INSTANCE);
		if (morphs != null) morphs.updateModes();
	}

	private void onStartContinuity(LivingEntity player, TrueGearFourthAbility ability) {
		IAbilityData props = AbilityDataCapability.get(player);
		float time = (float) (EntityStatsCapability.get(player).getDoriki()) * .005f;
		if (time >= 500) {
			time = -1;
		}if (!TrueGomuHelper.canActivateGear(props, INSTANCE)) {
			player.sendMessage(ModI18n.ABILITY_MESSAGE_GEAR_ACTIVE, Util.NIL_UUID);
		} else if (!canUnlock(player)) {
			player.sendMessage(new TranslationTextComponent("text.mineminenomi.too_weak"), Util.NIL_UUID);
		} else if (TrueGomuHelper.hasGearFifthActive(props) && ability.isSnakeman()) {
			player.sendMessage(ModI18n.ABILITY_MESSAGE_GEAR_ACTIVE, Util.NIL_UUID);
		} else {
			if (ability.continuousComponent.isContinuous()) {
				ability.continuousComponent.stopContinuity(player);
			} else if (ability.onTargetedTime) {
				ability.stopTargetedTime(player);
			} else {
				if (this.isSnakeman()) {
					time /= 2;
				}
				ability.continuousComponent.startContinuity(player, time * 20);
				ability.statsComponent.applyModifiers(player);
			}
		}
	}

	public void duringContinuity(LivingEntity player, IAbility ability) {
		float passiveTimer = this.continuousComponent.getContinueTime();
		if (passiveTimer % 2 == 0 && !this.isSnakeman()) {
			WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), player, player.getX(), player.getY() + 1.0D, player.getZ());
		}
		if (HakiHelper.checkForHakiOveruse(player, 2)) {
			this.continuousComponent.stopContinuity(player);
		}
		if (this.isBoundman() && player instanceof PlayerEntity) {
			((PlayerEntity) player).abilities.mayfly = true;
		}
		if (!TrueGomuHelper.hasGearFifthActive(AbilityDataCapability.get(player))) {
			HakiDataCapability.get(player).alterHakiOveruse(5);
		}
		if (player.isOnGround() && !(TrueGomuHelper.hasGearFifthActive(AbilityDataCapability.get(player)) || this.isSnakeman())) {
			player.push(0, 1, 0);
			player.level.playSound(null, player.blockPosition(), ModSounds.BOUNCE_2.get(), SoundCategory.PLAYERS, 1, 0.5f);
		}
	}

	private void stopTargetedTime(LivingEntity player) {
		if (this.onTargetedTime && this.targetedTime > 0) {
			this.onTargetedTime = false;
			this.isBonusTime = true;
			float time = (float) (EntityStatsCapability.get(player).getDoriki() * .005f);
			if (time >= 500) {
				time = -1;
			}
			this.continuousComponent.startContinuity(player, time * 20);
			try {
				((ServerPlayerEntity) player).getAdvancements().award(((ServerPlayerEntity) player).server.getAdvancements()
						.getAdvancement(new ResourceLocation("hito_hito_no_mi_nika:wait_power")), "use_gear_fourth");
			}
			catch (NullPointerException e) {
				e.printStackTrace();
			}
			WyNetwork.sendTo(new SSyncAbilityDataPacket(player.getId(), AbilityDataCapability.get(player)), (PlayerEntity) player);
		} else this.afterContinuityStopEvent(player);
	}

	protected void beforeContinuityStopEvent(LivingEntity player, IAbility ability) {
		if (player instanceof PlayerEntity) {
			((PlayerEntity) player).abilities.flying = false;
			((PlayerEntity) player).onUpdateAbilities();
		}
		IAbilityData props = AbilityDataCapability.get(player);
		if (this.isBoundman() && player instanceof PlayerEntity) {
			((PlayerEntity) player).abilities.mayfly = false;
		}
		this.statsComponent.removeModifiers(player);
		if (this.targetedTime > 0 && this.continuousComponent.getContinueTime() >= this.continuousComponent.getThresholdTime() && !this.continuousComponent.isInfinite() && !this.isBonusTime && props.hasUnlockedAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
			this.onTargetedTime = true;
			return;
		}
		this.afterContinuityStopEvent(player);
	}

	public void afterContinuityStopEvent(LivingEntity player) {
		int duration = (int) (600.0F - HakiDataCapability.get(player).getTotalHakiExp());
		this.cooldownComponent.startCooldown(player, 1 + (float) duration / 60 + (this.isBonusTime ? this.continuousComponent.getContinueTime() : ((float) Math.round(Math.sqrt(this.continuousComponent.getContinueTime() / 20)) * 20)));
		if ((this.continuousComponent.getContinueTime() > this.continuousComponent.getThresholdTime() / 10 && this.continuousComponent.getThresholdTime() != 0 && !TrueGomuHelper.hasGearFifthActive(AbilityDataCapability.get(player))) || this.isBonusTime) {
			player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, duration, 3, true, true));
			player.addEffect(new EffectInstance(Effects.HUNGER, duration, 1, true, true));
			player.addEffect(new EffectInstance(Effects.WEAKNESS, duration, 3, true, true));
		}
		if (this.isBonusTime) {
			player.addEffect(new EffectInstance(ModEffects.UNCONSCIOUS.get(), duration + 100 + (int) (EntityStatsCapability.get(player).getDoriki() / 200), 1, true, true));
		}
		IAbilityData props = AbilityDataCapability.get(player);
		GomuMorphsAbility morphs = props.getPassiveAbility(GomuMorphsAbility.INSTANCE);
		if (morphs != null)
			morphs.updateModes();
		this.targetedTime = 30;
		this.onTargetedTime = false;
		this.isBonusTime = false;
	}

	public HakiType getType() {
		return HakiType.BUSOSHOKU;
	}

	public boolean isOnTargetedTime() {
		return this.onTargetedTime;
	}

	protected static boolean canUnlock(LivingEntity user) {
		return EntityStatsCapability.get(user).getDoriki() * .005d >= 25d && HakiDataCapability.get(user).getBusoshokuHakiExp() > HakiHelper.getBusoshokuFullBodyExpNeeded(user) && TrueGomuHelper.hasFruit(user, new ResourceLocation("mineminenomi", "gomu_gomu_no_mi"));
	}

	public Mode getMode() {
		return this.mode.getCurrentMode();
	}

	public void setExtraData(CompoundNBT tag) {
		this.isBonusTime = tag.getBoolean("bonus_time");
		this.targetedTime = tag.getInt("timer");
		this.onTargetedTime = tag.getBoolean("target_time");
	}

	public CompoundNBT getExtraData() {
		CompoundNBT out = new CompoundNBT();
		out.putBoolean("bonus_time", this.isBonusTime);
		out.putInt("timer", this.targetedTime);
		out.putBoolean("target_time", this.onTargetedTime);
		return out;
	}

	public boolean isSnakeman() {
		return this.getMode() == Mode.SNAKEMAN;
	}

	public boolean isBoundman() {
		return this.getMode() == Mode.BOUNDMAN;
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("True Gear Fourth", AbilityCategory.DEVIL_FRUITS, TrueGearFourthAbility::new))
				.addDescriptionLine("The user inflates their muscle structure to tremendously increase the power of their attacks and also allows flight\n\n§2Uses Haki§r").setUnlockCheck(TrueGearFourthAbility::canUnlock).build();
		ARMOR_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_ARMOR_UUID, INSTANCE, "Gear Fourth Armor Modifier", 10.0D, Operation.ADDITION);
		STRENGTH_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_STRENGTH_UUID, INSTANCE, "Gear Fourth Attack Damage Modifier", 15.0D, Operation.ADDITION);
		DAMAGE_REDUCTION_MODIFIER = new AbilityAttributeModifier(AttributeHelper.MORPH_DAMAGE_REDUCTION_UUID, INSTANCE, "Gear Fourth Resistance Damage Modifier", 0.35D, Operation.ADDITION);
	}

	public boolean onUserDeath(LivingEntity entity) {
		IDevilFruit awakeningProps = DevilFruitCapability.get(entity);
		if (this.isBonusTime && !awakeningProps.hasAwakenedFruit() && CommonConfig.INSTANCE.hasAwakeningsEnabled() && EntityStatsCapability.get(entity).getDoriki() >= 8334) {
			awakeningProps.setAwakenedFruit(true);
			entity.setHealth(5);
			entity.addEffect(new EffectInstance(GomuReviveEffect.INSTANCE.get(), 600, 1, true, false));
			entity.addEffect(new EffectInstance(Effects.REGENERATION, 600, 4, true, true));
			entity.addEffect(new EffectInstance(ModEffects.UNCONSCIOUS.get(), 600, 1, true, true));
			if (entity instanceof PlayerEntity) {
				AbilityHelper.disableAbilities(entity, 600, (ability -> true));
				WyNetwork.sendTo(new SSyncAbilityDataPacket(entity.getId(), AbilityDataCapability.get(entity)), (PlayerEntity) entity);
			}
			return true;
		}
		return false;
	}

	public enum Mode {
		BOUNDMAN,
		SNAKEMAN;
	}
}
