package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.warcar.hito_hito_nika.config.CommonConfig;
import net.warcar.hito_hito_nika.projectiles.*;
import net.warcar.hito_hito_nika.projectiles.hand.*;
import net.warcar.hito_hito_nika.projectiles.leg.*;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiEmissionAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiHardeningAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.HaoshokuHakiInfusionAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.helpers.HakiHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.data.entity.haki.HakiDataCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModSounds;

public class StrongGomuPistol extends ChargeableAbility implements IAbilityMode, IExtraUpdateData {
	public static final AbilityCore<StrongGomuPistol> INSTANCE;
	private final GearSet gearSet = new GearSet();
	protected boolean leg = false;

	public StrongGomuPistol(AbilityCore core) {
		super(core);
		this.updateModes();
		this.duringChargingEvent = this::duringContinuityEvent;
		this.onEndChargingEvent = this::beforeContinuityStopEvent;
	}

	private void duringContinuityEvent(PlayerEntity player, int i) {
		IAbilityData props = AbilityDataCapability.get(player);
		if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
			HakiDataCapability.get(player).alterHakiOveruse(10);
		} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
			HakiDataCapability.get(player).alterHakiOveruse(15);
		}
	}

	private boolean beforeContinuityStopEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		AbilityProjectileEntity projectile;
		float speed = 2.5F;
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
				projectile = new JetRhinoSchneiderProjectile(player.level, player, this, 30f, 200);
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
				projectile = new JetCulverinProjectile(player.level, player, this, 30f, 200);
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
		player.level.addFreshEntity(projectile);
		projectile.shootFromRotation(player, player.xRot, player.yRot, 0.0F, speed, 1.0F);
		player.level.playSound(null, player.blockPosition(), ModSounds.GOMU_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
		return true;
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
			if (this.gearSet.containsAbility(GearSixthAbility.INSTANCE)) {
				this.setMaxChargeTime(25D);
				this.setMaxCooldown(120D);
				this.setDisplayName("Gomu Gomu no King Bajrang Gun");
				this.setCustomIcon("King Bajrang Gun");
			} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE)) {
				this.setMaxChargeTime(15D);
				this.setMaxCooldown(40D);
				this.setDisplayName("Gomu Gomu no Bajrang Gun");
				this.setCustomIcon("King Kong Gun");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiEmissionAbility.INSTANCE)) {
				this.setMaxCooldown(8D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Red Roc");
				this.setCustomIcon("Fire Pistol");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setMaxCooldown(4D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Strong Jet Gigant Pistol");
				this.setCustomIcon("Gomu Gomu no Pistol");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setMaxCooldown(4D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Red Hawk");
				this.setCustomIcon("Fire Pistol");
			} else if (this.gearSet.containsAbility(TrueGearFourthAbility.INSTANCE)) {
				TrueGearFourthAbility g4 = (TrueGearFourthAbility) this.gearSet.getAbility(TrueGearFourthAbility.INSTANCE).get();
				if (g4.isSnakeman()) {
					this.setMaxChargeTime(3D);
					this.setMaxCooldown(10D);
					this.setDisplayName("Gomu Gomu no King Cobra");
					this.setCustomIcon("King Cobra");
				} else if (g4.isBoundman() && gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE) && gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
					this.setMaxChargeTime(7.5D);
					this.setMaxCooldown(20D);
					this.setDisplayName("Gomu Gomu no Over King King Kong Gun");
					this.setCustomIcon("Over Kong Gun");
				} else if (g4.isBoundman() && gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
					this.setMaxChargeTime(15D);
					this.setMaxCooldown(40D);
					this.setDisplayName("Gomu Gomu no King King King Kong Gun");
					this.setCustomIcon("King Kong Gun");
				} else if (g4.isBoundman() && gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
					this.setMaxChargeTime(5D);
					this.setMaxCooldown(15D);
					this.setDisplayName("Gomu Gomu no Over Kong Gun");
					this.setCustomIcon("Over Kong Gun");
				} else if (g4.isBoundman()) {
					this.setMaxChargeTime(10D);
					this.setMaxCooldown(30D);
					this.setDisplayName("Gomu Gomu no King Kong Gun");
					this.setCustomIcon("King Kong Gun");
				}
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setMaxCooldown(9D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Thor Elephant Gun");
				this.setCustomIcon("Haki Pistol");
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setMaxCooldown(9D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Strong Gigant Pistol");
				this.setCustomIcon("Gomu Gomu no Pistol");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Strong Jet Pistol");
				this.setCustomIcon("Gomu Gomu no Pistol");
			} else if (this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setMaxCooldown(2D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Strong Hawk Pistol");
				this.setCustomIcon("Haki Pistol");
			} else {
				this.setMaxCooldown(2D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Strong Pistol");
				this.setCustomIcon("Gomu Gomu no Pistol");
			}
		} else {
			if (this.gearSet.containsAbility(GearSixthAbility.INSTANCE)) {
				this.setMaxChargeTime(25D);
				this.setMaxCooldown(120D);
				this.setDisplayName("Gomu Gomu no King Bajrang Stamp");
				this.setCustomIcon("King Bajrang Gun");
			} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE)) {
				this.setMaxChargeTime(15D);
				this.setMaxCooldown(40D);
				this.setDisplayName("Gomu Gomu no Bajrang Stamp");
				this.setCustomIcon("King Kong Gun");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiEmissionAbility.INSTANCE)) {
				this.setMaxCooldown(8D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Red Roc Stamp");
				this.setCustomIcon("Fire Stamp");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setMaxCooldown(4D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Strong Jet Gigant Stamp");
				this.setCustomIcon("Stamp");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setMaxCooldown(4D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Red Hawk Stamp");
				this.setCustomIcon("Fire Stamp");
			} else if (this.gearSet.containsAbility(TrueGearFourthAbility.INSTANCE)) {
				TrueGearFourthAbility g4 = (TrueGearFourthAbility) this.gearSet.getAbility(TrueGearFourthAbility.INSTANCE).get();
				if (g4.isSnakeman()) {
					this.setMaxChargeTime(3D);
					this.setMaxCooldown(10D);
					this.setDisplayName("Gomu Gomu no King Cobra Stamp");
					this.setCustomIcon("King Cobra");
				} else if (g4.isBoundman() && gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE) && gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
					this.setMaxChargeTime(7.5D);
					this.setMaxCooldown(20D);
					this.setDisplayName("Gomu Gomu no Over Over King Kong Stamp");
					this.setCustomIcon("Over Kong Gun");
				} else if (g4.isBoundman() && gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
					this.setMaxChargeTime(15D);
					this.setMaxCooldown(40D);
					this.setDisplayName("Gomu Gomu no King King King Kong Stamp");
					this.setCustomIcon("King Kong Gun");
				} else if (g4.isBoundman() && gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
					this.setMaxChargeTime(5D);
					this.setMaxCooldown(15D);
					this.setDisplayName("Gomu Gomu no Over Kong Stamp");
					this.setCustomIcon("Over Kong Gun");
				} else if (g4.isBoundman()) {
					this.setMaxChargeTime(10D);
					this.setMaxCooldown(30D);
					this.setDisplayName("Gomu Gomu no King Kong Stamp");
					this.setCustomIcon("King Kong Gun");
				}
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setMaxCooldown(9D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Thor Gigant Stamp");
				this.setCustomIcon("Haki Stamp");
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setMaxCooldown(9D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Strong Gigant Stamp");
				this.setCustomIcon("Stamp");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Strong Jet Stamp");
				this.setCustomIcon("Stamp");
			} else if (this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setMaxCooldown(2D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Strong Hawk Stamp");
				this.setCustomIcon("Haki Stamp");
			} else {
				this.setMaxCooldown(2D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Strong Stamp");
				this.setCustomIcon("Stamp");
			}
		}
	}

	public ResourceLocation getIcon(PlayerEntity player) {
		if (player != null) {
			boolean in = EntityStatsCapability.get(player).isBlackLeg() && CommonConfig.INSTANCE.isLegAbilities();
			if (this.leg != in) {
				this.updateModes();
			}
			this.leg = in;
		}
		return super.getIcon(player);
	}

	public void setExtraData(CompoundNBT tag) {
		this.leg = tag.getBoolean("leg") && CommonConfig.INSTANCE.isLegAbilities();
	}

	public CompoundNBT getExtraData() {
		CompoundNBT out = new CompoundNBT();
		out.putBoolean("leg", this.leg);
		return out;
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("Strong Gomu Gomu no Pistol", AbilityCategory.DEVIL_FRUITS, StrongGomuPistol::new)).addDescriptionLine("Almost same as simple pistol... until You get Haki").setSourceHakiNature(SourceHakiNature.HARDENING)
				.setSourceType(SourceType.FIST).build();
	}
}
