package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.warcar.hito_hito_nika.config.CommonConfig;
import net.warcar.hito_hito_nika.projectiles.hand.*;
import net.warcar.hito_hito_nika.projectiles.leg.*;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiEmissionAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiHardeningAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.HaoshokuHakiInfusionAbility;
import xyz.pixelatedw.mineminenomi.animations.gomu.GomuGatlingAnimation;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.animations.IAnimation;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.helpers.HakiHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

public class TrueGomuGatling extends ContinuousAbility implements IAbilityMode, IAnimatedAbility, IExtraUpdateData {
	public static final AbilityCore<TrueGomuGatling> INSTANCE;
	private final GearSet gearSet = new GearSet();
	private double leap = 3;
	protected boolean leg = false;

	public TrueGomuGatling(AbilityCore core) {
		super(core);
		this.setDisplayName("Gomu Gomu no Gatling");
		this.setCustomIcon("Gomu Gomu no Gatling");
		this.duringContinuityEvent = this::duringContinuityEvent;
		this.onStartContinuityEvent = this::onStartContinuityEvent;
		this.afterContinuityStopEvent = this::onContinuityStopEvent;
	}

	private boolean onStartContinuityEvent(PlayerEntity player) {
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
			this.leap = 0.5;
			dif = 50;
		} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
			this.leap = 0.5;
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
		if (time < 500) {
			this.setThreshold(time);
		} else {
			this.setThreshold(0);
		}
		return true;
	}

	private void duringContinuityEvent(PlayerEntity player, int timer) {
		if (leap >= 1) {
			if (timer % (int) leap == 0)
				this.onUseEvent(player);
		} else if (leap > 0) {
			for (int i = 0; i < 1 / leap; i++) {
				this.onUseEvent(player);
			}
		}
	}

	private void onUseEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		AbilityProjectileEntity projectile;
		float speed = 3.0F;
		float projDamageReduction = 0.8F;
		int projectileSpace = 2;
		if (this.leg) {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				projectile = new BajrangStampGunProjectile(player.level, player, this, 15f);
				projectileSpace = 10;
				speed = 5f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new GigantDawnStampProjectile(player.level, player, this);
				projectileSpace = 9;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetElephantStampProjectile(player.level, player, this);
				projectileSpace = 9;
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new DawnStampProjectile(player.level, player, this);
				speed = 4;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new KingKongStampProjectile(player.level, player, this);
				speed = 4;
				projectileSpace = 6;
				projDamageReduction = 0.6F;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
				projectile = new KongStampProjectile(player.level, player, this);
				projectile.setCollisionSize(2.5D);
				speed = 2.2F;
				projectileSpace = 6;
				projDamageReduction = 0.6F;
			} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
				projectile = new JetRhinoSchneiderProjectile(player.level, player, this, 7f, 20);
				speed = 7F;
				projectileSpace = 6;
				projDamageReduction = 0.4F;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new ElephantStampProjectile(player.level, player, this);
				projectile.setCollisionSize(2.5D);
				speed = 2.4F;
				projectileSpace = 9;
				projDamageReduction = 0.6F;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetStampProjectile(player.level, player, this);
				speed = 3.6F;
			} else {
				projectile = new StampProjectile(player.level, player, this);
			}
		} else {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				projectile = new BajrangGunProjectile(player.level, player, this, 15f);
				projectileSpace = 10;
				speed = 5f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new GigantDawnPistolProjectile(player.level, player, this);
				projectileSpace = 9;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetElephantGunProjectile(player.level, player, this);
				projectileSpace = 9;
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new DawnPistolProjectile(player.level, player, this);
				speed = 2;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new KingKongGunProjectile(player.level, player, this);
				speed = 4;
				projectileSpace = 6;
				projDamageReduction = 0.6F;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
				projectile = new TrueKongGunProjectile(player.level, player, this);
				projectile.setCollisionSize(2.5D);
				speed = 2.2F;
				projectileSpace = 6;
				projDamageReduction = 0.6F;
			} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
				projectile = new JetCulverinProjectile(player.level, player, this, 7f, 20);
				speed = 7F;
				projectileSpace = 6;
				projDamageReduction = 0.4F;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new TrueElephantGunProjectile(player.level, player, this);
				projectile.setCollisionSize(2.5D);
				speed = 2.4F;
				projectileSpace = 9;
				projDamageReduction = 0.6F;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new TrueJetPistolProjectile(player.level, player, this);
				speed = 3.6F;
			} else {
				projectile = new TruePistolProjectile(player.level, player, this);
			}
		}
		projectile.setDamage(projectile.getDamage() * (1.0F - projDamageReduction));
		projectile.setMaxLife((int) ((double) projectile.getMaxLife() * 0.75D));
		projectile.shootFromRotation(player, player.xRot, player.yRot, 0.0F, speed, 3.0F);
		projectile.setPos(player.getX() + WyHelper.randomWithRange(-projectileSpace, projectileSpace) + WyHelper.randomDouble(),
				player.getY() + player.getEyeHeight() + WyHelper.randomWithRange(0, projectileSpace) + WyHelper.randomDouble(), player.getZ() + WyHelper.randomWithRange(-projectileSpace, projectileSpace) + WyHelper.randomDouble());
		player.level.addFreshEntity(projectile);
		player.level.playSound(null, player.blockPosition(), ModSounds.GOMU_SFX.get(), SoundCategory.PLAYERS, 0.5F, 0.6F + this.random.nextFloat() / 2.0F);
	}

	public AbilityCore[] getParents() {
		return new AbilityCore[]{TrueGearSecondAbility.INSTANCE, TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE, FifthGearAbility.INSTANCE, GearSixthAbility.INSTANCE, BusoshokuHakiHardeningAbility.INSTANCE,
				BusoshokuHakiEmissionAbility.INSTANCE, HaoshokuHakiInfusionAbility.INSTANCE};
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

	protected void updateModes() {
		if (!this.leg) {
			if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiEmissionAbility.INSTANCE) && this.gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Roc Gatling");
				this.setCustomIcon("Haki Gatling");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Jet Elephant Gatling");
				this.setCustomIcon("Gomu Gomu no Gatling");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Jet Gigant Gatling");
				this.setCustomIcon("Gomu Gomu no Gatling");
			} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Gigant Dawn Gatling");
				this.setCustomIcon("Gomu Gomu no Gatling");
			} else if (this.gearSet.containsAbility(TrueGearFourthAbility.INSTANCE)) {
				TrueGearFourthAbility g4 = (TrueGearFourthAbility) this.gearSet.getAbility(TrueGearFourthAbility.INSTANCE).get();
				if (g4.isSnakeman()) {
					if (gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
						this.setDisplayName("Gomu Gomu no Hydra");
					} else {
						this.setDisplayName("Gomu Gomu no Black Mamba");
					}
					this.setCustomIcon("Black Mamba");
				} else if (g4.isBoundman() && gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
					this.setDisplayName("Gomu Gomu no Over Kong Gatling");
					this.setCustomIcon("King Kong Gatling");
				} else if (g4.isBoundman() && gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
					this.setDisplayName("Gomu Gomu no King Kong Gatling");
					this.setCustomIcon("King Kong Gatling");
				} else {
					this.setDisplayName("Gomu Gomu no Kong Gatling");
					this.setCustomIcon("Haki Gatling");
				}
			} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Dawn Gatling");
				this.setCustomIcon("Gomu Gomu no Gatling");
			} else if (this.gearSet.containsAbility(GearSixthAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Bajrang Gatling");
				this.setCustomIcon("King Kong Gatling");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Jet Gatling");
				this.setCustomIcon("Gomu Gomu no Gatling");
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Elephant Gatling");
				this.setCustomIcon("Gomu Gomu no Gatling");
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Gigant Gatling");
				this.setCustomIcon("Gomu Gomu no Gatling");
			} else if (this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Hawk Gatling");
				this.setCustomIcon("Haki Gatling");
			} else {
				this.setDisplayName("Gomu Gomu no Gatling");
				this.setCustomIcon("Gomu Gomu no Gatling");
			}
			if (this.getIcon(null).equals(new ResourceLocation("mineminenomi:textures/abilities/gomu_gomu_no_gatling.png")) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setCustomIcon("Haki Gatling");
			}
		} else {
			if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiEmissionAbility.INSTANCE) && this.gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Stamp Roc Gatling");
				this.setCustomIcon("Haki Gatling");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Jet Elephant Stamp Gatling");
				this.setCustomIcon("Gomu Gomu no Gatling");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Jet Gigant Stamp Gatling");
				this.setCustomIcon("Gomu Gomu no Gatling");
			} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Gigant Dawn Stamp Gatling");
				this.setCustomIcon("Gomu Gomu no Gatling");
			} else if (this.gearSet.containsAbility(TrueGearFourthAbility.INSTANCE)) {
				TrueGearFourthAbility g4 = (TrueGearFourthAbility) this.gearSet.getAbility(TrueGearFourthAbility.INSTANCE).get();
				if (g4.isSnakeman()) {
					if (gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
						this.setDisplayName("Gomu Gomu no Hydra");
					} else {
						this.setDisplayName("Gomu Gomu no Rhino Stampede Gatling");
					}
					this.setCustomIcon("Black Mamba");
				} else if (g4.isBoundman() && gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
					this.setDisplayName("Gomu Gomu no Over Kong Stamp Gatling");
					this.setCustomIcon("King Kong Gatling");
				} else if (g4.isBoundman() && gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
					this.setDisplayName("Gomu Gomu no King Kong Stamp Gatling");
					this.setCustomIcon("King Kong Gatling");
				} else {
					this.setDisplayName("Gomu Gomu no Kong Gatling");
					this.setCustomIcon("Haki Gatling");
				}
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Jet Stamp Gatling");
				this.setCustomIcon("Gomu Gomu no Gatling");
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Elephant Stamp Gatling");
				this.setCustomIcon("Gomu Gomu no Gatling");
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Gigant Stamp Gatling");
				this.setCustomIcon("Gomu Gomu no Gatling");
			} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Dawn Gatling");
				this.setCustomIcon("Gomu Gomu no Gatling");
			} else if (this.gearSet.containsAbility(GearSixthAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Bajrang Stamp Gatling");
				this.setCustomIcon("King Kong Gatling");
			} else if (this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setDisplayName("Gomu Gomu no Tako Stamp");
				this.setCustomIcon("Haki Gatling");
			} else {
				this.setDisplayName("Gomu Gomu no Stamp Gatling");
				this.setCustomIcon("Gomu Gomu no Gatling");
			}
			if (this.getIcon(null).equals(new ResourceLocation("mineminenomi:textures/abilities/gomu_gomu_no_gatling.png")) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setCustomIcon("Haki Gatling");
			}
		}
	}

	private void onContinuityStopEvent(PlayerEntity player) {
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
		this.setMaxCooldown(Math.max(((double) this.continueTime / 200) * dif, 3));
	}

	public IAnimation getAnimation() {
		return GomuGatlingAnimation.INSTANCE;
	}

	public boolean isAnimationActive(LivingEntity entity) {
		return this.isContinuous();
	}

	public void setExtraData(CompoundNBT tag) {
		this.leap = tag.getDouble("leap");
		this.leg = tag.getBoolean("leg") && CommonConfig.INSTANCE.isLegAbilities();
	}

	public CompoundNBT getExtraData() {
		CompoundNBT out = new CompoundNBT();
		out.putDouble("leap", this.leap);
		out.putBoolean("leg", this.leg);
		return out;
	}

	public ResourceLocation getIcon(PlayerEntity player) {
		if (player != null)
			this.leg = EntityStatsCapability.get(player).isBlackLeg() && CommonConfig.INSTANCE.isLegAbilities();
		return super.getIcon(player);
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gomu Gomu no Gatling", AbilityCategory.DEVIL_FRUITS, TrueGomuGatling::new)).addDescriptionLine("Rapidly punches enemies using rubber fists").setSourceHakiNature(SourceHakiNature.HARDENING)
				.setSourceType(SourceType.FIST).build();
	}
}
