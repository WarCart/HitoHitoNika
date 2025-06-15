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
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.models.BoundmanMorphModel;
import net.warcar.hito_hito_nika.renderers.morphs.GomuMorphRenderer;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.abilities.gomu.GearFourthAbility;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;
import xyz.pixelatedw.mineminenomi.init.ModResources;

import java.util.Map;

public class GearFourthBoundmanMorph extends MorphInfo {
    private static final EntitySize STANDING_SIZE = EntitySize.scalable(2.8F, 4.0F);
    private static final EntitySize CROUCHING_SIZE = EntitySize.scalable(2.8F, 3.9F);

    @OnlyIn(Dist.CLIENT)
    public MorphModel getModel() {
        return new BoundmanMorphModel(true);
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTexture(LivingEntity entity) {
        return entity instanceof AbstractClientPlayerEntity ? ((AbstractClientPlayerEntity) entity).getSkinTextureLocation() : null;
    }

    @OnlyIn(Dist.CLIENT)
    public IRenderFactory getRendererFactory(LivingEntity entity) {
        return new GomuMorphRenderer.Factory(this).setOverlays(ModResources.G4_OVERLAY);
    }

    public String getForm() {
        return "gear_4th_boundman";
    }

    public String getDisplayName() {
        return GearFourthAbility.INSTANCE.getUnlocalizedName();
    }

    public double getEyeHeight() {
        return 4.0D;
    }

    public float getShadowSize() {
        return 1.2F;
    }

    public Map<Pose, EntitySize> getSizes() {
        return ImmutableMap.<Pose, EntitySize>builder().put(Pose.STANDING, STANDING_SIZE).put(Pose.CROUCHING, CROUCHING_SIZE).build();
    }
}
