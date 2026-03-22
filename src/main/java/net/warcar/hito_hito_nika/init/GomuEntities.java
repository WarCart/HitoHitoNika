package net.warcar.hito_hito_nika.init;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.entities.LuffyBoss;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.init.ModMobs;
import xyz.pixelatedw.mineminenomi.init.ModRegistry;
import xyz.pixelatedw.mineminenomi.renderers.entities.mobs.OPHumanoidRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class GomuEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HitoHitoNoMiNikaMod.MOD_ID);
    public static final EntityType<LuffyBoss> LUFFY = register("Luffy", ModRegistry.<LuffyBoss>createEntityType(LuffyBoss::new, ModMobs.PIRATES).build(HitoHitoNoMiNikaMod.MOD_ID + ":luffy"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    public static <E extends Entity> EntityType<E> register(String name, EntityType<E> entityType) {
        String registryName = WyHelper.getResourceName(name);
        ENTITIES.register(registryName, () -> entityType);
        HitoHitoNoMiNikaMod.getLangMap().put(String.format("entity.%s.%s", HitoHitoNoMiNikaMod.MOD_ID, registryName), name);
        return entityType;
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(LUFFY, LuffyBoss.createAttributes().build());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        Minecraft mc = Minecraft.getInstance();
        EntityRendererProvider.Context ctx = new EntityRendererProvider.Context(mc.getEntityRenderDispatcher(), mc.getItemRenderer(), mc.getBlockRenderer(), mc.gameRenderer.itemInHandRenderer, mc.getResourceManager(), mc.getEntityModels(), mc.font);
        event.registerEntityRenderer(LUFFY, new OPHumanoidRenderer.Factory<>(ctx));
        NikaProjectiles.registerEntityRenderers(event, ctx);
    }
}
