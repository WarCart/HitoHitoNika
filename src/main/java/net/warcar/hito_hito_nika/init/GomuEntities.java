package net.warcar.hito_hito_nika.init;

import com.google.common.base.Joiner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.entities.LuffyBoss;
import xyz.pixelatedw.mineminenomi.models.entities.mobs.humanoids.HumanoidModel;
import xyz.pixelatedw.mineminenomi.renderers.entities.HumanoidRenderer;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class GomuEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, HitoHitoNoMiNikaMod.MOD_ID);
    public static final EntityType<LuffyBoss> LUFFY = register("Luffy", WyRegistry.createEntityType(LuffyBoss::new).build(""));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    public static <E extends Entity> EntityType<E> register(String name, EntityType<E> entityType) {
        String registryName = WyHelper.getResourceName(name);
        ENTITIES.register(registryName, () -> entityType);
        HitoHitoNoMiNikaMod.getLangMap().put(Joiner.on('.').join("entity", HitoHitoNoMiNikaMod.MOD_ID, registryName), name);
        return entityType;
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(LUFFY, LuffyBoss.createAttributes().build());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderers(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(LUFFY, new HumanoidRenderer.Factory(new HumanoidModel<>(), 1));
    }
}
