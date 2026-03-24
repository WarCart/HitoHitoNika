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

public class GiantFusenMorph extends MorphInfo {
    private static final EntityDimensions STANDING_SIZE = EntityDimensions.scalable(3.6F, 4.8F);
    private static final EntityDimensions CROUCHING_SIZE = EntityDimensions.scalable(3.7F, 4.6F);
    public Component getDisplayName() {
        return Component.literal("Gomu Gomu no Giant Fusen");
    }

    public String getForm() {
        return "giant_fusen";
    }

    public float getEyeHeight(LivingEntity entity) {
        return 4.8f;
    }

    public float getShadowSize() {
        return 2f;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTexture(LivingEntity entity) {
        return entity instanceof AbstractClientPlayer ? ((AbstractClientPlayer)entity).getSkinTextureLocation() : null;
    }

    public Map<Pose, EntityDimensions> getSizes() {
        return ImmutableMap.<Pose, EntityDimensions>builder().put(Pose.STANDING, STANDING_SIZE).put(Pose.CROUCHING, CROUCHING_SIZE).build();
    }
}
