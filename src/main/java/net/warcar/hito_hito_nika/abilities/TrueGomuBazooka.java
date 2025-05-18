package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.hito_hito_nika.config.CommonConfig;
import net.warcar.hito_hito_nika.projectiles.hand.*;
import net.warcar.hito_hito_nika.projectiles.leg.*;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AnimationComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AnimeScreamComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChargeComponent;
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
import xyz.pixelatedw.mineminenomi.init.ModSounds;

public class TrueGomuBazooka extends Ability implements IExtraUpdateData {
	public static final AbilityCore<TrueGomuBazooka> INSTANCE;
	public static final TranslationTextComponent JET_GRIZZLY_MAGNUM = TrueGomuHelper.getName("Gomu Gomu no Jet Grizzly Magnum");
	public static final TranslationTextComponent JET_GIANT_BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Jet Giant Bazooka");
	public static final TranslationTextComponent GIANT_DAWN_BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Giant Dawn Bazooka");
	public static final TranslationTextComponent JET_BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Jet Bazooka");
	public static final TranslationTextComponent TWIN_JET_CULVERIN = TrueGomuHelper.getName("Gomu Gomu no Twin Jet Culverin");
	public static final TranslationTextComponent LEO_REX_BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Leo Rex Bazooka");
	public static final TranslationTextComponent LEO_BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Leo Bazooka");
	public static final TranslationTextComponent GRIZZLY_MAGNUM = TrueGomuHelper.getName("Gomu Gomu no Grizzly Magnum");
	public static final TranslationTextComponent GIANT_BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Giant Bazooka");
	public static final TranslationTextComponent DAWN_BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Dawn Bazooka");
	public static final TranslationTextComponent DOUBLE_BAJRANG_GUN = TrueGomuHelper.getName("Gomu Gomu no Double Bajrang Gun");
	public static final TranslationTextComponent EAGLE_BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Eagle Bazooka");
	public static final TranslationTextComponent BAZOOKA = TrueGomuHelper.getName("Gomu Gomu no Bazooka");
	public static final TranslationTextComponent JET_GIANT_YARI = TrueGomuHelper.getName("Gomu Gomu no Jet Giant Yari");
	public static final TranslationTextComponent TWIN_RHINO_STAMPEDE = TrueGomuHelper.getName("Gomu Gomu no Twin Rhino Stampede");
	public static final TranslationTextComponent RHINO_REX_SCHNEIDER = TrueGomuHelper.getName("Gomu Gomu no Rhino Rex Schneider");
	public static final TranslationTextComponent RHINO_SCHNEIDER = TrueGomuHelper.getName("Gomu Gomu no Rhino Schneider");
	public static final TranslationTextComponent GIANT_YARI = TrueGomuHelper.getName("Gomu Gomu no Giant Yari");
	public static final TranslationTextComponent JET_LANCE = TrueGomuHelper.getName("Gomu Gomu no Jet Lance");
	public static final TranslationTextComponent DAWN_YARI = TrueGomuHelper.getName("Gomu Gomu no Dawn Yari");
	public static final TranslationTextComponent DOUBLE_BAJRANG_STAMP = TrueGomuHelper.getName("Gomu Gomu no Double Bajrang Stamp");
	public static final TranslationTextComponent YARI = TrueGomuHelper.getName("Gomu Gomu no Yari");
	private final ChargeComponent chargeComponent;
	private final ProjectileComponent projectileComponent;
	private final AnimationComponent animationComponent;
	private final AnimeScreamComponent trueScreamComponent = new AnimeScreamComponent(this) {
		@Override
		public void setupDefaultScreams(IAbility ability) {
			ability.getComponent(ModAbilityKeys.CHARGE).ifPresent(chargeComponent -> {
				chargeComponent.addStartEvent((entity, iAbility) -> this.scream(entity, "Gomu gomu no..."));
				chargeComponent.addEndEvent((entity, iAbility) -> this.scream(entity, ability.getDisplayName().getString().replace("Gomu Gomu no ", "")));
			});
		}
	};
	private float cooldown;
	private int chargeTime;
	private float speed = 2;
	private float spacingMod = 1;
	protected boolean leg = false;

	public TrueGomuBazooka(AbilityCore<TrueGomuBazooka> core) {
		super(core);
		this.addTickEvent(this::updateModes);
		this.chargeComponent = (new ChargeComponent(this)).addEndEvent(this::onEndCharging);
		this.projectileComponent = new ProjectileComponent(this, this::createProjectile);
		this.animationComponent = new AnimationComponent(this);
		this.cooldown = 200.0F;
		this.chargeTime = 40;
		this.isNew = true;
		this.addComponents(this.chargeComponent, this.projectileComponent, this.animationComponent, this.trueScreamComponent);
		this.addUseEvent(this::onUse);
	}

	private void onUse(LivingEntity entity, IAbility abl) {
		this.chargeComponent.startCharging(entity, this.chargeTime);
		this.animationComponent.start(entity, ModAnimations.GOMU_BAZOOKA);
	}

	private AbilityProjectileEntity createProjectile(LivingEntity player) {
		AbilityProjectileEntity projectile;
		IAbilityData props = AbilityDataCapability.get(player);
		if (this.leg) {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				projectile = new BajrangStampGunProjectile(player.level, player, this);
				spacingMod = 25F;
				speed = 4f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new GigantDawnYariProjectile(player.level, player, this);
				spacingMod = 1;
				speed = 2F;
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new DawnYariProjectile(player.level, player, this);
				spacingMod = 1;
				speed = 3.0F;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new RhinoRexSchneiderProjectile(player.level, player, this);
				speed = 3.0F;
				spacingMod = 4F;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props)) {
				projectile = new RhinoSchneiderProjectile(player.level, player, this);
				speed = 3.0F;
				spacingMod = 2.5F;
			} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
				projectile = new JetRhinoSchneiderProjectile(player.level, player, this, 7f, 100);
				speed = 7F;
				spacingMod = 1.5F;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetGigantYariProjectile(player.level, player, this);
				speed = 3F;
				spacingMod = 2.5F;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new GigantYariProjectile(player.level, player, this);
				speed = 1.8F;
				spacingMod = 2.5F;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetLanceProjectile(player.level, player, this);
				spacingMod = 1;
				speed = 3.0F;
			} else {
				projectile = new YariProjectile(player.level, player, this);
				spacingMod = 1;
				speed = 2F;
			}
		} else {
			if (TrueGomuHelper.hasAbilityActive(props, GearSixthAbility.INSTANCE)) {
				projectile = new BajrangGunProjectile(player.level, player, this);
				spacingMod = 25F;
				speed = 4f;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new GigantDawnBazookaProjectile(player.level, player, this);
				spacingMod = 1;
				speed = 2F;
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				projectile = new DawnBazookaProjectile(player.level, player, this);
				spacingMod = 1;
				speed = 3.0F;
			} else if (TrueGomuHelper.hasGearFourthBoundmanActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new LeoRexBazookaProjectile(player.level, player, this);
				speed = 5.0F;
				spacingMod = 2.5F;
			} else if (TrueGomuHelper.hasGearFourthSnakemanActive(props)) {
				projectile = new JetCulverinProjectile(player.level, player, this, 7f, 100);
				speed = 7F;
				spacingMod = 1.5F;
			} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new JetGrizzlyMagnumProjectile(player.level, player, this);
				speed = 3F;
				spacingMod = 2.5F;
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				projectile = new TrueGrizzlyMagnumProjectile(player.level, player, this);
				speed = 1.8F;
				spacingMod = 2.5F;
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				projectile = new TrueJetBazookaProjectile(player.level, player, this);
				speed = 3.0F;
				spacingMod = 1;
			} else {
				projectile = new TrueBazookaProjectile(player.level, player, this);
				spacingMod = 1;
				speed = 2F;
			}
		}
		return projectile;
	}

	private void onEndCharging(LivingEntity player, IAbility abl) {
		this.animationComponent.stop(player);
		AbilityProjectileEntity projectile1 = this.projectileComponent.getNewProjectile(player);
		AbilityProjectileEntity projectile2 = this.projectileComponent.getNewProjectile(player);
		Vector3d dirVec = Vector3d.ZERO;
		Direction dir = Direction.fromYRot(player.yRot);
		dirVec = dirVec.add(Math.abs(dir.getNormal().getX()), Math.abs(dir.getNormal().getY()), Math.abs(dir.getNormal().getZ()));
		dirVec = dirVec.multiply(this.spacingMod, 1.0D, this.spacingMod);
		projectile1.moveTo(player.getX() + dirVec.z, player.getEyeY(), player.getZ() + dirVec.x, 0.0F, 0.0F);
		projectile2.moveTo(player.getX() - dirVec.z, player.getEyeY(), player.getZ() - dirVec.x, 0.0F, 0.0F);
		this.projectileComponent.shoot(projectile1, player, this.speed, 0.0F);
		this.projectileComponent.shoot(projectile2, player, this.speed, 0.0F);
		player.swing(Hand.MAIN_HAND, true);
		player.level.playSound(null, player.blockPosition(), ModSounds.GOMU_SFX.get(), SoundCategory.PLAYERS, 2.0F, 0.75F);
		this.cooldownComponent.startCooldown(player, this.cooldown);
	}

	public void setMaxChargeTime(double time) {
		this.chargeTime = (int) (time * 20);
	}

	public void setMaxCooldown(double time) {
		this.cooldown = (int) (time * 20);
	}

	protected void updateModes(LivingEntity entity, IAbility abl) {
		IAbilityData props = AbilityDataCapability.get(entity);
		if (!EntityStatsCapability.get(entity).isBlackLeg()) {
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
				this.setMaxCooldown(10D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(GIANT_DAWN_BAZOOKA);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Bazooka"));
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				this.setMaxCooldown(7D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(JET_BAZOOKA);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Bazooka"));
			} else if (TrueGomuHelper.hasGearFourthActive(props)) {
				TrueGearFourthAbility g4 = AbilityDataCapability.get(entity).getEquippedAbility(TrueGearFourthAbility.INSTANCE);
				if (g4.isSnakeman()) {
					this.setMaxCooldown(5D);
					this.setMaxChargeTime(0D);
					this.setDisplayName(TWIN_JET_CULVERIN);
					this.setDisplayIcon(TrueGomuHelper.getIcon("Twin Jet Culverine"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setMaxCooldown(15D);
					this.setMaxChargeTime(5.0D);
					this.setDisplayName(LEO_REX_BAZOOKA);
					this.setDisplayIcon(TrueGomuHelper.getIcon("Double King Kong Gun"));
				} else {
					this.setMaxCooldown(12D);
					this.setMaxChargeTime(2.0D);
					this.setDisplayName(LEO_BAZOOKA);
					this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Bazooka"));
				}
			} else if (TrueGomuHelper.hasGearThirdActive(props) && HakiHelper.hasHardeningActive(entity, false, true)) {
				this.setMaxCooldown(15D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(GRIZZLY_MAGNUM);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Bazooka"));
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				this.setMaxCooldown(15D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(GIANT_BAZOOKA);
				this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Bazooka"));
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				this.setMaxCooldown(7D);
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
			} else if (TrueGomuHelper.hasGearSecondActive(props) && TrueGomuHelper.hasGearThirdActive(props)) {
				this.setMaxCooldown(10D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(JET_GIANT_YARI);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Yari"));
			} else if (TrueGomuHelper.hasGearFourthActive(props)) {
				TrueGearFourthAbility g4 = AbilityDataCapability.get(entity).getEquippedAbility(TrueGearFourthAbility.INSTANCE);
				if (g4.isSnakeman()) {
					this.setMaxCooldown(5D);
					this.setMaxChargeTime(0D);
					this.setDisplayName(TWIN_RHINO_STAMPEDE);
					this.setDisplayIcon(TrueGomuHelper.getIcon("Twin Jet Culverine"));
				} else if (g4.isBoundman() && TrueGomuHelper.hasGearThirdActive(props)) {
					this.setMaxCooldown(15D);
					this.setMaxChargeTime(5.0D);
					this.setDisplayName(RHINO_REX_SCHNEIDER);
					this.setDisplayIcon(TrueGomuHelper.getIcon("Double King Kong Gun"));
				} else {
					this.setMaxCooldown(12D);
					this.setMaxChargeTime(2.0D);
					this.setDisplayName(RHINO_SCHNEIDER);
					this.setDisplayIcon(TrueGomuHelper.getIcon("Haki Yari"));
				}
			} else if (TrueGomuHelper.hasGearThirdActive(props)) {
				this.setMaxCooldown(15D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(GIANT_YARI);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Yari"));
			} else if (TrueGomuHelper.hasGearSecondActive(props)) {
				this.setMaxCooldown(7D);
				this.setMaxChargeTime(2.0D);
				this.setDisplayName(JET_LANCE);
				this.setDisplayIcon(TrueGomuHelper.getIcon("Yari"));
			} else if (TrueGomuHelper.hasGearFifthActive(props)) {
				this.setMaxCooldown(7D);
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

	public ResourceLocation getIcon(LivingEntity player) {
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
