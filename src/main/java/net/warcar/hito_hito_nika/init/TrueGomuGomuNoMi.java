package net.warcar.hito_hito_nika.init;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
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
import java.util.List;
import java.util.Objects;

public class TrueGomuGomuNoMi {
	public static final DeferredRegister<AbilityCore<?>> ABILITIES;
	public static final AkumaNoMiItem HITO_HITO_NO_MI_NIKA;

	private static <T extends AkumaNoMiItem> T registerFruit(T fruit) {
		ModRegistry.registerItem(fruit.getDevilFruitName().getString(), () -> fruit);
		if (fruit.getAbilities() != null && !fruit.getAbilities().isEmpty()) {
			registerAbilities(fruit.getAbilities());
		}
		return fruit;
	}

	private static void registerAbilities(List<? extends AbilityCore<?>> abilities) {
		abilities.stream().filter(Objects::nonNull).forEach(TrueGomuGomuNoMi::registerAbility);
	}

	public static <T extends IAbility> AbilityCore<T> registerAbility(AbilityCore<T> core) {
		String resourceName = WyHelper.getResourceName(core.getId());
		HitoHitoNoMiNikaMod.getLangMap().put("ability."+ HitoHitoNoMiNikaMod.MOD_ID +"." + resourceName, core.getUnlocalizedName());
		ABILITIES.register(resourceName, () -> core);

		return core;
	}

	public static void register(IEventBus bus) {
		ABILITIES.register(bus);
	}

	static {
		ABILITIES = DeferredRegister.create(WyRegistry.Keys.ABILITIES, HitoHitoNoMiNikaMod.MOD_ID);
		RegistryObject<AbilityCore<?>>[] cores = new RegistryObject<AbilityCore<?>>[]{TrueGomuPistol.INSTANCE, GomuBulletAbility.INSTANCE, TrueGomuGatling.INSTANCE,
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
