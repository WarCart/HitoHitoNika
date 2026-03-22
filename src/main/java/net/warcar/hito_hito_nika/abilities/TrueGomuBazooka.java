package net.warcar.hito_hito_nika.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.GomuAnimations;
import net.warcar.hito_hito_nika.projectiles.hand.*;
import net.warcar.hito_hito_nika.projectiles.leg.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.abilities.haki.HakiHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AnimationComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AnimeScreamComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChargeComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ProjectileComponent;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.init.ModAbilityComponents;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.ModSounds;

public class TrueGomuBazooka extends Ability {
	private static final Component[] DESCRIPTION = AbilityHelper.registerDescriptionText("mineminenomi", "gomu_gomu_no_bazooka", new Pair[]{ImmutablePair.of("Hits the enemy with both hands to launch them away.", (Object)null)});
	public static final AbilityCore<TrueGomuBazooka> INSTANCE = (new AbilityCore.Builder<>("gomu_gomu_no_bazooka", "Gomu Gomu no Bazooka", AbilityCategory.DEVIL_FRUITS, TrueGomuBazooka::new))
			.addDescriptionLine(DESCRIPTION).setSourceHakiNature(SourceHakiNature.HARDENING).setSourceType(SourceType.FIST).build();
	public static final Component JET_GRIZZLY_MAGNUM = TrueGomuHelper.getName("Gomu Gomu no Jet Grizzly Magnum");
	public static final Component JET_GIANT_BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Jet Giant Bazooka");
	public static final Component GIANT_DAWN_BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Giant Dawn Bazooka");
	public static final Component JET_BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Jet Bazooka");
	public static final Component TWIN_JET_CULVERIN = TrueGomuHelper.getName("Gomu Gomu no Twin Jet Culverin");
	public static final Component LEO_REX_BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Leo Rex Bazooka");
	public static final Component LEO_BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Leo Bazooka");
	public static final Component GRIZZLY_MAGNUM = TrueGomuHelper.getName("Gomu Gomu no Grizzly Magnum");
	public static final Component GIANT_BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Giant Bazooka");
	public static final Component DAWN_BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Dawn Bazooka");
	public static final Component DOUBLE_BAJRANG_GUN = TrueGomuHelper.getName("Gomu Gomu no Double Bajrang Gun");
	public static final Component EAGLE_BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Eagle Bazooka");
	public static final Component BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Bazooka");
	public static final Component JET_GIANT_YARI = TrueGomuHelper.getName("Gomu Gomu no Jet Giant Yari");
	public static final Component TWIN_RHINO_STAMPEDE = TrueGomuHelper.getName("Gomu Gomu no Twin Rhino Stampede");
	public static final Component RHINO_REX_SCHNEIDER = TrueGomuHelper.getName("Gomu Gomu no Rhino Rex Schneider");
	public static final Component RHINO_SCHNEIDER = TrueGomuHelper.getName("Gomu Gomu no Rhino Schneider");
	public static final Component GIANT_YARI = TrueGomuHelper.getName("Gomu Gomu no Giant Yari");
	public static final Component JET_LANCE = TrueGomuHelper.getName("Gomu Gomu no Jet Lance");
	public static final Component DAWN_YARI = TrueGomuHelper.getName("Gomu Gomu no Dawn Yari");
	public static final Component DOUBLE_BAJRANG_STAMP = TrueGomuHelper.getName("Gomu Gomu no Double Bajrang Stamp");
	public static final Component YARI = TrueGomuHelper.getName("Gomu Gomu no Yari");
	private static final Component GIANT_DAWN_YARI = TrueGomuHelper.getName("Gomu Gomu no Giant Dawn Yari");
	private final ChargeComponent chargeComponent;
	private final ProjectileComponent projectileComponent;
	private final AnimationComponent animationComponent;
	private final AnimeScreamComponent trueScreamComponent = new AnimeScreamComponent(this) {
		@Override
		public void postInit(IAbility ability) {
			ability.getComponent(ModAbilityComponents.CHARGE.get()).ifPresent(chargeComponent -> {
				chargeComponent.addStartEvent((entity, iAbility) -> this.scream(entity, "Gomu gomu no..."));
				chargeComponent.addEndEvent((entity, iAbility) -> this.scream(entity, ability.getDisplayName().getString().replace("Gomu Gomu no ", "")));
			});
		}
	};
	private float cooldown;
	private int chargeTime;
	private float speed = 2;
	private float spacingMod = 1;

	public TrueGomuBazooka(AbilityCore<TrueGomuBazooka> core) {
		super(core);
		this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Bazooka"));
		this.addTickEvent(this::updateModes);
		this.chargeComponent = (new ChargeComponent(this)).addEndEvent(this::onEndCharging);
		this.projectileComponent = new ProjectileComponent(this, this::createProjectile);
		this.animationComponent = new AnimationComponent(this);
		this.cooldown = 200.0F;
		this.chargeTime = 40;
		this.addComponents(this.chargeComponent, this.projectileComponent, this.animationComponent, this.trueScreamComponent);
		this.addUseEvent(this::onUse);
	}

	private void onUse(LivingEntity entity, IAbility abl) {
		this.chargeComponent.startCharging(entity, this.chargeTime);
		if (!TrueGomuHelper.hasGearFourthBoundmanActive(AbilityCapability.get(entity).get())) {
            this.animationComponent.start(entity, GomuAnimations.BAZOOKA, this.chargeTime);
        }
		entity.addEffect(new MobEffectInstance(ModEffects.MOVEMENT_BLOCKED.get(), this.chargeTime, 0));
	}

	private NuProjectileEntity createProjectile(LivingEntity player) {
		NuProjectileEntity projectile;
		IAbilityData props = AbilityCapability.get(player).get();
		if (EntityStatsCapability.get(player).get().isBlackLeg()) {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				projectile = new BajrangStampGunProjectile(player.level(), player, this);
				spacingMod = 25F;
				speed = 4f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new GigantDawnYariProjectile(player.level(), player, this);
				spacingMod = 1;
				speed = 2F;
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new DawnYariProjectile(player.level(), player, this);
				spacingMod = 1;
				speed = 3.0F;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new RhinoRexSchneiderProjectile(player.level(), player, this);
				speed = 3.0F;
				spacingMod = 4F;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) || TrueGomuHelper.hasPartialGearFourthActive(props)) {
				projectile = new RhinoSchneiderProjectile(player.level(), player, this);
				speed = 3.0F;
				spacingMod = 2.5F;
			} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
				projectile = new JetRhinoSchneiderProjectile(player.level(), player, this, 7f, 100);
				speed = 7F;
				spacingMod = 1.5F;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetGigantYariProjectile(player.level(), player, this);
				speed = 3F;
				spacingMod = 2.5F;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new GigantYariProjectile(player.level(), player, this);
				speed = 1.8F;
				spacingMod = 2.5F;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetLanceProjectile(player.level(), player, this);
				spacingMod = 1;
				speed = 3.0F;
			} else {
				projectile = new YariProjectile(player.level(), player, this);
				spacingMod = 1;
				speed = 2F;
			}
		} else {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				projectile = new BajrangGunProjectile(player.level(), player, this);
				spacingMod = 25F;
				speed = 4f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new GigantDawnBazookaProjectile(player.level(), player, this);
				spacingMod = 1;
				speed = 2F;
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new DawnBazookaProjectile(player.level(), player, this);
				spacingMod = 1;
				speed = 3.0F;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new LeoRexBazookaProjectile(player.level(), player, this);
				speed = 5.0F;
				spacingMod = 2.5F;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) || TrueGomuHelper.hasPartialGearFourthActive(props)) {
				projectile = new TrueLeoBazookaProjectile(player.level(), player, this);
				speed = 3.0F;
				spacingMod = 2.5F;
			} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
				projectile = new JetCulverinProjectile(player.level(), player, this, 7f, 100);
				speed = 7F;
				spacingMod = 1.5F;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetGrizzlyMagnumProjectile(player.level(), player, this);
				speed = 3F;
				spacingMod = 2.5F;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new TrueGrizzlyMagnumProjectile(player.level(), player, this);
				speed = 1.8F;
				spacingMod = 2.5F;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new TrueJetBazookaProjectile(player.level(), player, this);
				speed = 3.0F;
				spacingMod = 1;
			} else {
				projectile = new TrueBazookaProjectile(player.level(), player, this);
				spacingMod = 1;
				speed = 2F;
			}
		}
		return projectile;
	}

	private void onEndCharging(LivingEntity player, IAbility abl) {
		this.animationComponent.stop(player);
		NuProjectileEntity projectile1 = this.projectileComponent.getNewProjectile(player);
		NuProjectileEntity projectile2 = this.projectileComponent.getNewProjectile(player);
		Vec3 dirVec = player.getLookAngle().cross(new Vec3(0, 1, 0)).scale(this.spacingMod);
		projectile1.moveTo(player.getX() + dirVec.x, player.getEyeY(), player.getZ() + dirVec.z, 0.0F, 0.0F);
		projectile2.moveTo(player.getX() - dirVec.x, player.getEyeY(), player.getZ() - dirVec.z, 0.0F, 0.0F);
		this.projectileComponent.shoot(projectile1, player, player.getXRot(), player.getYRot(), this.speed, 0.0F);
		this.projectileComponent.shoot(projectile2, player, player.getXRot(), player.getYRot(), this.speed, 0.0F);
		player.swing(InteractionHand.MAIN_HAND, true);
		player.swing(InteractionHand.OFF_HAND, true);
		player.level().playSound(null, player.blockPosition(), ModSounds.GOMU_SFX.get(), SoundSource.PLAYERS, 2.0F, 0.75F);
		this.cooldownComponent.startCooldown(player, this.cooldown);
	}

	public void setMaxChargeTime(double time) {
		this.chargeTime = (int) (time * 20);
	}

	public void setMaxCooldown(double time) {
		this.cooldown = (int) (time * 20);
	}

	protected void updateModes(LivingEntity entity, IAbility abl) {
		IAbilityData props = AbilityCapability.get(entity).get();
		if (!EntityStatsCapability.get(entity).get().isBlackLeg()) {
			if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setMaxCooldown(10D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(JET_GRIZZLY_MAGNUM);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Bazooka")) ;
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setMaxCooldown(10D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(JET_GIANT_BAZOOKA);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Bazooka"));
			} else if (TrueGomuHelper.hasGearFifthActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setMaxCooldown(20D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(GIANT_DAWN_BAZOOKA);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Bazooka"));
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				this.setMaxCooldown(7D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(JET_BAZOOKA);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Bazooka"));
			} else if (TrueGomuHelper.hasGearFourthActive(props)) {
				TrueGearFourthAbility g4 = AbilityCapability.getEquippedAbility(entity, TrueGearFourthAbility.INSTANCE);
				if (g4.isSnakeman()) {
					this.setMaxCooldown(5D);
					this.setMaxChargeTime(0D);
					this.setDisplayName(TWIN_JET_CULVERIN);
					this.setDisplayIcon(TrueGomuHelper.getIcon("Twin Jet Culverine"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setMaxCooldown(25D);
					this.setMaxChargeTime(5.0D);
					this.setDisplayName(LEO_REX_BAZOOKA);
					this.setDisplayIcon(TrueGomuHelper.getIcon("Double King Kong Gun"));
				} else {
					this.setMaxCooldown(25f);
					this.setMaxChargeTime(2.0D);
					this.setDisplayName(LEO_BAZOOKA);
					this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Bazooka"));
				}
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setMaxCooldown(10);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(GRIZZLY_MAGNUM);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Bazooka"));
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				this.setMaxCooldown(15D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(GIANT_BAZOOKA);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Bazooka"));
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				this.setMaxCooldown(15D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(DAWN_BAZOOKA);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Bazooka"));
			} else if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				this.setMaxCooldown(20D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(DOUBLE_BAJRANG_GUN);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Double King Kong Gun"));
			} else if (HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setMaxCooldown(10D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(EAGLE_BAZOOKA);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Bazooka"));
			} else {
				this.setMaxCooldown(10D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(BAZOOKA);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Bazooka"));
			}
			if (this.getIcon(entity).equals(new ResourceLocation("mineminenomi:textures/abilities/gomu_gomu_no_bazooka.png")) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Bazooka"));
			}
		} else {
			if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setMaxCooldown(10D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(JET_GIANT_YARI);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Yari"));
			} else if (TrueGomuHelper.hasGearFifthActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setMaxCooldown(20D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(GIANT_DAWN_YARI);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Yari"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setMaxCooldown(15D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(JET_GIANT_YARI);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Yari"));
			} else if (TrueGomuHelper.hasGearFourthActive(props)) {
				TrueGearFourthAbility g4 = AbilityCapability.getEquippedAbility(entity, TrueGearFourthAbility.INSTANCE);
				if (g4.isSnakeman()) {
					this.setMaxCooldown(5D);
					this.setMaxChargeTime(0D);
					this.setDisplayName(TWIN_RHINO_STAMPEDE);
					this.setDisplayIcon(TrueGomuHelper.getIcon("Twin Jet Culverine"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setMaxCooldown(25D);
					this.setMaxChargeTime(5.0D);
					this.setDisplayName(RHINO_REX_SCHNEIDER);
					this.setDisplayIcon(TrueGomuHelper.getIcon("Double King Kong Gun"));
				} else {
					this.setMaxCooldown(6);
					this.setMaxChargeTime(2.0D);
					this.setDisplayName(RHINO_SCHNEIDER);
					this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Yari"));
				}
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				this.setMaxCooldown(20);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(GIANT_YARI);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Yari"));
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				this.setMaxCooldown(7D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(JET_LANCE);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Yari"));
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				this.setMaxCooldown(15D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(DAWN_YARI);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Yari"));
			} else if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				this.setMaxCooldown(20D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(DOUBLE_BAJRANG_STAMP);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Double King Kong Gun"));
			} else {
				this.setMaxCooldown(10D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(YARI);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Yari"));
			}
			if (this.getIcon(entity).equals(new ResourceLocation("mineminenomi:textures/abilities/yari.png")) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Yari"));
			}
		}
	}

}
