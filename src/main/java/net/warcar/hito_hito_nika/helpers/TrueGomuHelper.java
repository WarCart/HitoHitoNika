package net.warcar.hito_hito_nika.helpers;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.abilities.*;
import net.warcar.hito_hito_nika.config.CommonConfig;
import org.apache.commons.lang3.tuple.Pair;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiEmissionAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiInternalDestructionAbility;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityExplosion;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityUseConditions;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.init.ModAbilityComponents;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;

import java.util.HashMap;
import java.util.Map;

public class TrueGomuHelper {
	public static final Component TOO_HEAVY = getName("You are to heavy to use this ability", "text.mineminenomi.too_heavy");
	private static final Object[] EMPTY_ARGS = new Object[0];
	public static final EntityDataSerializer<Vec3> VECTOR_SERIALIZER = new EntityDataSerializer<Vec3>() {
		@Override
		public void write(FriendlyByteBuf buffer, Vec3 vector) {
			buffer.writeDouble(vector.x);
			buffer.writeDouble(vector.y);
			buffer.writeDouble(vector.z);
		}

		@Override
		public Vec3 read(FriendlyByteBuf buffer) {
			double x = buffer.readDouble();
			double y = buffer.readDouble();
			double z = buffer.readDouble();
			return new Vec3(x, y, z);
		}

		@Override
		public Vec3 copy(Vec3 vector) {
			return new Vec3(vector.x, vector.y, vector.z);
		}
	};

	public static <A extends Ability> boolean canActivateGear(IAbilityData props, A gear) {
		if (gear instanceof TrueGearFourthAbility) {
			TrueGearFourthAbility fourthGear = (TrueGearFourthAbility) gear;
			if (fourthGear.isBoundman()) {
				return !hasGearThirdActive(props) && !hasGearSecondActive(props) && !hasFusenActive(props);
			} else if (fourthGear.isSnakeman()) {
				return !hasGearThirdActive(props) && !hasGearSecondActive(props) && !hasGearFifthActive(props) && !hasFusenActive(props);
			} else if (fourthGear.isPartial()) {
				return !hasGearThirdActive(props) && !hasGearSecondActive(props) && !hasGearFifthActive(props);
			} else if (fourthGear.isTankman()) {
				return !hasGearThirdActive(props) && !hasGearSecondActive(props) && !hasGearFifthActive(props);
			}
			return false;
		}
		AbilityCore<?> core = gear.getCore();
		return !(
				(core.equals(TrueGearSecondAbility.INSTANCE) && (hasGearFourthActive(props) || hasGearFifthActive(props)))
				|| (core.equals(TrueGearThirdAbility.INSTANCE) && hasGearFourthActive(props) && !CommonConfig.INSTANCE.isNonCanon())
				|| (core.equals(TrueGearFifthAbility.INSTANCE) && (hasGearThirdActive(props) || hasGearSecondActive(props) || hasGearFourthActive(props))));
	}

	public static boolean hasFusenActive(IAbilityData props) {
		Ability ability = props.getEquippedAbility(GomuFusenAbility.INSTANCE.get());
		return ability != null && ability.isContinuous();
	}

	public static boolean hasGearSecondActive(IAbilityData props) {
		Ability ability = props.getEquippedAbility(TrueGearSecondAbility.INSTANCE.get());
		return ability != null && ability.isContinuous();
	}

	public static boolean hasGearThirdActive(IAbilityData props) {
		Ability ability = props.getEquippedAbility(TrueGearThirdAbility.INSTANCE.get());
		return ability != null && ability.isContinuous();
	}

	public static boolean hasGigantActive(IAbilityData props) {
		TrueGearThirdAbility ability = props.getEquippedAbility(TrueGearThirdAbility.INSTANCE.get());
		return ability != null && ability.isContinuous() && ability.isGiant();
	}

	public static boolean isSmall(IAbilityData props) {
		TrueGearThirdAbility ability = props.getEquippedAbility(TrueGearThirdAbility.INSTANCE.get());
		return ability != null && ability.getSmallFormCooldown() > 1;
	}

	public static boolean hasGearFourthBoundmanActive(IAbilityData props) {
		TrueGearFourthAbility ability = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE.get());
		return ability != null && ability.isContinuous() && ability.isBoundman();
	}

	public static boolean hasGearFourthActive(IAbilityData props) {
		Ability ability = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE.get());
		return ability != null && ability.isContinuous();
	}

	public static boolean hasGearFourthSnakemanActive(IAbilityData props) {
		TrueGearFourthAbility ability = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE.get());
		return ability != null && ability.isContinuous() && ability.isSnakeman();
	}

	public static boolean hasGearFourthTankmanActive(IAbilityData props) {
		TrueGearFourthAbility ability = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE.get());
		return ability != null && ability.isContinuous() && ability.isTankman();
	}

	public static boolean hasPartialGearFourthActive(IAbilityData props) {
		TrueGearFourthAbility ability = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE.get());
		return ability != null && ability.isContinuous() && ability.isPartial();
	}

	public static boolean hasGearFifthActive(IAbilityData props) {
		Ability ability = props.getEquippedAbility(TrueGearFifthAbility.INSTANCE.get());
		return ability != null && ability.isContinuous();
	}

	public static boolean hasHakiEmissionActive(IAbilityData props) {
		return hasAbilityActive(props, BusoshokuHakiEmissionAbility.INSTANCE) || hasAbilityActive(props, BusoshokuHakiInternalDestructionAbility.INSTANCE);
	}

	public static<A extends Ability> boolean hasAbilityActive(IAbilityData props, RegistryObject<AbilityCore<A>> ability) {
		Ability abl = props.getEquippedAbility(ability.get());
		return abl != null && abl.isContinuous();
	}

	public static ResourceLocation getIcon(String name) {
		return getIcon(HitoHitoNoMiNikaMod.MOD_ID, name);
	}

	public static ResourceLocation getIcon(String modId, String name) {
		return ResourceLocation.fromNamespaceAndPath(modId.toLowerCase(), "textures/abilities/" + WyHelper.getResourceName(name) + ".png");
	}

	public static Component getName(String name, String resourceName) {
		return getName(HitoHitoNoMiNikaMod.MOD_ID, name, resourceName);
	}

	public static Component getName(String name) {
		return getName(HitoHitoNoMiNikaMod.MOD_ID, name, WyHelper.getResourceName(name));
	}

	public static Component getName(String modId, String name, String resourceName) {
		String key = "ability." + modId + "." + resourceName;
		HitoHitoNoMiNikaMod.getLangMap().put(key, name);
		return Component.translatable(key, name);
	}

	public static void stopGatling(LivingEntity entity) {
		TrueGomuGatling abl = AbilityCapability.getEquippedAbility(entity, TrueGomuGatling.INSTANCE.get());
		if (abl != null && abl.isContinuous()) {
			abl.getComponent(ModAbilityComponents.CONTINUOUS.get()).ifPresent(comp -> comp.stopContinuity(entity));
		}
	}

	public static ContinuousComponent.IStartContinuousEvent basicGearStuff() {
		return (entity, ability) -> {
			stopGatling(entity);
		};
	}

	public static Map<String, Double> getBasicBonusData(float continueTime) {
		Map<String, Double> bonusData = new HashMap<>();
		bonusData.put("length", (double) continueTime);
		return bonusData;
	}


    @SafeVarargs
    public static Component[] registerDescriptionText(String abilityName, Pair<String, Object[]>... pairs) {
        return registerDescriptionText(HitoHitoNoMiNikaMod.MOD_ID, abilityName, pairs);
    }

    @SafeVarargs
	public static Component[] registerDescriptionText(String modid, String abilityName, Pair<String, Object[]>... pairs) {
		Component[] components = new Component[pairs.length];

		for(int i = 0; i < pairs.length; ++i) {
			String key = String.format("ability.%s.%s.description.%s", modid, abilityName, i);
			key = registerName(key, pairs[i].getKey());

			Component comp = Component.translatable(key);
			components[i] = comp;
		}

		return components;
	}

	private static String registerName(String key, String localizedName) {
		HitoHitoNoMiNikaMod.getLangMap().put(key, localizedName);
		return key;
	}

	public static ContinuousComponent.IDuringContinuousEvent getSpeedEvent(float speed) {
		return (entity, ability) -> {
			if (AbilityUseConditions.canUseMomentumAbilities(entity, ability).isFail() || !entity.isSprinting()) {
				return;
			}
            Vec3 vec = entity.getLookAngle();

			if (!entity.isFallFlying()) {
				AbilityHelper.setDeltaMovement(entity, (vec.x * speed), entity.getDeltaMovement().y, (vec.z * speed));
			}
			else {
				AbilityHelper.setDeltaMovement(entity, (vec.x * speed * 0.5F), entity.getDeltaMovement().y, (vec.z * speed * 0.5F));
			}
		};
	}

	public static void init() {
		getName("You are to heavy to use this ability", "text.mineminenomi.too_heavy");
		EntityDataSerializers.registerSerializer(VECTOR_SERIALIZER);
	}

    public static NuProjectileEntity.IOnHitEntityEvent getBazookaOnEntityImpactEvent(NuProjectileEntity projectile, double power) {
        return hit -> {
            Vec3 speed = projectile.getDeltaMovement().normalize().scale(power);
            AbilityHelper.setDeltaMovement(hit.getEntity(), speed.x, 0.5, speed.z);
        };
    }

	public static NuProjectileEntity.IOnTickEvent getG2Tick(NuProjectileEntity entity) {
		return () -> WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), entity, entity.getX(), entity.getY(), entity.getZ());
	}

	public static NuProjectileEntity.IOnTickEvent getG2Tick(NuProjectileEntity entity, int mod) {
		return () -> {
			if ((entity.getLife() + entity.getId()) % mod == 0)
            	WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), entity, entity.getX(), entity.getY(), entity.getZ());
        };
	}

	public static NuProjectileEntity.IOnTickEvent getFlameTick(NuProjectileEntity entity, int amount) {
		return () -> {
			for (int i = 0; i < amount; i++)
				WyHelper.spawnParticleEffect(ModParticleEffects.DAI_ENKAI_1.get(), entity, entity.getX(), entity.getY(), entity.getZ());
		};
	}

    public static NuProjectileEntity.IOnHitBlockEvent onBlockImpactEvent(NuProjectileEntity entity, float power, float staticDamage) {
        return onBlockImpactEvent(entity, power, staticDamage, false);
    }

	public static NuProjectileEntity.IOnHitBlockEvent onFlamingBlockImpactEvent(NuProjectileEntity entity, float power, float staticDamage) {
		return onBlockImpactEvent(entity, power, staticDamage, true);
	}

    public static NuProjectileEntity.IOnHitBlockEvent onBlockImpactEvent(NuProjectileEntity entity, float power, float staticDamage, boolean flame) {
		return hit -> {
			AbilityExplosion explosion = new AbilityExplosion(entity.getOwner(), entity.getParent().orElse(null), entity.getX(), entity.getY(), entity.getZ(), power);
			explosion.setStaticDamage(staticDamage);
			explosion.setExplosionSound(false);
			explosion.setDamageOwner(false);
			explosion.setDestroyBlocks(true);
			explosion.setFireAfterExplosion(flame);
			explosion.setDamageEntities(true);
			explosion.explode();
		};
	}
}
