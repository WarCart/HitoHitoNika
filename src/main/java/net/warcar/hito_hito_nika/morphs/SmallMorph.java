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
import net.warcar.hito_hito_nika.models.SmallMorphModel;
import net.warcar.hito_hito_nika.renderers.morphs.GomuMorphRenderer;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

import java.util.Map;

public class SmallMorph extends MorphInfo {
    private static final EntitySize STANDING_SIZE_SMALL = EntitySize.scalable(0.2F, 1.1F);
    private static final EntitySize CROUCHING_SIZE_SMALL = EntitySize.scalable(0.3F, 1F);


    public String getDisplayName() {
        return "Small Form";
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
        return new SmallMorphModel();
    }

    public AkumaNoMiItem getDevilFruit() {
        return TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA;
    }

    public String getForm() {
        return "small";
    }

    public double getEyeHeight() {
        return 0.9;
    }

    public float getShadowSize() {
        return 0.2f;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTexture(LivingEntity entity) {
        return entity instanceof AbstractClientPlayerEntity ? ((AbstractClientPlayerEntity)entity).getSkinTextureLocation() : null;
    }

    public Map<Pose, EntitySize> getSizes() {
        return ImmutableMap.<Pose, EntitySize>builder().put(Pose.STANDING, STANDING_SIZE_SMALL).put(Pose.CROUCHING, CROUCHING_SIZE_SMALL).build();
    }
}
