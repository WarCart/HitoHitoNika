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
    private final ForgeConfigSpec.ConfigValue<String> g2Length;
    private final ForgeConfigSpec.ConfigValue<String> g2Cooldown;
    private final ForgeConfigSpec.ConfigValue<String> g3Length;
    private final ForgeConfigSpec.ConfigValue<String> g3Cooldown;
    private final ForgeConfigSpec.ConfigValue<String> g4Length;
    private final ForgeConfigSpec.ConfigValue<String> g4Cooldown;
    private final ForgeConfigSpec.ConfigValue<String> g4PostBonusCooldown;
    private final ForgeConfigSpec.ConfigValue<String> g4EffectsLength;
    private final ForgeConfigSpec.ConfigValue<String> g5Length;
    private final ForgeConfigSpec.ConfigValue<String> g5Cooldown;
    private final ForgeConfigSpec.ConfigValue<String> fusenLength;
    private final ForgeConfigSpec.ConfigValue<String> fusenCooldown;
    private final ForgeConfigSpec.ConfigValue<String> gatlingLength;
    private final ForgeConfigSpec.ConfigValue<String> gatlingCooldown;

    public CommonConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Abilities");
        this.blackLegAbilities = builder.comment("Uses leg abilities if you are Black leg user\nDefault: true").define("Black Leg Abilities", true);
        this.nonCanonAbilities = builder.comment("Abilities from video games, films, ect.\nDefault: false").define("Non-Canon Abilities", false);
        builder.pop();
        builder.push("Balance");
        builder.push("G2");
        this.g2Length = builder.comment("Regulates length of second gear").define("G2 length equation", "multiply(doriki(), n(0.02f))");
        this.g2Cooldown = builder.comment("Regulates length of second gear's cooldown").define("G2 cooldown equation", "multiply(n(20), add(n(1), sqrt(length())))");
        builder.pop();
        builder.push("G3");
        this.g3Length = builder.comment("Regulates length of third gear").define("G3 length equation", "multiply(doriki(), n(0.01f))");
        this.g3Cooldown = builder.comment("Regulates length of third gear's cooldown").define("G3 cooldown equation", "multiply(n(20), add(n(2), sqrt(multiply(n(2), length()))))");
        builder.pop();
        builder.push("G4");
        this.g4Length = builder.comment("Regulates length of fourth gear").define("G4 length equation", "multiply(doriki(), n(0.005f))");
        this.g4Cooldown = builder.comment("Regulates length of fourth gear's cooldown").define("G4 cooldown equation", "add(n(1), divide(effect(), n(60)), multiply(sqrt(divide(length(), n(20))), n(20)))");
        this.g4PostBonusCooldown = builder.comment("Regulates length of fourth gear's cooldown if bonus time was activated").define("G4 bonus cooldown equation", "add(n(1), divide(effect(), n(60)), length())");
        this.g4EffectsLength = builder.comment("Regulates length of fourth gear's cooldown's effects (weakness, hunger, slowness)").define("G4 effects equation", "subtract(n(600), totalHakiXp())");
        builder.pop();
        builder.push("G5");
        this.g5Length = builder.comment("Regulates length of fifth gear").define("G5 length equation", "multiply(doriki(), n(0.003f))");
        this.g5Cooldown = builder.comment("Regulates length of fifth gear's cooldown").define("G5 cooldown equation", "length()");
        builder.pop();
        builder.push("Fusen");
        this.fusenLength = builder.comment("Regulates length of fusen").define("Fusen length equation", "add(multiply(doriki(), n(0.1f)), n(15))");
        this.fusenCooldown = builder.comment("Regulates length of fusen's cooldown").define("Fusen cooldown equation", "length()");
        builder.pop();
        builder.push("Gatling");
        this.gatlingLength = builder.comment("Regulates length of gatling", "Old function: multiply(n(3), add(n(1), sqrt(divide(doriki(), multiply(n(100), dif())))))").define("Gatling length equation", "n(10)");
        this.gatlingCooldown = builder.comment("Regulates length of gatling's cooldown").define("Gatling cooldown equation", "multiply(n(20), max(n(3), divide(multiply(length(), dif()), n(200))))");
        builder.pop();
        builder.pop();
    }

    public boolean isLegAbilities() {
        return this.blackLegAbilities.get();
    }

    public boolean isNonCanon() {
        return this.nonCanonAbilities.get();
    }

    public String getG2Length() {
        return this.g2Length.get();
    }

    public String getG2Cooldown() {
        return this.g2Cooldown.get();
    }

    public String getG3Length() {
        return this.g3Length.get();
    }

    public String getG3Cooldown() {
        return this.g3Cooldown.get();
    }

    public String getG4Length() {
        return this.g4Length.get();
    }

    public String getG4Cooldown() {
        return this.g4Cooldown.get();
    }

    public String getG4PostBonusCooldown() {
        return this.g4PostBonusCooldown.get();
    }

    public String getG4EffectsLength() {
        return this.g4EffectsLength.get();
    }

    public String getG5Length() {
        return this.g5Length.get();
    }

    public String getG5Cooldown() {
        return this.g5Cooldown.get();
    }

    public String getFusenLength() {
        return this.fusenLength.get();
    }

    public String getFusenCooldown() {
        return this.fusenCooldown.get();
    }

    public String getGatlingLength() {
        return this.gatlingLength.get();
    }

    public String getGatlingCooldown() {
        return this.gatlingCooldown.get();
    }

    static {
        Pair<CommonConfig, ForgeConfigSpec> pair = (new ForgeConfigSpec.Builder()).configure(CommonConfig::new);
        SPEC = pair.getRight();
        INSTANCE = pair.getLeft();
    }
}
