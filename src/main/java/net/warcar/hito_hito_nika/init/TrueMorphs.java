package net.warcar.hito_hito_nika.init;

import net.minecraftforge.fml.RegistryObject;
import net.warcar.hito_hito_nika.morphs.*;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;

public class TrueMorphs {

    public static final RegistryObject<MorphInfo> BOUNDMAN = WyRegistry.registerMorph("gear_4_boundman", GearFourthBoundmanMorph::new);

    public static final RegistryObject<MorphInfo> SNAKEMAN = WyRegistry.registerMorph("gear_4_snakeman", GearFourthSnakemanMorph::new);

    public static final RegistryObject<MorphInfo> FUSEN = WyRegistry.registerMorph("fusen", FusenMorph::new);

    public static final RegistryObject<MorphInfo> SMALL = WyRegistry.registerMorph("small", SmallMorph::new);

    public static final RegistryObject<MorphInfo> GIANT_FUSEN = WyRegistry.registerMorph("giant_fusen", GigantFusenMorph::new);

    public static final RegistryObject<MorphInfo> GIANT = WyRegistry.registerMorph("giant", GomuGigantMorph::new);

    public static void init() {
    }
}
