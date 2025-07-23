package net.warcar.hito_hito_nika.setup;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.init.GomuAnimations;
import net.warcar.hito_hito_nika.renderers.layers.TrueGomuSmokeLayer;

import java.util.Map;

@Mod.EventBusSubscriber(modid = HitoHitoNoMiNikaMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            for (Map.Entry<EntityType<?>, EntityRenderer<?>> entry : Minecraft.getInstance().getEntityRenderDispatcher().renderers.entrySet()) {
                EntityRenderer<?> entityRenderer = entry.getValue();
                if (entityRenderer instanceof BipedRenderer) {
                    ((BipedRenderer<?, ?>) entityRenderer).addLayer(new TrueGomuSmokeLayer((IEntityRenderer<?, ?>) entityRenderer));
                }
            }

            for (Map.Entry<String, PlayerRenderer> entry : Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().entrySet()) {
                PlayerRenderer playerRenderer = entry.getValue();
                playerRenderer.addLayer(new TrueGomuSmokeLayer<>(playerRenderer));
            }
            GomuAnimations.clientInit();
        });
    }
}
