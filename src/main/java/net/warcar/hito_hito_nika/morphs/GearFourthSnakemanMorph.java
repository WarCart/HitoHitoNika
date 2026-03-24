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

public class GearFourthSnakemanMorph extends MorphInfo {
    private static final EntityDimensions STANDING_SIZE_SNAKEMAN = EntityDimensions.scalable(0.7F, 1.9875F);
    private static final EntityDimensions CROUCHING_SIZE_SNAKEMAN = EntityDimensions.scalable(0.8F, 1.8F);

    public Component getDisplayName() {
        return Component.literal("Gomu Gomu Gear Fourth Snakeman");
    }

    public float getEyeHeight(LivingEntity entity) {
        return 1.8f;
    }

    public float getShadowSize() {
        return 1.2f;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTexture(LivingEntity entity) {
        return entity instanceof AbstractClientPlayer ? ((AbstractClientPlayer)entity).getSkinTextureLocation() : null;
    }

    public Map<Pose, EntityDimensions> getSizes() {
        return ImmutableMap.<Pose, EntityDimensions>builder().put(Pose.STANDING, STANDING_SIZE_SNAKEMAN).put(Pose.CROUCHING, CROUCHING_SIZE_SNAKEMAN).build();
    }

}
