package net.warcar.hito_hito_nika.init;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.abilities.*;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.WyRegistry;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.enums.FruitType;
import xyz.pixelatedw.mineminenomi.init.ModRegistry;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

import java.util.Arrays;

public class TrueGomuGomuNoMi {
	public static final DeferredRegister<AbilityCore<?>> ABILITIES;
	public static final AkumaNoMiItem HITO_HITO_NO_MI_NIKA;

	private static <T extends AkumaNoMiItem> T registerFruit(T fruit) {
		ModRegistry.registerFruitItem(fruit.getDevilFruitName().getString(), () -> fruit);
		return fruit;
	}

	public static <T extends IAbility> RegistryObject<AbilityCore<T>> registerAbility(AbilityCore.Builder<T> builder) {
		AbilityCore<T> core = builder.build();
		String resourceName = WyHelper.getResourceName(core.getId());
		HitoHitoNoMiNikaMod.getLangMap().put("ability."+ HitoHitoNoMiNikaMod.MOD_ID +"." + resourceName, core.getUnlocalizedName());
		return ABILITIES.register(resourceName, () -> core);
	}

	public static void register(IEventBus bus) {
		ABILITIES.register(bus);
	}

	static {
		ABILITIES = DeferredRegister.create(WyRegistry.Keys.ABILITIES, HitoHitoNoMiNikaMod.MOD_ID);
		// Just... pretend they are abilities ok?
		RegistryObject[] cores = new RegistryObject[]{TrueGomuPistol.INSTANCE, GomuBulletAbility.INSTANCE, TrueGomuGatling.INSTANCE,
				TrueGomuBazooka.INSTANCE, GomuFusenAbility.INSTANCE, TrueGomuRocket.INSTANCE, TrueGearSecondAbility.INSTANCE,
				TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE, TrueGearFifthAbility.INSTANCE, GomuUfoAbility.INSTANCE,
				MoguraPistolAbility.INSTANCE, GomuGomuNoKaminariAbility.INSTANCE, GomuGomuNoCannonballAbility.INSTANCE,
				TrueBouncyAbility.INSTANCE, GomuMorphsAbility.INSTANCE, GomuTrampleAbility.INSTANCE, GomuGomuNoCymbalAbility.INSTANCE,
		};
		if (WyHelper.isAprilFirst()) {
            cores = Arrays.copyOf(cores, cores.length + 1);
			cores[cores.length-1] = GearSixthAbility.INSTANCE;
        }
		HITO_HITO_NO_MI_NIKA = registerFruit(new AkumaNoMiItem(2, FruitType.PARAMECIA, cores));
	}
}
