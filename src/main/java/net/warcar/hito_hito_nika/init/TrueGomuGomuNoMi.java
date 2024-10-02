package net.warcar.hito_hito_nika.init;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.warcar.hito_hito_nika.abilities.*;
import xyz.pixelatedw.mineminenomi.abilities.gomu.BouncyAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.enums.FruitType;
import xyz.pixelatedw.mineminenomi.init.ModValues;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;

import java.util.Arrays;
import java.util.Objects;

public class TrueGomuGomuNoMi {
	public static final AkumaNoMiItem HITO_HITO_NO_MI_NIKA;

	public static final RegistryObject<SoundEvent> DRUMS_OF_LIBERATION = WyRegistry.registerSound("Drums of Liberation");

	private static <T extends AkumaNoMiItem> T registerFruit(T fruit) {
		if (fruit.type == FruitType.LOGIA) {
			ModValues.logias.add(fruit);
		}
		ModValues.devilfruits.add(fruit);
		String resourceName = WyHelper.getResourceName(fruit.getDevilFruitName());
		WyRegistry.getLangMap().put("item.mineminenomi." + resourceName, fruit.getDevilFruitName());
		WyRegistry.registerItem(fruit.getDevilFruitName(), () -> fruit);
		if (fruit.getAbilities() != null && fruit.getAbilities().length > 0) {
			registerAbilities(fruit.getAbilities());
		}
		return fruit;
	}

	private static void registerAbilities(AbilityCore<?>[] abilities) {
		Arrays.stream(abilities).filter(Objects::nonNull).forEach(WyRegistry::registerAbility);
	}

	public static void register(IEventBus eventBus) {
	}

	static {
		AbilityCore<?>[] cores = new AbilityCore[]{TrueGomuPistol.INSTANCE, StrongGomuPistol.INSTANCE, TrueGomuGatling.INSTANCE, TrueGomuBazooka.INSTANCE, GomuFusenAbility.INSTANCE, TrueGomuRocket.INSTANCE, TrueGearSecondAbility.INSTANCE, TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE,
				FifthGearAbility.INSTANCE, BouncyAbility.INSTANCE, GomuMorphsAbility.INSTANCE};
		if (WyHelper.isAprilFirst())
			cores = new AbilityCore[]{TrueGomuPistol.INSTANCE, StrongGomuPistol.INSTANCE, TrueGomuGatling.INSTANCE, TrueGomuBazooka.INSTANCE, GomuFusenAbility.INSTANCE, TrueGomuRocket.INSTANCE, TrueGearSecondAbility.INSTANCE, TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE,
					FifthGearAbility.INSTANCE, GearSixthAbility.INSTANCE, BouncyAbility.INSTANCE, GomuMorphsAbility.INSTANCE};
		HITO_HITO_NO_MI_NIKA = registerFruit(new AkumaNoMiItem("Gomu Gomu no Mi", 2, FruitType.PARAMECIA, cores));
	}
}
