package net.warcar.hito_hito_nika.challenges;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.world.World;
import net.warcar.hito_hito_nika.entities.LuffyBoss;
import net.warcar.hito_hito_nika.init.GomuEntities;
import net.warcar.hito_hito_nika.init.NPCGroups;
import xyz.pixelatedw.mineminenomi.api.challenges.*;
import xyz.pixelatedw.mineminenomi.challenges.arenas.JungleClearingSimpleArena;
import xyz.pixelatedw.mineminenomi.init.ModArmors;
import xyz.pixelatedw.mineminenomi.items.armors.StrawHatItem;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class PreTSLuffyChallenge extends Challenge {
    public static final ChallengeCore INSTANCE = new ChallengeCore.Builder<>("luffy", "Luffy",
            "Defeat Luffy (Sabaody)", NPCGroups.STRAWHATS.getName(), PreTSLuffyChallenge::new).setDifficultyStars(5)
            .setEnemySpawns(PreTSLuffyChallenge::getEnemySpawns).setTargetShowcase(PreTSLuffyChallenge::createShowcase)
            .addArena(ArenaStyle.SIMPLE, JungleClearingSimpleArena.INSTANCE, JungleClearingSimpleArena::getChallengerSpawnPos, JungleClearingSimpleArena::getEnemySpawnPos)
            .build();

    public PreTSLuffyChallenge(ChallengeCore<?> core) {
        super(core);
    }

    public static Set<ChallengeArena.EnemySpawn> getEnemySpawns(InProgressChallenge challenge, ChallengeArena.SpawnPosition[] poss) {
        Set<ChallengeArena.EnemySpawn> spawns = new HashSet<>();
        spawns.add(new ChallengeArena.EnemySpawn(new LuffyBoss(challenge), poss[0]));
        return spawns;
    }

    public static LivingEntity createShowcase(World level) {
        LuffyBoss boss = GomuEntities.LUFFY.create(level);
        StrawHatItem hat = (StrawHatItem) ModArmors.STRAW_HAT.get();
        ItemStack item = new ItemStack(hat);
        hat.setColor(item, Color.RED.getRGB());
        boss.setItemSlot(EquipmentSlotType.HEAD, item);
        return boss;
    }
}
