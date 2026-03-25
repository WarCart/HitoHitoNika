package net.warcar.hito_hito_nika.init;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.models.*;
import net.warcar.hito_hito_nika.morphs.*;
import net.warcar.hito_hito_nika.renderers.morphs.GomuMorphRenderer;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.init.ModMorphs;
import xyz.pixelatedw.mineminenomi.init.ModRegistry;
import xyz.pixelatedw.mineminenomi.models.morphs.DaibutsuModel;
import xyz.pixelatedw.mineminenomi.models.morphs.partials.HanaCalendulaModel;
import xyz.pixelatedw.mineminenomi.models.morphs.partials.HanaWingsModel;
import xyz.pixelatedw.mineminenomi.models.morphs.partials.SparClawModel;
import xyz.pixelatedw.mineminenomi.renderers.morphs.PlayerMorphRenderer;

public class TrueMorphs {

    public static final RegistryObject<MorphInfo> BOUNDMAN = ModRegistry.registerMorph("gear_4_boundman", GearFourthBoundmanMorph::new);

    public static final RegistryObject<MorphInfo> TANKMAN = ModRegistry.registerMorph("gear_4_tankman", GearFourthTankmanMorph::new);

    public static final RegistryObject<MorphInfo> SNAKEMAN = ModRegistry.registerMorph("gear_4_snakeman", GearFourthSnakemanMorph::new);

    public static final RegistryObject<MorphInfo> FUSEN = ModRegistry.registerMorph("fusen", FusenMorph::new);

    public static final RegistryObject<MorphInfo> SMALL = ModRegistry.registerMorph("small", SmallMorph::new);

    public static final RegistryObject<MorphInfo> GIANT_FUSEN = ModRegistry.registerMorph("giant_fusen", GiantFusenMorph::new);

    public static final RegistryObject<MorphInfo> GIANT = ModRegistry.registerMorph("giant", GomuGigantMorph::new);

    public static void init() {
    }

    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GomuFusenModel.LAYER_LOCATION, GomuFusenModel::createBodyLayer);
        event.registerLayerDefinition(GomuGiantFusenModel.LAYER_LOCATION, GomuGiantFusenModel::createBodyLayer);
        event.registerLayerDefinition(KingBajrangGunModel.LAYER_LOCATION, KingBajrangGunModel::createBodyLayer);
        event.registerLayerDefinition(SnakemanMorphModel.LAYER_LOCATION,  SnakemanMorphModel::createBodyLayer);
        event.registerLayerDefinition(TrueEntityLegModel.LAYER_LOCATION, TrueEntityLegModel::createBodyLayer);
    }

    public static void addLayers(EntityRenderersEvent.AddLayers event, EntityRendererProvider.Context ctx) {
        for (String skin : event.getSkins()) {
            boolean isSlim = skin.equals("slim");
            ModelPart root = ctx.bakeLayer(isSlim ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER);
            PlayerModel<LivingEntity> playerModel = new PlayerModel<>(root, isSlim);
            ModMorphs.Client.MORPH_RENDERERS.put(GIANT.get(), new PlayerMorphRenderer.Factory(GIANT.get(), playerModel, isSlim).create(ctx));
            ModMorphs.Client.MORPH_RENDERERS.put(FUSEN.get(), new GomuMorphRenderer<>(ctx, FUSEN.get(), new GomuFusenModel<>(ctx.bakeLayer(GomuFusenModel.LAYER_LOCATION))));
            ModMorphs.Client.MORPH_RENDERERS.put(GIANT_FUSEN.get(), new GomuMorphRenderer<>(ctx, GIANT_FUSEN.get(), new GomuGiantFusenModel<>(ctx.bakeLayer(GomuGiantFusenModel.LAYER_LOCATION))));
            ModMorphs.Client.MORPH_RENDERERS.put(SMALL.get(), new GomuMorphRenderer<>(ctx, SMALL.get(), new SmallMorphModel<>(root, isSlim)));

            ModMorphs.Client.MORPH_RENDERERS.put(SNAKEMAN.get(), new GomuMorphRenderer<>(ctx, SNAKEMAN.get(), new SnakemanMorphModel<>(ctx.bakeLayer(SnakemanMorphModel.LAYER_LOCATION))));
            ModMorphs.Client.MORPH_RENDERERS.put(BOUNDMAN.get(), new GomuMorphRenderer<>(ctx, BOUNDMAN.get(), new SnakemanMorphModel<>(ctx.bakeLayer(SnakemanMorphModel.LAYER_LOCATION))));
            ModMorphs.Client.MORPH_RENDERERS.put(TANKMAN.get(), new GomuMorphRenderer<>(ctx, TANKMAN.get(), new SnakemanMorphModel<>(ctx.bakeLayer(SnakemanMorphModel.LAYER_LOCATION))));
        }
    }
}
