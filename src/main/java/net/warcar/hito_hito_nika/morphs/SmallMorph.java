package net.warcar.hito_hito_nika.morphs;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;

import java.util.Map;

public class SmallMorph extends MorphInfo {
    private static final EntityDimensions STANDING_SIZE_SMALL = EntityDimensions.scalable(0.2F, 1.1F);
    private static final EntityDimensions CROUCHING_SIZE_SMALL = EntityDimensions.scalable(0.3F, 1F);


    public Component getDisplayName() {
        return Component.literal("Small Form");
    }

    public String getForm() {
        return "small";
    }

    public float getEyeHeight(LivingEntity entity) {
        return 0.9f;
    }

    public float getShadowSize() {
        return 0.2f;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTexture(LivingEntity entity) {
        return entity instanceof AbstractClientPlayer ? ((AbstractClientPlayer)entity).getSkin().texture() : null;
    }

    public Map<Pose, EntityDimensions> getSizes() {
        return ImmutableMap.<Pose, EntityDimensions>builder().put(Pose.STANDING, STANDING_SIZE_SMALL).put(Pose.CROUCHING, CROUCHING_SIZE_SMALL).build();
    }
}
