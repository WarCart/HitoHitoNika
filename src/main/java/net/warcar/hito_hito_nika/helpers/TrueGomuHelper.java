package net.warcar.hito_hito_nika.helpers;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.abilities.*;
import net.warcar.hito_hito_nika.config.CommonConfig;
import org.apache.commons.lang3.tuple.Pair;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiEmissionAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiInternalDestructionAbility;
import xyz.pixelatedw.mineminenomi.api.ReferenceTextComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.init.ModAbilityKeys;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.HashMap;
import java.util.Map;

public class TrueGomuHelper {
	public static final TranslationTextComponent TOO_HEAVY = getName("You are to heavy to use this ability", "text.mineminenomi.too_heavy");
	private static final Object[] EMPTY_ARGS = new Object[0];
	public static final IDataSerializer<Vector3d> VECTOR_SERIALIZER = new IDataSerializer<Vector3d>() {
		@Override
		public void write(PacketBuffer buffer, Vector3d vector) {
			buffer.writeDouble(vector.x);
			buffer.writeDouble(vector.y);
			buffer.writeDouble(vector.z);
		}

		@Override
		public Vector3d read(PacketBuffer buffer) {
			double x = buffer.readDouble();
			double y = buffer.readDouble();
			double z = buffer.readDouble();
			return new Vector3d(x, y, z);
		}

		@Override
		public Vector3d copy(Vector3d vector) {
			return new Vector3d(vector.x, vector.y, vector.z);
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
		Ability ability = props.getEquippedAbility(GomuFusenAbility.INSTANCE);
		return ability != null && ability.isContinuous();
	}

	public static boolean hasGearSecondActive(IAbilityData props) {
		Ability ability = props.getEquippedAbility(TrueGearSecondAbility.INSTANCE);
		return ability != null && ability.isContinuous();
	}

	public static boolean hasGearThirdActive(IAbilityData props) {
		Ability ability = props.getEquippedAbility(TrueGearThirdAbility.INSTANCE);
		return ability != null && ability.isContinuous();
	}

	public static boolean hasGigantActive(IAbilityData props) {
		TrueGearThirdAbility ability = props.getEquippedAbility(TrueGearThirdAbility.INSTANCE);
		return ability != null && ability.isContinuous() && ability.isGiant();
	}

	public static boolean isSmall(IAbilityData props) {
		TrueGearThirdAbility ability = props.getEquippedAbility(TrueGearThirdAbility.INSTANCE);
		return ability != null && ability.getSmallFormCooldown() > 1;
	}

	public static boolean hasGearFourthBoundmanActive(IAbilityData props) {
		TrueGearFourthAbility ability = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE);
		return ability != null && ability.isContinuous() && ability.isBoundman();
	}

	public static boolean hasGearFourthActive(IAbilityData props) {
		Ability ability = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE);
		return ability != null && ability.isContinuous();
	}

	public static boolean hasGearFourthSnakemanActive(IAbilityData props) {
		TrueGearFourthAbility ability = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE);
		return ability != null && ability.isContinuous() && ability.isSnakeman();
	}

	public static boolean hasGearFourthTankmanActive(IAbilityData props) {
		TrueGearFourthAbility ability = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE);
		return ability != null && ability.isContinuous() && ability.isTankman();
	}

	public static boolean hasPartialGearFourthActive(IAbilityData props) {
		TrueGearFourthAbility ability = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE);
		return ability != null && ability.isContinuous() && ability.isPartial();
	}

	public static boolean hasGearFifthActive(IAbilityData props) {
		Ability ability = props.getEquippedAbility(TrueGearFifthAbility.INSTANCE);
		return ability != null && ability.isContinuous();
	}

	public static boolean hasHakiEmissionActive(IAbilityData props) {
		return hasAbilityActive(props, BusoshokuHakiEmissionAbility.INSTANCE) || hasAbilityActive(props, BusoshokuHakiInternalDestructionAbility.INSTANCE);
	}

	public static<A extends Ability> boolean hasAbilityActive(IAbilityData props, AbilityCore<A> ability) {
		Ability abl = props.getEquippedAbility(ability);
		return abl != null && abl.isContinuous();
	}

	public static ResourceLocation getIcon(String name) {
		return getIcon(HitoHitoNoMiNikaMod.MOD_ID, name);
	}

	public static ResourceLocation getIcon(String modId, String name) {
		return new ResourceLocation(modId.toLowerCase(), "textures/abilities/" + WyHelper.getResourceName(name) + ".png");
	}

	public static TranslationTextComponent getName(String name, String resourceName) {
		return getName(HitoHitoNoMiNikaMod.MOD_ID, name, resourceName);
	}

	public static TranslationTextComponent getName(String name) {
		return getName(HitoHitoNoMiNikaMod.MOD_ID, name, WyHelper.getResourceName(name));
	}

	public static TranslationTextComponent getName(String modId, String name, String resourceName) {
		String key = "ability." + modId + "." + resourceName;
		HitoHitoNoMiNikaMod.getLangMap().put(key, name);
		return new TranslationTextComponent(key, name);
	}

	public static void stopGatling(LivingEntity entity) {
		TrueGomuGatling abl = AbilityDataCapability.get(entity).getEquippedAbility(TrueGomuGatling.INSTANCE);
		if (abl != null && abl.isContinuous()) {
			abl.getComponent(ModAbilityKeys.CONTINUOUS).ifPresent(comp -> comp.stopContinuity(entity));
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
    public static IFormattableTextComponent[] registerDescriptionText(String abilityName, Pair<String, Object[]>... pairs) {
        return registerDescriptionText(HitoHitoNoMiNikaMod.MOD_ID, abilityName, pairs);
    }

    @SafeVarargs
	public static IFormattableTextComponent[] registerDescriptionText(String modid, String abilityName, Pair<String, Object[]>... pairs) {
		IFormattableTextComponent[] components = new IFormattableTextComponent[pairs.length];

		for(int i = 0; i < pairs.length; ++i) {
			String key = String.format("ability.%s.%s.description.%s", modid, abilityName, i);
			key = registerName(key, pairs[i].getKey());
			Object[] args = pairs[i].getValue();
			if (args != null) {
				for(int j = 0; j < args.length; ++j) {
					Object o = args[j];
					if (o instanceof RegistryObject) {
						args[j] = new ReferenceTextComponent((RegistryObject)o);
					} else if (o instanceof IForgeRegistryEntry) {
						args[j] = mentionEntry((IForgeRegistryEntry)o);
					}
				}
			} else {
				args = EMPTY_ARGS;
			}

			TranslationTextComponent comp = new TranslationTextComponent(key, args);
			components[i] = comp;
		}

		return components;
	}

	private static String registerName(String key, String localizedName) {
		HitoHitoNoMiNikaMod.getLangMap().put(key, localizedName);
		return key;
	}


	private static IFormattableTextComponent mentionEntry(IForgeRegistryEntry<?> entry) {
		if (entry instanceof AbilityCore) {
			return AbilityHelper.mentionAbility((AbilityCore)entry);
		} else if (entry instanceof Item) {
			return AbilityHelper.mentionItem((Item)entry);
		} else if (entry instanceof Effect) {
			return AbilityHelper.mentionEffect((Effect)entry);
		} else {
			return entry instanceof MorphInfo ? AbilityHelper.mentionMorph((MorphInfo)entry) : null;
		}
	}

	public static ContinuousComponent.IDuringContinuousEvent getSpeedEvent(float speed) {
		return (entity, ability) -> {
			if (!AbilityHelper.canUseMomentumAbilities(entity) || !entity.isSprinting()) {
				return;
			}
            Vector3d vec = entity.getLookAngle();

			if (entity.isOnGround()) {
				AbilityHelper.setDeltaMovement(entity, (vec.x * speed), entity.getDeltaMovement().y, (vec.z * speed));
			}
			else {
				AbilityHelper.setDeltaMovement(entity, (vec.x * speed * 0.5F), entity.getDeltaMovement().y, (vec.z * speed * 0.5F));
			}
		};
	}

	public static void init() {
		getName("You are to heavy to use this ability", "text.mineminenomi.too_heavy");
		DataSerializers.registerSerializer(VECTOR_SERIALIZER);
	}
}
