package net.warcar.hito_hito_nika.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.effects.GomuReviveEffect;
import net.warcar.hito_hito_nika.effects.SquishedEffect;
import xyz.pixelatedw.mineminenomi.api.WyHelper;

import java.util.function.Supplier;

public class GomuEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, HitoHitoNoMiNikaMod.MOD_ID);
    public static final RegistryObject<GomuReviveEffect> GOMU_REVIVE = register("Dead", GomuReviveEffect::new);
    public static final RegistryObject<SquishedEffect> SQUISHED = register("Squished", SquishedEffect::new);

    private static <E extends MobEffect> RegistryObject<E> register(String name, Supplier<E> supplier) {
        String registryName = WyHelper.getResourceName(name);
        return EFFECTS.register(registryName, supplier);
    }

    public static void register(IEventBus bus) {
        EFFECTS.register(bus);
    }
}
