package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.warcar.hito_hito_nika.config.CommonConfig;
import net.warcar.hito_hito_nika.projectiles.hand.*;
import net.warcar.hito_hito_nika.projectiles.leg.*;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiEmissionAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiHardeningAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.HaoshokuHakiInfusionAbility;
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

public class TrueGomuGatling extends Ability implements IExtraUpdateData {
	public static final AbilityCore<TrueGomuGatling> INSTANCE;
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
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
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
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
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
			this.leap = 2;
			dif = 5;
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props) && HakiHelper.hasInfusionActive(player) && TrueGomuHelper.hasHakiEmissionActive(props)) {
			this.leap = 1;
			dif = 50;
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
			this.leap = 3;
			dif = 40;
		} else if (TrueGomuHelper.hasGearFifthActive(props)) {
			this.leap = 1;
			dif = 2;
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasInfusionActive(player)) {
			this.leap = 20;
			dif = 70;
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
			this.leap = 40;
			dif = 70;
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
			this.leap = 3;
			dif = 20;
		} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props) && HakiHelper.hasInfusionActive(player)) {
			this.leap = 1;
			dif = 50;
		} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
			this.leap = 1;
			dif = 20;
		} else if (TrueGomuHelper.hasGearThirdActive(props)) {
			this.leap = 5;
			dif = 30;
		} else if (TrueGomuHelper.hasGearSecondActive(props)) {
			this.leap = 2;
			dif = 10;
		} else {
			dif = 5;
			this.leap = 3;
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
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Roc Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Gatling"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Jet Elephant Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Jet Gigant Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearFifthActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Gigant Dawn Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearFourthActive(props)) {
				TrueGearFourthAbility g4 = AbilityDataCapability.get(entity).getEquippedAbility(TrueGearFourthAbility.INSTANCE);
				if (g4.isSnakeman()) {
					if (HakiHelper.hasInfusionActive(entity)) {
						this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Hydra"));
					} else {
						this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Black Mamba"));
					}
					this.setDisplayIcon(TrueGomuHelper.getIcon("Black Mamba"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasInfusionActive(entity)) {
					this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Over Kong Gatling"));
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gatling"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no King Kong Gatling"));
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gatling"));
				} else {
					this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Kong Gatling"));
					this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Gatling"));
				}
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Dawn Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Bajrang Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gatling"));
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Jet Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Elephant Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Gigant Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Hawk Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Gatling"));
			} else {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			}
			if (this.getIcon(null).equals(new ResourceLocation("mineminenomi:textures/abilities/gomu_gomu_no_gatling.png")) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Gatling"));
			}
		} else {
			if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasHakiEmissionActive(props) && HakiHelper.hasInfusionActive(entity)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Stamp Roc Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Gatling"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Jet Elephant Stamp Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Jet Gigant Stamp Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearFifthActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Gigant Dawn Stamp Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearFourthActive(props)) {
				TrueGearFourthAbility g4 = AbilityDataCapability.get(entity).getEquippedAbility(TrueGearFourthAbility.INSTANCE);
				if (g4.isSnakeman()) {
					if (HakiHelper.hasInfusionActive(entity)) {
						this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Hydra"));
					} else {
						this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Rhino Stampede Gatling"));
					}
					this.setDisplayIcon(TrueGomuHelper.getIcon("Black Mamba"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasInfusionActive(entity)) {
					this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Over Kong Stamp Gatling"));
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gatling"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no King Kong Stamp Gatling"));
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gatling"));
				} else {
					this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Kong Gatling"));
					this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Gatling"));
				}
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Jet Stamp Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Elephant Stamp Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Gigant Stamp Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Dawn Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			} else if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Bajrang Stamp Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gatling"));
			} else if (HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Tako Stamp"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Gatling"));
			} else {
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Stamp Gatling"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Gatling"));
			}
			if (this.getIcon(null).equals(new ResourceLocation("mineminenomi:textures/abilities/gomu_gomu_no_gatling.png")) && HakiHelper.hasHardeningActive(entity, false, true)) {
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
