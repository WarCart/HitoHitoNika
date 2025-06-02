package net.warcar.hito_hito_nika.init;

import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import xyz.pixelatedw.mineminenomi.api.crew.Crew;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.time.Instant;
import java.util.UUID;

public class NPCGroups {
    public static final TranslationTextComponent STRAWHATS_NAME = getName("Strawhats");
    public static final UUID STRAWHATS_UUID = UUID.fromString("f7fea672-ca16-49b2-ae9a-eb6d8180faa8");
    public static final Crew STRAWHATS = new Crew(STRAWHATS_NAME.getKey(), STRAWHATS_UUID, "CaptainMarker", Instant.now().getEpochSecond());


    private static TranslationTextComponent getName(String name) {
        String key = "crew.name." + WyHelper.getResourceName(name);
        HitoHitoNoMiNikaMod.getLangMap().put(key, name);
        return new TranslationTextComponent(key);
    }
}
