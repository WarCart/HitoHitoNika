package net.warcar.hito_hito_nika.challenges;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
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

public class DressrosaLuffyChallenge extends Challenge {
    public static final ChallengeCore INSTANCE = new ChallengeCore.Builder<>("luffy_hard", "Luffy (Hard)",
            "Defeat Luffy (Dressrosa)", NPCGroups.STRAWHATS.getName(), DressrosaLuffyChallenge::new).setDifficultyStars(5)
            .setEnemySpawns(PreTSLuffyChallenge::getEnemySpawns).setTargetShowcase(DressrosaLuffyChallenge::createShowcase)
            .addArena(ArenaStyle.SIMPLE, JungleClearingSimpleArena.INSTANCE, JungleClearingSimpleArena::getChallengerSpawnPos, JungleClearingSimpleArena::getEnemySpawnPos)
            .setDifficulty(ChallengeDifficulty.HARD).build();

    public DressrosaLuffyChallenge(ChallengeCore<?> core) {
        super(core);
    }


    public static LivingEntity createShowcase(World level) {
        LuffyBoss boss = GomuEntities.LUFFY.create(level);
        boss.setPostTs(true);
        StrawHatItem hat = (StrawHatItem) ModArmors.STRAW_HAT.get();
        ItemStack item = new ItemStack(hat);
        hat.setColor(item, Color.RED.getRGB());
        boss.setItemSlot(EquipmentSlotType.HEAD, item);
        return boss;
    }
}
