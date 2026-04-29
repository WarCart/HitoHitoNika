package net.warcar.hito_hito_nika.abilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.helpers.EquationHelper;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.GomuEffects;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.abilities.haki.HakiHelper;
import xyz.pixelatedw.mineminenomi.abilities.haki.HaoshokuHakiInfusionAbility;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AltModeComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChangeStatsComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.enums.HakiType;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.AttributeHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.data.entity.haki.HakiCapability;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.init.*;
import xyz.pixelatedw.mineminenomi.init.i18n.ModI18nAbilities;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncAbilityDataPacket;
import xyz.pixelatedw.mineminenomi.packets.server.ability.SUpdateEquippedAbilityPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import static xyz.pixelatedw.mineminenomi.config.ServerConfig.hasAwakeningsEnabled;

public class TrueGearFourthAbility extends Ability {
	private static final Component[] DESCRIPTION = TrueGomuHelper.registerDescriptionText(HitoHitoNoMiNikaMod.MOD_ID, "gear_fourth",
			ImmutablePair.of("The user inflates their muscle structure to tremendously increase the power of their attacks and also allows flight", null),
			ImmutablePair.of("§2Uses Haki§r", null));
	public static final RegistryObject<AbilityCore<TrueGearFourthAbility>> INSTANCE = TrueGomuGomuNoMi.registerAbility(new AbilityCore.Builder<>("gear_fourth", "Gear Fourth", AbilityCategory.DEVIL_FRUITS, TrueGearFourthAbility::new)
			.addDescriptionLine(DESCRIPTION).addAdvancedDescriptionLine(AbilityDescriptionLine.NEW_LINE, ChangeStatsComponent.getTooltip())
			.setUnlockCheck(TrueGearFourthAbility::canUnlock));
	private final AltModeComponent<Mode> modeComponent;
	private final ContinuousComponent continuousComponent;
	private final ChangeStatsComponent statsComponent;
	protected int targetedTime = 30;
	protected boolean onTargetedTime = false;
	protected boolean isBonusTime = false;

	public TrueGearFourthAbility(AbilityCore<TrueGearFourthAbility> core) {
		super(core);
		this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "gear_fourth"));
		statsComponent = new ChangeStatsComponent(this);
		modeComponent = new AltModeComponent<>(this, Mode.class, Mode.BOUNDMAN);
		modeComponent.addChangeModeEvent(this::changeMode);
		continuousComponent = new ContinuousComponent(this, true);
		this.addComponents(continuousComponent, statsComponent, modeComponent);
		this.statsComponent.addAttributeModifier(Attributes.ARMOR, new AbilityAttributeModifier(AttributeHelper.MORPH_ARMOR_UUID, INSTANCE, "Gear Fourth Armor Modifier", 10.0D, AttributeModifier.Operation.ADDITION),
				(e) -> this.continuousComponent.isContinuous() && (this.isBoundman() || this.isTankman()));
		this.statsComponent.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, new AbilityAttributeModifier(AttributeHelper.MORPH_ARMOR_TOUGHNESS_UUID, INSTANCE, "Gear Fourth Armor Toughness Modifier", 3.0D, AttributeModifier.Operation.ADDITION),
				(e) -> this.continuousComponent.isContinuous() && (this.isBoundman() || this.isTankman()));
		this.statsComponent.addAttributeModifier(ModAttributes.PUNCH_DAMAGE, new AbilityAttributeModifier(AttributeHelper.MORPH_STRENGTH_UUID, INSTANCE, "Gear Fourth Attack Damage Modifier", 5.0D, AttributeModifier.Operation.ADDITION),
				(e) -> this.continuousComponent.isContinuous() && (this.isBoundman() || this.isTankman()));
		//this.statsComponent.addAttributeModifier(ModAttributes.DAMAGE_REDUCTION, new AbilityAttributeModifier(AttributeHelper.MORPH_DAMAGE_REDUCTION_UUID, INSTANCE, "Gear Fourth Resistance Damage Modifier", 0.35D, AttributeModifier.Operation.ADDITION),
		//		(e) -> this.continuousComponent.isContinuous() && !this.isPartial());
		this.statsComponent.addAttributeModifier(Attributes.MOVEMENT_SPEED, new AbilityAttributeModifier(AttributeHelper.MORPH_MOVEMENT_SPEED_UUID, INSTANCE, "Gear Fourth Resistance Damage Modifier", 0.5D, AttributeModifier.Operation.MULTIPLY_TOTAL),
				(e) -> this.continuousComponent.isContinuous() && this.isSnakeman());
		this.continuousComponent.addTickEvent(this::duringContinuity);
		this.continuousComponent.addEndEvent(this::beforeContinuityStopEvent);
		this.continuousComponent.addStartEvent(this::afterStart);
		continuousComponent.addStartEvent(TrueGomuHelper.basicGearStuff());
		continuousComponent.addTickEvent(this::duringContinuous);
		this.addUseEvent(this::onStartContinuity);
		this.addTickEvent(this::onTick);
		this.getComponent(ModAbilityComponents.SLOT_DECORATION.get()).ifPresent(component -> component.addPreRenderEvent(30, ((livingEntity, minecraft, matrixStack, bufferSource, ui, x, y, partialTicks) ->  {
			if (this.isOnTargetedTime()) {
				component.setSlotColor(0, 1, 0);
				component.setMaxValue(1);
				component.setCurrentValue(1);
				component.setDisplayText(" ");
			}
		})));
	}

	private void duringContinuous(LivingEntity entity, IAbility ability) {
		if (this.isSnakeman()) {
			TrueGomuHelper.getSpeedEvent(1f).duringContinuous(entity, ability);
		}
	}

	private void onTick(LivingEntity player, IAbility ability) {
		if (TrueGomuHelper.hasGearFifthActive(AbilityCapability.get(player).get())) {
			this.setDisplayIcon(TrueGomuHelper.getIcon("G4 Muscles"));
		} else {
			this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "gear_fourth"));
		}
		if (this.onTargetedTime) {
			if (this.targetedTime <= 0) {
				this.afterContinuityStopEvent(player);
				return;
			}
			for (int i = 0; i < 3; i++) {
				WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), player, player.getX(), player.getY() + 1.0D, player.getZ());
			}
			--this.targetedTime;
		}
	}

	private void afterStart(LivingEntity entity, IAbility ability) {
		GomuMorphsAbility morphs = AbilityCapability.get(entity).get().getPassiveAbility(GomuMorphsAbility.INSTANCE.get());
		if (morphs != null) morphs.updateModes(entity);
	}

	private void onStartContinuity(LivingEntity player, IAbility ability) {
		if (!this.modeComponent.getCurrentMode().canUnlock.test(player)) {
			this.modeComponent.revertToDefault(player);
		}
		IAbilityData props = AbilityCapability.get(player).get();
		float time = (float) EquationHelper.parseEquation(net.warcar.hito_hito_nika.config.CommonConfig.INSTANCE.getG4Length(), player, new HashMap<>()).getValue();
		if (time >= 500) {
			time = -1;
		} if (!TrueGomuHelper.canActivateGear(props, this)) {
			player.sendSystemMessage(ModI18nAbilities.MESSAGE_GEAR_ACTIVE);
		} else {
			if (this.continuousComponent.isContinuous()) {
				this.continuousComponent.stopContinuity(player);
			} else if (this.onTargetedTime) {
				this.stopTargetedTime(player);
			} else {
				if (this.isSnakeman()) {
					time /= 1.2f;
				}
				this.continuousComponent.startContinuity(player, time * 20);
			}
		}
	}

	public void duringContinuity(LivingEntity player, IAbility ability) {
		float passiveTimer = this.continuousComponent.getContinueTime();
		if (this.isTankman()) {
			player.addEffect(new MobEffectInstance(ModEffects.MOVEMENT_BLOCKED.get(), 2, 1, false, false));
		}
		if (passiveTimer % 2 == 0 && this.isBoundman()) {
			WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), player, player.getX(), player.getY() + 1.0D, player.getZ());
		}
		if (HakiHelper.checkForHakiOveruse(player, 2)) {
			this.continuousComponent.stopContinuity(player);
		}
		if (this.isBoundman() && player instanceof Player) {
			((Player) player).getAbilities().mayfly = true;
		}
		if (!TrueGomuHelper.hasGearFifthActive(AbilityCapability.get(player).get())) {
			HakiCapability.get(player).get().alterHakiOveruse(5);
		}
		if (player.onGround() && !TrueGomuHelper.hasGearFifthActive(AbilityCapability.get(player).get()) && this.isBoundman()) {
			player.push(0, 1, 0);
			player.level().playSound(null, player.blockPosition(), ModSounds.BOUNCE_2.get(), SoundSource.PLAYERS, 1, 0.5f);
		}
	}

	private void stopTargetedTime(LivingEntity player) {
		if (this.onTargetedTime && this.targetedTime > 0) {
			this.onTargetedTime = false;
			this.isBonusTime = true;
			float time = (float) (EntityStatsCapability.get(player).get().getDoriki() * .005f);
			if (time >= 500) {
				time = -1;
			}
			this.continuousComponent.startContinuity(player, time * 20);
			TrueGomuHelper.unlockAdvancement((ServerPlayer) player, "wait_power", "use_gear_fourth");
			ModNetwork.sendTo(new SSyncAbilityDataPacket(player, AbilityCapability.get(player).get()), (Player) player);
		} else this.afterContinuityStopEvent(player);
	}

	protected void beforeContinuityStopEvent(LivingEntity player, IAbility ability) {
		if (player.level().isClientSide()) {
			//return;
		}
		if (this.isBoundman() && player instanceof Player) {
			((Player) player).getAbilities().mayfly = ((Player) player).isCreative() || player.isSpectator();
		}
		if (player instanceof Player) {
			((Player) player).getAbilities().flying = false;
			((Player) player).onUpdateAbilities();
		}
		IAbilityData props = AbilityCapability.get(player).get();
		GomuMorphsAbility morphs = props.getPassiveAbility(GomuMorphsAbility.INSTANCE.get());
		if (morphs != null)
			morphs.updateModes(player);
		if (this.targetedTime > 0 && this.continuousComponent.getContinueTime() >= this.continuousComponent.getThresholdTime() && !this.continuousComponent.isInfinite() && !this.isBonusTime && props.hasUnlockedAbility(HaoshokuHakiInfusionAbility.INSTANCE.get()) && this.isBoundman()) {
			this.onTargetedTime = true;
			ModNetwork.sendToAllTrackingAndSelf(new SUpdateEquippedAbilityPacket(player, this), player);
			return;
		}
		this.afterContinuityStopEvent(player);
	}

	public void afterContinuityStopEvent(LivingEntity player) {
		Map<String, Double> bonus = TrueGomuHelper.getBasicBonusData(this.continuousComponent.getContinueTime());
		double duration = EquationHelper.parseEquation(net.warcar.hito_hito_nika.config.CommonConfig.INSTANCE.getG4EffectsLength(), player, bonus).getValue();
		double cooldown;
		bonus.put("effect", duration);
		if (this.isBonusTime) {
			cooldown = EquationHelper.parseEquation(net.warcar.hito_hito_nika.config.CommonConfig.INSTANCE.getG4PostBonusCooldown(), player, bonus).getValue();
		} else {
            cooldown = EquationHelper.parseEquation(net.warcar.hito_hito_nika.config.CommonConfig.INSTANCE.getG4Cooldown(), player, bonus).getValue();
        }
		this.cooldownComponent.startCooldown(player, (float) cooldown);
		if ((this.continuousComponent.getContinueTime() > this.continuousComponent.getThresholdTime() / 10 && this.continuousComponent.getThresholdTime() != 0 && !TrueGomuHelper.hasGearFifthActive(AbilityCapability.get(player).get())) || this.isBonusTime) {
			player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (int) duration, 3, true, true));
			player.addEffect(new MobEffectInstance(MobEffects.HUNGER, (int) duration, 1, true, true));
			player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, (int) duration, 3, true, true));
		}
		if (this.isBonusTime) {
			player.addEffect(new MobEffectInstance(ModEffects.UNCONSCIOUS.get(), (int) duration + 100, 1, true, true));
		}
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
		return EntityStatsCapability.get(user).get().getDoriki() >= 5000d && HakiCapability.get(user).get().getBusoshokuHakiExp() > HakiHelper.getBusoshokuFullBodyExpNeeded(user) && DevilFruitCapability.get(user).get().hasDevilFruit(TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA);
	}

	public Mode getModeComponent() {
		return this.modeComponent.getCurrentMode();
	}


	public void loadAdditional(CompoundTag tag) {
		this.isBonusTime = tag.getBoolean("bonus_time");
		this.targetedTime = tag.getInt("timer");
		this.onTargetedTime = tag.getBoolean("target_time");
	}

	public void saveAdditional(CompoundTag out) {
		out.putBoolean("bonus_time", this.isBonusTime);
		out.putInt("timer", this.targetedTime);
		out.putBoolean("target_time", this.onTargetedTime);
	}

	public boolean isSnakeman() {
		return this.getModeComponent() == Mode.SNAKEMAN;
	}

	public boolean isBoundman() {
		return this.getModeComponent() == Mode.BOUNDMAN;
	}

	public boolean isTankman() {
		return this.getModeComponent() == Mode.TANKMAN;
	}

	public boolean isPartial() {
		return this.getModeComponent() == Mode.PARTIAL;
	}

    public void stopContinuity(LivingEntity user) {
		this.continuousComponent.stopContinuity(user);
	}

	public boolean onUserDeath(LivingEntity entity) {
		IDevilFruit awakeningProps = DevilFruitCapability.get(entity).get();
		if (this.isBonusTime && !awakeningProps.hasAwakenedFruit() && hasAwakeningsEnabled() && EntityStatsCapability.get(entity).get().getDoriki() >= 8300) {
			awakeningProps.setAwakenedFruit(true);
			entity.setHealth(5);
			entity.addEffect(new MobEffectInstance(GomuEffects.GOMU_REVIVE.get(), 600, 1, true, false));
			entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 4, true, true));
			entity.addEffect(new MobEffectInstance(ModEffects.UNCONSCIOUS.get(), 600, 1, true, true));
			if (entity instanceof Player) {
				AbilityHelper.disableAbilities(entity, 600, (ability -> true));
				ModNetwork.sendTo(new SSyncAbilityDataPacket(entity, AbilityCapability.get(entity).get()), (Player) entity);
			}
			return true;
		}
		return false;
	}

	private boolean changeMode(LivingEntity livingEntity, IAbility iAbility, Mode mode) {
		if (mode != this.modeComponent.getCurrentMode() && !mode.canUnlock.test(livingEntity)) {
			this.modeComponent.setMode(livingEntity, mode.next());
			return false;
		}
		return true;
	}

	public void setSnakeman(LivingEntity entity) {
		this.modeComponent.setMode(entity, Mode.SNAKEMAN);
	}

	public void setBoundman(LivingEntity entity) {
		this.modeComponent.setMode(entity, Mode.BOUNDMAN);
	}

	public void setTankman(LivingEntity entity) {
		this.modeComponent.setMode(entity, Mode.TANKMAN);
	}

	public void setPartial(LivingEntity entity) {
		this.modeComponent.setMode(entity, Mode.PARTIAL);
	}

	public enum Mode {
		BOUNDMAN,
		SNAKEMAN,
		TANKMAN(Mode::hasFusen),
		PARTIAL(e -> DevilFruitCapability.get(e).get().hasAwakenedFruit() && !hasFusen(e));

        private final Predicate<LivingEntity> canUnlock;

        Mode() {
			this(e -> !hasFusen(e));
		}

		Mode(Predicate<LivingEntity> canUnlock) {
            this.canUnlock = canUnlock;
        }

		private static boolean hasFusen(LivingEntity e) {
			return TrueGomuHelper.hasAbilityActive(AbilityCapability.get(e).get(), GomuFusenAbility.INSTANCE);
		}

		public Mode next() {
			return values()[(this.ordinal() + 1) % values().length];
		}
	}
}
