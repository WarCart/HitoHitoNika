package net.warcar.hito_hito_nika.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.abilities.GomuFusenAbility;
import net.warcar.hito_hito_nika.abilities.GomuMorphsAbility;
import net.warcar.hito_hito_nika.abilities.TrueGearFourthAbility;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import xyz.pixelatedw.mineminenomi.abilities.gomu.GomuGomuNoDawnWhipAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.BonusOperation;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceElement;
import xyz.pixelatedw.mineminenomi.api.events.ability.AbilityUseEvent;
import xyz.pixelatedw.mineminenomi.api.events.stats.DorikiEvent;
import xyz.pixelatedw.mineminenomi.api.events.stats.HakiExpEvent;
import xyz.pixelatedw.mineminenomi.api.helpers.HakiHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.ItemsHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.entities.projectiles.extra.CannonBallProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.extra.NormalBulletProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.extra.PopGreenProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.hitodaibutsu.ImpactBlastProjectile;
import xyz.pixelatedw.mineminenomi.events.abilities.AbilityValidationEvents;
import xyz.pixelatedw.mineminenomi.init.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = HitoHitoNoMiNikaMod.MOD_ID)
public class TrueGomuPassiveEffects {
	@SubscribeEvent
	public static void onEntityHurt(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			DamageSource source = event.getSource();
			Entity instantSource = source.getDirectEntity();
			Entity trueSource = source.getEntity();
			PlayerEntity attacked = (PlayerEntity) event.getEntityLiving();
			IDevilFruit props = DevilFruitCapability.get(attacked);
			if (props.hasDevilFruit(ModAbilities.GOMU_GOMU_NO_MI) && !source.isMagic()) {
				float reduction = 0.0F;
				ArrayList<String> instantSources = new ArrayList<>(Arrays.asList("mob", "player"));
				boolean a = false;
				if (instantSource instanceof LivingEntity) {
					ItemStack mainhandGear = ((LivingEntity) instantSource).getItemBySlot(EquipmentSlotType.MAINHAND);
					a = trueSource instanceof LivingEntity && !HakiHelper.hasHardeningActive((LivingEntity) instantSource) && instantSources.contains(source.getMsgId()) && !source.isProjectile()
							&& getGomuDamagingItems(mainhandGear.getItem()) && !ItemsHelper.isKairosekiWeapon(mainhandGear);
				}
				boolean b = source.isProjectile() && instantSource instanceof AbilityProjectileEntity && ((AbilityProjectileEntity) instantSource).isPhysical() && !((AbilityProjectileEntity) instantSource).isAffectedByHaki();
				if ((a || b) && !source.isExplosion()) {
					reduction = 0.75F;
				}
				if (source.getMsgId().equals(DamageSource.LIGHTNING_BOLT.getMsgId())) {
					reduction = 1.0F;
				}
				if (source instanceof ModDamageSource && ((ModDamageSource) source).getElement() == SourceElement.LIGHTNING) {
					reduction = 1.0F;
				}
				event.setAmount(event.getAmount() * (1.0F - reduction));
			}
		}
	}

	@SubscribeEvent
	public static void onEntityAttackEvent(LivingAttackEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity attacked = (PlayerEntity) event.getEntityLiving();
			IAbilityData props = AbilityDataCapability.get(attacked);
			IDevilFruit devilFruitProps = DevilFruitCapability.get(attacked);
			if (devilFruitProps.hasDevilFruit(ModAbilities.GOMU_GOMU_NO_MI)) {
				DamageSource source = event.getSource();
				Entity instantSource = source.getDirectEntity();
				if (instantSource instanceof NormalBulletProjectile || (instantSource instanceof CannonBallProjectile && TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE)) || instantSource instanceof PopGreenProjectile
						|| (instantSource instanceof ImpactBlastProjectile && TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE) && TrueGomuHelper.hasGearThirdActive(props))) {
					AbilityProjectileEntity ablProj = (AbilityProjectileEntity) instantSource;
					if (ablProj.getThrower() != null && ablProj.isAffectedByHaki()) {
						LivingEntity thrower = ablProj.getThrower();
						boolean isImbued = ablProj.isAffectedByImbuing() && HakiHelper.hasImbuingActive(thrower);
						if (isImbued) {
							return;
						}
					}
					event.setCanceled(true);
					((AbilityProjectileEntity) instantSource).setThrower(attacked);
					((AbilityProjectileEntity) instantSource).shoot(-instantSource.getDeltaMovement().x, -instantSource.getDeltaMovement().y, -instantSource.getDeltaMovement().z, 0.8F, 20.0F);
				}
			}
		}
	}

	private static boolean getGomuDamagingItems(Item item) {
		return item instanceof SwordItem;
	}

	@SubscribeEvent
	public static void onDeath(LivingDeathEvent event) {
		if (!event.isCanceled()) {
			TrueGearFourthAbility ability = AbilityDataCapability.get(event.getEntityLiving()).getEquippedAbility(TrueGearFourthAbility.INSTANCE);
			if (ability != null)
				event.setCanceled(ability.onUserDeath(event.getEntityLiving()));
		}
	}

	@SubscribeEvent
	public static void dorikiGain(DorikiEvent.Post event) {
		AbilityValidationEvents.checkForPossibleFruitAbilities(event.getEntityLiving());
	}

	@SubscribeEvent
	public static void hakiEvent(HakiExpEvent.Post event) {
		AbilityValidationEvents.checkForPossibleFruitAbilities(event.getEntityLiving());
	}

	@SubscribeEvent
	public static void usage(AbilityUseEvent.Pre event) {
		if (Arrays.asList(TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA.getAbilities()).contains(event.getAbility().getCore())) {
			GomuMorphsAbility morphs = AbilityDataCapability.get(event.getEntityLiving()).getPassiveAbility(GomuMorphsAbility.INSTANCE);
			if (morphs != null)
				morphs.updateModes();
		}
		if (event.getAbility().getCore() == GomuGomuNoDawnWhipAbility.INSTANCE) {
			event.getAbility().getComponent(ModAbilityKeys.COOLDOWN).ifPresent(cooldown -> {
				cooldown.getBonusManager().addBonus(UUID.fromString("14d86583-ffb8-48ae-8088-9bb9a8f7c6de"), "Rebalanced cooldown", BonusOperation.ADD, 160);
			});
		}
	}
}
