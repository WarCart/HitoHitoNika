package net.warcar.hito_hito_nika.init;

import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.morphs.*;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.init.ModRegistry;

public class TrueMorphs {

    public static final RegistryObject<MorphInfo> BOUNDMAN = ModRegistry.registerMorph("gear_4_boundman", GearFourthBoundmanMorph::new);

    public static final RegistryObject<MorphInfo> TANKMAN = ModRegistry.registerMorph("gear_4_tankman", GearFourthTankmanMorph::new);

    public static final RegistryObject<MorphInfo> SNAKEMAN = ModRegistry.registerMorph("gear_4_snakeman", GearFourthSnakemanMorph::new);

    public static final RegistryObject<MorphInfo> FUSEN = ModRegistry.registerMorph("fusen", FusenMorph::new);

    public static final RegistryObject<MorphInfo> SMALL = ModRegistry.registerMorph("small", SmallMorph::new);

    public static final RegistryObject<MorphInfo> GIANT_FUSEN = ModRegistry.registerMorph("giant_fusen", GiantFusenMorph::new);

    public static final RegistryObject<MorphInfo> GIANT = ModRegistry.registerMorph("giant", GomuGigantMorph::new);

    public static void init() {
    }
}
