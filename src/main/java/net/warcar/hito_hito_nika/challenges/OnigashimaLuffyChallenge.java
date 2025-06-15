package net.warcar.hito_hito_nika.challenges;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.warcar.hito_hito_nika.entities.LuffyBoss;
import net.warcar.hito_hito_nika.init.GomuEntities;
import net.warcar.hito_hito_nika.init.NPCGroups;
import xyz.pixelatedw.mineminenomi.api.challenges.ArenaStyle;
import xyz.pixelatedw.mineminenomi.api.challenges.Challenge;
import xyz.pixelatedw.mineminenomi.api.challenges.ChallengeCore;
import xyz.pixelatedw.mineminenomi.api.challenges.ChallengeDifficulty;
import xyz.pixelatedw.mineminenomi.challenges.arenas.JungleClearingSimpleArena;
import xyz.pixelatedw.mineminenomi.init.ModArmors;
import xyz.pixelatedw.mineminenomi.items.armors.StrawHatItem;

import java.awt.*;

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
