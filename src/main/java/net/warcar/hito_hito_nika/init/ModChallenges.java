package net.warcar.hito_hito_nika.init;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.challenges.DressrosaLuffyChallenge;
import net.warcar.hito_hito_nika.challenges.OnigashimaLuffyChallenge;
import net.warcar.hito_hito_nika.challenges.PreTSLuffyChallenge;
import xyz.pixelatedw.mineminenomi.api.ModRegistries;
import xyz.pixelatedw.mineminenomi.api.challenges.ChallengeCore;

public class ModChallenges {
    public static final DeferredRegister<ChallengeCore<?>> CHALLENGES = DeferredRegister.create(ModRegistries.CHALLENGES, HitoHitoNoMiNikaMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        CHALLENGES.register(eventBus);
        registerChallenge(PreTSLuffyChallenge.INSTANCE);
        registerChallenge(DressrosaLuffyChallenge.INSTANCE);
        registerChallenge(OnigashimaLuffyChallenge.INSTANCE);
    }

    public static void registerChallenge(ChallengeCore<?> core) {
        CHALLENGES.register(core.getId(), () -> core);
    }
}
