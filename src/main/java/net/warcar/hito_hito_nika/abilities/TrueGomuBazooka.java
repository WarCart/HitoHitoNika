package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.warcar.hito_hito_nika.config.CommonConfig;
import net.warcar.hito_hito_nika.projectiles.hand.*;
import net.warcar.hito_hito_nika.projectiles.leg.*;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiHardeningAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.HaoshokuHakiInfusionAbility;
import xyz.pixelatedw.mineminenomi.animations.gomu.GomuBazookaAnimation;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.animations.IAnimation;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModSounds;

public class TrueGomuBazooka extends ChargeableAbility implements IAnimatedAbility, IAbilityMode, IExtraUpdateData {
	public static final AbilityCore<TrueGomuBazooka> INSTANCE;
	private final GearSet gearSet = new GearSet();
	protected boolean leg = false;

	public TrueGomuBazooka(AbilityCore core) {
		super(core);
		this.setCustomIcon("Gomu Gomu no Bazooka");
		this.setDisplayName("Gomu Gomu no Bazooka");
		this.setMaxCooldown(10.0D);
		this.setMaxChargeTime(2.0D);
		this.onEndChargingEvent = this::onEndChargingEvent;
	}

	private boolean onEndChargingEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		AbilityProjectileEntity projectile1;
		AbilityProjectileEntity projectile2;
		float speed = 2.0F;
		double spacingMod = 1.0D;
		if (this.leg) {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				projectile1 = new BajrangStampGunProjectile(player.level, player, this);
				projectile2 = new BajrangStampGunProjectile(player.level, player, this);
				spacingMod = 25d;
				speed = 4f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
				projectile1 = new GigantDawnYariProjectile(player.level, player, this);
				projectile2 = new GigantDawnYariProjectile(player.level, player, this);
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				projectile1 = new DawnYariProjectile(player.level, player, this);
				projectile2 = new DawnYariProjectile(player.level, player, this);
				speed = 3.0F;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				projectile1 = new RhinoRexSchneiderProjectile(player.level, player, this);
				projectile2 = new RhinoRexSchneiderProjectile(player.level, player, this);
				speed = 3.0F;
				spacingMod = 4D;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
				projectile1 = new RhinoSchneiderProjectile(player.level, player, this);
				projectile2 = new RhinoSchneiderProjectile(player.level, player, this);
				speed = 3.0F;
				spacingMod = 2.5D;
			} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
				projectile1 = new JetRhinoSchneiderProjectile(player.level, player, this, 7f, 100);
				projectile2 = new JetRhinoSchneiderProjectile(player.level, player, this, 7f, 100);
				speed = 7F;
				spacingMod = 1.5D;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
				projectile1 = new JetGigantYariProjectile(player.level, player, this);
				projectile2 = new JetGigantYariProjectile(player.level, player, this);
				speed = 3F;
				spacingMod = 2.5D;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile1 = new GigantYariProjectile(player.level, player, this);
				projectile2 = new GigantYariProjectile(player.level, player, this);
				speed = 1.8F;
				spacingMod = 2.5D;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile1 = new JetLanceProjectile(player.level, player, this);
				projectile2 = new JetLanceProjectile(player.level, player, this);
				speed = 3.0F;
			} else {
				projectile1 = new YariProjectile(player.level, player, this);
				projectile2 = new YariProjectile(player.level, player, this);
			}
		} else {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				projectile1 = new BajrangGunProjectile(player.level, player, this);
				projectile2 = new BajrangGunProjectile(player.level, player, this);
				spacingMod = 25d;
				speed = 4f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
				projectile1 = new GigantDawnBazookaProjectile(player.level, player, this);
				projectile2 = new GigantDawnBazookaProjectile(player.level, player, this);
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				projectile1 = new DawnBazookaProjectile(player.level, player, this);
				projectile2 = new DawnBazookaProjectile(player.level, player, this);
				speed = 3.0F;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				projectile1 = new LeoRexBazookaProjectile(player.level, player, this);
				projectile2 = new LeoRexBazookaProjectile(player.level, player, this);
				speed = 5.0F;
				spacingMod = 2.5D;
			} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
				projectile1 = new JetCulverinProjectile(player.level, player, this, 7f, 100);
				projectile2 = new JetCulverinProjectile(player.level, player, this, 7f, 100);
				speed = 7F;
				spacingMod = 1.5D;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
				projectile1 = new JetGrizzlyMagnumProjectile(player.level, player, this);
				projectile2 = new JetGrizzlyMagnumProjectile(player.level, player, this);
				speed = 3F;
				spacingMod = 2.5D;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile1 = new TrueGrizzlyMagnumProjectile(player.level, player, this);
				projectile2 = new TrueGrizzlyMagnumProjectile(player.level, player, this);
				speed = 1.8F;
				spacingMod = 2.5D;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile1 = new TrueJetBazookaProjectile(player.level, player, this);
				projectile2 = new TrueJetBazookaProjectile(player.level, player, this);
				speed = 3.0F;
			} else {
				projectile1 = new TrueBazookaProjectile(player.level, player, this);
				projectile2 = new TrueBazookaProjectile(player.level, player, this);
			}
		}
		Vector3d dirVec = Vector3d.ZERO;
		Direction dir = Direction.fromYRot(player.yRot);
		dirVec = dirVec.add(Math.abs(dir.step().x()), Math.abs(dir.step().y()), Math.abs(dir.step().z())).add(spacingMod, 1.0D, spacingMod);
		double yPos = player.getY() + (double) player.getEyeHeight() - 0.7D;
		projectile1.shootFromRotation(player, player.xRot, player.yRot, 0f, speed, 0f);
		projectile2.shootFromRotation(player, player.xRot, player.yRot, 0f, speed, 0f);
		projectile1.setPos(player.getX() + dirVec.x, yPos, player.getZ() + dirVec.z);
		projectile2.setPos(player.getX() - dirVec.x, yPos, player.getZ() - dirVec.z);
		player.level.addFreshEntity(projectile1);
		player.level.addFreshEntity(projectile2);
		player.level.playSound(null, player.blockPosition(), ModSounds.GOMU_SFX.get(), SoundCategory.PLAYERS, 0.5F, 0.75F);
		return true;
	}

	public IAnimation getAnimation() {
		return GomuBazookaAnimation.INSTANCE;
	}

	public boolean isAnimationActive(LivingEntity entity) {
		return this.isCharging();
	}

	public AbilityCore[] getParents() {
		return new AbilityCore[]{TrueGearSecondAbility.INSTANCE, TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE, FifthGearAbility.INSTANCE, GearSixthAbility.INSTANCE, BusoshokuHakiHardeningAbility.INSTANCE};
	}

	public void enableMode(Ability parent) {
		if (!this.gearSet.containsAbility(parent.getCore())) {
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
			if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setMaxCooldown(10D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Jet Grizzly Magnum");
				this.setCustomIcon("Gomu Gomu no Bazooka");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setMaxCooldown(10D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Jet Gigant Bazooka");
				this.setCustomIcon("Gomu Gomu no Bazooka");
			} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setMaxCooldown(10D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Gigant Dawn Bazooka");
				this.setCustomIcon("Gomu Gomu no Bazooka");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE)) {
				this.setMaxCooldown(7D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Jet Bazooka");
				this.setCustomIcon("Gomu Gomu no Bazooka");
			} else if (this.gearSet.containsAbility(TrueGearFourthAbility.INSTANCE)) {
				TrueGearFourthAbility g4 = (TrueGearFourthAbility) this.gearSet.getAbility(TrueGearFourthAbility.INSTANCE).get();
				if (g4.isSnakeman()) {
					this.setMaxCooldown(5D);
					this.setMaxChargeTime(0D);
					this.setDisplayName("Gomu Gomu no Twin Jet Culverin");
					this.setCustomIcon("Twin Jet Culverine");
				} else if (g4.isBoundman() && gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
					this.setMaxCooldown(15D);
					this.setMaxChargeTime(5.0D);
					this.setDisplayName("Gomu Gomu no Leo Rex Bazooka");
					this.setCustomIcon("Double King Kong Gun");
				} else {
					this.setMaxCooldown(12D);
					this.setMaxChargeTime(2.0D);
					this.setDisplayName("Gomu Gomu no Leo Bazooka");
					this.setCustomIcon("Haki Bazooka");
				}
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setMaxCooldown(15D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Grizzly Magnum");
				this.setCustomIcon("Gomu Gomu no Bazooka");
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setMaxCooldown(15D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Gigant Bazooka");
				this.setCustomIcon("Gomu Gomu no Bazooka");
			} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE)) {
				this.setMaxCooldown(7D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Dawn Bazooka");
				this.setCustomIcon("Gomu Gomu no Bazooka");
			} else if (this.gearSet.containsAbility(GearSixthAbility.INSTANCE)) {
				this.setMaxCooldown(20D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Double Bajrang Gun");
				this.setCustomIcon("Double King Kong Gun");
			} else if (this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setMaxCooldown(10D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Eagle Bazooka");
				this.setCustomIcon("Haki Bazooka");
			} else {
				this.setMaxCooldown(10D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Bazooka");
				this.setCustomIcon("Gomu Gomu no Bazooka");
			}
			if (this.getIcon(null).equals(new ResourceLocation("mineminenomi:textures/abilities/gomu_gomu_no_bazooka.png")) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setCustomIcon("Haki Bazooka");
			}
		} else {
			if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setMaxCooldown(10D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Jet Gigant Yari");
				this.setCustomIcon("Yari");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE) && this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setMaxCooldown(10D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Jet Gigant Yari");
				this.setCustomIcon("Yari");
			} else if (this.gearSet.containsAbility(TrueGearFourthAbility.INSTANCE)) {
				TrueGearFourthAbility g4 = (TrueGearFourthAbility) this.gearSet.getAbility(TrueGearFourthAbility.INSTANCE).get();
				if (g4.isSnakeman()) {
					this.setMaxCooldown(5D);
					this.setMaxChargeTime(0D);
					this.setDisplayName("Gomu Gomu no Twin Rhino Stampede");
					this.setCustomIcon("Twin Jet Culverine");
				} else if (g4.isBoundman() && gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
					this.setMaxCooldown(15D);
					this.setMaxChargeTime(5.0D);
					this.setDisplayName("Gomu Gomu no Rhino Rex Schneider");
					this.setCustomIcon("Double King Kong Gun");
				} else {
					this.setMaxCooldown(12D);
					this.setMaxChargeTime(2.0D);
					this.setDisplayName("Gomu Gomu no Rhino Schneider");
					this.setCustomIcon("Haki Yari");
				}
			} else if (this.gearSet.containsAbility(TrueGearThirdAbility.INSTANCE)) {
				this.setMaxCooldown(15D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Gigant Yari");
				this.setCustomIcon("Yari");
			} else if (this.gearSet.containsAbility(TrueGearSecondAbility.INSTANCE)) {
				this.setMaxCooldown(7D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Jet Lance");
				this.setCustomIcon("Yari");
			} else if (this.gearSet.containsAbility(FifthGearAbility.INSTANCE)) {
				this.setMaxCooldown(7D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Dawn Yari");
				this.setCustomIcon("Yari");
			} else if (this.gearSet.containsAbility(GearSixthAbility.INSTANCE)) {
				this.setMaxCooldown(20D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Double Bajrang Stamp");
				this.setCustomIcon("Double King Kong Gun");
			} else {
				this.setMaxCooldown(10D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName("Gomu Gomu no Yari");
				this.setCustomIcon("Yari");
			}
			if (this.getIcon(null).equals(new ResourceLocation("mineminenomi:textures/abilities/yari.png")) && this.gearSet.containsAbility(BusoshokuHakiHardeningAbility.INSTANCE)) {
				this.setCustomIcon("Haki Yari");
			}
		}
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

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gomu Gomu no Bazooka", AbilityCategory.DEVIL_FRUITS, TrueGomuBazooka::new)).addDescriptionLine("Hits the enemy with both hands to launch them away").setSourceHakiNature(SourceHakiNature.HARDENING)
				.setSourceType(SourceType.FIST).build();
	}
}
