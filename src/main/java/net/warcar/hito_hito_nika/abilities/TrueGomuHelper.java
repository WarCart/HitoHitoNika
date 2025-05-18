package net.warcar.hito_hito_nika.abilities;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.config.CommonConfig;
import xyz.pixelatedw.mineminenomi.abilities.gomu.GearFifthAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiEmissionAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiInternalDestructionAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

public class TrueGomuHelper {
	public static <A extends Ability> boolean canActivateGear(IAbilityData props, AbilityCore<A> gear) {
		return !(
				(gear.equals(TrueGearSecondAbility.INSTANCE) && (hasGearFourthActive(props) || hasGearFifthActive(props)))
				|| (gear.equals(TrueGearThirdAbility.INSTANCE) && (hasGearFourthActive(props) && !CommonConfig.INSTANCE.isNonCanon()))
				|| (gear.equals(TrueGearFourthAbility.INSTANCE) && (hasGearThirdActive(props) || hasGearSecondActive(props)))
				|| (gear.equals(GearFifthAbility.INSTANCE) && (hasGearThirdActive(props) || hasGearSecondActive(props) || hasGearFourthActive(props))));
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
}
