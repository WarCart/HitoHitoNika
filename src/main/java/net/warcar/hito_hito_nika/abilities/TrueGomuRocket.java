package net.warcar.hito_hito_nika.abilities;

import net.warcar.hito_hito_nika.projectiles.hand.TrueGomuRocketProjectile;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.init.ModEntityPredicates;
import xyz.pixelatedw.mineminenomi.init.ModDamageSource;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbilityMode;
import xyz.pixelatedw.mineminenomi.api.abilities.ContinuousAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.abilities.haki.HaoshokuHakiInfusionAbility;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.Util;
import net.minecraft.util.SoundCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;

import java.util.List;

public class TrueGomuRocket extends ContinuousAbility implements IAbilityMode {
	public static final AbilityCore<TrueGomuRocket> INSTANCE;
	private final GearSet gearSet = new GearSet();
	protected boolean isFlying = false;
	protected boolean isWaiting = false;

	public TrueGomuRocket(AbilityCore core) {
		super(core);
		this.setDisplayName("Gomu Gomu no Rocket");
		this.setCustomIcon("Gomu Gomu no Rocket");
		this.setMaxCooldown(3.0D);
		this.onStartContinuityEvent = this::onStartContinuityEvent;
		this.duringContinuityEvent = this::duringContinuityEvent;
		this.beforeContinuityStopEvent = this::beforeContinuityStopEvent;
	}

	private boolean onStartContinuityEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		if ((TrueGomuHelper.hasGearFifthActive(props) && TrueGomuHelper.hasGearThirdActive(props)) || TrueGomuHelper.hasGearFourthActive(props)) {
			player.sendMessage(new TranslationTextComponent("text.mineminenomi.too_heavy"), Util.NIL_UUID);
			return false;
		}
		return true;
	}

	private void duringContinuityEvent(PlayerEntity player, int i) {
		IAbilityData props = AbilityDataCapability.get(player);
		if (i >= this.getThreshold() && !(this.isFlying || this.isWaiting)) {
			this.tryStoppingContinuity(player);
		}
		if (this.isFlying && this.isWaiting) {
			this.isWaiting = false;
			((GomuMorphsAbility) props.getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
		} else if (this.isFlying && player.isOnGround()) {
			this.tryStoppingContinuity(player);
		} else if (this.isFlying) {
			List<LivingEntity> targets = WyHelper.getNearbyLiving(player.blockPosition(), player.level, player.getBbWidth(), player.getBbHeight(), player.getBbWidth(), ModEntityPredicates.getEnemyFactions(player));
			targets.removeIf(target -> target == player);
			targets.forEach(target -> {
				float damage = 2;
				if (TrueGomuHelper.hasGearSecondActive(props))
					damage *= 5;
				if (TrueGomuHelper.hasGearThirdActive(props))
					damage *= 10;
				if (TrueGomuHelper.hasGearFifthActive(props))
					damage *= 100;
				if (target.hurt(ModDamageSource.causeAbilityDamage(player, this), damage)) {
					target.push(player.getDeltaMovement().x(), player.getDeltaMovement().y(), player.getDeltaMovement().z());
				}
			});
		}
	}

	private boolean beforeContinuityStopEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		if (!this.isFlying) {
			AbilityHelper.slowEntityFall(player, 10);
			TrueGomuRocketProjectile projectile = new TrueGomuRocketProjectile(player.level, player, this);
			player.level.addFreshEntity(projectile);
			projectile.gear2 = TrueGomuHelper.hasGearSecondActive(props);
			float speed = TrueGomuHelper.hasGearSecondActive(props) ? 4.0F : 3.125F;
			projectile.shootFromRotation(player, player.xRot, player.yRot, 0.0F, speed, 0.0F);
			player.level.playSound(null, player.blockPosition(), ModSounds.GOMU_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
			this.setThreshold(0);
			this.isWaiting = true;
			return false;
		} else if (this.isWaiting) {
			return false;
		} else {
			this.isFlying = false;
			this.isWaiting = false;
			((GomuMorphsAbility) props.getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
			return true;
		}
	}

	public AbilityCore<?>[] getParents() {
		return new AbilityCore[]{TrueGearSecondAbility.INSTANCE, TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE, FifthGearAbility.INSTANCE, GearSixthAbility.INSTANCE, HaoshokuHakiInfusionAbility.INSTANCE};
	}

	public void enableMode(Ability parent) {
		if (!this.gearSet.contains(parent)) {
			this.gearSet.add(parent);
		}
		this.updateModes();
	}

	public void disableMode(Ability parent) {
		this.gearSet.remove(parent);
		this.updateModes();
	}

	public void setFlying() {
		this.isFlying = true;
	}

	protected void updateModes() {
		if (gearSet.containsAbility(GearSixthAbility.INSTANCE)) {
			this.setThreshold(0D);
			this.setMaxCooldown(0D);
		} else if (gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
			this.setThreshold(0.0D);
			this.setMaxCooldown(3.0D);
			this.setDisplayName("Gomu Gomu no Jet Gigant Shell");
			this.setCustomIcon("Gomu Gomu no Rocket");
		} else if (gearSet.containsAbility(FifthGearAbility.INSTANCE)) {
			this.setThreshold(0D);
			this.setMaxCooldown(12.0D);
			this.setDisplayName("Gomu Gomu no Dawn Rocket");
			this.setCustomIcon("Gomu Gomu no Rocket");
		} else if (gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
			this.setThreshold(0.0D);
			this.setMaxCooldown(10.0D);
			this.setDisplayName("Gomu Gomu no Gigant Shell");
			this.setCustomIcon("Gomu Gomu no Rocket");
		} else if (gearSet.containsAbility(TrueGearSecondAbility.INSTANCE)) {
			this.setThreshold(0.0D);
			this.setMaxCooldown(1.0D);
			this.setDisplayName("Gomu Gomu no Jet Missile");
			this.setCustomIcon("Gomu Gomu no Rocket");
		} else if (gearSet.containsAbility(TrueGearFourthAbility.INSTANCE)) {
			this.setThreshold(0D);
			this.setMaxCooldown(0D);
		} else {
			this.setMaxCooldown(3.0D);
			this.setThreshold(0.0D);
			this.setDisplayName("Gomu Gomu no Rocket");
			this.setCustomIcon("Gomu Gomu no Rocket");
		}
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gomu Gomu no Rocket", AbilityCategory.DEVIL_FRUITS, TrueGomuRocket::new)).addDescriptionLine("Stretches towards a block, then launches the user on an arch depending on where they fist landed")
				.setSourceHakiNature(SourceHakiNature.HARDENING).setSourceType(SourceType.FIST).build();
	}
}
