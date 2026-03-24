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

public class GearFourthTankmanMorph extends MorphInfo {
    private static final EntityDimensions STANDING_SIZE = EntityDimensions.scalable(4.8F, 4.5F);
    private static final EntityDimensions CROUCHING_SIZE = EntityDimensions.scalable(4.8F, 4.4F);

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTexture(LivingEntity entity) {
        return entity instanceof AbstractClientPlayer ? ((AbstractClientPlayer) entity).getSkinTextureLocation() : null;
    }

    @Override
    public double getCameraZoom(LivingEntity entity) {
        return 7;
    }

    public String getForm() {
        return "gear_4th_tankman";
    }

    public Component getDisplayName() {
        return Component.literal("Gear fourth: Tankman");
    }

    public float getEyeHeight(LivingEntity entity) {
        return 4.5f;
    }

    public float getShadowSize() {
        return 3.2F;
    }

    public Map<Pose, EntityDimensions> getSizes() {
        return ImmutableMap.<Pose, EntityDimensions>builder().put(Pose.STANDING, STANDING_SIZE).put(Pose.CROUCHING, CROUCHING_SIZE).build();
    }
}
