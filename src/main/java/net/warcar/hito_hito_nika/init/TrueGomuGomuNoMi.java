package net.warcar.hito_hito_nika.init;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.abilities.*;
import xyz.pixelatedw.mineminenomi.api.ModRegistries;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.enums.FruitType;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;

import java.util.Arrays;
import java.util.Objects;

public class TrueGomuGomuNoMi {
	public static final DeferredRegister<AbilityCore<?>> ABILITIES;
	public static final AkumaNoMiItem HITO_HITO_NO_MI_NIKA;

	private static <T extends AkumaNoMiItem> T registerFruit(T fruit) {
		WyRegistry.registerItem(fruit.getDevilFruitName(), () -> fruit);
		if (fruit.getAbilities() != null && fruit.getAbilities().length > 0) {
			registerAbilities(fruit.getAbilities());
		}
		return fruit;
	}

	private static void registerAbilities(AbilityCore<?>[] abilities) {
		Arrays.stream(abilities).filter(Objects::nonNull).forEach(TrueGomuGomuNoMi::registerAbility);
	}

	public static <T extends IAbility> AbilityCore<T> registerAbility(AbilityCore<T> core) {
		String resourceName = WyHelper.getResourceName(core.getId());
		ResourceLocation key = new ResourceLocation(HitoHitoNoMiNikaMod.MOD_ID, resourceName);
		HitoHitoNoMiNikaMod.getLangMap().put("ability."+ HitoHitoNoMiNikaMod.MOD_ID +"." + resourceName, core.getUnlocalizedName());
		ABILITIES.register(resourceName, () -> core);
		if (core.getIcon() == null) {
			core.setIcon(new ResourceLocation(key.getNamespace(), "textures/abilities/" + key.getPath() + ".png"));
		}

		return core;
	}

	public static void register(IEventBus bus) {
		ABILITIES.register(bus);
	}

	static {
		ABILITIES = DeferredRegister.create(ModRegistries.ABILITIES, HitoHitoNoMiNikaMod.MOD_ID);
		AbilityCore<?>[] cores = new AbilityCore[]{TrueGomuPistol.INSTANCE, GomuBulletAbility.INSTANCE, TrueGomuGatling.INSTANCE, TrueGomuBazooka.INSTANCE, GomuFusenAbility.INSTANCE, TrueGomuRocket.INSTANCE, TrueGearSecondAbility.INSTANCE, TrueGearThirdAbility.INSTANCE, TrueGearFourthAbility.INSTANCE,
				TrueGearFifthAbility.INSTANCE, GomuUfoAbility.INSTANCE, MoguraPistolAbility.INSTANCE, GomuGomuNoCannonballAbility.INSTANCE, TrueBouncyAbility.INSTANCE, GomuMorphsAbility.INSTANCE, GomuTrampleAbility.INSTANCE};
		if (WyHelper.isAprilFirst()) {
            cores = Arrays.copyOf(cores, cores.length + 1);
			cores[cores.length-1] = GearSixthAbility.INSTANCE;
        }
		HITO_HITO_NO_MI_NIKA = registerFruit(new AkumaNoMiItem("Gomu Gomu no Mi", 2, FruitType.PARAMECIA, cores));
	}
}
