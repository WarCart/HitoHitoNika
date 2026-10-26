package net.warcar.hito_hito_nika.setup;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.init.GomuAnimations;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import net.warcar.hito_hito_nika.init.TrueMorphs;
import net.warcar.hito_hito_nika.renderers.layers.TrueGomuSmokeLayer;
import xyz.pixelatedw.mineminenomi.MineMineNoMi;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;

import java.lang.reflect.Field;
import java.util.Map;

@EventBusSubscriber(modid = HitoHitoNoMiNikaMod.MOD_ID)
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

        for (PlayerSkin.Model skin : event.getSkins()) {
            boolean isSlim = skin.equals(PlayerSkin.Model.SLIM);
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

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            if (BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(MineMineNoMi.PROJECT_ID, "gomu_gomu_no_mi")) instanceof AkumaNoMiItem gomu) {
                AkumaNoMiItem nika = TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA.get();
                if (gomu != nika) {
                    Class<AkumaNoMiItem> fruits = AkumaNoMiItem.class;
                    try {
                        Field abilities = fruits.getField("abilities");
                        abilities.setAccessible(true);
                        abilities.set(gomu, abilities.get(nika));
                        abilities.setAccessible(false);
                    } catch (NoSuchFieldException e) {
                        HitoHitoNoMiNikaMod.LOGGER.info("Well i'm stupid apparently, and devil fruits have no abilities");
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        HitoHitoNoMiNikaMod.LOGGER.info("Well i'm stupid apparently, and even tho i MADE it accessible it's not accessible anyway");
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
