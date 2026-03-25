package net.warcar.hito_hito_nika.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import net.warcar.hito_hito_nika.projectiles.hand.TrueGomuRocketProjectile;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.DealDamageComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.HitTrackerComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ProjectileComponent;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.init.ModEntityPredicates;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;

import java.util.List;

public class TrueGomuRocket extends Ability {

	private static final Component[] DESCRIPTION = TrueGomuHelper.registerDescriptionText("gomu_gomu_no_rocket", ImmutablePair.of("Stretches towards a block, then launches the user on an arch depending on where they fist landed.", null),
			ImmutablePair.of("Slamming into enemies will deal damage", null));
	public static final RegistryObject<AbilityCore<TrueGomuRocket>> INSTANCE = TrueGomuGomuNoMi.registerAbility(new AbilityCore.Builder<>("gomu_gomu_no_rocket", "Gomu Gomu no Rocket", AbilityCategory.DEVIL_FRUITS, TrueGomuRocket::new)
			.addDescriptionLine(DESCRIPTION).setSourceHakiNature(SourceHakiNature.HARDENING).setSourceType(SourceType.PHYSICAL)
			.addAdvancedDescriptionLine(DealDamageComponent.getTooltip(2, 100)));
	public static final Component JET_GIANT_SHELL = TrueGomuHelper.getName("Gomu Gomu no Jet Giant Shell");
	public static final Component DAWN_ROCKET = TrueGomuHelper.getName("Gomu Gomu no Dawn Rocket");
	public static final Component GIANT_SHELL = TrueGomuHelper.getName("Gomu Gomu no Giant Shell");
	public static final Component JET_MISSILE = TrueGomuHelper.getName("Gomu Gomu no Jet Missile");
	public static final Component ROCKET = TrueGomuHelper.getName("Gomu Gomu no Rocket");
	protected boolean isFlying = false;
	protected boolean readyToFly = false;
	private float cooldown;
	private final ContinuousComponent continuousComponent;
	private final ProjectileComponent projectileComponent;
	private final HitTrackerComponent trackerComponent;
	private final DealDamageComponent dealDamageComponent;

	public TrueGomuRocket(AbilityCore<TrueGomuRocket> core) {
		super(core);
		this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Rocket"));
		this.continuousComponent = new ContinuousComponent(this);
		this.addTickEvent(this::updateModes);
		this.addUseEvent(this::onStartContinuityEvent);
		this.continuousComponent.addTickEvent(this::duringContinuityEvent);
		this.continuousComponent.addEndEvent(this::beforeContinuityStopEvent);
		this.projectileComponent = new ProjectileComponent(this, this::createProj);
		this.trackerComponent = new HitTrackerComponent(this);
		this.dealDamageComponent = new DealDamageComponent(this);
		this.addComponents(continuousComponent, projectileComponent, trackerComponent, dealDamageComponent);
	}

	private TrueGomuRocketProjectile createProj(LivingEntity entity) {
		return new TrueGomuRocketProjectile(entity.level(), entity, this);
	}

	private void onStartContinuityEvent(LivingEntity player, IAbility abl) {
		if (this.continuousComponent.isContinuous()) {
			this.continuousComponent.stopContinuity(player);
			return;
		}
		IAbilityData props = AbilityCapability.get(player).get();
		if (TrueGomuHelper.hasGigantActive(props) || (TrueGomuHelper.hasGearFourthActive(props) && !TrueGomuHelper.hasPartialGearFourthActive(props))) {
			player.sendSystemMessage(TrueGomuHelper.TOO_HEAVY);
			return;
		}
		this.continuousComponent.startContinuity(player, -1);
		this.projectileComponent.shoot(player, 3, 0);
		player.level().playSound(null, player.blockPosition(), ModSounds.GOMU_SFX.get(), SoundSource.PLAYERS, 0.5F, 1.0F);
	}

	private void duringContinuityEvent(LivingEntity player, IAbility abl) {
		IAbilityData props = AbilityCapability.get(player).get();
		if (this.readyToFly && player.isFallFlying()) {
			this.readyToFly = false;
			this.isFlying = true;
		}
		if (this.isFlying) {
			List<LivingEntity> targets = WyHelper.getNearbyLiving(player.position(), player.level(), player.getBbWidth() * 4, player.getBbHeight() * 4, player.getBbWidth() * 4, ModEntityPredicates.getEnemyFactions(player));
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
				if (this.trackerComponent.canHit(target) && dealDamageComponent.hurtTarget(player, target, damage)) {
					target.push(player.getDeltaMovement().x(), player.getDeltaMovement().y(), player.getDeltaMovement().z());
				}
			});
		}
		if (this.continuousComponent.getContinueTime() > 24 && !player.isFallFlying()) {
			this.continuousComponent.stopContinuity(player);
		}
	}

	private void beforeContinuityStopEvent(LivingEntity entity, IAbility ability) {
		this.isFlying = false;
		this.readyToFly = false;
		this.cooldownComponent.startCooldown(entity, this.cooldown);
		this.trackerComponent.clearHits();
		AbilityCapability.get(entity).get().getPassiveAbility(GomuMorphsAbility.INSTANCE.get()).updateModes(entity);
	}

	public void setFlying() {
		this.readyToFly = true;
	}

	protected void updateModes(LivingEntity entity, IAbility abl) {
		IAbilityData props = AbilityCapability.get(entity).get();
		this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Rocket"));
		if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
			this.setMaxCooldownNew(0D);
		} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
			this.setMaxCooldownNew(3.0D);
			this.setDisplayName(JET_GIANT_SHELL);
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

}
