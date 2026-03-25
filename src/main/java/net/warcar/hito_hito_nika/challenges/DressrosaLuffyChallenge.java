package net.warcar.hito_hito_nika.challenges;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.entities.LuffyBoss;
import net.warcar.hito_hito_nika.init.GomuEntities;
import net.warcar.hito_hito_nika.init.NPCGroups;
import xyz.pixelatedw.mineminenomi.api.challenges.*;
import xyz.pixelatedw.mineminenomi.challenges.arenas.JungleClearingSimpleArena;
import xyz.pixelatedw.mineminenomi.init.ModArmors;
import xyz.pixelatedw.mineminenomi.items.armors.Mod3DArmorItem;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class DressrosaLuffyChallenge extends Challenge {
    public static final ChallengeCore INSTANCE = new ChallengeCore.Builder<>("luffy_hard", "Luffy (Hard)",
            "Defeat Luffy (Dressrosa)", NPCGroups.STRAWHATS.getName(), DressrosaLuffyChallenge::new).setDifficultyStars(5)
            .setEnemySpawns(PreTSLuffyChallenge::getEnemySpawns).setTargetShowcase(DressrosaLuffyChallenge::createShowcase)
            .addArena(ArenaStyle.SIMPLE, JungleClearingSimpleArena.INSTANCE, JungleClearingSimpleArena::getChallengerSpawnPos, JungleClearingSimpleArena::getEnemySpawnPos)
            .setDifficulty(ChallengeDifficulty.HARD).build();

    public DressrosaLuffyChallenge(ChallengeCore<?> core) {
        super(core);
    }


    public static LivingEntity createShowcase(Level level) {
        LuffyBoss boss = GomuEntities.LUFFY.get().create(level);
        boss.setPostTs(true);
        Mod3DArmorItem hat = ModArmors.STRAW_HAT.get();
        ItemStack item = new ItemStack(hat);
        boss.setItemSlot(EquipmentSlot.HEAD, item);
        return boss;
    }
}
