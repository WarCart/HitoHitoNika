package net.warcar.hito_hito_nika.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.abilities.*;
import xyz.pixelatedw.mineminenomi.MineMineNoMi;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.WyRegistry;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.enums.FruitType;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

import java.util.Arrays;
import java.util.function.Supplier;

public class TrueGomuGomuNoMi {
	public static final DeferredRegister<AbilityCore<?>> ABILITIES;
	public static final DeferredRegister<Item> ITEMS;
	public static final DeferredHolder<Item, AkumaNoMiItem> HITO_HITO_NO_MI_NIKA;

	private static <T extends AkumaNoMiItem> DeferredHolder<Item, T> registerFruit(Supplier<T> fruit, String name) {
		String resourceName = WyHelper.getResourceName(name);
		return ITEMS.register(resourceName, fruit);
	}

	public static <T extends IAbility> DeferredHolder<AbilityCore<?>, AbilityCore<T>> registerSixthGear(AbilityCore.Builder<T> builder) {
		return WyHelper.isAprilFirst() ? registerAbility(builder) : null;
	}

	public static <T extends IAbility> DeferredHolder<AbilityCore<?>, AbilityCore<T>> registerAbility(AbilityCore.Builder<T> builder) {
		AbilityCore<T> core = builder.build(HitoHitoNoMiNikaMod.MOD_ID);
		String resourceName = WyHelper.getResourceName(core.getId());
		HitoHitoNoMiNikaMod.getLangMap().put("ability."+ HitoHitoNoMiNikaMod.MOD_ID +"." + resourceName, core.getUnlocalizedName());
		return ABILITIES.register(resourceName, () -> core);
	}

	public static void register(IEventBus bus) {
		ABILITIES.register(bus);
		ITEMS.register(bus);
	}

	static {
		ABILITIES = DeferredRegister.create(WyRegistry.Keys.ABILITIES, HitoHitoNoMiNikaMod.MOD_ID);
		ITEMS = DeferredRegister.create(Registries.ITEM, MineMineNoMi.PROJECT_ID);
		// Just... pretend they are abilities ok?
		DeferredHolder[] cores = new DeferredHolder[]{TrueGomuPistol.INSTANCE, GomuBulletAbility.INSTANCE, TrueGomuGatling.INSTANCE,
				TrueGomuBazooka.INSTANCE, GomuFusenAbility.INSTANCE, TrueGomuRocket.INSTANCE, TrueGearSecondAbility.INSTANCE,
				TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE, TrueGearFifthAbility.INSTANCE, GomuUfoAbility.INSTANCE,
				MoguraPistolAbility.INSTANCE, GomuGomuNoKaminariAbility.INSTANCE, GomuGomuNoCannonballAbility.INSTANCE,
				TrueBouncyAbility.INSTANCE, GomuMorphsAbility.INSTANCE, GomuTrampleAbility.INSTANCE, GomuGomuNoCymbalAbility.INSTANCE,
		};
		if (WyHelper.isAprilFirst()) {
            cores = Arrays.copyOf(cores, cores.length + 1);
			cores[cores.length-1] = GearSixthAbility.INSTANCE;
        }
		DeferredHolder[] finalCores = cores;
		HITO_HITO_NO_MI_NIKA = registerFruit(() -> new AkumaNoMiItem(2, FruitType.PARAMECIA, finalCores), "Gomu Gomu no Mi");
	}
}
