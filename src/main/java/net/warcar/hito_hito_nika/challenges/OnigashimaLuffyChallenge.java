package net.warcar.hito_hito_nika.challenges;

import net.warcar.hito_hito_nika.init.NPCGroups;
import xyz.pixelatedw.mineminenomi.api.challenges.ArenaStyle;
import xyz.pixelatedw.mineminenomi.api.challenges.Challenge;
import xyz.pixelatedw.mineminenomi.api.challenges.ChallengeCore;
import xyz.pixelatedw.mineminenomi.api.challenges.ChallengeDifficulty;
import xyz.pixelatedw.mineminenomi.challenges.arenas.JungleClearingSimpleArena;

public class OnigashimaLuffyChallenge extends Challenge {
    public static final ChallengeCore INSTANCE = new ChallengeCore.Builder<>("luffy_ultimate", "Luffy (Ultimate)",
            "Defeat Luffy (Onigashima)", NPCGroups.STRAWHATS.getName(), OnigashimaLuffyChallenge::new).setDifficultyStars(5)
            .setEnemySpawns(PreTSLuffyChallenge::getEnemySpawns).setTargetShowcase(DressrosaLuffyChallenge::createShowcase)
            .addArena(ArenaStyle.SIMPLE, JungleClearingSimpleArena.INSTANCE, JungleClearingSimpleArena::getChallengerSpawnPos, JungleClearingSimpleArena::getEnemySpawnPos)
            .setDifficulty(ChallengeDifficulty.ULTIMATE).build();

    public OnigashimaLuffyChallenge(ChallengeCore<?> core) {
        super(core);
    }
}
