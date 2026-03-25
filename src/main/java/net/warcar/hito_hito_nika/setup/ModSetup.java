package net.warcar.hito_hito_nika.setup;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.init.GomuAnimations;
import net.warcar.hito_hito_nika.init.TrueMorphs;
import net.warcar.hito_hito_nika.renderers.layers.TrueGomuSmokeLayer;
import xyz.pixelatedw.mineminenomi.init.ModArmors;
import xyz.pixelatedw.mineminenomi.init.ModLayers;
import xyz.pixelatedw.mineminenomi.init.ModMorphs;

import java.util.Map;

@Mod.EventBusSubscriber(modid = HitoHitoNoMiNikaMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientInit(EntityRenderersEvent.AddLayers event) {
        Minecraft mc = Minecraft.getInstance();
        EntityRendererProvider.Context ctx = event.getContext();
        for (Map.Entry<EntityType<?>, EntityRenderer<?>> entry : mc.getEntityRenderDispatcher().renderers.entrySet()) {
            EntityRenderer<?> entityRenderer = entry.getValue();
            if (entityRenderer instanceof LivingEntityRenderer renderer) {
                renderer.addLayer(new TrueGomuSmokeLayer<>(ctx, renderer));
            }
        }

        for (String skin : event.getSkins()) {
            boolean isSlim = skin.equals("slim");
            LivingEntityRenderer<Player, PlayerModel<Player>> renderer = event.getSkin(skin);
            renderer.addLayer(new TrueGomuSmokeLayer<>(ctx, renderer));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void modClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            GomuAnimations.init();
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        TrueMorphs.registerLayers(event);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        // Modify renderers with new layers
        Minecraft mc = Minecraft.getInstance();
        EntityRendererProvider.Context ctx = new EntityRendererProvider.Context(mc.getEntityRenderDispatcher(), mc.getItemRenderer(), mc.getBlockRenderer(), mc.gameRenderer.itemInHandRenderer, mc.getResourceManager(), mc.getEntityModels(), mc.font);

        TrueMorphs.addLayers(event, ctx);
    }
}
