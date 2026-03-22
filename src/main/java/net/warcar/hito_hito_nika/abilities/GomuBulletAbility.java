package net.warcar.hito_hito_nika.abilities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.GomuAnimations;
import net.warcar.hito_hito_nika.projectiles.KingBajrangGunProjectile;
import net.warcar.hito_hito_nika.projectiles.hand.*;
import net.warcar.hito_hito_nika.projectiles.leg.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.abilities.haki.HakiHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.*;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.RendererHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.init.ModAbilityComponents;
import xyz.pixelatedw.mineminenomi.init.ModAbilityPools;
import xyz.pixelatedw.mineminenomi.init.ModAnimations;
import xyz.pixelatedw.mineminenomi.init.ModSounds;

public class GomuBulletAbility extends Ability {
	private static final Component[] DESCRIPTION = TrueGomuHelper.registerDescriptionText("gomu_gomu_no_bullet", ImmutablePair.of("User stretches his hand far back to strike enemies with immense force", null));
	public static final AbilityCore<GomuBulletAbility> INSTANCE = new AbilityCore.Builder<>("gomu_gomu_no_bullet", "Gomu Gomu no Bullet", AbilityCategory.DEVIL_FRUITS, GomuBulletAbility::new)
			.setSourceHakiNature(SourceHakiNature.HARDENING).setSourceType(SourceType.FIST).addDescriptionLine(DESCRIPTION).build();
	public static final Component KING_BAJRANG_GUN = TrueGomuHelper.getName("Gomu Gomu no King Bajrang Gun");
	public static final Component BAJRANG_GUN = TrueGomuHelper.getName("Gomu Gomu no Bajrang Gun");
	public static final Component RED_ROC = TrueGomuHelper.getName("Gomu Gomu no Red Roc");
	public static final Component JET_GIANT_BULLET = TrueGomuHelper.getName("Gomu Gomu no Jet Giant Bullet");
	public static final Component RED_HAWK = TrueGomuHelper.getName("Gomu Gomu no Red Hawk");
	public static final Component KING_COBRA = TrueGomuHelper.getName("Gomu Gomu no King Cobra");
	public static final Component OVER_KING2_GUN = TrueGomuHelper.getName("Gomu Gomu no Over King King Kong Gun");
	public static final Component KING3_GUN = TrueGomuHelper.getName("Gomu Gomu no King King King Kong Gun");
	public static final Component OVER_KONG_GUN = TrueGomuHelper.getName("Gomu Gomu no Over Kong Gun");
	public static final Component KING_KONG_GUN = TrueGomuHelper.getName("Gomu Gomu no King Kong Gun");
	private static final Component KONG_BULLET = TrueGomuHelper.getName("Gomu Gomu no Kong Bullet");
	public static final Component THOR_ELEPHANT_GUN = TrueGomuHelper.getName("Gomu Gomu no Thor Elephant Gun");
	public static final Component GIANT_BULLET = TrueGomuHelper.getName("Gomu Gomu no Giant Bullet");
	public static final Component JET_BULLET = TrueGomuHelper.getName("Gomu Gomu no Jet Bullet");
	public static final Component HAWK_BULLET = TrueGomuHelper.getName("Gomu Gomu no Hawk Bullet");
	public static final Component BULLET = TrueGomuHelper.getName("Gomu Gomu no Bullet");
	public static final Component KING_BAJRANG_STAMP = TrueGomuHelper.getName("Gomu Gomu no King Bajrang Stamp");
	public static final Component BAJRANG_STAMP = TrueGomuHelper.getName("Gomu Gomu no Bajrang Stamp");
	public static final Component RED_ROC_STAMP = TrueGomuHelper.getName("Gomu Gomu no Red Roc Stamp");
	public static final Component JET_GIANT_AXE = TrueGomuHelper.getName("Gomu Gomu no Jet Giant Axe");
	public static final Component RED_HAWK_STAMP = TrueGomuHelper.getName("Gomu Gomu no Red Hawk Stamp");
	public static final Component KING_COBRA_STAMP = TrueGomuHelper.getName("Gomu Gomu no King Cobra Stamp");
	public static final Component OVER_KING_2_STAMP = TrueGomuHelper.getName("Gomu Gomu no Over King King Kong Stamp");
	public static final Component KING_3_KONG_STAMP = TrueGomuHelper.getName("Gomu Gomu no King King King Kong Stamp");
	public static final Component OVER_KONG_STAMP = TrueGomuHelper.getName("Gomu Gomu no Over Kong Stamp");
	private static final Component KONG_AXE = TrueGomuHelper.getName("Gomu Gomu no Kong Axe");
	public static final Component KING_KONG_STAMP = TrueGomuHelper.getName("Gomu Gomu no King Kong Stamp");
	public static final Component THOR_GIANT_AXE = TrueGomuHelper.getName("Gomu Gomu no Thor Giant Axe");
	public static final Component GIANT_AXE = TrueGomuHelper.getName("Gomu Gomu no Giant Axe");
	public static final Component JET_AXE = TrueGomuHelper.getName("Gomu Gomu no Jet Axe");
	public static final Component HAWK_AXE = TrueGomuHelper.getName("Gomu Gomu no Hawk Axe");
	public static final Component ONO = TrueGomuHelper.getName("Gomu Gomu no Ono");
	private final ChargeComponent chargeComponent;
	private final ProjectileComponent projectileComponent;
	/*private final AnimeScreamComponent trueScreamComponent = new AnimeScreamComponent(this) {
		@Override
		public void setupDefaultScreams(IAbility ability) {
			ability.getComponent(ModAbilityComponents.CHARGE).ifPresent(chargeComponent -> {
				chargeComponent.addStartEvent((entity, iAbility) -> this.scream(entity, "Gomu gomu no..."));
				chargeComponent.addEndEvent((entity, iAbility) -> this.scream(entity, ability.getDisplayName().getString().replace("Gomu Gomu no ", "")));
			});
		}
	};*/
	private final PoolComponent poolComponent = new PoolComponent(this, ModAbilityPools.GRAB_ABILITY);
	private final AnimationComponent animationComponent = new AnimationComponent(this);
	private float chargeTime = 0;
	private float cooldown = 2;
	private float speed = 2.5f;
	protected boolean leg = false;

	public GomuBulletAbility(AbilityCore<GomuBulletAbility> core) {
		super(core);
		this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
		this.projectileComponent = new ProjectileComponent(this, this::createProjectile);
		this.chargeComponent = new ChargeComponent(this, false);
		this.addComponents(chargeComponent, projectileComponent, poolComponent, animationComponent);
		this.addTickEvent(this::updateModes);
		this.chargeComponent.addTickEvent(this::duringContinuityEvent);
		this.chargeComponent.addEndEvent(this::beforeContinuityStopEvent);
		this.chargeComponent.addStartEvent(this::onStart);
		if (this.isClientSide()) {
			this.getComponent(ModAbilityComponents.SLOT_DECORATION.get()).ifPresent(component -> component.addPostRenderEvent(100, this::hakiOverlay));
		}
		this.addUseEvent(this::start);
	}

	private void onStart(LivingEntity entity, IAbility iAbility) {
		IAbilityData props = AbilityCapability.get(entity).orElse(null);
		if (TrueGomuHelper.hasGearFifthActive(props)) {
			Vec3 movement = entity.getDeltaMovement();
			AbilityHelper.setDeltaMovement(entity, movement.x, 3, movement.z);
		}
		if (TrueGomuHelper.hasGearFifthActive(props) && TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
			this.animationComponent.start(entity, ModAnimations.BODY_ROTATION_WIDE_ARMS, (int) (this.chargeTime * 20));
		} else if (!TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
			this.animationComponent.start(entity, GomuAnimations.BULLET, (int) (this.chargeTime * 20));
		}
	}


	private void start(LivingEntity entity, IAbility ability) {
		if (TrueGomuHelper.hasGearFifthActive(AbilityCapability.get(entity).orElse(null)) && !HakiHelper.hasInfusionActive(entity)) {
			entity.sendMessage(Component.translatable("text.mineminenomi.requires_infusion"), Util.NIL_UUID);
			return;
		}
		if (this.chargeTime == 0) {
			//this.trueScreamComponent.scream(entity);
			this.beforeContinuityStopEvent(entity, ability);
		} else {
            this.chargeComponent.startCharging(entity, this.chargeTime * 20);
        }
	}

	@OnlyIn(Dist.CLIENT)
	private void hakiOverlay(LivingEntity entity, Minecraft client, PoseStack matrixStack, float x, float y, float partialTicks) {
		if (HakiHelper.hasInfusionActive(entity) && TrueGomuHelper.hasGearFourthBoundmanActive(AbilityCapability.get(entity).orElse(null))) {
			RendererHelper.drawIcon(TrueGomuHelper.getIcon("Over Kong Gun Overlay"), matrixStack, x + 4, y + 4, 1.5f, 16, 16, HakiHelper.getHaoshokuColour(entity));
		}
	}

	private void duringContinuityEvent(LivingEntity entity, IAbility i) {
		IAbilityData props = AbilityCapability.get(entity).orElse(null);
		if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
			HakiDataCapability.get(entity).alterHakiOveruse(10);
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
			HakiDataCapability.get(entity).alterHakiOveruse(15);
		}
		if (TrueGomuHelper.hasGearFifthActive(props)) {
			AbilityHelper.slowEntityFall(entity);
		}
	}

	private NuProjectileEntity createProjectile(LivingEntity entity) {
		IAbilityData props = AbilityCapability.get(entity).orElse(null);
		NuProjectileEntity projectile;
		if (EntityStatsCapability.get(entity).get().isBlackLeg()) {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				projectile = new KingBajrangGunProjectile(entity.level(), entity, this);
				speed = 1f;
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new BajrangStampGunProjectile(entity.level(), entity, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new King3KongStampProjectile(entity.level(), entity, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
				projectile = new KingKongStampProjectile(entity.level(), entity, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
				projectile = new JetRhinoSchneiderProjectile(entity.level(), entity, this, 30f, 15);
				projectile.setDamage(140f);
				speed = 30f;
			} else if (TrueGomuHelper.hasPartialGearFourthActive(props)) {
				projectile = new KongStampProjectile(entity.level(), entity, this);
				projectile.setDamage(projectile.getDamage() * 1.5f);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasHakiEmissionActive(props)) {
				projectile = new RedRocStampProjectile(entity.level(), entity, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetElephantStampProjectile(entity.level(), entity, this);
				projectile.setDamage(projectile.getDamage() * 1.5f);
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity)) {
				projectile = new ThorElephantStampProjectile(entity.level(), entity, this);
				speed = 2F;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new ElephantStampProjectile(entity.level(), entity, this);
				projectile.setDamage(projectile.getDamage() * 1.5f);
				speed = 2F;
			} else if (TrueGomuHelper.hasGearSecondActive(props) && HakiHelper.hasHardeningActive(entity)) {
				projectile = new RedHawkStampProjectile(entity.level(), entity, this);
				speed = 3.5F;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetStampProjectile(entity.level(), entity, this);
				speed = 3.5F;
				projectile.setDamage(projectile.getDamage() * 1.5f);
			} else {
				projectile = new StampProjectile(entity.level(), entity, this);
				projectile.setDamage(projectile.getDamage() * 1.5f);
			}
		} else {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				projectile = new KingBajrangGunProjectile(entity.level(), entity, this);
				speed = 1f;
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new BajrangGunProjectile(entity.level(), entity, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new King3KongGunProjectile(entity.level(), entity, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
				projectile = new KingKongGunProjectile(entity.level(), entity, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
				projectile = new JetCulverinProjectile(entity.level(), entity, this, 30f, 75);
				projectile.setDamage(140f);
				speed = 30f;
			} else if (TrueGomuHelper.hasPartialGearFourthActive(props)) {
				projectile = new TrueKongGunProjectile(entity.level(), entity, this);
				projectile.setDamage(projectile.getDamage() * 1.5f);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasHakiEmissionActive(props)) {
				projectile = new RedRocProjectile(entity.level(), entity, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetElephantGunProjectile(entity.level(), entity, this);
				projectile.setDamage(projectile.getDamage() * 1.5f);
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity)) {
				projectile = new ThorElephantGunProjectile(entity.level(), entity, this);
				speed = 2F;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new TrueElephantGunProjectile(entity.level(), entity, this);
				projectile.setDamage(projectile.getDamage() * 1.5f);
				speed = 2F;
			} else if (TrueGomuHelper.hasGearSecondActive(props) && HakiHelper.hasHardeningActive(entity)) {
				projectile = new RedHawkProjectile(entity.level(), entity, this);
				speed = 3.5F;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new TrueJetPistolProjectile(entity.level(), entity, this);
				speed = 3.5F;
				projectile.setDamage(projectile.getDamage() * 1.5f);
			} else {
				projectile = new TruePistolProjectile(entity.level(), entity, this);
				projectile.setDamage(projectile.getDamage() * 1.5f);
			}
		}
		return projectile;
	}

	private void beforeContinuityStopEvent(LivingEntity entity, IAbility i) {
		entity.swing(InteractionHand.MAIN_HAND, true);
		this.projectileComponent.shoot(entity, this.speed, 0.0F);
		entity.level().playSound(null, entity.blockPosition(), ModSounds.GOMU_SFX.get(), SoundSource.PLAYERS, 0.5F, 1.0F);
		this.cooldownComponent.startCooldown(entity, this.cooldown * 20);
	}

	protected void updateModes(LivingEntity entity, IAbility abl) {
		IAbilityData props = AbilityCapability.get(entity).orElse(null);
		if (!EntityStatsCapability.get(entity).get().isBlackLeg()) {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				this.setMaxChargeTime(25D);
				this.cooldown = 120;
				this.setDisplayName(KING_BAJRANG_GUN);
				this.setDisplayIcon(TrueGomuHelper.getIcon("mineminenomi", "King Bajrang Gun"));
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				this.setMaxChargeTime(15D);
				this.cooldown = 40;
				this.setDisplayName(BAJRANG_GUN);
				this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasHakiEmissionActive(props)) {
				this.cooldown = 15;
				this.setMaxChargeTime(3D);
				this.setDisplayName(RED_ROC);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Fire Pistol"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.cooldown = 4;
				this.setMaxChargeTime(3D);
				this.setDisplayName(JET_GIANT_BULLET);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.cooldown = 10;
				this.setMaxChargeTime(2D);
				this.setDisplayName(RED_HAWK);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Fire Pistol"));
			} else if (TrueGomuHelper.hasGearFourthActive(props)) {
				TrueGearFourthAbility g4 = AbilityCapability.get(entity).orElse(null).getEquippedAbility(TrueGearFourthAbility.INSTANCE);
				if (g4.isSnakeman()) {
					this.setMaxChargeTime(3D);
					this.cooldown = 10;
					this.setDisplayName(KING_COBRA);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Cobra"));
				} else if (g4.isBoundman() && HakiHelper.hasInfusionActive(entity) && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setMaxChargeTime(7.5D);
					this.cooldown = 15;
					this.setDisplayName(OVER_KING2_GUN);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setMaxChargeTime(15D);
					this.cooldown = 20;
					this.setDisplayName(KING3_GUN);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else if (g4.isBoundman() && HakiHelper.hasInfusionActive(entity)) {
					this.setMaxChargeTime(5D);
					this.cooldown = 15;
					this.setDisplayName(OVER_KONG_GUN);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else if (g4.isBoundman()) {
					this.setMaxChargeTime(10D);
					this.cooldown = 20;
					this.setDisplayName(KING_KONG_GUN);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else if (g4.isPartial()) {
					this.setMaxChargeTime(1D);
					this.cooldown = 6;
					this.setDisplayName(KONG_BULLET);
					this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Pistol"));
				}
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.cooldown = 15;
				this.setMaxChargeTime(3D);
				this.setDisplayName(THOR_ELEPHANT_GUN);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Pistol"));
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				this.cooldown = 9;
				this.setMaxChargeTime(4D);
				this.setDisplayName(GIANT_BULLET);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				this.cooldown = 1.5f;
				this.setMaxChargeTime(0.5D);
				this.setDisplayName(JET_BULLET);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			} else if (HakiHelper.hasHardeningActive(entity, false, true)) {
				this.cooldown = 2;
				this.setMaxChargeTime(1D);
				this.setDisplayName(HAWK_BULLET);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Pistol"));
			} else {
				this.cooldown = 2;
				this.setMaxChargeTime(1D);
				this.setDisplayName(BULLET);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			}
		} else {
			/*if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				this.setMaxChargeTime(25D);
				this.cooldown = 120;
				this.setDisplayName(KING_BAJRANG_STAMP);
				this.setDisplayIcon(TrueGomuHelper.getIcon("King Bajrang Gun"));
			} else */if (TrueGomuHelper.hasGearFifthActive(props)) {
				this.setMaxChargeTime(15D);
				this.cooldown = 40;
				this.setDisplayName(BAJRANG_STAMP);
				this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasHakiEmissionActive(props)) {
				this.cooldown = 8;
				this.setMaxChargeTime(3D);
				this.setDisplayName(RED_ROC_STAMP);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Fire Stamp"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.cooldown = 4;
				this.setMaxChargeTime(3D);
				this.setDisplayName(JET_GIANT_AXE);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.cooldown = 8;
				this.setMaxChargeTime(2D);
				this.setDisplayName(RED_HAWK_STAMP);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Fire Stamp"));
			} else if (TrueGomuHelper.hasGearFourthActive(props)) {
				TrueGearFourthAbility g4 = AbilityCapability.get(entity).orElse(null).getEquippedAbility(TrueGearFourthAbility.INSTANCE);
				if (g4.isSnakeman()) {
					this.setMaxChargeTime(3D);
					this.cooldown = 10;
					this.setDisplayName(KING_COBRA_STAMP);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Cobra"));
				} else if (g4.isBoundman() && HakiHelper.hasInfusionActive(entity) && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setMaxChargeTime(7.5D);
					this.cooldown = 15;
					this.setDisplayName(OVER_KING_2_STAMP);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setMaxChargeTime(15D);
					this.cooldown = 20;
					this.setDisplayName(KING_3_KONG_STAMP);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else if (g4.isBoundman() && HakiHelper.hasInfusionActive(entity)) {
					this.setMaxChargeTime(5D);
					this.cooldown = 15;
					this.setDisplayName(OVER_KONG_STAMP);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else if (g4.isBoundman()) {
					this.setMaxChargeTime(10D);
					this.cooldown = 20;
					this.setDisplayName(KING_KONG_STAMP);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else if (g4.isPartial()) {
					this.setMaxChargeTime(1D);
					this.cooldown = 6;
					this.setDisplayName(KONG_AXE);
					this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Stamp"));
				}
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.cooldown = 15;
				this.setMaxChargeTime(2D);
				this.setDisplayName(THOR_GIANT_AXE);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Stamp"));
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				this.cooldown = 9;
				this.setMaxChargeTime(4D);
				this.setDisplayName(GIANT_AXE);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				this.cooldown = 1.5f;
				this.setMaxChargeTime(0.5D);
				this.setDisplayName(JET_AXE);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			} else if (HakiHelper.hasHardeningActive(entity, false, true)) {
				this.cooldown = 2;
				this.setMaxChargeTime(1D);
				this.setDisplayName(HAWK_AXE);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Stamp"));
			} else {
				this.cooldown = 2;
				this.setMaxChargeTime(1D);
				this.setDisplayName(ONO);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			}
		}
	}

	private void setMaxChargeTime(double v) {
		this.chargeTime = (float) v;
	}

	public float getMaxChargeTime() {
		return this.chargeComponent.getMaxChargeTime();
	}

	public float getChargeTime() {
		return this.chargeComponent.getChargeTime();
	}

}
