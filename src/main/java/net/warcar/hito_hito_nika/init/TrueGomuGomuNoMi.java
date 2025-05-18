package net.warcar.hito_hito_nika.init;

import net.warcar.hito_hito_nika.abilities.*;
import xyz.pixelatedw.mineminenomi.abilities.gomu.BouncyAbility;
import xyz.pixelatedw.mineminenomi.abilities.gomu.GomuGomuNoDawnWhipAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.enums.FruitType;
import xyz.pixelatedw.mineminenomi.init.ModAbilities;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public class TrueGomuGomuNoMi {
	public static final AkumaNoMiItem HITO_HITO_NO_MI_NIKA;

	private static <T extends AkumaNoMiItem> T registerFruit(T fruit) {
		WyRegistry.registerItem(fruit.getDevilFruitName(), () -> fruit);
		if (fruit.getAbilities() != null && fruit.getAbilities().length > 0) {
			registerAbilities(fruit.getAbilities());
		}
		return fruit;
	}

	private static void registerAbilities(AbilityCore<?>[] abilities) {
		Arrays.stream(abilities).filter(Objects::nonNull).forEach(WyRegistry::registerAbility);
	}

	public static void register() {
	}

	static {
		AbilityCore<?>[] cores = new AbilityCore[]{TrueGomuPistol.INSTANCE, StrongGomuPistol.INSTANCE, TrueGomuGatling.INSTANCE, TrueGomuBazooka.INSTANCE, GomuFusenAbility.INSTANCE, TrueGomuRocket.INSTANCE, TrueGearSecondAbility.INSTANCE, TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE,
				TrueGearFifthAbility.INSTANCE, GomuGomuNoDawnWhipAbility.INSTANCE, BouncyAbility.INSTANCE, GomuMorphsAbility.INSTANCE};
		if (WyHelper.isAprilFirst())
			cores = new AbilityCore[]{TrueGomuPistol.INSTANCE, StrongGomuPistol.INSTANCE, TrueGomuGatling.INSTANCE, TrueGomuBazooka.INSTANCE, GomuFusenAbility.INSTANCE, TrueGomuRocket.INSTANCE, TrueGearSecondAbility.INSTANCE, TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE,
					TrueGearFifthAbility.INSTANCE, GomuGomuNoDawnWhipAbility.INSTANCE, GearSixthAbility.INSTANCE, BouncyAbility.INSTANCE, GomuMorphsAbility.INSTANCE};
		HITO_HITO_NO_MI_NIKA = registerFruit(new AkumaNoMiItem("Gomu Gomu no Mi", 2, FruitType.PARAMECIA, cores));
	}
}
