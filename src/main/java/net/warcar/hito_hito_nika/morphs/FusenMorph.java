package net.warcar.hito_hito_nika.morphs;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import net.warcar.hito_hito_nika.models.GomuFusenModel;
import net.warcar.hito_hito_nika.renderers.morphs.GomuMorphRenderer;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

import java.util.HashMap;
import java.util.Map;

public class FusenMorph extends MorphInfo {
    private static final EntityDimensions STANDING_SIZE_FUSEN = EntityDimensions.scalable(1.6F, 2.8F);
    private static final EntityDimensions CROUCHING_SIZE_FUSEN = EntityDimensions.scalable(1.7F, 2.6F);


    public Component getDisplayName() {
        return Component.literal("Gomu Gomu no Fusen");
    }

    @OnlyIn(Dist.CLIENT)
    public IRenderFactory getRendererFactory(LivingEntity entity) {
        return new GomuMorphRenderer.Factory(this);
    }

    @OnlyIn(Dist.CLIENT)
    public MorphModel getModel() {
        return new GomuFusenModel();
    }

    public AkumaNoMiItem getDevilFruit() {
        return TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTexture(LivingEntity entity) {
        return entity instanceof AbstractClientPlayer player ? player.getSkinTextureLocation() : null;
    }

    public float getEyeHeight(LivingEntity entity) {
        return 2.8f;
    }

    public float getShadowSize() {
        return 1f;
    }

    public Map<Pose, EntityDimensions> getSizes() {
        Map<Pose, EntityDimensions> sizes = new HashMap<>();
        sizes.put(Pose.STANDING, STANDING_SIZE_FUSEN);
        sizes.put(Pose.CROUCHING, CROUCHING_SIZE_FUSEN);
        return sizes;
    }
}
