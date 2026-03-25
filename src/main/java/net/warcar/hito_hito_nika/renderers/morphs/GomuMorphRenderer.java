package net.warcar.hito_hito_nika.renderers.morphs;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.hito_hito_nika.renderers.layers.TrueGomuSmokeLayer;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.renderers.layers.BodyCoatingLayer;
import xyz.pixelatedw.mineminenomi.renderers.layers.abilities.GomuDawnWhipLayer;
import xyz.pixelatedw.mineminenomi.renderers.morphs.MorphRenderer;

public class GomuMorphRenderer<T extends LivingEntity, M extends HumanoidModel<T>> extends MorphRenderer<T, M> {
    public GomuMorphRenderer(EntityRendererProvider.Context ctx, MorphInfo info, M model) {
        super(ctx, info, model);
        this.addLayer(new TrueGomuSmokeLayer<>(ctx, this));
        this.addLayer(new GomuDawnWhipLayer<>(ctx, this));
        this.addLayer(new BodyCoatingLayer<>(this));
    }

    public ResourceLocation getTextureLocation(T entity) {
        if (entity instanceof AbstractClientPlayer) {
            return ((AbstractClientPlayer) entity).getSkinTextureLocation();
        }
        return this.getOriginalRenderer().getTextureLocation(entity);
    }
}
