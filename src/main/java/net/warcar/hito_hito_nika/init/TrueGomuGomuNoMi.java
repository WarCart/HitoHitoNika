package net.warcar.hito_hito_nika.init;

import net.warcar.hito_hito_nika.abilities.*;
import xyz.pixelatedw.mineminenomi.abilities.gomu.BouncyAbility;
import xyz.pixelatedw.mineminenomi.abilities.gomu.GearFifthAbility;
import xyz.pixelatedw.mineminenomi.abilities.gomu.GomuGomuNoDawnWhipAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.enums.AbilityCommandGroup;
import xyz.pixelatedw.mineminenomi.init.ModAbilities;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public class TrueGomuGomuNoMi {
	public static final AbilityCore[] GOMU_ABILITIES;
	public static final AbilityCore[] GOMU_GEARS = new AbilityCore[]{TrueGearSecondAbility.INSTANCE,
			TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE, GearFifthAbility.INSTANCE,
			GearSixthAbility.INSTANCE};

	private static void registerAbilities(AbilityCore<?>[] abilities) {
		Arrays.stream(abilities).filter(Objects::nonNull).forEach(WyRegistry::registerAbility);
	}

	public static void register() {
		try {
			Field abilities = AkumaNoMiItem.class.getDeclaredField("abilities");
			abilities.setAccessible(true);
			abilities.set(ModAbilities.GOMU_GOMU_NO_MI, GOMU_ABILITIES);
			abilities.setAccessible(false);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static {
		AbilityCore<?>[] cores = new AbilityCore[]{TrueGomuPistol.INSTANCE, StrongGomuPistol.INSTANCE, TrueGomuGatling.INSTANCE, TrueGomuBazooka.INSTANCE, GomuFusenAbility.INSTANCE, TrueGomuRocket.INSTANCE, TrueGearSecondAbility.INSTANCE, TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE,
				GearFifthAbility.INSTANCE, GomuGomuNoDawnWhipAbility.INSTANCE, BouncyAbility.INSTANCE, GomuMorphsAbility.INSTANCE};
		if (WyHelper.isAprilFirst())
			cores = new AbilityCore[]{TrueGomuPistol.INSTANCE, StrongGomuPistol.INSTANCE, TrueGomuGatling.INSTANCE, TrueGomuBazooka.INSTANCE, GomuFusenAbility.INSTANCE, TrueGomuRocket.INSTANCE, TrueGearSecondAbility.INSTANCE, TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE,
					GearFifthAbility.INSTANCE, GomuGomuNoDawnWhipAbility.INSTANCE, GearSixthAbility.INSTANCE, BouncyAbility.INSTANCE, GomuMorphsAbility.INSTANCE};
		GOMU_ABILITIES = cores;
		registerAbilities(cores);
	}
}
