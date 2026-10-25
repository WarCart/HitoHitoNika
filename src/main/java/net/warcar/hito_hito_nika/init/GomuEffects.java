package net.warcar.hito_hito_nika.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.effects.GomuReviveEffect;
import net.warcar.hito_hito_nika.effects.SquishedEffect;
import xyz.pixelatedw.mineminenomi.api.WyHelper;

import java.util.function.Supplier;

public class GomuEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, HitoHitoNoMiNikaMod.MOD_ID);
    public static final DeferredHolder<MobEffect, GomuReviveEffect> GOMU_REVIVE = register("Dead", GomuReviveEffect::new);
    public static final DeferredHolder<MobEffect, SquishedEffect> SQUISHED = register("Squished", SquishedEffect::new);

    private static <E extends MobEffect> DeferredHolder<MobEffect, E> register(String name, Supplier<E> supplier) {
        String registryName = WyHelper.getResourceName(name);
        return EFFECTS.register(registryName, supplier);
    }

    public static void register(IEventBus bus) {
        EFFECTS.register(bus);
    }
}
