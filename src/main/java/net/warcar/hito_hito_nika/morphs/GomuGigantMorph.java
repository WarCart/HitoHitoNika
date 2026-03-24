package net.warcar.hito_hito_nika.morphs;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;

import java.util.Map;

public class GomuGigantMorph extends MorphInfo {
    private static final EntityDimensions STANDING_SIZE = EntityDimensions.scalable(1.7F, 2.4F);
    private static final EntityDimensions CROUCHING_SIZE = EntityDimensions.scalable(1.7F, 2.39F);

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTexture(LivingEntity entity) {
        return entity instanceof AbstractClientPlayer ? ((AbstractClientPlayer) entity).getSkinTextureLocation() : null;
    }

    public String getForm() {
        return "gomu_gigant";
    }

    public Component getDisplayName() {
        return Component.literal("Gomu Gomu no Gigant");
    }

    public float getEyeHeight(LivingEntity entity) {
        return 8.45f;
    }

    public Map<Pose, EntityDimensions> getSizes() {
        return ImmutableMap.<Pose, EntityDimensions>builder().put(Pose.STANDING, STANDING_SIZE).put(Pose.CROUCHING, CROUCHING_SIZE).build();
    }
}
