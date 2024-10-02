package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.fruit_progression.data.entity.awakening.AwakeningDataCapability;
import net.warcar.fruit_progression.data.entity.awakening.IAwakeningData;
import net.warcar.hito_hito_nika.effects.GomuReviveEffect;
import xyz.pixelatedw.mineminenomi.abilities.haki.HaoshokuHakiInfusionAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.enums.HakiType;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.AttributeHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.HakiHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.haki.HakiDataCapability;
import xyz.pixelatedw.mineminenomi.init.*;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncAbilityDataPacket;
import xyz.pixelatedw.mineminenomi.packets.server.ability.SUpdateEquippedAbilityPacket;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;

import java.awt.*;
import java.util.Objects;

public class TrueGearFourthAbility extends StatsChangeAbility implements IHakiAbility, IBodyOverlayAbility, IParallelContinuousAbility, IAbilityModeSwitcher, IAbilityMode, IExtraUpdateData, IDeathAbility {
	public static final AbilityCore<TrueGearFourthAbility> INSTANCE;
	private static final AbilityAttributeModifier ARMOR_MODIFIER;
	private static final AbilityAttributeModifier STRENGTH_MODIFIER;
	private static final AbilityAttributeModifier DAMAGE_REDUCTION_MODIFIER;
	private static final AbilityOverlay OVERLAY;
	private static final AbilityOverlay OVERLAY_SNAKEMAN;
	protected Mode mode = Mode.BOUNDMAN;
	public float speed = 0.0F;
	private double currentHP = 0.0D;
	protected int targetedTime = 30;
	protected boolean onTargetedTime = false;
	protected boolean isBonusTime = false;
	protected boolean g5 = false;

	public TrueGearFourthAbility(AbilityCore core) {
		super(core);
		this.setCustomIcon("Gear Fourth");
		this.setDisplayName("Gear Fourth: Boundman");
		this.addModifier(Attributes.ARMOR, ARMOR_MODIFIER);
		this.addModifier(Attributes.ARMOR_TOUGHNESS, ARMOR_MODIFIER);
		this.addModifier(ModAttributes.PUNCH_DAMAGE, STRENGTH_MODIFIER);
		this.addModifier(ModAttributes.DAMAGE_REDUCTION, DAMAGE_REDUCTION_MODIFIER);
		this.needsClientSide();
		this.onStartContinuityEvent = this::onStartContinuityEvent;
		this.duringContinuityEvent = this::duringContinuity;
		this.afterContinuityStopEvent = this::afterContinuityStopEvent;
		this.beforeContinuityStopEvent = this::beforeContinuityStopEvent;
		this.onEndCooldownEvent = this::onEndCooldownEvent;
	}

	public boolean onStartContinuityEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		if (player.isCrouching()) {
			if (this.g5) {
				this.mode = Mode.BOUNDMAN;
				this.setDisplayName("Muscles");
			}
			else if (this.mode == Mode.BOUNDMAN) {
				this.mode = Mode.SNAKEMAN;
				this.setDisplayName("Gear Fourth: Snakeman");
			} else {
				this.mode = Mode.BOUNDMAN;
				this.setDisplayName("Gear Fourth: Boundman");
			}
			player.sendMessage(new StringTextComponent("Changed Mode to " + this.mode), Util.NIL_UUID);
			WyNetwork.sendTo(new SSyncAbilityDataPacket(player.getId(), props), player);
			return false;
		} else if (!TrueGomuHelper.canActivateGear(props, INSTANCE)) {
			player.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_GEAR_ACTIVE), Util.NIL_UUID);
			return false;
		} else if (!canUnlock(player)) {
			player.sendMessage(new TranslationTextComponent("text.mineminenomi.too_weak"), Util.NIL_UUID);
			return false;
		} else if (TrueGomuHelper.hasGearFifthActive(props) && this.isSnakeman()) {
			player.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_GEAR_ACTIVE), Util.NIL_UUID);
			return false;
		} else {
			this.onStartContinuity(player);
			this.enableModes(player, this);
			((GomuMorphsAbility) props.getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
			this.currentHP = player.getHealth();
			return true;
		}
	}

	private void onStartContinuity(PlayerEntity player) {
		double time = EntityStatsCapability.get(player).getDoriki() * .005d;
		if (time >= 500) {
			this.setThreshold(0f);
		} else {
			this.setThreshold(time);
		}
	}

	public void duringContinuity(PlayerEntity player, int passiveTimer) {
		int diff;
		if (passiveTimer % 2 == 0 && !this.isSnakeman()) {
			WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), player, player.getX(), player.getY() + 1.0D, player.getZ());
		}
		boolean isOnMaxOveruse = HakiHelper.checkForHakiOveruse(player, 2);
		if (isOnMaxOveruse || AbilityHelper.isNearbyKairoseki(player)) {
			this.tryStoppingContinuity(player);
		}
		if (this.onTargetedTime) {
			for (int i = 0; i < 3; i++) {
				WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), player, player.getX(), player.getY() + 1.0D, player.getZ());
			}
			--this.targetedTime;
		}
		if (this.targetedTime <= 0) {
			this.tryStoppingContinuity(player);
		}
		if (this.isBoundman()) {
			player.abilities.mayfly = true;
		}
		if (!TrueGomuHelper.hasGearFifthActive(AbilityDataCapability.get(player))) {
			HakiDataCapability.get(player).alterHakiOveruse(5);
		}
		if (player.isOnGround() && !(TrueGomuHelper.hasGearFifthActive(AbilityDataCapability.get(player)) || this.isSnakeman())) {
			player.push(0, 1, 0);
		}
		if (this.currentHP > (double) player.getHealth() + WyHelper.randomWithRange(3, 6)) {
			diff = Math.abs((int) ((double) player.getHealth() - this.currentHP));
			this.setThresholdInTicks(Math.max((int) ((float) this.threshold * (1.0F - (float) diff / 20.0F)), 0));
			IAbilityData props = AbilityDataCapability.get(player);
			WyNetwork.sendTo(new SSyncAbilityDataPacket(player.getId(), props), player);
			WyNetwork.sendToAllTrackingAndSelf(new SUpdateEquippedAbilityPacket(player, this), player);
		}
		this.currentHP = player.getHealth();
	}

	protected boolean beforeContinuityStopEvent(PlayerEntity player) {
		player.abilities.flying = false;
		IAbilityData props = AbilityDataCapability.get(player);
		if (this.isBoundman()) {
			player.abilities.mayfly = false;
		}
		player.onUpdateAbilities();
		if (this.targetedTime > 0 && this.continueTime >= this.getThreshold() && this.getThreshold() > 0 && !this.isBonusTime && props.hasUnlockedAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
			this.setThreshold(0d);
			this.onTargetedTime = true;
			WyNetwork.sendToAllTrackingAndSelf(new SUpdateEquippedAbilityPacket(player, this), player);
			return false;
		}
		if (this.onTargetedTime && this.targetedTime > 0) {
			this.onTargetedTime = false;
			this.isBonusTime = true;
			this.continueTime = 0;
			this.onStartContinuity(player);
			try {
				((ServerPlayerEntity) player).getAdvancements().award(((ServerPlayerEntity) player).server.getAdvancements()
						.getAdvancement(new ResourceLocation("hito_hito_no_mi_nika:wait_power")), "use_gear_fourth");
			}
			catch (NullPointerException e) {
				e.printStackTrace();
			}
			WyNetwork.sendToAllTrackingAndSelf(new SUpdateEquippedAbilityPacket(player, this), player);
			WyNetwork.sendTo(new SSyncAbilityDataPacket(player.getId(), AbilityDataCapability.get(player)), player);
			return false;
		}
		return true;
	}

	public void afterContinuityStopEvent(PlayerEntity player) {
		int duration = (int) (600.0F - HakiDataCapability.get(player).getTotalHakiExp());
		if ((this.continueTime > this.getThreshold() / 10 && this.getThreshold() != 0 && !TrueGomuHelper.hasGearFifthActive(AbilityDataCapability.get(player))) || this.isBonusTime) {
			player.addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, duration, 3, true, true));
			player.addEffect(new EffectInstance(Effects.HUNGER, duration, 1, true, true));
			player.addEffect(new EffectInstance(Effects.WEAKNESS, duration, 3, true, true));
			if (!this.isBonusTime)
				AbilityHelper.disableAbilities(player, duration, (ability) -> ability instanceof IHakiAbility);
		}
		if (this.isBonusTime) {
			AbilityHelper.disableAbilities(player, duration + 100, Objects::nonNull);
			player.addEffect(new EffectInstance(ModEffects.UNCONSCIOUS.get(), duration + 100, 1, true, true));
		}
		this.disableModes(player, this);
		IAbilityData props = AbilityDataCapability.get(player);
		((GomuMorphsAbility) props.getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
		int cooldown = duration / 60 + (int) Math.round(Math.sqrt(this.continueTime));
		this.setMaxCooldown(1 + cooldown);
	}

	public void onEndCooldownEvent(PlayerEntity player) {
		this.targetedTime = 30;
		this.onTargetedTime = false;
		this.isBonusTime = false;
	}

	public AbilityOverlay getBodyOverlay(LivingEntity entity) {
		IAbilityData props = AbilityDataCapability.get(entity);
		if (TrueGomuHelper.hasGearFifthActive(props)) {
			return new AbilityOverlay.Builder().setColor(new Color(0, 0, 0, 0)).build();
		} else if (this.isSnakeman()) {
			return OVERLAY_SNAKEMAN;
		}
		return OVERLAY;
	}

	public HakiType getType() {
		return HakiType.BUSOSHOKU;
	}

	protected static boolean canUnlock(LivingEntity user) {
		return EntityStatsCapability.get(user).getDoriki() * .005d >= 25d && HakiDataCapability.get(user).getBusoshokuHakiExp() > HakiHelper.getBusoshokuFullBodyExpNeeded(user);
	}

	public AbilityCore<?>[] getParents() {
		return new AbilityCore[]{FifthGearAbility.INSTANCE};
	}

	public void disableMode(Ability ignored) {
		this.setCustomIcon("Gear Fourth");
		this.setDisplayName("Gear Fourth: Boundman");
	}

	public void enableMode(Ability ignored) {
		this.setCustomIcon("G4 Muscles");
		this.setDisplayName("Muscles");
	}

	public void setMode(Mode newMode) {
		this.mode = newMode;
	}

	public Mode getMode() {
		return this.mode;
	}

	public void setExtraData(CompoundNBT tag) {
		this.setMode(Mode.getFromString(tag.getString("g4_mode")));
		this.isBonusTime = tag.getBoolean("bonus_time");
		this.onTargetedTime = tag.getBoolean("target_time");
	}

	public CompoundNBT getExtraData() {
		CompoundNBT out = new CompoundNBT();
		out.putString("g4_mode", "" + this.mode);
		out.putBoolean("bonus_time", this.isBonusTime);
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
		OVERLAY = (new AbilityOverlay.Builder()).setTexture(ModResources.G4_OVERLAY).build();
		OVERLAY_SNAKEMAN = new AbilityOverlay.Builder().setTexture(new ResourceLocation("hito_hito_no_mi_nika", "textures/models/gear_4_snakeman_overlay.png")).build();
	}

	public boolean onUserDeath(LivingEntity entity, DamageSource damageSource) {
		IAwakeningData awakeningProps = AwakeningDataCapability.get(entity);
		if (this.isBonusTime && !awakeningProps.isAwakened() && EntityStatsCapability.get(entity).getDoriki() >= 8334) {
			awakeningProps.setAwake(true);
			entity.setHealth(5);
			entity.addEffect(new EffectInstance(GomuReviveEffect.INSTANCE.get(), 600, 1, true, false));
			entity.addEffect(new EffectInstance(Effects.REGENERATION, 600, 4, true, true));
			entity.addEffect(new EffectInstance(ModEffects.UNCONSCIOUS.get(), 600, 1, true, true));
			if (entity instanceof PlayerEntity) {
				AbilityHelper.validateDevilFruitMoves((PlayerEntity) entity);
				AbilityHelper.disableAbilities((PlayerEntity) entity, 600, (ability -> true));
				WyNetwork.sendTo(new SSyncAbilityDataPacket(entity.getId(), AbilityDataCapability.get(entity)), (PlayerEntity) entity);
			}
			return true;
		}
		return false;
	}

	public enum Mode {
		BOUNDMAN,
		SNAKEMAN;

		public static Mode getFromString(String s) {
			for (Mode mode : Mode.values()) {
				if (s.equalsIgnoreCase(mode.name())) return mode;
			}
			throw new IllegalArgumentException(s + " not present in Gear Fourth Modes");
		}
	}
}
