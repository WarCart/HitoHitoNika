package net.warcar.hito_hito_nika.projectiles;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.models.KingBajrangGunModel;
import net.warcar.hito_hito_nika.projectiles.hand.*;
import net.warcar.hito_hito_nika.projectiles.leg.*;
import net.warcar.hito_hito_nika.renderers.projectiles.GomuLightningProjectileRenderer;
import net.warcar.hito_hito_nika.renderers.projectiles.GomuProjectileRenderer;
import net.warcar.hito_hito_nika.renderers.projectiles.MolePistolRenderer;
import net.warcar.hito_hito_nika.renderers.projectiles.PythonProjectileRenderer;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileRenderer;
import xyz.pixelatedw.mineminenomi.init.ModRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NikaProjectiles {
	public static final RegistryObject<EntityType<TrueGomuRocketProjectile>> GOMU_GOMU_NO_ROCKET = createType("Gomu Gomu no True Rocket", TrueGomuRocketProjectile::new, .5f, .5F);

	public static final RegistryObject<EntityType<KingKongGunProjectile>> GOMU_GOMU_NO_KING_KONG_GUN = createType("Gomu Gomu no True King Kong Gun", KingKongGunProjectile::new, 8F, 8F);

	public static final RegistryObject<EntityType<King3KongGunProjectile>> GOMU_GOMU_NO_KING_3_KONG_GUN = createType("Gomu Gomu no King King King Kong Gun", King3KongGunProjectile::new, 12, 12);

	public static final RegistryObject<EntityType<BajrangGunProjectile>> GOMU_GOMU_NO_BAJRANG_GUN = createType("Gomu Gomu no True Bajrang Gun", BajrangGunProjectile::new,  20F, 20F);

	public static final RegistryObject<EntityType<TruePistolProjectile>> GOMU_GOMU_NO_PISTOL = createType("Gomu Gomu no True Pistol", TruePistolProjectile::new,  .5F, .5F);

	public static final RegistryObject<EntityType<TrueBazookaProjectile>> GOMU_GOMU_NO_BAZOOKA = createType("Gomu Gomu no True Bazooka", TrueBazookaProjectile::new,  .5F, .5F);

	public static final RegistryObject<EntityType<TrueElephantGunProjectile>> GOMU_GOMU_NO_ELEPHANT_GUN = createType("Gomu Gomu no True Elephant Gun", TrueElephantGunProjectile::new,  5F, 5F);

	public static final RegistryObject<EntityType<TrueGrizzlyMagnumProjectile>> GOMU_GOMU_NO_GRIZZLY_MAGNUM = createType("Gomu Gomu no True Grizzly Magnum", TrueGrizzlyMagnumProjectile::new,  5F, 5F);

	public static final RegistryObject<EntityType<TrueKongGunProjectile>> GOMU_GOMU_NO_KONG_GUN = createType("Gomu Gomu no Kong Gun", TrueKongGunProjectile::new,  5F, 5F);

	public static final RegistryObject<EntityType<JetCulverinProjectile>> GOMU_GOMU_NO_JET_CULVERIN = createType("Gomu Gomu no Jet Culverin", JetCulverinProjectile::new,  5F, 5F);

	public static final RegistryObject<EntityType<TrueLeoBazookaProjectile>> GOMU_GOMU_NO_LEO_BAZOOKA = createType("Gomu Gomu no True Leo Bazooka", TrueLeoBazookaProjectile::new,  5F, 5F);

	public static final RegistryObject<EntityType<LeoRexBazookaProjectile>> GOMU_GOMU_NO_LEO_REX_BAZOOKA = createType("Gomu Gomu no Leo Rex Bazooka", LeoRexBazookaProjectile::new,  5F, 5F);

	public static final RegistryObject<EntityType<BajrangGunProjectile>> GOMU_GOMU_NO_BAJRANG_STAMP_GUN = createType("Gomu Gomu no Bajrang Stamp Gun", BajrangGunProjectile::new,  20F, 20F);

	public static final RegistryObject<EntityType<KingKongStampProjectile>> GOMU_GOMU_NO_KING_KONG_STAMP = createType("Gomu Gomu no King Kong Stamp", KingKongStampProjectile::new,  8F, 8F);

	public static final RegistryObject<EntityType<King3KongStampProjectile>> GOMU_GOMU_NO_KING_3_KONG_STAMP = createType("Gomu Gomu no King King King Kong Stamp", King3KongStampProjectile::new,  12F, 12F);

	public static final RegistryObject<EntityType<StampProjectile>> GOMU_GOMU_NO_STAMP = createType("Gomu Gomu no Stamp", StampProjectile::new,  .5F, .5F);

	public static final RegistryObject<EntityType<YariProjectile>> GOMU_GOMU_NO_YARI = createType("Gomu Gomu no Yari", YariProjectile::new,  .5F, .5F);

	public static final RegistryObject<EntityType<ElephantStampProjectile>> GOMU_GOMU_NO_ELEPHANT_STAMP = createType("Gomu Gomu no Elephant Stamp", ElephantStampProjectile::new,  5F, 5F);

	public static final RegistryObject<EntityType<GigantYariProjectile>> GOMU_GOMU_NO_GIGANT_YARI = createType("Gomu Gomu no Gigant Yari", GigantYariProjectile::new,  5F, 5F);

	public static final RegistryObject<EntityType<KongStampProjectile>> GOMU_GOMU_NO_KONG_STAMP = createType("Gomu Gomu no Kong Stamp", KongStampProjectile::new,  5F, 5F);

	public static final RegistryObject<EntityType<JetRhinoSchneiderProjectile>> GOMU_GOMU_NO_JET_RHINO_SCHNEIDER = createType("Gomu Gomu no Jet Rhino Schneider", JetRhinoSchneiderProjectile::new,  5F, 5F);

	public static final RegistryObject<EntityType<RhinoSchneiderProjectile>> GOMU_GOMU_NO_RHINO_SCHNEIDER = createType("Gomu Gomu no Rhino Schneider", RhinoSchneiderProjectile::new,  5F, 5F);

	public static final RegistryObject<EntityType<RhinoRexSchneiderProjectile>> GOMU_GOMU_NO_RHINO_REX_SCHNEIDER = createType("Gomu Gomu no Rhino Rex Schneider", RhinoRexSchneiderProjectile::new,  5F, 5F);

	public static final RegistryObject<EntityType<GomuGomuNoMoguraPistolProjectile>> GOMU_GOMU_NO_MOLE_PISTOL = createType("Gomu Gomu no Mole Pistol", GomuGomuNoMoguraPistolProjectile::new,  1.25f, 2.25f);

	public static final RegistryObject<EntityType<KingBajrangGunProjectile>> GOMU_GOMU_NO_KING_BAJRANG_GUN = WyHelper.isAprilFirst() ? createType("Gomu Gomu no King Bajrang Gun", KingBajrangGunProjectile::new,  0.5F, 0.5F) : null;

	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event, EntityRendererProvider.Context ctx) {
		GomuProjectileRenderer.Model arm = new GomuProjectileRenderer.Model(false);
		GomuProjectileRenderer.Model leg = new GomuProjectileRenderer.Model(true);
		event.registerEntityRenderer(GOMU_GOMU_NO_ROCKET.get(), (new GomuProjectileRenderer.Factory<>(arm)).setStretchScale(3.1D, 3.1D));
		event.registerEntityRenderer(GOMU_GOMU_NO_BAJRANG_GUN.get(), (new GomuProjectileRenderer.Factory<>(arm, arm)).setStretchScale(4.5D, 4.5D).setScale(150D, 150D, 150D));
		event.registerEntityRenderer(GOMU_GOMU_NO_KING_KONG_STAMP.get(), (new GomuProjectileRenderer.Factory<>(leg)).setStretchScale(25D, 25D, 10D));
		event.registerEntityRenderer(GOMU_GOMU_NO_KING_3_KONG_STAMP.get(), (new GomuProjectileRenderer.Factory<>(leg)).setStretchScale(75D, 75D, 10D));
		event.registerEntityRenderer(GOMU_GOMU_NO_BAJRANG_STAMP_GUN.get(), (new GomuProjectileRenderer.Factory<>(leg, leg)).setStretchScale(4.5D, 4.5D).setScale(150D, 150D, 150D));
		event.registerEntityRenderer(GOMU_GOMU_NO_ELEPHANT_GUN.get(), (new GomuProjectileRenderer.Factory<>(arm, arm)).setStretchScale(3.1D, 3.1D).setScale(20D, 20D, 10D));
		event.registerEntityRenderer(GOMU_GOMU_NO_GRIZZLY_MAGNUM.get(), (new GomuProjectileRenderer.Factory<>(arm, arm)).setStretchScale(3.1D, 3.1D).setScale(20D, 20D, 10D));
		event.registerEntityRenderer(GOMU_GOMU_NO_ELEPHANT_STAMP.get(), (new GomuProjectileRenderer.Factory<>(leg, leg)).setStretchScale(3.1D, 3.1D).setScale(20D, 20D, 10D));
		event.registerEntityRenderer(GOMU_GOMU_NO_GIGANT_YARI.get(), (new GomuProjectileRenderer.Factory<>(leg, leg)).setStretchScale(3.1D, 3.1D).setScale(20D, 20D, 10D));
		event.registerEntityRenderer(GOMU_GOMU_NO_STAMP.get(), (new GomuProjectileRenderer.Factory<>(leg)).setStretchScale(3.1D, 3.1D));
		event.registerEntityRenderer(GOMU_GOMU_NO_YARI.get(), (new GomuProjectileRenderer.Factory<>(leg)).setStretchScale(3.1D, 3.1D));
		event.registerEntityRenderer(GOMU_GOMU_NO_KONG_STAMP.get(), (new GomuProjectileRenderer.Factory<>(leg)).setStretchScale(4.5D, 4.5D));
		event.registerEntityRenderer(GOMU_GOMU_NO_RHINO_SCHNEIDER.get(), (new GomuProjectileRenderer.Factory<>(leg)).setStretchScale(4.5D, 4.5D));
		event.registerEntityRenderer(GOMU_GOMU_NO_RHINO_REX_SCHNEIDER.get(), (new GomuProjectileRenderer.Factory<>(leg)).setStretchScale(25D, 25D, 10D));
		event.registerEntityRenderer(GOMU_GOMU_NO_MOLE_PISTOL.get(), MolePistolRenderer::new);

		event.registerEntityRenderer(GOMU_GOMU_NO_KING_KONG_GUN.get(), new GomuLightningProjectileRenderer.Factory(false));
		event.registerEntityRenderer(GOMU_GOMU_NO_KING_3_KONG_GUN.get(), new GomuLightningProjectileRenderer.Factory(false));
		//BAJRANG
		event.registerEntityRenderer(GOMU_GOMU_NO_PISTOL.get(), new GomuLightningProjectileRenderer.Factory(false));
		event.registerEntityRenderer(GOMU_GOMU_NO_BAZOOKA.get(), new GomuLightningProjectileRenderer.Factory(false));
		event.registerEntityRenderer(GOMU_GOMU_NO_KONG_GUN.get(), new GomuLightningProjectileRenderer.Factory(false));
		event.registerEntityRenderer(GOMU_GOMU_NO_LEO_BAZOOKA.get(), new GomuLightningProjectileRenderer.Factory(false));
		event.registerEntityRenderer(GOMU_GOMU_NO_LEO_REX_BAZOOKA.get(), new GomuLightningProjectileRenderer.Factory(false));
		//giant variations

		//FEET

		event.registerEntityRenderer(GOMU_GOMU_NO_JET_CULVERIN.get(), new PythonProjectileRenderer.Factory<>(false).setScale(4.5D, 4.5D, 4.5));
		event.registerEntityRenderer(GOMU_GOMU_NO_JET_RHINO_SCHNEIDER.get(), new PythonProjectileRenderer.Factory<>(true).setScale(4.5D, 4.5D, 4.5));


		if (WyHelper.isAprilFirst())
			event.registerEntityRenderer(GOMU_GOMU_NO_KING_BAJRANG_GUN.get(), new NuProjectileRenderer.Factory<>().setModel(c -> new KingBajrangGunModel<>(c.bakeLayer(KingBajrangGunModel.LAYER_LOCATION))).setTexture(ResourceLocation.parse("hito_hito_no_mi_nika:textures/entities/king_bajrang_gun.png")));
	}

	private static <E extends NuProjectileEntity, T extends EntityType<E>> RegistryObject<T> createType(String localizedName, EntityType.EntityFactory<E> factory, float sizeX, float sizeY) {
		return ModRegistry.registerEntityType(localizedName, () -> (T) ModRegistry.createEntityType(factory, MobCategory.MISC).sized(sizeX, sizeY).build("mineminenomi:" + WyHelper.getResourceName(localizedName)));
	}
}
