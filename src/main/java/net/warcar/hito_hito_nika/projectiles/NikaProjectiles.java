package net.warcar.hito_hito_nika.projectiles;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.models.KingBajrangGunModel;
import net.warcar.hito_hito_nika.models.TrueEntityLegModel;
import net.warcar.hito_hito_nika.projectiles.hand.*;
import net.warcar.hito_hito_nika.projectiles.leg.*;
import net.warcar.hito_hito_nika.renderers.projectiles.GomuProjectileRenderer;
import net.warcar.hito_hito_nika.renderers.projectiles.MolePistolRenderer;
import net.warcar.hito_hito_nika.renderers.projectiles.PythonProjectileRenderer;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileRenderer;
import xyz.pixelatedw.mineminenomi.init.ModRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NikaProjectiles {
	public static final RegistryObject<EntityType<KingKongGunProjectile>> GOMU_GOMU_NO_KING_KONG_GUN = ModRegistry.registerEntityType("Gomu Gomu no True King Kong Gun", () -> ModRegistry.createEntityType(KingKongGunProjectile::new).sized(8F, 8F).build("mineminenomi:gomu_gomu_no_true_king_kong_gun"));

	public static final RegistryObject<EntityType<King3KongGunProjectile>> GOMU_GOMU_NO_KING_3_KONG_GUN = ModRegistry.registerEntityType("Gomu Gomu no King King King Kong Gun", () -> ModRegistry.createEntityType(King3KongGunProjectile::new).sized(12F, 12F).build("mineminenomi:gomu_gomu_no_king_3_kong_gun"));

	public static final RegistryObject<EntityType<BajrangGunProjectile>> GOMU_GOMU_NO_BAJRANG_GUN = ModRegistry.registerEntityType("Gomu Gomu no True Bajrang Gun", () -> ModRegistry.createEntityType(BajrangGunProjectile::new).sized(20F, 20F).build("mineminenomi:gomu_gomu_no_true_bajrang_gun"));

	public static final RegistryObject<EntityType<KingKongStampProjectile>> GOMU_GOMU_NO_KING_KONG_STAMP = ModRegistry.registerEntityType("Gomu Gomu no King Kong Stamp", () -> ModRegistry.createEntityType(KingKongStampProjectile::new).sized(8F, 8F).build("mineminenomi:gomu_gomu_no_king_kong_stamp"));

	public static final RegistryObject<EntityType<King3KongGunProjectile>> GOMU_GOMU_NO_KING_3_KONG_STAMP = ModRegistry.registerEntityType("Gomu Gomu no King King King Kong Stamp", () -> ModRegistry.createEntityType(King3KongStampProjectile::new).sized(12F, 12F).build("mineminenomi:gomu_gomu_no_king_3_kong_stamp"));

	public static final RegistryObject<EntityType<BajrangGunProjectile>> GOMU_GOMU_NO_BAJRANG_STAMP_GUN = ModRegistry.registerEntityType("Gomu Gomu no Bajrang Stamp Gun", () -> ModRegistry.createEntityType(BajrangGunProjectile::new).sized(20F, 20F).build("mineminenomi:gomu_gomu_no_bajrang_stamp_gun"));

	public static final RegistryObject<EntityType<TruePistolProjectile>> GOMU_GOMU_NO_PISTOL = ModRegistry.registerEntityType("Gomu Gomu no True Pistol", () -> ModRegistry.createEntityType(TruePistolProjectile::new).sized(.5F, .5F).build("mineminenomi:gomu_gomu_no_true_pistol"));

	public static final RegistryObject<EntityType<TrueBazookaProjectile>> GOMU_GOMU_NO_BAZOOKA = ModRegistry.registerEntityType("Gomu Gomu no True Bazooka", () -> ModRegistry.createEntityType(TrueBazookaProjectile::new).sized(.5F, .5F).build("mineminenomi:gomu_gomu_no_true_bazooka"));

	public static final RegistryObject<EntityType<TrueElephantGunProjectile>> GOMU_GOMU_NO_ELEPHANT_GUN = ModRegistry.registerEntityType("Gomu Gomu no True Elephant Gun", () -> ModRegistry.createEntityType(TrueElephantGunProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_true_elephant_gun"));

	public static final RegistryObject<EntityType<TrueGrizzlyMagnumProjectile>> GOMU_GOMU_NO_GRIZZLY_MAGNUM = ModRegistry.registerEntityType("Gomu Gomu no True Grizzly Magnum", () -> ModRegistry.createEntityType(TrueGrizzlyMagnumProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_true_grizzly_magnum"));

	public static final RegistryObject<EntityType<TrueKongGunProjectile>> GOMU_GOMU_NO_KONG_GUN = ModRegistry.registerEntityType("Gomu Gomu no Kong Gun", () -> ModRegistry.createEntityType(TrueKongGunProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_kong_gun"));

	public static final RegistryObject<EntityType<JetCulverinProjectile>> GOMU_GOMU_NO_JET_CULVERIN = ModRegistry.registerEntityType("Gomu Gomu no Jet Culverin", () -> ModRegistry.createEntityType(JetCulverinProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_jet_culverin"));

	public static final RegistryObject<EntityType<TrueLeoBazookaProjectile>> GOMU_GOMU_NO_LEO_BAZOOKA = ModRegistry.registerEntityType("Gomu Gomu no True Leo Bazooka", () -> ModRegistry.createEntityType(TrueLeoBazookaProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_true_leo_bazooka"));

	public static final RegistryObject<EntityType<LeoRexBazookaProjectile>> GOMU_GOMU_NO_LEO_REX_BAZOOKA = ModRegistry.registerEntityType("Gomu Gomu no Leo Rex Bazooka", () -> ModRegistry.createEntityType(LeoRexBazookaProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_leo_rex_bazooka"));

	public static final RegistryObject<EntityType<StampProjectile>> GOMU_GOMU_NO_STAMP = ModRegistry.registerEntityType("Gomu Gomu no Stamp", () -> ModRegistry.createEntityType(StampProjectile::new).sized(.5F, .5F).build("mineminenomi:gomu_gomu_no_stamp"));

	public static final RegistryObject<EntityType<YariProjectile>> GOMU_GOMU_NO_YARI = ModRegistry.registerEntityType("Gomu Gomu no Yari", () -> ModRegistry.createEntityType(YariProjectile::new).sized(.5F, .5F).build("mineminenomi:gomu_gomu_no_yari"));

	public static final RegistryObject<EntityType<ElephantStampProjectile>> GOMU_GOMU_NO_ELEPHANT_STAMP = ModRegistry.registerEntityType("Gomu Gomu no Elephant Stamp", () -> ModRegistry.createEntityType(ElephantStampProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_elephant_stamp"));

	public static final RegistryObject<EntityType<GigantYariProjectile>> GOMU_GOMU_NO_GIGANT_YARI = ModRegistry.registerEntityType("Gomu Gomu no Gigant Yari", () -> ModRegistry.createEntityType(GigantYariProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_true_grizzly_magnum"));

	public static final RegistryObject<EntityType<KongStampProjectile>> GOMU_GOMU_NO_KONG_STAMP = ModRegistry.registerEntityType("Gomu Gomu no Kong Stamp", () -> ModRegistry.createEntityType(KongStampProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_true_kong_gun"));

	public static final RegistryObject<EntityType<JetRhinoSchneiderProjectile>> GOMU_GOMU_NO_JET_RHINO_SCHNEIDER = ModRegistry.registerEntityType("Gomu Gomu no Jet Rhino Schneider", () -> ModRegistry.createEntityType(JetRhinoSchneiderProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_jet_culverin"));

	public static final RegistryObject<EntityType<RhinoSchneiderProjectile>> GOMU_GOMU_NO_RHINO_SCHNEIDER = ModRegistry.registerEntityType("Gomu Gomu no Rhino Schneider", () -> ModRegistry.createEntityType(RhinoSchneiderProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_rhino_schneider"));

	public static final RegistryObject<EntityType<RhinoRexSchneiderProjectile>> GOMU_GOMU_NO_RHINO_REX_SCHNEIDER = ModRegistry.registerEntityType("Gomu Gomu no Rhino Rex Schneider", () -> ModRegistry.createEntityType(RhinoRexSchneiderProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_rhino_rex_schneider"));

	public static final RegistryObject<EntityType<GomuGomuNoMoguraPistolProjectile>> GOMU_GOMU_NO_MOLE_PISTOL = ModRegistry.registerEntityType("Gomu Gomu no Mole Pistol", () -> ModRegistry.createEntityType(GomuGomuNoMoguraPistolProjectile::new).sized(1.25f, 2.25f).build("meh"));

	public static final RegistryObject<EntityType<KingBajrangGunProjectile>> GOMU_GOMU_NO_KING_BAJRANG_GUN = WyHelper.isAprilFirst() ? ModRegistry.registerEntityType("Gomu Gomu no King Bajrang Gun", () -> ModRegistry.createEntityType(KingBajrangGunProjectile::new).sized(0.5F, 0.5F).build("mineminenomi:gomu_gomu_no_king_bajrang_gun")) : null;

	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event, EntityRendererProvider.Context ctx) {
		event.registerEntityRenderer(GOMU_GOMU_NO_KING_KONG_GUN.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel())).setStretchScale(25D, 25D, 10D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_BAJRANG_GUN.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel(), new EntityArmModel())).setStretchScale(4.5D, 4.5D).setScale(150D, 150D, 150D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_KING_KONG_STAMP.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel())).setStretchScale(25D, 25D, 10D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_KING_3_KONG_GUN.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel())).setStretchScale(75D, 75D, 10D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_KING_3_KONG_STAMP.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel())).setStretchScale(75D, 75D, 10D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_BAJRANG_STAMP_GUN.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel(), new TrueEntityLegModel())).setStretchScale(4.5D, 4.5D).setScale(150D, 150D, 150D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_ELEPHANT_GUN.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel(), new EntityArmModel())).setStretchScale(3.1D, 3.1D).setScale(20D, 20D, 10D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_GRIZZLY_MAGNUM.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel(), new EntityArmModel())).setStretchScale(3.1D, 3.1D).setScale(20D, 20D, 10D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_PISTOL.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel())).setStretchScale(3.1D, 3.1D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_BAZOOKA.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel())).setStretchScale(3.1D, 3.1D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_KONG_GUN.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel())).setStretchScale(4.5D, 4.5D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_JET_CULVERIN.get(), (new PythonProjectileRenderer.Factory(new EntityArmModel())).setScale(4.5D, 4.5D, 4.5));
		event.registerEntityRenderer(GOMU_GOMU_NO_LEO_BAZOOKA.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel())).setStretchScale(4.5D, 4.5D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_LEO_REX_BAZOOKA.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel())).setStretchScale(25D, 25D, 10D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_ELEPHANT_STAMP.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel(), new TrueEntityLegModel())).setStretchScale(3.1D, 3.1D).setScale(20D, 20D, 10D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_GIGANT_YARI.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel(), new TrueEntityLegModel())).setStretchScale(3.1D, 3.1D).setScale(20D, 20D, 10D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_STAMP.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel())).setStretchScale(3.1D, 3.1D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_YARI.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel())).setStretchScale(3.1D, 3.1D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_KONG_STAMP.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel())).setStretchScale(4.5D, 4.5D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_JET_RHINO_SCHNEIDER.get(), (new PythonProjectileRenderer.Factory(new TrueEntityLegModel())).setScale(4.5D, 4.5D, 4.5));
		event.registerEntityRenderer(GOMU_GOMU_NO_RHINO_SCHNEIDER.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel())).setStretchScale(4.5D, 4.5D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_RHINO_REX_SCHNEIDER.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel())).setStretchScale(25D, 25D, 10D).setPlayerTexture());
		event.registerEntityRenderer(GOMU_GOMU_NO_MOLE_PISTOL.get(), MolePistolRenderer::new);
		if (WyHelper.isAprilFirst())
			event.registerEntityRenderer(GOMU_GOMU_NO_KING_BAJRANG_GUN.get(), new NuProjectileRenderer.Factory<>().setModel(KingBajrangGunModel::new).setTexture(new ResourceLocation("hito_hito_no_mi_nika:textures/entities/king_bajrang_gun.png")));
	}
}
