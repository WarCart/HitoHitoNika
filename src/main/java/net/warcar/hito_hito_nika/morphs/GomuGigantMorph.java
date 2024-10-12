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
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;
import xyz.pixelatedw.mineminenomi.init.ModAbilities;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;
import xyz.pixelatedw.mineminenomi.renderers.morphs.MegaRenderer;

import java.util.Map;

public class GomuGigantMorph extends MorphInfo {
    private static final EntitySize STANDING_SIZE = EntitySize.scalable(1.7F, 2.4F);
    private static final EntitySize CROUCHING_SIZE = EntitySize.scalable(1.7F, 2.39F);

    public AkumaNoMiItem getDevilFruit() {
        return ModAbilities.GOMU_GOMU_NO_MI;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTexture(LivingEntity entity) {
        return entity instanceof AbstractClientPlayerEntity ? ((AbstractClientPlayerEntity) entity).getSkinTextureLocation() : null;
    }

    @OnlyIn(Dist.CLIENT)
    public IRenderFactory getRendererFactory(LivingEntity entity) {
        boolean isSlim = false;
        if (entity instanceof AbstractClientPlayerEntity) {
            isSlim = ((AbstractClientPlayerEntity)entity).getModelName().equals("slim");
        }

        return new MegaRenderer.Factory(this, isSlim);
    }

    public String getForm() {
        return "gomu_gigant";
    }

    public MorphModel getModel() {
        return null;
    }

    public String getDisplayName() {
        return "Gomu Gomu no Gigant";
    }

    public double getEyeHeight() {
        return 8.45D;
    }

    public Map<Pose, EntitySize> getSizes() {
        return ImmutableMap.<Pose, EntitySize>builder().put(Pose.STANDING, STANDING_SIZE).put(Pose.CROUCHING, CROUCHING_SIZE).build();
    }
}
