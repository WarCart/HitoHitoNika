package net.warcar.hito_hito_nika;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.fml.event.lifecycle.InterModProcessEvent;
import net.warcar.hito_hito_nika.config.CommonConfig;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.*;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
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

    public HitoHitoNoMiNikaMod(IEventBus bus, ModContainer modContainer) {
        bus.addListener(this::setup);
        bus.addListener(this::enqueueIMC);
        bus.addListener(this::processIMC);
        bus.addListener(this::doClientStuff);
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        TrueMorphs.init();
        TrueGomuHelper.init();
        TrueGomuGomuNoMi.register(bus);
        ModEntityDataSerializers.register(bus);
        ModChallenges.register(bus);
        GomuEntities.register(bus);
        NikaProjectiles.init();
        GomuEffects.register(bus);
        NPCGroups.init();
        langMap.put("text.hito_hito_no_mi_nika.too_weak", "You are to weak to use this ability");
        langMap.put("text.hito_hito_no_mi_nika.requires_infusion", "You need to activate Hao Infusion to use this move");
        langMap.put("text.hito_hito_no_mi_nika.the_peak", "The peak");
        langMap.put("text.hito_hito_no_mi_nika.wait_power", "Wait, power!");
        langMap.put("text.hito_hito_no_mi_nika.the_peak.desc", "I can do everything I wanted to do!! I think I can fight a bit longer... My heartbeat sounds so funny!! This is the pinnacle of what I can do!! This is... Gear 5!!!");
        langMap.put("text.hito_hito_no_mi_nika.wait_power.desc", "Stop Gear 4 from ending to fight even longer");
    }

    private void setup(final FMLCommonSetupEvent event) {}

    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            GomuAnimations.init();
        });
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {}

    private void processIMC(final InterModProcessEvent event) {}

    public static Map<String, String> getLangMap() {
        return langMap;
    }
}
