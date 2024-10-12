package net.warcar.hito_hito_nika.morphs;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import net.warcar.hito_hito_nika.models.GomuFusenModel;
import net.warcar.hito_hito_nika.renderers.morphs.GomuMorphRenderer;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.init.ModAbilities;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

import java.util.Map;

public class FusenMorph extends MorphInfo {
    private static final EntitySize STANDING_SIZE_FUSEN = EntitySize.scalable(1.6F, 2.8F);
    private static final EntitySize CROUCHING_SIZE_FUSEN = EntitySize.scalable(1.7F, 2.6F);


    public String getDisplayName() {
        return "Gomu Gomu no Fusen";
    }

    @OnlyIn(Dist.CLIENT)
    public IRenderFactory getRendererFactory(LivingEntity entity) {
        boolean isSlim = false;
        if (entity instanceof AbstractClientPlayerEntity) {
            isSlim = ((AbstractClientPlayerEntity)entity).getModelName().equals("slim");
        }

        return new GomuMorphRenderer.Factory(this, isSlim);
    }

    @OnlyIn(Dist.CLIENT)
    public MorphModel getModel() {
        return new GomuFusenModel();
    }

    public AkumaNoMiItem getDevilFruit() {
        return ModAbilities.GOMU_GOMU_NO_MI;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTexture(LivingEntity entity) {
        return entity instanceof AbstractClientPlayerEntity ? ((AbstractClientPlayerEntity)entity).getSkinTextureLocation() : null;
    }

    public String getForm() {
        return "fusen";
    }

    public double getEyeHeight() {
        return 2.8;
    }

    public float getShadowSize() {
        return 1f;
    }

    public Map<Pose, EntitySize> getSizes() {
        return ImmutableMap.<Pose, EntitySize>builder().put(Pose.STANDING, STANDING_SIZE_FUSEN).put(Pose.CROUCHING, CROUCHING_SIZE_FUSEN).build();
    }
}
