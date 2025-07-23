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
import net.warcar.hito_hito_nika.models.TankmanMorphModel;
import net.warcar.hito_hito_nika.renderers.morphs.GomuMorphRenderer;
import xyz.pixelatedw.mineminenomi.abilities.gomu.GearFourthAbility;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;
import xyz.pixelatedw.mineminenomi.init.ModResources;
import xyz.pixelatedw.mineminenomi.models.morphs.NoMorphModel;

import java.util.Map;

public class GearFourthTankmanMorph extends MorphInfo {
    private static final EntitySize STANDING_SIZE = EntitySize.scalable(4.8F, 4.5F);
    private static final EntitySize CROUCHING_SIZE = EntitySize.scalable(4.8F, 4.4F);

    @OnlyIn(Dist.CLIENT)
    public MorphModel getModel() {
        return new TankmanMorphModel(true);
        //return new NoMorphModel(true);
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTexture(LivingEntity entity) {
        return entity instanceof AbstractClientPlayerEntity ? ((AbstractClientPlayerEntity) entity).getSkinTextureLocation() : null;
    }

    @OnlyIn(Dist.CLIENT)
    public IRenderFactory getRendererFactory(LivingEntity entity) {
        return new GomuMorphRenderer.Factory(this).setOverlays(new ResourceLocation(HitoHitoNoMiNikaMod.MOD_ID, "textures/models/g4_tankman_overlay.png"));
    }

    @Override
    public double getCameraZoom(LivingEntity entity) {
        return 7;
    }

    public String getForm() {
        return "gear_4th_tankman";
    }

    public String getDisplayName() {
        return GearFourthAbility.INSTANCE.getUnlocalizedName();
    }

    public double getEyeHeight() {
        return 4.5;
    }

    public float getShadowSize() {
        return 3.2F;
    }

    public Map<Pose, EntitySize> getSizes() {
        return ImmutableMap.<Pose, EntitySize>builder().put(Pose.STANDING, STANDING_SIZE).put(Pose.CROUCHING, CROUCHING_SIZE).build();
    }
}
