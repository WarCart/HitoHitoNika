package net.warcar.hito_hito_nika.morphs;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import net.warcar.hito_hito_nika.renderers.morphs.GomuMorphRenderer;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

import java.util.Map;

public class GomuGigantMorph extends MorphInfo {
    private static final EntitySize STANDING_SIZE = EntitySize.scalable(1.7F, 2.4F);
    private static final EntitySize CROUCHING_SIZE = EntitySize.scalable(1.7F, 2.39F);

    public AkumaNoMiItem getDevilFruit() {
        return TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getTexture(AbstractClientPlayerEntity entity) {
        return entity.getSkinTextureLocation();
    }

    @OnlyIn(Dist.CLIENT)
    public IRenderFactory getRendererFactory(AbstractClientPlayerEntity entity) {
        boolean isSlim = entity.getModelName().equals("slim");
        return new GomuMorphRenderer.Factory(this, isSlim).setOverlays().setSize(new Vector3f(4.5f, 4.5f, 4.5f));
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
