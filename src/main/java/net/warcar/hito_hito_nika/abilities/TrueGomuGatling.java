package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.hito_hito_nika.projectiles.hand.*;
import net.warcar.hito_hito_nika.projectiles.leg.*;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AnimationComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AnimeScreamComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ProjectileComponent;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.helpers.HakiHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModAbilityKeys;
import xyz.pixelatedw.mineminenomi.init.ModAnimations;
import xyz.pixelatedw.mineminenomi.init.ModEffects;

public class TrueGomuGatling extends Ability implements IExtraUpdateData {
	public static final AbilityCore<TrueGomuGatling> INSTANCE;
	public static final TranslationTextComponent ROC_GATLING = TrueGomuHelper.getName("Gomu Gomu no Roc Gatling");
	public static final TranslationTextComponent JET_ELEPHANT_GATLING = TrueGomuHelper.getName("Gomu Gomu no Jet Elephant Gatling");
	public static final TranslationTextComponent JET_GIANT_GATLING = TrueGomuHelper.getName("Gomu Gomu no Jet Giant Gatling");
	public static final TranslationTextComponent GIANT_DAWN_GATLING = TrueGomuHelper.getName("Gomu Gomu no Giant Dawn Gatling");
	public static final TranslationTextComponent HYDRA = TrueGomuHelper.getName("Gomu Gomu no Hydra");
	public static final TranslationTextComponent BLACK_MAMBA = TrueGomuHelper.getName("Gomu Gomu no Black Mamba");
	public static final TranslationTextComponent OVER_KONG_GATLING = TrueGomuHelper.getName("Gomu Gomu no Over Kong Gatling");
	public static final TranslationTextComponent KING_KONG_GATLING = TrueGomuHelper.getName("Gomu Gomu no King Kong Gatling");
	public static final TranslationTextComponent KONG_GATLING = TrueGomuHelper.getName("Gomu Gomu no Kong Gatling");
	public static final TranslationTextComponent DAWN_GATLING = TrueGomuHelper.getName("Gomu Gomu no Dawn Gatling");
	public static final TranslationTextComponent BAJRANG_GATLING = TrueGomuHelper.getName("Gomu Gomu no Bajrang Gatling");
	public static final TranslationTextComponent JET_GATLING = TrueGomuHelper.getName("Gomu Gomu no Jet Gatling");
	public static final TranslationTextComponent ELEPHANT_GATLING = TrueGomuHelper.getName("Gomu Gomu no Elephant Gatling");
	public static final TranslationTextComponent GIANT_GATLING = TrueGomuHelper.getName("Gomu Gomu no Giant Gatling");
	public static final TranslationTextComponent HAWK_GATLING = TrueGomuHelper.getName("Gomu Gomu no Hawk Gatling");
	public static final TranslationTextComponent GATLING = TrueGomuHelper.getName("Gomu Gomu no Gatling");
	public static final TranslationTextComponent STAMP_ROC_GATLING = TrueGomuHelper.getName("Gomu Gomu no Stamp Roc Gatling");
	public static final TranslationTextComponent JET_ELEPHANT_STAMP_GATLING = TrueGomuHelper.getName("Gomu Gomu no Jet Elephant Stamp Gatling");
	public static final TranslationTextComponent JET_GIANT_STAMP_GATLING = TrueGomuHelper.getName("Gomu Gomu no Jet Giant Stamp Gatling");
	public static final TranslationTextComponent GIANT_DAWN_STAMP_GATLING = TrueGomuHelper.getName("Gomu Gomu no Giant Dawn Stamp Gatling");
	public static final TranslationTextComponent STAMP_HYDRA = TrueGomuHelper.getName("Gomu Gomu no Stamp Hydra");
	public static final TranslationTextComponent RHINO_STAMPEDE_GATLING = TrueGomuHelper.getName("Gomu Gomu no Rhino Stampede Gatling");
	public static final TranslationTextComponent OVER_KONG_STAMP_GATLING = TrueGomuHelper.getName("Gomu Gomu no Over Kong Stamp Gatling");
	public static final TranslationTextComponent KING_KONG_STAMP_GATLING = TrueGomuHelper.getName("Gomu Gomu no King Kong Stamp Gatling");
	public static final TranslationTextComponent KONG_STAMP_GATLING = TrueGomuHelper.getName("Gomu Gomu no Kong Stamp Gatling");
	public static final TranslationTextComponent JET_STAMP_GATLING = TrueGomuHelper.getName("Gomu Gomu no Jet Stamp Gatling");
	public static final TranslationTextComponent ELEPHANT_STAMP_GATLING = TrueGomuHelper.getName("Gomu Gomu no Elephant Stamp Gatling");
	public static final TranslationTextComponent GIANT_STAMP_GATLING = TrueGomuHelper.getName("Gomu Gomu no Giant Stamp Gatling");
	public static final TranslationTextComponent DAWN_STAMP_GATLING = TrueGomuHelper.getName("Gomu Gomu no Dawn Stamp Gatling");
	public static final TranslationTextComponent BAJRANG_STAMP_GATLING = TrueGomuHelper.getName("Gomu Gomu no Bajrang Stamp Gatling");
	public static final TranslationTextComponent TAKO_STAMP = TrueGomuHelper.getName("Gomu Gomu no Tako Stamp");
	public static final TranslationTextComponent STAMP_GATLING = TrueGomuHelper.getName("Gomu Gomu no Stamp Gatling");
	private final ContinuousComponent continuousComponent;
	private final ProjectileComponent projectileComponent;
	private final AnimationComponent animationComponent;
	private final AnimeScreamComponent trueScreamComponent = new AnimeScreamComponent(this) {
		@Override
		public void setupDefaultScreams(IAbility ability) {
			ability.getComponent(ModAbilityKeys.CONTINUOUS).ifPresent(chargeComponent -> chargeComponent.addStartEvent((entity, iAbility) -> this.scream(entity, ability.getDisplayName().getString())));
		}
	};
	private double leap = 3;
	private float speed = 3;
	private float projDamageReduction = 0.8F;
	private int projectileSpace = 2;

	public TrueGomuGatling(AbilityCore core) {
		super(core);
		this.addTickEvent(this::updateModes);
		this.addUseEvent(this::onStartContinuityEvent);
		this.continuousComponent = new ContinuousComponent(this);
		this.animationComponent = new AnimationComponent(this);
		this.projectileComponent = new ProjectileComponent(this, this::createProjectile);
		this.continuousComponent.addTickEvent(this::duringContinuityEvent);
		this.continuousComponent.addEndEvent(this::onContinuityStopEvent);
		this.isNew = true;
		this.addComponents(continuousComponent, projectileComponent, trueScreamComponent, animationComponent);
	}

	private AbilityProjectileEntity createProjectile(LivingEntity entity) {
		IAbilityData props = AbilityDataCapability.get(entity);
		AbilityProjectileEntity projectile;
		if (EntityStatsCapability.get(entity).isBlackLeg()) {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				projectile = new BajrangStampGunProjectile(entity.level, entity, this, 15f);
				projectileSpace = 10;
				speed = 5f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new GigantDawnStampProjectile(entity.level, entity, this);
				projectileSpace = 9;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetElephantStampProjectile(entity.level, entity, this);
				projectileSpace = 9;
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new DawnStampProjectile(entity.level, entity, this);
				speed = 4;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new KingKongStampProjectile(entity.level, entity, this);
				speed = 4;
				projectileSpace = 6;
				projDamageReduction = 0.6F;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) || TrueGomuHelper.hasPartialGearFourthActive(props)) {
				projectile = new KongStampProjectile(entity.level, entity, this);
				speed = 2.2F;
				projectileSpace = 6;
				projDamageReduction = 0.6F;
			} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
				projectile = new JetRhinoSchneiderProjectile(entity.level, entity, this, 7f, 5);
				projectile.setKnockbackStrength(1);
				speed = 7F;
				projectileSpace = 6;
				projDamageReduction = 0.4F;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new ElephantStampProjectile(entity.level, entity, this);
				speed = 2.4F;
				projectileSpace = 9;
				projDamageReduction = 0.6F;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetStampProjectile(entity.level, entity, this);
				speed = 3.6F;
			} else {
				projectile = new StampProjectile(entity.level, entity, this);
			}
		} else {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				projectile = new BajrangGunProjectile(entity.level, entity, this, 15f);
				projectileSpace = 10;
				speed = 5f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new GigantDawnPistolProjectile(entity.level, entity, this);
				projectileSpace = 9;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetElephantGunProjectile(entity.level, entity, this);
				projectileSpace = 9;
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new DawnPistolProjectile(entity.level, entity, this);
				speed = 2;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new KingKongGunProjectile(entity.level, entity, this);
				speed = 4;
				projectileSpace = 6;
				projDamageReduction = 0.6F;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) || TrueGomuHelper.hasPartialGearFourthActive(props)) {
				projectile = new TrueKongGunProjectile(entity.level, entity, this);
				speed = 2.2F;
				projectileSpace = 6;
				projDamageReduction = 0.6F;
			} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
				projectile = new JetCulverinProjectile(entity.level, entity, this, 7f, 5);
				projectile.setKnockbackStrength(1);
				speed = 7F;
				projectileSpace = 6;
				projDamageReduction = 0.4F;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new TrueElephantGunProjectile(entity.level, entity, this);
				speed = 2.4F;
				projectileSpace = 9;
				projDamageReduction = 0.6F;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new TrueJetPistolProjectile(entity.level, entity, this);
				speed = 3.6F;
			} else {
				projectile = new TruePistolProjectile(entity.level, entity, this);
			}
		}
		projectile.setDamage(projectile.getDamage() * (1.0F - projDamageReduction));
		projectile.setMaxLife((int) ((double) projectile.getMaxLife() * 0.75D));
		return projectile;
	}

	private void onStartContinuityEvent(LivingEntity player, IAbility abl) {
		if (this.continuousComponent.isContinuous()) {
			this.continuousComponent.stopContinuity(player);
			return;
		}
		double time = EntityStatsCapability.get(player).getDoriki() / 100;
		IAbilityData props = AbilityDataCapability.get(player);
		double dif;
		if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
			this.leap = 1;
			dif = 5;
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props) && HakiHelper.hasInfusionActive(player) && TrueGomuHelper.hasHakiEmissionActive(props)) {
			this.leap = 1;
			dif = 50;
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
			this.leap = 1;
			dif = 40;
		} else if (TrueGomuHelper.hasGearFifthActive(props)) {
			this.leap = 0.25;
			dif = 2;
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasInfusionActive(player)) {
			this.leap = 20;
			dif = 70;
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
			this.leap = 40;
			dif = 70;
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
			this.leap = 1;
			dif = 20;
		} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props) && HakiHelper.hasInfusionActive(player)) {
			this.leap = 1;
			dif = 50;
		} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
			this.leap = 1;
			dif = 20;
		} else if (TrueGomuHelper.hasGearThirdActive(props)) {
			this.leap = 2;
			dif = 30;
		} else if (TrueGomuHelper.hasGearSecondActive(props)) {
			this.leap = 0.2d;
			dif = 10;
		} else {
			dif = 5;
			this.leap = 0.5;
		}
		time /= dif;
		time = 3 * (1 + Math.sqrt(time));
		this.animationComponent.start(player, ModAnimations.PUNCH_RUSH);
		if (time >= 500) {
			this.continuousComponent.startContinuity(player, -1);
		} else {
			this.continuousComponent.startContinuity(player, (float) time * 20);
		}
	}

	private void duringContinuityEvent(LivingEntity player, IAbility abl) {
		player.addEffect(new EffectInstance(ModEffects.MOVEMENT_BLOCKED.get(), 5, 1, false, false));
		if (leap >= 1) {
			if (this.continuousComponent.getContinueTime() % (int) leap == 0)
				this.projectileComponent.shootWithSpread(player, this.speed, 0, this.projectileSpace / 3);
		} else if (leap > 0) {
			for (int j = 0; j < 1 / leap; j++) {
				this.projectileComponent.shootWithSpread(player, this.speed, 0, this.projectileSpace / 3);
			}
		}
	}

	protected void updateModes(LivingEntity entity, IAbility abl) {
		IAbilityData props = AbilityDataCapability.get(entity);
		if (!EntityStatsCapability.get(entity).isBlackLeg()) {
			if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasHakiEmissionActive(props) && HakiHelper.hasInfusionActive(entity)) {
				this.setDisplayName(ROC_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Gatling"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setDisplayName(JET_ELEPHANT_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setDisplayName(JET_GIANT_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearFifthActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setDisplayName(GIANT_DAWN_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearFourthActive(props)) {
				TrueGearFourthAbility g4 = AbilityDataCapability.get(entity).getEquippedAbility(TrueGearFourthAbility.INSTANCE);
				if (g4.isSnakeman()) {
					if (HakiHelper.hasInfusionActive(entity)) {
						this.setDisplayName(HYDRA);
					} else {
						this.setDisplayName(BLACK_MAMBA);
					}
					this.setDisplayIcon(TrueGomuHelper.getIcon("Black Mamba"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasInfusionActive(entity)) {
					this.setDisplayName(OVER_KONG_GATLING);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gatling"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setDisplayName(KING_KONG_GATLING);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gatling"));
				} else {
					this.setDisplayName(KONG_GATLING);
					this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Gatling"));
				}
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				this.setDisplayName(DAWN_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				this.setDisplayName(BAJRANG_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gatling"));
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				this.setDisplayName(JET_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setDisplayName(ELEPHANT_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				this.setDisplayName(GIANT_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setDisplayName(HAWK_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Gatling"));
			} else {
				this.setDisplayName(GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			}
			if (this.getIcon(entity).equals(new ResourceLocation("mineminenomi:textures/abilities/gomu_gomu_no_gatling.png")) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Gatling"));
			}
		} else {
			if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasHakiEmissionActive(props) && HakiHelper.hasInfusionActive(entity)) {
				this.setDisplayName(STAMP_ROC_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Gatling"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setDisplayName(JET_ELEPHANT_STAMP_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setDisplayName(JET_GIANT_STAMP_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearFifthActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setDisplayName(GIANT_DAWN_STAMP_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearFourthActive(props)) {
				TrueGearFourthAbility g4 = AbilityDataCapability.get(entity).getEquippedAbility(TrueGearFourthAbility.INSTANCE);
				if (g4.isSnakeman()) {
					if (HakiHelper.hasInfusionActive(entity)) {
						this.setDisplayName(STAMP_HYDRA);
					} else {
						this.setDisplayName(RHINO_STAMPEDE_GATLING);
					}
					this.setDisplayIcon(TrueGomuHelper.getIcon("Black Mamba"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasInfusionActive(entity)) {
					this.setDisplayName(OVER_KONG_STAMP_GATLING);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gatling"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setDisplayName(KING_KONG_STAMP_GATLING);
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gatling"));
				} else {
					this.setDisplayName(KONG_STAMP_GATLING);
					this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Gatling"));
				}
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				this.setDisplayName(JET_STAMP_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setDisplayName(ELEPHANT_STAMP_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				this.setDisplayName(GIANT_STAMP_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				this.setDisplayName(DAWN_STAMP_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				this.setDisplayName(BAJRANG_STAMP_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gatling"));
			} else if (HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setDisplayName(TAKO_STAMP);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Gatling"));
			} else {
				this.setDisplayName(STAMP_GATLING);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			}
			if (this.getIcon(entity).equals(new ResourceLocation("mineminenomi:textures/abilities/gomu_gomu_no_gatling.png")) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Gatling"));
			}
		}
	}

	private void onContinuityStopEvent(LivingEntity player, IAbility abl) {
		IAbilityData props = AbilityDataCapability.get(player);
		double dif;
		if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
			dif = 5;
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
			dif = 10;
		} else if (TrueGomuHelper.hasGearFifthActive(props)) {
			dif = 2;
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
			dif = 20;
		} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
			dif = 20;
		} else if (TrueGomuHelper.hasGearThirdActive(props)) {
			dif = 30;
		} else if (TrueGomuHelper.hasGearSecondActive(props)) {
			dif = 3;
		} else {
			dif = 5;
		}
		this.animationComponent.stop(player);
		this.cooldownComponent.startCooldown(player, (float) Math.max(((double) this.continuousComponent.getContinueTime() / 200) * dif, 3) * 20);
	}

	public void setExtraData(CompoundNBT tag) {
		this.leap = tag.getDouble("leap");
	}

	public CompoundNBT getExtraData() {
		CompoundNBT out = new CompoundNBT();
		out.putDouble("leap", this.leap);
		return out;
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gomu Gomu no Gatling", AbilityCategory.DEVIL_FRUITS, TrueGomuGatling::new)).addDescriptionLine("Rapidly punches enemies using rubber fists").setSourceHakiNature(SourceHakiNature.HARDENING)
				.setSourceType(SourceType.FIST).build();
	}
}
