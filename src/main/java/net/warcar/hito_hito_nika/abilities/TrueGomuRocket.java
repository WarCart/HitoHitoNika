package net.warcar.hito_hito_nika.abilities;

import net.warcar.hito_hito_nika.projectiles.hand.TrueGomuRocketProjectile;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.HitTrackerComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ProjectileComponent;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.init.ModEntityPredicates;
import xyz.pixelatedw.mineminenomi.init.ModDamageSource;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.Util;
import net.minecraft.util.SoundCategory;
import net.minecraft.entity.LivingEntity;

import java.util.List;

public class TrueGomuRocket extends Ability {
	public static final AbilityCore<TrueGomuRocket> INSTANCE;
	public static final TranslationTextComponent JetGiantShell = TrueGomuHelper.getName("Gomu Gomu no Jet Giant Shell");
	public static final TranslationTextComponent DAWN_ROCKET = TrueGomuHelper.getName("Gomu Gomu no Dawn Rocket");
	public static final TranslationTextComponent GIANT_SHELL = TrueGomuHelper.getName("Gomu Gomu no Giant Shell");
	public static final TranslationTextComponent JET_MISSILE = TrueGomuHelper.getName("Gomu Gomu no Jet Missile");
	public static final TranslationTextComponent ROCKET = TrueGomuHelper.getName("Gomu Gomu no Rocket");
	protected boolean isFlying = false;
	protected boolean readyToFly = false;
	private float cooldown;
	private final ContinuousComponent continuousComponent;
	private final ProjectileComponent projectileComponent;
	private final HitTrackerComponent trackerComponent;

	public TrueGomuRocket(AbilityCore core) {
		super(core);
		this.isNew = true;
		this.continuousComponent = new ContinuousComponent(this);
		this.addTickEvent(this::updateModes);
		this.addUseEvent(this::onStartContinuityEvent);
		this.continuousComponent.addTickEvent(this::duringContinuityEvent);
		this.continuousComponent.addEndEvent(this::beforeContinuityStopEvent);
		this.projectileComponent = new ProjectileComponent(this, this::createProj);
		this.trackerComponent = new HitTrackerComponent(this);
		this.addComponents(continuousComponent, projectileComponent, trackerComponent);
	}

	private AbilityProjectileEntity createProj(LivingEntity entity) {
		return new TrueGomuRocketProjectile(entity.level, entity, this);
	}

	private void onStartContinuityEvent(LivingEntity player, IAbility abl) {
		if (this.continuousComponent.isContinuous()) {
			this.continuousComponent.stopContinuity(player);
			return;
		}
		IAbilityData props = AbilityDataCapability.get(player);
		if (TrueGomuHelper.hasGigantActive(props) || (TrueGomuHelper.hasGearFourthActive(props) && !TrueGomuHelper.hasPartialGearFourthActive(props))) {
			player.sendMessage(new TranslationTextComponent("text.mineminenomi.too_heavy"), Util.NIL_UUID);
			return;
		}
		this.continuousComponent.startContinuity(player, -1);
		this.projectileComponent.shoot(player, 3, 0);
		player.level.playSound(null, player.blockPosition(), ModSounds.GOMU_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
	}

	private void duringContinuityEvent(LivingEntity player, IAbility abl) {
		IAbilityData props = AbilityDataCapability.get(player);
		if (this.readyToFly && !player.isOnGround()) {
			this.readyToFly = false;
			this.isFlying = true;
		}
		if (this.isFlying) {
			List<LivingEntity> targets = WyHelper.getNearbyLiving(player.position(), player.level, player.getBbWidth() * 4, player.getBbHeight() * 4, player.getBbWidth() * 4, ModEntityPredicates.getEnemyFactions(player));
			targets.removeIf(target -> target == player);
			targets.forEach(target -> {
				float damage = 2;
				if (TrueGomuHelper.hasGearSecondActive(props))
					damage *= 5;
				if (TrueGomuHelper.hasGigantActive(props))
					damage *= 25;
				else if (TrueGomuHelper.hasGearThirdActive(props))
					damage *= 10;
				if (TrueGomuHelper.hasGearFourthBoundmanActive(props))
					damage *= 15;
				if (TrueGomuHelper.hasGearFourthSnakemanActive(props))
					damage *= 7.5f;
				if (TrueGomuHelper.hasGearFifthActive(props))
					damage *= 100;
				if (this.trackerComponent.canHit(target) && target.hurt(ModDamageSource.causeAbilityDamage(player, this), damage)) {
					target.push(player.getDeltaMovement().x(), player.getDeltaMovement().y(), player.getDeltaMovement().z());
				}
			});
		}
		if (this.continuousComponent.getContinueTime() > 24 && player.isOnGround()) {
			this.continuousComponent.stopContinuity(player);
		}
	}

	private void beforeContinuityStopEvent(LivingEntity entity, IAbility ability) {
		this.isFlying = false;
		this.readyToFly = false;
		this.cooldownComponent.startCooldown(entity, this.cooldown);
		this.trackerComponent.clearHits();
		AbilityDataCapability.get(entity).getPassiveAbility(GomuMorphsAbility.INSTANCE).updateModes();
	}

	public void setFlying() {
		this.readyToFly = true;
	}

	protected void updateModes(LivingEntity entity, IAbility abl) {
		IAbilityData props = AbilityDataCapability.get(entity);
		this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Rocket"));
		if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
			this.setMaxCooldownNew(0D);
		} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
			this.setMaxCooldownNew(3.0D);
			this.setDisplayName(JetGiantShell);
		} else if (TrueGomuHelper.hasGearFifthActive(props)) {
			this.setMaxCooldownNew(12.0D);
			this.setDisplayName(DAWN_ROCKET);
		} else if (TrueGomuHelper.hasGearThirdActive(props)) {
			this.setMaxCooldownNew(10.0D);
			this.setDisplayName(GIANT_SHELL);
		} else if (TrueGomuHelper.hasGearSecondActive(props)) {
			this.setMaxCooldownNew(1.0D);
			this.setDisplayName(JET_MISSILE);
		} else if (TrueGomuHelper.hasGearFourthActive(props)) {
			this.setMaxCooldownNew(0D);
		} else {
			this.setMaxCooldownNew(3.0D);
			this.setDisplayName(ROCKET);
		}
	}

	public void setMaxCooldownNew(double cooldown) {
		this.cooldown = (float) cooldown * 20;
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gomu Gomu no Rocket", AbilityCategory.DEVIL_FRUITS, TrueGomuRocket::new)).addDescriptionLine("Stretches towards a block, then launches the user on an arch depending on where they fist landed")
				.setSourceHakiNature(SourceHakiNature.HARDENING).setSourceType(SourceType.PHYSICAL).build();
	}
}
