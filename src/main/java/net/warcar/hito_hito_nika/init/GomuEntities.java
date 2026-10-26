package net.warcar.hito_hito_nika.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.entities.LuffyBoss;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.init.ModMobs;
import xyz.pixelatedw.mineminenomi.init.ModRegistry;
import xyz.pixelatedw.mineminenomi.renderers.entities.mobs.OPHumanoidRenderer;

@EventBusSubscriber(modid = HitoHitoNoMiNikaMod.MOD_ID)
public class GomuEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, HitoHitoNoMiNikaMod.MOD_ID);
    public static final DeferredHolder<EntityType<?>, EntityType<LuffyBoss>> LUFFY = register("Luffy", ModRegistry.createEntityType(LuffyBoss::new, ModMobs.PIRATES.getValue()));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    public static <E extends Entity> DeferredHolder<EntityType<?>, EntityType<E>> register(String name, EntityType.Builder<E> entityType) {
        String registryName = WyHelper.getResourceName(name);
        HitoHitoNoMiNikaMod.getLangMap().put(String.format("entity.%s.%s", HitoHitoNoMiNikaMod.MOD_ID, registryName), name);
        return ENTITIES.register(registryName, () -> entityType.build(HitoHitoNoMiNikaMod.MOD_ID + ":" + registryName));
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(LUFFY.get(), LuffyBoss.createAttributes().build());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        Minecraft mc = Minecraft.getInstance();
        EntityRendererProvider.Context ctx = new EntityRendererProvider.Context(mc.getEntityRenderDispatcher(), mc.getItemRenderer(), mc.getBlockRenderer(), mc.gameRenderer.itemInHandRenderer, mc.getResourceManager(), mc.getEntityModels(), mc.font);
        event.registerEntityRenderer(LUFFY.get(), new OPHumanoidRenderer.Factory<>(ctx));
        NikaProjectiles.registerEntityRenderers(event, ctx);
    }
}
