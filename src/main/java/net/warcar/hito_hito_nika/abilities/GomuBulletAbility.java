package net.warcar.hito_hito_nika.abilities;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.warcar.hito_hito_nika.projectiles.KingBajrangGunProjectile;
import net.warcar.hito_hito_nika.projectiles.hand.*;
import net.warcar.hito_hito_nika.projectiles.leg.*;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AnimeScreamComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChargeComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ProjectileComponent;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.helpers.HakiHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.RendererHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.haki.HakiDataCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModAbilityKeys;
import xyz.pixelatedw.mineminenomi.init.ModSounds;

public class GomuBulletAbility extends Ability {
	public static final AbilityCore<GomuBulletAbility> INSTANCE;
	public static final TranslationTextComponent KING_BAJRANG_GUN = TrueGomuHelper.getName("Gomu Gomu no King Bajrang Gun");
	public static final TranslationTextComponent BAJRANG_GUN = TrueGomuHelper.getName("Gomu Gomu no Bajrang Gun");
	public static final TranslationTextComponent RED_ROC = TrueGomuHelper.getName("Gomu Gomu no Red Roc");
	public static final TranslationTextComponent JET_GIANT_BULLET = TrueGomuHelper.getName("Gomu Gomu no Jet Giant Bullet");
	public static final TranslationTextComponent RED_HAWK = TrueGomuHelper.getName("Gomu Gomu no Red Hawk");
	public static final TranslationTextComponent KING_COBRA = TrueGomuHelper.getName("Gomu Gomu no King Cobra");
	public static final TranslationTextComponent OVER_KING2_GUN = TrueGomuHelper.getName("Gomu Gomu no Over King King Kong Gun");
	public static final TranslationTextComponent KING3_GUN = TrueGomuHelper.getName("Gomu Gomu no King King King Kong Gun");
	public static final TranslationTextComponent OVER_KONG_GUN = TrueGomuHelper.getName("Gomu Gomu no Over Kong Gun");
	public static final TranslationTextComponent KING_KONG_GUN = TrueGomuHelper.getName("Gomu Gomu no King Kong Gun");
	public static final TranslationTextComponent THOR_ELEPHANT_GUN = TrueGomuHelper.getName("Gomu Gomu no Thor Elephant Gun");
	public static final TranslationTextComponent GIANT_BULLET = TrueGomuHelper.getName("Gomu Gomu no Giant Bullet");
	public static final TranslationTextComponent JET_BULLET = TrueGomuHelper.getName("Gomu Gomu no Jet Bullet");
	public static final TranslationTextComponent HAWK_BULLET = TrueGomuHelper.getName("Gomu Gomu no Hawk Bullet");
	public static final TranslationTextComponent BULLET = TrueGomuHelper.getName("Gomu Gomu no Bullet");
	public static final TranslationTextComponent KING_BAJRANG_STAMP = TrueGomuHelper.getName("Gomu Gomu no King Bajrang Stamp");
	public static final TranslationTextComponent BAJRANG_STAMP = TrueGomuHelper.getName("Gomu Gomu no Bajrang Stamp");
	public static final TranslationTextComponent RedRocStamp = TrueGomuHelper.getName("Gomu Gomu no Red Roc Stamp");
	public static final TranslationTextComponent JetGiantAxe = TrueGomuHelper.getName("Gomu Gomu no Jet Giant Axe");
	public static final TranslationTextComponent RedHawkStamp = TrueGomuHelper.getName("Gomu Gomu no Red Hawk Stamp");
	public static final TranslationTextComponent KingCobraStamp = TrueGomuHelper.getName("Gomu Gomu no King Cobra Stamp");
	public static final TranslationTextComponent OverKing2Stamp = TrueGomuHelper.getName("Gomu Gomu no Over King King Kong Stamp");
	public static final TranslationTextComponent King3KongStamp = TrueGomuHelper.getName("Gomu Gomu no King King King Kong Stamp");
	public static final TranslationTextComponent OverKongStamp = TrueGomuHelper.getName("Gomu Gomu no Over Kong Stamp");
	public static final TranslationTextComponent KingKongStamp = TrueGomuHelper.getName("Gomu Gomu no King Kong Stamp");
	public static final TranslationTextComponent ThorGiantAxe = TrueGomuHelper.getName("Gomu Gomu no Thor Giant Axe");
	public static final TranslationTextComponent GIANT_AXE = TrueGomuHelper.getName("Gomu Gomu no Giant Axe");
	public static final TranslationTextComponent JET_AXE = TrueGomuHelper.getName("Gomu Gomu no Jet Axe");
	public static final TranslationTextComponent HAWK_AXE = TrueGomuHelper.getName("Gomu Gomu no Hawk Axe");
	public static final TranslationTextComponent ONO = TrueGomuHelper.getName("Gomu Gomu no Ono");
	private final ChargeComponent chargeComponent;
	private final ProjectileComponent projectileComponent;
	private final AnimeScreamComponent trueScreamComponent = new AnimeScreamComponent(this) {
		@Override
		public void setupDefaultScreams(IAbility ability) {
			ability.getComponent(ModAbilityKeys.CHARGE).ifPresent(chargeComponent -> {
				chargeComponent.addStartEvent((entity, iAbility) -> this.scream(entity, "Gomu gomu no..."));
				chargeComponent.addEndEvent((entity, iAbility) -> this.scream(entity, ability.getDisplayName().getString().replace("Gomu Gomu no ", "")));
			});
		}
	};
	private float chargeTime = 0;
	private float cooldown = 2;
	private float speed = 2.5f;
	protected boolean leg = false;

	public GomuBulletAbility(AbilityCore<GomuBulletAbility> core) {
		super(core);
		this.setDisplayIcon(TrueGomuPistol.INSTANCE);
		this.projectileComponent = new ProjectileComponent(this, this::createProjectile);
		this.chargeComponent = new ChargeComponent(this, true);
		this.addComponents(chargeComponent, projectileComponent, trueScreamComponent);
		this.addTickEvent(this::updateModes);
		this.chargeComponent.addTickEvent(this::duringContinuityEvent);
		this.chargeComponent.addEndEvent(this::beforeContinuityStopEvent);
		if (this.isClientSide()) {
			this.getComponent(ModAbilityKeys.SLOT_DECORATION).ifPresent(component -> component.addPostRenderEvent(100, this::hakiOverlay));
		}
		this.isNew = true;
		this.addUseEvent(this::start);
	}



	private void start(LivingEntity entity, IAbility ability) {
		if (this.chargeTime == 0) {
			this.beforeContinuityStopEvent(entity, ability);
		} else {
            this.chargeComponent.startCharging(entity, this.chargeTime * 20);
        }
	}

	@OnlyIn(Dist.CLIENT)
	private void hakiOverlay(LivingEntity entity, Minecraft client, MatrixStack matrixStack, float x, float y, float partialTicks) {
		if (HakiHelper.hasInfusionActive(entity) && TrueGomuHelper.hasGearFourthBoundmanActive(AbilityDataCapability.get(entity))) {
			RendererHelper.drawIcon(TrueGomuHelper.getIcon("Over Kong Gun Overlay"), matrixStack, x + 4, y + 4, 1.5f, 16, 16, HakiHelper.getHaoshokuColour(entity));
		}
	}

	private void duringContinuityEvent(LivingEntity player, IAbility i) {
		IAbilityData props = AbilityDataCapability.get(player);
		if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
			HakiDataCapability.get(player).alterHakiOveruse(10);
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
			HakiDataCapability.get(player).alterHakiOveruse(15);
		}
	}

	private AbilityProjectileEntity createProjectile(LivingEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		AbilityProjectileEntity projectile;
		if (this.leg) {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				projectile = new KingBajrangGunProjectile(player.level, player, this);
				speed = 1f;
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new BajrangStampGunProjectile(player.level, player, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new King3KongStampProjectile(player.level, player, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
				projectile = new KingKongStampProjectile(player.level, player, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
				projectile = new JetRhinoSchneiderProjectile(player.level, player, this, 30f, 15);
				projectile.setDamage(140f);
				speed = 30f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasHakiEmissionActive(props)) {
				projectile = new RedRocStampProjectile(player.level, player, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetElephantStampProjectile(player.level, player, this);
				projectile.setDamage(projectile.getDamage() * 1.5f);
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(player)) {
				projectile = new ThorElephantStampProjectile(player.level, player, this);
				speed = 2F;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new ElephantStampProjectile(player.level, player, this);
				projectile.setDamage(projectile.getDamage() * 1.5f);
				speed = 2F;
			} else if (TrueGomuHelper.hasGearSecondActive(props) && HakiHelper.hasHardeningActive(player)) {
				projectile = new RedHawkStampProjectile(player.level, player, this);
				speed = 3.5F;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetStampProjectile(player.level, player, this);
				speed = 3.5F;
				projectile.setDamage(projectile.getDamage() * 1.5f);
			} else {
				projectile = new StampProjectile(player.level, player, this);
				projectile.setDamage(projectile.getDamage() * 1.5f);
			}
		} else {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				projectile = new KingBajrangGunProjectile(player.level, player, this);
				speed = 1f;
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new BajrangGunProjectile(player.level, player, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new King3KongGunProjectile(player.level, player, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
				projectile = new KingKongGunProjectile(player.level, player, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
				projectile = new JetCulverinProjectile(player.level, player, this, 30f, 15);
				projectile.setDamage(140f);
				speed = 30f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasHakiEmissionActive(props)) {
				projectile = new RedRocProjectile(player.level, player, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetElephantGunProjectile(player.level, player, this);
				projectile.setDamage(projectile.getDamage() * 1.5f);
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(player)) {
				projectile = new ThorElephantGunProjectile(player.level, player, this);
				speed = 2F;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new TrueElephantGunProjectile(player.level, player, this);
				projectile.setDamage(projectile.getDamage() * 1.5f);
				speed = 2F;
			} else if (TrueGomuHelper.hasGearSecondActive(props) && HakiHelper.hasHardeningActive(player)) {
				projectile = new RedHawkProjectile(player.level, player, this);
				speed = 3.5F;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new TrueJetPistolProjectile(player.level, player, this);
				speed = 3.5F;
				projectile.setDamage(projectile.getDamage() * 1.5f);
			} else {
				projectile = new TruePistolProjectile(player.level, player, this);
				projectile.setDamage(projectile.getDamage() * 1.5f);
			}
		}
		return projectile;
	}

	private void beforeContinuityStopEvent(LivingEntity entity, IAbility i) {
		entity.swing(Hand.MAIN_HAND, true);
		this.projectileComponent.shoot(entity, this.speed, 0.0F);
		entity.level.playSound(null, entity.blockPosition(), ModSounds.GOMU_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
		this.cooldownComponent.startCooldown(entity, this.cooldown * 20);
	}

	protected void updateModes(LivingEntity entity, IAbility abl) {
		IAbilityData props = AbilityDataCapability.get(entity);
		if (!EntityStatsCapability.get(entity).isBlackLeg()) {
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
				this.cooldown = 8;
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(RED_ROC);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Fire Pistol"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.cooldown = 4;
				this.setMaxChargeTime(0.0);
				this.setDisplayName(JET_GIANT_BULLET);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.cooldown = 4;
				this.setMaxChargeTime(0.0);
				this.setDisplayName(RED_HAWK);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Fire Pistol"));
			} else if (TrueGomuHelper.hasGearFourthActive(props)) {
				TrueGearFourthAbility g4 = AbilityDataCapability.get(entity).getEquippedAbility(TrueGearFourthAbility.INSTANCE);
				if (g4.isSnakeman()) {
					this.setMaxChargeTime(3D);
					this.cooldown = 10;
					this.setDisplayName(KING_COBRA);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Cobra"));
				} else if (g4.isBoundman() && HakiHelper.hasInfusionActive(entity) && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setMaxChargeTime(7.5D);
					this.cooldown = 20;
					this.setDisplayName(OVER_KING2_GUN);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setMaxChargeTime(15D);
					this.cooldown = 40;
					this.setDisplayName(KING3_GUN);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else if (g4.isBoundman() && HakiHelper.hasInfusionActive(entity)) {
					this.setMaxChargeTime(5D);
					this.cooldown = 15;
					this.setDisplayName(OVER_KONG_GUN);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else if (g4.isBoundman()) {
					this.setMaxChargeTime(10D);
					this.cooldown = 30;
					this.setDisplayName(KING_KONG_GUN);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				}
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.cooldown = 9;
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(THOR_ELEPHANT_GUN);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Pistol"));
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				this.cooldown = 9;
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(GIANT_BULLET);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				this.cooldown = 1.5f;
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(JET_BULLET);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			} else if (HakiHelper.hasHardeningActive(entity, false, true)) {
				this.cooldown = 2;
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(HAWK_BULLET);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Pistol"));
			} else {
				this.cooldown = 2;
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(BULLET);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			}
		} else {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				this.setMaxChargeTime(25D);
				this.cooldown = 120;
				this.setDisplayName(KING_BAJRANG_STAMP);
				this.setDisplayIcon(TrueGomuHelper.getIcon("King Bajrang Gun"));
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				this.setMaxChargeTime(15D);
				this.cooldown = 40;
				this.setDisplayName(BAJRANG_STAMP);
				this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasHakiEmissionActive(props)) {
				this.cooldown = 8;
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(RedRocStamp);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Fire Stamp"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.cooldown = 4;
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(JetGiantAxe);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.cooldown = 4;
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(RedHawkStamp);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Fire Stamp"));
			} else if (TrueGomuHelper.hasGearFourthActive(props)) {
				TrueGearFourthAbility g4 = AbilityDataCapability.get(entity).getEquippedAbility(TrueGearFourthAbility.INSTANCE);
				if (g4.isSnakeman()) {
					this.setMaxChargeTime(3D);
					this.cooldown = 10;
					this.setDisplayName(KingCobraStamp);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Cobra"));
				} else if (g4.isBoundman() && HakiHelper.hasInfusionActive(entity) && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setMaxChargeTime(7.5D);
					this.cooldown = 20;
					this.setDisplayName(OverKing2Stamp);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setMaxChargeTime(15D);
					this.cooldown = 40;
					this.setDisplayName(King3KongStamp);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else if (g4.isBoundman() && HakiHelper.hasInfusionActive(entity)) {
					this.setMaxChargeTime(5D);
					this.cooldown = 15;
					this.setDisplayName(OverKongStamp);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else if (g4.isBoundman()) {
					this.setMaxChargeTime(10D);
					this.cooldown = 30;
					this.setDisplayName(KingKongStamp);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				}
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.cooldown = 9;
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(ThorGiantAxe);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Stamp"));
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				this.cooldown = 9;
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(GIANT_AXE);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				this.cooldown = 1.5f;
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(JET_AXE);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			} else if (HakiHelper.hasHardeningActive(entity, false, true)) {
				this.cooldown = 2;
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(HAWK_AXE);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Stamp"));
			} else {
				this.cooldown = 2;
				this.setMaxChargeTime(0.0D);
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

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gomu Gomu no Bullet", AbilityCategory.DEVIL_FRUITS, GomuBulletAbility::new)).setSourceHakiNature(SourceHakiNature.HARDENING).setSourceType(SourceType.FIST).build();
	}
}
