package net.warcar.hito_hito_nika.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.abilities.GomuFusenAbility;
import net.warcar.hito_hito_nika.abilities.GomuMorphsAbility;
import net.warcar.hito_hito_nika.abilities.TrueGearFourthAbility;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.GomuEffects;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import xyz.pixelatedw.mineminenomi.abilities.haki.HakiHelper;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.damagesources.BaseDamageSource;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceElement;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.events.ability.AbilityUseEvent;
import xyz.pixelatedw.mineminenomi.api.events.stats.DorikiEvent;
import xyz.pixelatedw.mineminenomi.api.events.stats.HakiExpEvent;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.ItemsHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.entities.projectiles.CannonBallProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.NormalBulletProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.PopGreenProjectile;
import xyz.pixelatedw.mineminenomi.entities.projectiles.abilities.hitodaibutsu.ImpactBlastProjectile;
import xyz.pixelatedw.mineminenomi.handlers.ability.ProgressionHandler;
import xyz.pixelatedw.mineminenomi.init.ModEntityPredicates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = HitoHitoNoMiNikaMod.MOD_ID)
public class TrueGomuPassiveEffects {
	@SubscribeEvent
	public static void onEntityHurt(LivingHurtEvent event) {
		if (event.getEntity().hasEffect(GomuEffects.GOMU_REVIVE.get())) {
			event.setCanceled(true);
			event.setAmount(0);
			return;
		}
		if (event.getEntity() != null) {
			DamageSource source = event.getSource();
			Entity instantSource = source.getDirectEntity();
			Entity trueSource = source.getEntity();
			LivingEntity attacked = event.getEntity();
			IDevilFruit props = DevilFruitCapability.get(attacked).get();
			if (props.hasDevilFruit(TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA) && !source.is(DamageTypes.MAGIC)) {
				float reduction = 0.0F;
				ArrayList<String> instantSources = new ArrayList<>(Arrays.asList("mob", "player"));
				boolean a = false;
				if (instantSource instanceof LivingEntity) {
					ItemStack mainhandGear = ((LivingEntity) instantSource).getItemBySlot(EquipmentSlot.MAINHAND);
					a = trueSource instanceof LivingEntity && !HakiHelper.hasHardeningActive((LivingEntity) instantSource) && instantSources.contains(source.getMsgId())
							&& getGomuDamagingItems(mainhandGear.getItem()) && !ItemsHelper.isKairosekiWeapon(mainhandGear);
				}
				boolean b = instantSource instanceof NuProjectileEntity proj && proj.isPhysical();
				if ((a || b) && !source.is(DamageTypes.EXPLOSION)) {
					reduction = 0.75F;
				}
				if (source.getMsgId().equals("lightning_bolt")) {
					reduction = 1.0F;
				}
				if (source instanceof BaseDamageSource src && src.getElement() == SourceElement.LIGHTNING) {
					reduction = 1.0F;
				}
				event.setAmount(event.getAmount() * (1.0F - reduction));
			}
		}
	}

	@SubscribeEvent
	public static void onEntityAttackEvent(LivingAttackEvent event) {
		if (event.getEntity() instanceof Player attacked) {
			IAbilityData props = AbilityCapability.get(attacked).get();
			IDevilFruit devilFruitProps = DevilFruitCapability.get(attacked).get();
			if (devilFruitProps.hasDevilFruit(TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA)) {
				DamageSource source = event.getSource();
				Entity instantSource = source.getDirectEntity();
				if (instantSource instanceof NormalBulletProjectile || (instantSource instanceof CannonBallProjectile && TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE)) || instantSource instanceof PopGreenProjectile
						|| (instantSource instanceof ImpactBlastProjectile && TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE) && TrueGomuHelper.hasGearThirdActive(props))) {
					NuProjectileEntity ablProj = (NuProjectileEntity) instantSource;
					if (ablProj.getOwner() != null && ablProj.isAffectedByImbuing()) {
						LivingEntity thrower = ablProj.getOwner();
						if (HakiHelper.hasImbuingActive(thrower)) {
							return;
						}
					}
					event.setCanceled(true);
					((NuProjectileEntity) instantSource).setOwner(attacked);
					((NuProjectileEntity) instantSource).shoot(-instantSource.getDeltaMovement().x, -instantSource.getDeltaMovement().y, -instantSource.getDeltaMovement().z, 0.8F, 20.0F);
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
			TrueGearFourthAbility ability = AbilityCapability.getEquippedAbility(event.getEntity(), TrueGearFourthAbility.INSTANCE.get());
			if (ability != null)
				event.setCanceled(ability.onUserDeath(event.getEntity()));
		}
	}

	@SubscribeEvent
	public static void dorikiGain(DorikiEvent.Post event) {
		ProgressionHandler.checkForPossibleFruitAbilities(event.getEntity());
	}

	@SubscribeEvent
	public static void hakiEvent(HakiExpEvent.Post event) {
		ProgressionHandler.checkForPossibleFruitAbilities(event.getEntity());
	}

	@SubscribeEvent
	public static void usage(AbilityUseEvent.Pre event) {
		if (Arrays.asList(TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA.get().getAbilities()).contains(event.getAbility().getCore())) {
			GomuMorphsAbility morphs = AbilityCapability.get(event.getEntity()).get().getPassiveAbility(GomuMorphsAbility.INSTANCE.get());
			if (morphs != null)
				morphs.updateModes(event.getEntity());
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onFall(LivingFallEvent event) {
		LivingEntity entity = event.getEntity();
		if (event.getDistance() < 3) {
			return;
		}
		List<LivingEntity> entities = WyHelper.getNearbyLiving(entity.position(), entity.level(), 5, ModEntityPredicates.getFriendlyFactions(entity).and(ent -> ent instanceof LivingEntity && TrueGomuHelper.hasGearFifthActive(AbilityCapability.get((LivingEntity) ent).get())));
		if (!entities.isEmpty()) {
			Vec3 speed = new Vec3(entity.getDeltaMovement().x, event.getDistance() / 15, entity.getDeltaMovement().z);
			AbilityHelper.setDeltaMovement(entity, speed);
			event.setCanceled(true);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void beforeEntityRender(RenderLivingEvent.Pre<LivingEntity, ?> event) {
		LivingEntity entity = event.getEntity();
		if (entity.hasEffect(GomuEffects.SQUISHED.get())) {
			entity.getPersistentData().putBoolean("GomuSquished", true);
			PoseStack stack = event.getPoseStack();
			stack.pushPose();
			MobEffectInstance squishedEffect = entity.getEffect(GomuEffects.SQUISHED.get());
			float angle = (float) (squishedEffect.getAmplifier() * Math.PI / 256);
			stack.mulPose(Axis.YP.rotation(angle));
			stack.scale(1, 1, 0.01f);
			stack.mulPose(Axis.YN.rotation(angle));
		}
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void afterEntityRender(RenderLivingEvent.Post<LivingEntity, ?> event) {
		LivingEntity entity = event.getEntity();
		if (entity.getPersistentData().getBoolean("GomuSquished")) {
			PoseStack stack = event.getPoseStack();
			stack.popPose();
			entity.getPersistentData().remove("GomuSquished");
		}
	}
}
