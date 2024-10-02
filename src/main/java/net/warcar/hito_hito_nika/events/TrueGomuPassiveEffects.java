package net.warcar.hito_hito_nika.events;

import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.abilities.GomuFusenAbility;
import net.warcar.hito_hito_nika.abilities.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.IDeathAbility;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceElement;
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
import xyz.pixelatedw.mineminenomi.init.ModAttributes;
import xyz.pixelatedw.mineminenomi.init.ModDamageSource;
import xyz.pixelatedw.mineminenomi.items.weapons.ClimaTactItem;
import xyz.pixelatedw.mineminenomi.items.weapons.CoreSwordItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = "hito_hito_no_mi_nika")
public class TrueGomuPassiveEffects {
	@SubscribeEvent
	public static void onEntityHurt(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			DamageSource source = event.getSource();
			Entity instantSource = source.getDirectEntity();
			Entity trueSource = source.getEntity();
			PlayerEntity attacked = (PlayerEntity) event.getEntityLiving();
			IDevilFruit props = DevilFruitCapability.get(attacked);
			if (props.hasDevilFruit(TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA) && !source.isMagic()) {
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
			event.setAmount(event.getAmount() / ((float) attacked.getAttributeValue(ModAttributes.DAMAGE_REDUCTION.get()) + 1f));
		}
	}

	@SubscribeEvent
	public static void onEntityAttackEvent(LivingAttackEvent event) {
		if (event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity attacked = (PlayerEntity) event.getEntityLiving();
			IAbilityData props = AbilityDataCapability.get(attacked);
			IDevilFruit devilFruitProps = DevilFruitCapability.get(attacked);
			if (devilFruitProps.hasDevilFruit(TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA)) {
				DamageSource source = event.getSource();
				Entity instantSource = source.getDirectEntity();
				if (instantSource instanceof NormalBulletProjectile || (instantSource instanceof CannonBallProjectile && TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE)) || instantSource instanceof PopGreenProjectile
						|| (instantSource instanceof ImpactBlastProjectile && TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE) && TrueGomuHelper.hasGearThirdActive(props))) {
					AbilityProjectileEntity ablProj = (AbilityProjectileEntity) instantSource;
					if (ablProj.getThrower() != null && ablProj.isAffectedByHaki()) {
						LivingEntity thrower = ablProj.getThrower();
						boolean isImbued = ablProj.isAffectedByImbuing() && HakiHelper.hasImbuingActive(thrower, true);
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
		if ((!(item instanceof SwordItem) || item instanceof CoreSwordItem) && !(item instanceof PickaxeItem) && !(item instanceof AxeItem) && !(item instanceof TridentItem) && !(item instanceof ClimaTactItem)) {
			return !(item instanceof CoreSwordItem) || ((CoreSwordItem) item).isBlunt();
		} else {
			return false;
		}
	}

	@SubscribeEvent
	public static void onDeath(LivingDeathEvent event) {
		if (!event.isCanceled()) {
			List<Ability> abilities = AbilityDataCapability.get(event.getEntityLiving()).getUnlockedAbilities((ability -> ability instanceof IDeathAbility));
			boolean out = false;
			for (Ability ability : abilities) {
				boolean i = ((IDeathAbility) ability).onUserDeath(event.getEntityLiving(), event.getSource());
				out = out || i;
			}
			event.setCanceled(out);
		}
	}
}
