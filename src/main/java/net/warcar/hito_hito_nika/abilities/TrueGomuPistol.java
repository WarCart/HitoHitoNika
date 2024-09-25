package net.warcar.hito_hito_nika.abilities;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.warcar.hito_hito_nika.projectiles.hand.*;
import net.warcar.hito_hito_nika.projectiles.leg.*;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiEmissionAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiHardeningAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.HaoshokuHakiInfusionAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AnimeScreamComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChargeComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ProjectileComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.SlotDecorationComponent;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.helpers.HakiHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.RendererHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModAbilityKeys;
import xyz.pixelatedw.mineminenomi.init.ModSounds;

public class TrueGomuPistol extends Ability {
	public static final AbilityCore<TrueGomuPistol> INSTANCE;
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
	private double cooldown = 1.5;
	private float speed = 2;
	private float charge = 0;

	public TrueGomuPistol(AbilityCore core) {
		super(core);
		this.addTickEvent(this::updateModes);
		chargeComponent = new ChargeComponent(this);
		projectileComponent = new ProjectileComponent(this, this::createProjectile);
		if (this.isClientSide()) {
			this.getComponent(ModAbilityKeys.SLOT_DECORATION).ifPresent(component -> component.addPostRenderEvent(100, this::hakiOverlay));
		}
		this.addComponents(chargeComponent, projectileComponent, trueScreamComponent);
		this.addUseEvent(((entity, ability) -> this.chargeComponent.startCharging(entity, charge * 20)));
		this.chargeComponent.addEndEvent(this::shoot);
		this.isNew = true;
	}

	@OnlyIn(Dist.CLIENT)
	private void hakiOverlay(LivingEntity entity, Minecraft client, MatrixStack matrixStack, float x, float y, float partialTicks) {
		IAbilityData props = AbilityDataCapability.get(entity);
		if (HakiHelper.hasInfusionActive(entity) && TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
			RendererHelper.drawIcon(TrueGomuHelper.getIcon("Over Kong Gun Overlay"), matrixStack, x + 4, y + 4, 1.5f, 16, 16, HakiHelper.getHaoshokuColour(entity));
		}
	}

	private AbilityProjectileEntity createProjectile(LivingEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		AbilityProjectileEntity projectile;
		if (EntityStatsCapability.get(player).isBlackLeg()) {
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
				projectile = new JetRhinoSchneiderProjectile(player.level, player, this, 7f, 10);
				speed = 7f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasInfusionActive(player) && TrueGomuHelper.hasHakiEmissionActive(props)) {
				projectile = new ElephantStampProjectile(player.level, player, this);
				projectile.setDamage(projectile.getDamage() * 2);
				speed = 2F;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new ElephantStampProjectile(player.level, player, this);
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
				projectile = new JetCulverinProjectile(player.level, player, this, 7f, 10);
				speed = 7f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasInfusionActive(player) && TrueGomuHelper.hasHakiEmissionActive(props)) {
				projectile = new TrueElephantGunProjectile(player.level, player, this);
				projectile.setDamage(projectile.getDamage() * 2);
				speed = 2F;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new TrueElephantGunProjectile(player.level, player, this);
				speed = 1.8F;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new TrueJetPistolProjectile(player.level, player, this);
				speed = 2.5F;
			} else {
				projectile = new TruePistolProjectile(player.level, player, this);
			}
		}
		return projectile;
	}

	private void shoot(LivingEntity player, IAbility ability) {
		this.projectileComponent.shoot(player, speed, 0);
		player.level.playSound(null, player.blockPosition(), ModSounds.GOMU_SFX.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
		this.cooldownComponent.startCooldown(player, (float) this.cooldown * 20);
	}

	protected void updateModes(LivingEntity entity, IAbility abl) {
		IAbilityData props = AbilityDataCapability.get(entity);
		if (!EntityStatsCapability.get(entity).isBlackLeg()){
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				this.setMaxCooldown(7.0D);
				this.setMaxChargeTime(3.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Bajrang Gun"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasHakiEmissionActive(props)
					&& HakiHelper.hasInfusionActive(entity)) {
				this.setMaxCooldown(6D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Roc Gun"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Jet Elephant Gun"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Jet Gigant Pistol"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			} else if (TrueGomuHelper.hasGearFifthActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Gigant Dawn Pistol"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			} else if (TrueGomuHelper.hasGearFifthActive(props) && TrueGomuHelper.hasGearFourthActive(props)) {
				this.setMaxCooldown(4D);
				this.setMaxChargeTime(1D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Star Cannon"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Star Cannon"));
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				this.setMaxCooldown(1.0D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Jet Pistol"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			} else if (TrueGomuHelper.hasGearFourthActive(props)) {
				TrueGearFourthAbility g4 = AbilityDataCapability.get(entity).getEquippedAbility(TrueGearFourthAbility.INSTANCE);
				if (g4.isSnakeman()) {
					this.setMaxCooldown(5.0D);
					this.setMaxChargeTime(0.0D);
					this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Jet Culverin"));
					this.setDisplayIcon(TrueGomuHelper.getIcon("Jet Culverine"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasInfusionActive(entity)) {
					this.setMaxChargeTime(1D);
					this.setMaxCooldown(3D);
					this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Over Kong Gun"));
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setMaxChargeTime(2.5D);
					this.setMaxCooldown(7D);
					this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no King Kong Gun"));
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else {
					this.setMaxCooldown(4.0D);
					this.setMaxChargeTime(0.5D);
					this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Kong Gun"));
					this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Pistol"));
				}
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasHakiEmissionActive(props) && HakiHelper.hasInfusionActive(entity)) {
				this.setMaxCooldown(6D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Roc Gun"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setMaxCooldown(6D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Elephant Gun"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				this.setMaxCooldown(6D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Gigant Pistol"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				this.setMaxCooldown(1.0D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Dawn Pistol"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			} else if (HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Hawk Pistol"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Pistol"));
			} else {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Pistol"));
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Pistol"));
			}
			if (this.getIcon(null).equals(new ResourceLocation("mineminenomi:textures/abilities/gomu_gomu_no_pistol.png")) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Pistol"));
			}
		} else {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				this.setMaxCooldown(7.0D);
				this.setMaxChargeTime(3.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Bajrang Stamp"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasHakiEmissionActive(props)
					&& HakiHelper.hasInfusionActive(entity)) {
				this.setMaxCooldown(6D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Roc Stamp"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Jet Elephant Stamp"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Jet Gigant Stamp"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			} else if (TrueGomuHelper.hasGearFifthActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Gigant Dawn Stamp"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			} else if (TrueGomuHelper.hasGearFifthActive(props) && TrueGomuHelper.hasGearFourthActive(props)) {
				this.setMaxCooldown(4D);
				this.setMaxChargeTime(1D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Star Cannon Stamp"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Star Cannon"));
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				this.setMaxCooldown(1.0D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Jet Stamp"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasHakiEmissionActive(props) && HakiHelper.hasInfusionActive(entity)) {
				this.setMaxCooldown(6D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Roc Stamp"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			} else if (TrueGomuHelper.hasGearFourthActive(props)) {
				TrueGearFourthAbility g4 = AbilityDataCapability.get(entity).getEquippedAbility(TrueGearFourthAbility.INSTANCE);
				if (g4.isSnakeman()) {
					this.setMaxCooldown(5.0D);
					this.setMaxChargeTime(0.0D);
					this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Rhino Stampede"));
					this.setDisplayIcon(TrueGomuHelper.getIcon("Jet Culverine"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearFourthActive(props) && HakiHelper.hasInfusionActive(entity)) {
					this.setMaxChargeTime(1D);
					this.setMaxCooldown(3D);
					this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Over Kong Stamp"));
					this.setDisplayIcon(TrueGomuHelper.getIcon("Over Kong Gun"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearFourthActive(props)) {
					this.setMaxChargeTime(2.5D);
					this.setMaxCooldown(7D);
					this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no King Kong Stamp"));
					this.setDisplayIcon(TrueGomuHelper.getIcon("King Kong Gun"));
				} else {
					this.setMaxCooldown(4.0D);
					this.setMaxChargeTime(0.5D);
					this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Kong Stamp"));
					this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Stamp"));
				}
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setMaxCooldown(6D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Elephant Stamp"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				this.setMaxCooldown(6D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Gigant Stamp"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				this.setMaxCooldown(1.0D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Dawn Stamp"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			} else if (HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Hawk Stamp"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Stamp"));
			} else {
				this.setMaxCooldown(1.5D);
				this.setMaxChargeTime(0.0D);
				this.setDisplayName(TrueGomuHelper.getName("Gomu Gomu no Stamp"));
				this.setDisplayIcon(TrueGomuHelper.getIcon("Stamp"));
			}
			if (this.getIcon(entity).equals(new ResourceLocation("mineminenomi:textures/abilities/stamp.png")) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Stamp"));
			}
		}
	}

	private void setMaxChargeTime(double time) {
		this.charge = (float) time;
	}

	public void setMaxCooldown(double cooldown) {
		this.cooldown = cooldown;
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gomu Gomu no Pistol", AbilityCategory.DEVIL_FRUITS, TrueGomuPistol::new)).addDescriptionLine("The user stretches their arm to hit the opponent").setSourceHakiNature(SourceHakiNature.HARDENING)
				.setSourceType(SourceType.FIST).build();
	}
}
