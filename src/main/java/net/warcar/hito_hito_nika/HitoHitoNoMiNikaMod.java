package net.warcar.hito_hito_nika;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.warcar.hito_hito_nika.config.CommonConfig;
import net.warcar.hito_hito_nika.effects.GomuReviveEffect;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.GomuEntities;
import net.warcar.hito_hito_nika.init.ModChallenges;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import net.warcar.hito_hito_nika.init.TrueMorphs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(HitoHitoNoMiNikaMod.MOD_ID)
public class HitoHitoNoMiNikaMod
{
    public static final String MOD_ID = "hito_hito_no_mi_nika";
    public static final Logger LOGGER = LogManager.getLogger();
    private static final Map<String, String> langMap = new HashMap<>();

    public HitoHitoNoMiNikaMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(this::enqueueIMC);
        bus.addListener(this::processIMC);
        bus.addListener(this::doClientStuff);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        GomuReviveEffect.register();
        TrueMorphs.init();
        TrueGomuHelper.init();
        TrueGomuGomuNoMi.register(bus);
        ModChallenges.register(bus);
        GomuEntities.register(bus);
        langMap.put("text.mineminenomi.too_weak", "You are to weak to use this ability");
        langMap.put("text.mineminenomi.requires_infusion", "You need to activate Hao Infusion to use this move");
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {}

    private void doClientStuff(final FMLClientSetupEvent event) {}

    private void enqueueIMC(final InterModEnqueueEvent event) {}

    private void processIMC(final InterModProcessEvent event) {}

    public static Map<String, String> getLangMap() {
        return langMap;
    }
}
