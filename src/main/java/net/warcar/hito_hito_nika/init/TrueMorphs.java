package net.warcar.hito_hito_nika.init;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.warcar.hito_hito_nika.models.*;
import net.warcar.hito_hito_nika.morphs.*;
import net.warcar.hito_hito_nika.renderers.morphs.GomuMorphRenderer;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.init.ModMorphs;
import xyz.pixelatedw.mineminenomi.init.ModRegistry;
import xyz.pixelatedw.mineminenomi.renderers.morphs.PlayerMorphRenderer;

public class TrueMorphs {

    public static final DeferredHolder<MorphInfo, GearFourthBoundmanMorph> BOUNDMAN = ModRegistry.registerMorph("gear_4_boundman", GearFourthBoundmanMorph::new);

    public static final DeferredHolder<MorphInfo, GearFourthTankmanMorph> TANKMAN = ModRegistry.registerMorph("gear_4_tankman", GearFourthTankmanMorph::new);

    public static final DeferredHolder<MorphInfo, GearFourthSnakemanMorph> SNAKEMAN = ModRegistry.registerMorph("gear_4_snakeman", GearFourthSnakemanMorph::new);

    public static final DeferredHolder<MorphInfo, FusenMorph> FUSEN = ModRegistry.registerMorph("fusen", FusenMorph::new);

    public static final DeferredHolder<MorphInfo, SmallMorph> SMALL = ModRegistry.registerMorph("small", SmallMorph::new);

    public static final DeferredHolder<MorphInfo, GiantFusenMorph> GIANT_FUSEN = ModRegistry.registerMorph("giant_fusen", GiantFusenMorph::new);

    public static final DeferredHolder<MorphInfo, GomuGigantMorph> GIANT = ModRegistry.registerMorph("giant", GomuGigantMorph::new);

    public static void init() {
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GomuFusenModel.LAYER_LOCATION, GomuFusenModel::createBodyLayer);
        event.registerLayerDefinition(GomuGiantFusenModel.LAYER_LOCATION, GomuGiantFusenModel::createBodyLayer);
        event.registerLayerDefinition(KingBajrangGunModel.LAYER_LOCATION, KingBajrangGunModel::createBodyLayer);
        event.registerLayerDefinition(SnakemanMorphModel.LAYER_LOCATION,  SnakemanMorphModel::createBodyLayer);
    }

    @OnlyIn(Dist.CLIENT)
    public static void addLayers(EntityRenderersEvent.AddLayers event, EntityRendererProvider.Context ctx) {
        for (PlayerSkin.Model skin : event.getSkins()) {
            boolean isSlim = skin.equals(PlayerSkin.Model.SLIM);
            ModelPart root = ctx.bakeLayer(isSlim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER);
            PlayerModel<LivingEntity> playerModel = new PlayerModel<>(root, isSlim);
            ModMorphs.Client.MORPH_RENDERERS.put(GIANT.get(), new PlayerMorphRenderer.Factory(GIANT.get(), playerModel, isSlim).create(ctx));
            ModMorphs.Client.MORPH_RENDERERS.put(FUSEN.get(), new GomuMorphRenderer<>(ctx, FUSEN.get(), new GomuFusenModel<>(ctx.bakeLayer(GomuFusenModel.LAYER_LOCATION))));
            ModMorphs.Client.MORPH_RENDERERS.put(GIANT_FUSEN.get(), new GomuMorphRenderer<>(ctx, GIANT_FUSEN.get(), new GomuGiantFusenModel<>(ctx.bakeLayer(GomuGiantFusenModel.LAYER_LOCATION))));
            ModMorphs.Client.MORPH_RENDERERS.put(SMALL.get(), new GomuMorphRenderer<>(ctx, SMALL.get(), new SmallMorphModel<>(root, isSlim)));

            ModMorphs.Client.MORPH_RENDERERS.put(SNAKEMAN.get(), new GomuMorphRenderer<>(ctx, SNAKEMAN.get(), new SnakemanMorphModel<>(ctx.bakeLayer(SnakemanMorphModel.LAYER_LOCATION)))
                    .addLayer(SnakemanMorphModel.OVERLAY));
            ModMorphs.Client.MORPH_RENDERERS.put(BOUNDMAN.get(), new GomuMorphRenderer<>(ctx, BOUNDMAN.get(), new GearFourthBoundmanModel<>(ctx.bakeLayer(GearFourthBoundmanModel.LAYER_LOCATION)))
                    .addLayer(GearFourthBoundmanModel.OVERLAY));
            ModMorphs.Client.MORPH_RENDERERS.put(TANKMAN.get(), new GomuMorphRenderer<>(ctx, TANKMAN.get(), new GearFourthTankmanModel<>(ctx.bakeLayer(GearFourthTankmanModel.LAYER_LOCATION)))
                    .addLayer(GearFourthTankmanModel.OVERLAY));
        }
    }
}
