package net.warcar.hito_hito_nika.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = "hito_hito_no_mi_nika", bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonConfig {
    public static final CommonConfig INSTANCE;
    public static final ForgeConfigSpec SPEC;
    private final ForgeConfigSpec.BooleanValue blackLegAbilities;
    private final ForgeConfigSpec.BooleanValue nonCanonAbilities;

    public CommonConfig(ForgeConfigSpec.Builder builder) {
        this.blackLegAbilities = builder.comment("Uses leg abilities if you are Black leg user\nDefault: true").define("Black Leg Abilities", true);
        this.nonCanonAbilities = builder.comment("Abilities from video games, films, ect.\nDefault: false").define("Non-Canon Abilities", false);
    }

    public boolean isLegAbilities() {
        return this.blackLegAbilities.get();
    }

    public boolean isNonCanon() {
        return this.nonCanonAbilities.get();
    }

    static {
        Pair<CommonConfig, ForgeConfigSpec> pair = (new ForgeConfigSpec.Builder()).configure(CommonConfig::new);
        SPEC = pair.getRight();
        INSTANCE = pair.getLeft();
    }
}
