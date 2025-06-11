package net.warcar.hito_hito_nika.morphs;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import net.warcar.hito_hito_nika.models.SnakemanMorphModel;
import net.warcar.hito_hito_nika.renderers.morphs.GomuMorphRenderer;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;
import xyz.pixelatedw.mineminenomi.entities.zoan.GomuGiantMorphInfo;
import xyz.pixelatedw.mineminenomi.init.ModAbilities;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

import java.util.Map;

public class GearFourthSnakemanMorph extends MorphInfo {
    private static final EntitySize STANDING_SIZE_SNAKEMAN = EntitySize.scalable(0.7F, 1.9875F);
    private static final EntitySize CROUCHING_SIZE_SNAKEMAN = EntitySize.scalable(0.8F, 1.8F);

    public String getDisplayName() {
        return "Gomu Gomu Gear Fourth Snakeman";
    }

    @OnlyIn(Dist.CLIENT)
    public MorphModel<PlayerEntity> getModel() {
        return new SnakemanMorphModel<>();
    }

    @OnlyIn(Dist.CLIENT)
    public IRenderFactory getRendererFactory(LivingEntity entity) {
        return new GomuMorphRenderer.Factory(this).setOverlays(new ResourceLocation(HitoHitoNoMiNikaMod.MOD_ID, "textures/models/gear_4_snakeman_overlay.png"));
    }

    public AkumaNoMiItem getDevilFruit() {
        return ModAbilities.GOMU_GOMU_NO_MI;
    }

    public String getForm() {
        return "gear_4_snakeman";
    }

    public double getEyeHeight() {
        return 1.8;
    }

    public float getShadowSize() {
        return 1.2f;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTexture(LivingEntity entity) {
        return entity instanceof AbstractClientPlayerEntity ? ((AbstractClientPlayerEntity)entity).getSkinTextureLocation() : null;
    }

    public Map<Pose, EntitySize> getSizes() {
        return ImmutableMap.<Pose, EntitySize>builder().put(Pose.STANDING, STANDING_SIZE_SNAKEMAN).put(Pose.CROUCHING, CROUCHING_SIZE_SNAKEMAN).build();
    }

}
