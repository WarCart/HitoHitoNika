package net.warcar.hito_hito_nika.morphs;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import net.warcar.hito_hito_nika.models.GomuGigantFusenModel;
import net.warcar.hito_hito_nika.renderers.morphs.GomuMorphRenderer;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;
import xyz.pixelatedw.mineminenomi.init.ModAbilities;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

import java.util.Map;

public class GigantFusenMorph extends MorphInfo {
    private static final EntityDimensions STANDING_SIZE = EntityDimensions.scalable(3.6F, 4.8F);
    private static final EntityDimensions CROUCHING_SIZE = EntityDimensions.scalable(3.7F, 4.6F);
    public String getDisplayName() {
        return "Gomu Gomu no Gigant Fusen";
    }

    @OnlyIn(Dist.CLIENT)
    public MorphModel getModel() {
        return new GomuGigantFusenModel();
    }

    @OnlyIn(Dist.CLIENT)
    public IRenderFactory getRendererFactory(LivingEntity entity) {
        return new GomuMorphRenderer.Factory(this);
    }

    public AkumaNoMiItem getDevilFruit() {
        return ModAbilities.GOMU_GOMU_NO_MI;
    }

    public String getForm() {
        return "gigant_fusen";
    }

    public double getEyeHeight() {
        return 4.8;
    }

    public float getShadowSize() {
        return 2f;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTexture(LivingEntity entity) {
        return entity instanceof AbstractClientPlayerEntity ? ((AbstractClientPlayerEntity)entity).getSkinTextureLocation() : null;
    }

    public Map<Pose, EntityDimensions> getSizes() {
        return ImmutableMap.<Pose, EntityDimensions>builder().put(Pose.STANDING, STANDING_SIZE).put(Pose.CROUCHING, CROUCHING_SIZE).build();
    }
}
