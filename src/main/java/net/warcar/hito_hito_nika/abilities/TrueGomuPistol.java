package net.warcar.hito_hito_nika.abilities;

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
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.HakiHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModSounds;

public class TrueGomuPistol extends ChargeableAbility implements IAbilityMode, IExtraUpdateData {
	public static final AbilityCore<TrueGomuPistol> INSTANCE;
	private final GearSet gearSet = new GearSet();
	protected boolean leg = false;

	public TrueGomuPistol(AbilityCore core) {
		super(core);
		this.setCustomIcon("Gomu Gomu no Pistol");
		this.setDisplayName("Gomu Gomu no Pistol");
		this.setMaxCooldown(1.5D);
		this.onEndChargingEvent = this::onUseEvent;
	}

	private boolean onUseEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		AbilityProjectileEntity projectile;
		float speed = 2.0F;
		if (this.leg) {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				projectile = new BajrangStampGunProjectile(player.level, player, this);
				speed = 4f;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new StarStampCannonProjectile(player.level, player, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new GigantDawnStampProjectile(player.level, player, this);
				speed = 2f;
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new DawnStampProjectile(player.level, player, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props) && HakiHelper.hasInfusionActive(player) && TrueGomuHelper.hasHakiEmissionActive(props)) {
				projectile = new JetElephantStampProjectile(player.level, player, this);
				projectile.setDamage(56f);
				speed = 2.5F;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetElephantStampProjectile(player.level, player, this);
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new KingKongStampProjectile(player.level, player, this);
				speed = 3;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
				projectile = new KongStampProjectile(player.level, player, this);
				speed = 1.8F;
			} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
				projectile = new JetRhinoSchneiderProjectile(player.level, player, this, 7f, 100);
				speed = 7f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasInfusionActive(player) && TrueGomuHelper.hasHakiEmissionActive(props)) {
				projectile = new ElephantStampProjectile(player.level, player, this);
				projectile.setDamage(projectile.getDamage() * 2);
				speed = 2F;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new ElephantStampProjectile(player.level, player, this);
				projectile.setCollisionSize(2.5D);
				speed = 1.8F;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetStampProjectile(player.level, player, this);
				speed = 2.5F;
			} else {
				projectile = new StampProjectile(player.level, player, this);
			}
		} else {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				projectile = new BajrangGunProjectile(player.level, player, this);
				speed = 4f;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new StarCannonProjectile(player.level, player, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new GigantDawnPistolProjectile(player.level, player, this);
				speed = 2f;
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new DawnPistolProjectile(player.level, player, this);
				speed = 3f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props) && HakiHelper.hasInfusionActive(player) && TrueGomuHelper.hasHakiEmissionActive(props)) {
				projectile = new JetElephantGunProjectile(player.level, player, this);
				projectile.setDamage(56f);
				speed = 2.5F;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetElephantGunProjectile(player.level, player, this);
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new KingKongGunProjectile(player.level, player, this);
				speed = 3;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
				projectile = new TrueKongGunProjectile(player.level, player, this);
				speed = 1.8F;
			} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
				projectile = new JetCulverinProjectile(player.level, player, this, 7f, 100);
				speed = 7f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasInfusionActive(player) && TrueGomuHelper.hasHakiEmissionActive(props)) {
				projectile = new TrueElephantGunProjectile(player.level, player, this);
				projectile.setDamage(projectile.getDamage() * 2);
				speed = 2F;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new TrueElephantGunProjectile(player.level, player, this);
				projectile.setCollisionSize(2.5D);
				speed = 1.8F;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new TrueJetPistolProjectile(player.level, player, this);
				speed = 2.5F;
			} else {
				projectile = new TruePistolProjectile(player.level, player, this);
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

	public ResourceLocation getIcon(PlayerEntity player) {
		if (player != null)
			this.leg = EntityStatsCapability.get(player).isBlackLeg() && CommonConfig.INSTANCE.isLegAbilities();
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

	protected void updateModes() {
		if (!this.leg){
			if (this.gearSet.containsAbility(GearSixthAbility.INSTANCE)) {
				this.setMaxCooldown(7.0D);
				this.setMaxChargeTime(3.0D);
				this.setDisplayName("Gomu Gomu no Bajrang Gun");
				this.setCustomIcon("King Kong Gun");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiEmissionAbility.INSTANCE)
					&& this.gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
				this.setMaxCooldown(6D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Roc Gun");
				this.setCustomIcon("Gomu Gomu no Pistol");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Jet Elephant Gun");
				this.setCustomIcon("Gomu Gomu no Pistol");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Jet Gigant Pistol");
				this.setCustomIcon("Gomu Gomu no Pistol");
			} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Gigant Dawn Pistol");
				this.setCustomIcon("Gomu Gomu no Pistol");
			} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearFourthAbility.INSTANCE)) {
				this.setMaxCooldown(4D);
				this.setMaxChargeTime(1D);
				this.setDisplayName("Gomu Gomu no Star Cannon");
				this.setCustomIcon("Star Cannon");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE)) {
				this.setMaxCooldown(1.0D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Jet Pistol");
				this.setCustomIcon("Gomu Gomu no Pistol");
			} else if (this.gearSet.containsAbility(TrueGearFourthAbility.INSTANCE)) {
				TrueGearFourthAbility g4 = (TrueGearFourthAbility) this.gearSet.getAbility(TrueGearFourthAbility.INSTANCE).get();
				if (g4.isSnakeman()) {
					this.setMaxCooldown(5.0D);
					this.setMaxChargeTime(0.0D);
					this.setDisplayName("Gomu Gomu no Jet Culverin");
					this.setCustomIcon("Jet Culverine");
				} else if (g4.isBoundman() && gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
					this.setMaxChargeTime(1D);
					this.setMaxCooldown(3D);
					this.setDisplayName("Gomu Gomu no Over Kong Gun");
					this.setCustomIcon("Over Kong Gun");
				} else if (g4.isBoundman() && gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
					this.setMaxChargeTime(2.5D);
					this.setMaxCooldown(7D);
					this.setDisplayName("Gomu Gomu no King Kong Gun");
					this.setCustomIcon("King Kong Gun");
				} else {
					this.setMaxCooldown(4.0D);
					this.setMaxChargeTime(0.5D);
					this.setDisplayName("Gomu Gomu no Kong Gun");
					this.setCustomIcon("Haki Pistol");
				}
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiEmissionAbility.INSTANCE) && this.gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
				this.setMaxCooldown(6D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Roc Gun");
				this.setCustomIcon("Gomu Gomu no Pistol");
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setMaxCooldown(6D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Elephant Gun");
				this.setCustomIcon("Gomu Gomu no Pistol");
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setMaxCooldown(6D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Gigant Pistol");
				this.setCustomIcon("Gomu Gomu no Pistol");
			} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE)) {
				this.setMaxCooldown(1.0D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Dawn Pistol");
				this.setCustomIcon("Gomu Gomu no Pistol");
			} else if (this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Hawk Pistol");
				this.setCustomIcon("Haki Pistol");
			} else {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Pistol");
				this.setCustomIcon("Gomu Gomu no Pistol");
			}
			if (this.getIcon(null).equals(new ResourceLocation("mineminenomi:textures/abilities/gomu_gomu_no_pistol.png")) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setCustomIcon("Haki Pistol");
			}
		} else {
			if (this.gearSet.containsAbility(GearSixthAbility.INSTANCE)) {
				this.setMaxCooldown(7.0D);
				this.setMaxChargeTime(3.0D);
				this.setDisplayName("Gomu Gomu no Bajrang Stamp");
				this.setCustomIcon("King Kong Gun");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiEmissionAbility.INSTANCE)
					&& this.gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
				this.setMaxCooldown(6D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Roc Stamp");
				this.setCustomIcon("Stamp");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Jet Elephant Stamp");
				this.setCustomIcon("Stamp");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Jet Gigant Stamp");
				this.setCustomIcon("Stamp");
			} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Gigant Dawn Stamp");
				this.setCustomIcon("Stamp");
			} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearFourthAbility.INSTANCE)) {
				this.setMaxCooldown(4D);
				this.setMaxChargeTime(1D);
				this.setDisplayName("Gomu Gomu no Star Cannon Stamp");
				this.setCustomIcon("Star Cannon");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE)) {
				this.setMaxCooldown(1.0D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Jet Stamp");
				this.setCustomIcon("Stamp");
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiEmissionAbility.INSTANCE) && this.gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
				this.setMaxCooldown(6D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Roc Stamp");
				this.setCustomIcon("Stamp");
			} else if (this.gearSet.containsAbility(TrueGearFourthAbility.INSTANCE)) {
				TrueGearFourthAbility g4 = (TrueGearFourthAbility) this.gearSet.getAbility(TrueGearFourthAbility.INSTANCE).get();
				if (g4.isSnakeman()) {
					this.setMaxCooldown(5.0D);
					this.setMaxChargeTime(0.0D);
					this.setDisplayName("Gomu Gomu no Rhino Stampede");
					this.setCustomIcon("Jet Culverine");
				} else if (g4.isBoundman() && gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && gearSet.containsAbility(HaoshokuHakiInfusionAbility.INSTANCE)) {
					this.setMaxChargeTime(1D);
					this.setMaxCooldown(3D);
					this.setDisplayName("Gomu Gomu no Over Kong Stamp");
					this.setCustomIcon("Over Kong Gun");
				} else if (g4.isBoundman() && gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
					this.setMaxChargeTime(2.5D);
					this.setMaxCooldown(7D);
					this.setDisplayName("Gomu Gomu no King Kong Stamp");
					this.setCustomIcon("King Kong Gun");
				} else {
					this.setMaxCooldown(4.0D);
					this.setMaxChargeTime(0.5D);
					this.setDisplayName("Gomu Gomu no Kong Stamp");
					this.setCustomIcon("Haki Stamp");
				}
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setMaxCooldown(6D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Elephant Stamp");
				this.setCustomIcon("Stamp");
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setMaxCooldown(6D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Gigant Stamp");
				this.setCustomIcon("Stamp");
			} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE)) {
				this.setMaxCooldown(1.0D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Dawn Stamp");
				this.setCustomIcon("Stamp");
			} else if (this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Hawk Stamp");
				this.setCustomIcon("Haki Stamp");
			} else {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName("Gomu Gomu no Stamp");
				this.setCustomIcon("Stamp");
			}
			if (this.getIcon(null).equals(new ResourceLocation("mineminenomi:textures/abilities/stamp.png")) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setCustomIcon("Haki Stamp");
			}
		}
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gomu Gomu no Pistol", AbilityCategory.DEVIL_FRUITS, TrueGomuPistol::new)).addDescriptionLine("The user stretches their arm to hit the opponent").setSourceHakiNature(SourceHakiNature.HARDENING)
				.setSourceType(SourceType.FIST).build();
	}
}
