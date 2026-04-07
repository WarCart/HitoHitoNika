package net.warcar.hito_hito_nika.morphs;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.warcar.hito_hito_nika.abilities.TrueGearFourthAbility;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;

import java.util.Map;

public class GearFourthBoundmanMorph extends MorphInfo {
    private static final EntityDimensions STANDING_SIZE = EntityDimensions.scalable(2.8F, 3.5F);
    private static final EntityDimensions CROUCHING_SIZE = EntityDimensions.scalable(2.8F, 3.2F);


    public String getForm() {
        return "gear_4th_boundman";
    }

    public Component getDisplayName() {
        return TrueGearFourthAbility.INSTANCE.get().getLocalizedName();
    }

    public float getEyeHeight(LivingEntity entity) {
        if (entity.isCrouching()) {
            return 2.9F;
        }
        return 3.2f;
    }

    public float getShadowSize() {
        return 1.2F;
    }

    public Map<Pose, EntityDimensions> getSizes() {
        return ImmutableMap.<Pose, EntityDimensions>builder().put(Pose.STANDING, STANDING_SIZE).put(Pose.CROUCHING, CROUCHING_SIZE).build();
    }
}
