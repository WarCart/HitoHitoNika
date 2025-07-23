package net.warcar.hito_hito_nika.projectiles;

import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.warcar.hito_hito_nika.models.KingBajrangGunModel;
import net.warcar.hito_hito_nika.models.TrueEntityLegModel;
import net.warcar.hito_hito_nika.projectiles.hand.*;
import net.warcar.hito_hito_nika.projectiles.leg.*;
import net.warcar.hito_hito_nika.renderers.projectiles.GomuProjectileRenderer;
import net.warcar.hito_hito_nika.renderers.projectiles.MolePistolRenderer;
import net.warcar.hito_hito_nika.renderers.projectiles.PythonProjectileRenderer;
import xyz.pixelatedw.mineminenomi.models.abilities.EntityArmModel;
import xyz.pixelatedw.mineminenomi.renderers.abilities.AbilityProjectileRenderer;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NikaProjectiles {
	public static final RegistryObject<EntityType<KingKongGunProjectile>> GOMU_GOMU_NO_KING_KONG_GUN = WyRegistry.registerEntityType("Gomu Gomu no True King Kong Gun", () -> WyRegistry.createEntityType(KingKongGunProjectile::new).sized(8F, 8F).build("mineminenomi:gomu_gomu_no_true_king_kong_gun"));

	public static final RegistryObject<EntityType<King3KongGunProjectile>> GOMU_GOMU_NO_KING_3_KONG_GUN = WyRegistry.registerEntityType("Gomu Gomu no King King King Kong Gun", () -> WyRegistry.createEntityType(King3KongGunProjectile::new).sized(12F, 12F).build("mineminenomi:gomu_gomu_no_king_3_kong_gun"));

	public static final RegistryObject<EntityType<BajrangGunProjectile>> GOMU_GOMU_NO_BAJRANG_GUN = WyRegistry.registerEntityType("Gomu Gomu no True Bajrang Gun", () -> WyRegistry.createEntityType(BajrangGunProjectile::new).sized(20F, 20F).build("mineminenomi:gomu_gomu_no_true_bajrang_gun"));

	public static final RegistryObject<EntityType<KingKongStampProjectile>> GOMU_GOMU_NO_KING_KONG_STAMP = WyRegistry.registerEntityType("Gomu Gomu no King Kong Stamp", () -> WyRegistry.createEntityType(KingKongStampProjectile::new).sized(8F, 8F).build("mineminenomi:gomu_gomu_no_king_kong_stamp"));

	public static final RegistryObject<EntityType<King3KongGunProjectile>> GOMU_GOMU_NO_KING_3_KONG_STAMP = WyRegistry.registerEntityType("Gomu Gomu no King King King Kong Stamp", () -> WyRegistry.createEntityType(King3KongStampProjectile::new).sized(12F, 12F).build("mineminenomi:gomu_gomu_no_king_3_kong_stamp"));

	public static final RegistryObject<EntityType<BajrangGunProjectile>> GOMU_GOMU_NO_BAJRANG_STAMP_GUN = WyRegistry.registerEntityType("Gomu Gomu no Bajrang Stamp Gun", () -> WyRegistry.createEntityType(BajrangGunProjectile::new).sized(20F, 20F).build("mineminenomi:gomu_gomu_no_bajrang_stamp_gun"));

	public static final RegistryObject<EntityType<TruePistolProjectile>> GOMU_GOMU_NO_PISTOL = WyRegistry.registerEntityType("Gomu Gomu no True Pistol", () -> WyRegistry.createEntityType(TruePistolProjectile::new).sized(.5F, .5F).build("mineminenomi:gomu_gomu_no_true_pistol"));

	public static final RegistryObject<EntityType<TrueBazookaProjectile>> GOMU_GOMU_NO_BAZOOKA = WyRegistry.registerEntityType("Gomu Gomu no True Bazooka", () -> WyRegistry.createEntityType(TrueBazookaProjectile::new).sized(.5F, .5F).build("mineminenomi:gomu_gomu_no_true_bazooka"));

	public static final RegistryObject<EntityType<TrueElephantGunProjectile>> GOMU_GOMU_NO_ELEPHANT_GUN = WyRegistry.registerEntityType("Gomu Gomu no True Elephant Gun", () -> WyRegistry.createEntityType(TrueElephantGunProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_true_elephant_gun"));

	public static final RegistryObject<EntityType<TrueGrizzlyMagnumProjectile>> GOMU_GOMU_NO_GRIZZLY_MAGNUM = WyRegistry.registerEntityType("Gomu Gomu no True Grizzly Magnum", () -> WyRegistry.createEntityType(TrueGrizzlyMagnumProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_true_grizzly_magnum"));

	public static final RegistryObject<EntityType<TrueKongGunProjectile>> GOMU_GOMU_NO_KONG_GUN = WyRegistry.registerEntityType("Gomu Gomu no Kong Gun", () -> WyRegistry.createEntityType(TrueKongGunProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_kong_gun"));

	public static final RegistryObject<EntityType<JetCulverinProjectile>> GOMU_GOMU_NO_JET_CULVERIN = WyRegistry.registerEntityType("Gomu Gomu no Jet Culverin", () -> WyRegistry.createEntityType(JetCulverinProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_jet_culverin"));

	public static final RegistryObject<EntityType<TrueLeoBazookaProjectile>> GOMU_GOMU_NO_LEO_BAZOOKA = WyRegistry.registerEntityType("Gomu Gomu no True Leo Bazooka", () -> WyRegistry.createEntityType(TrueLeoBazookaProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_true_leo_bazooka"));

	public static final RegistryObject<EntityType<LeoRexBazookaProjectile>> GOMU_GOMU_NO_LEO_REX_BAZOOKA = WyRegistry.registerEntityType("Gomu Gomu no Leo Rex Bazooka", () -> WyRegistry.createEntityType(LeoRexBazookaProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_leo_rex_bazooka"));

	public static final RegistryObject<EntityType<StampProjectile>> GOMU_GOMU_NO_STAMP = WyRegistry.registerEntityType("Gomu Gomu no Stamp", () -> WyRegistry.createEntityType(StampProjectile::new).sized(.5F, .5F).build("mineminenomi:gomu_gomu_no_stamp"));

	public static final RegistryObject<EntityType<YariProjectile>> GOMU_GOMU_NO_YARI = WyRegistry.registerEntityType("Gomu Gomu no Yari", () -> WyRegistry.createEntityType(YariProjectile::new).sized(.5F, .5F).build("mineminenomi:gomu_gomu_no_yari"));

	public static final RegistryObject<EntityType<ElephantStampProjectile>> GOMU_GOMU_NO_ELEPHANT_STAMP = WyRegistry.registerEntityType("Gomu Gomu no Elephant Stamp", () -> WyRegistry.createEntityType(ElephantStampProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_elephant_stamp"));

	public static final RegistryObject<EntityType<GigantYariProjectile>> GOMU_GOMU_NO_GIGANT_YARI = WyRegistry.registerEntityType("Gomu Gomu no Gigant Yari", () -> WyRegistry.createEntityType(GigantYariProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_true_grizzly_magnum"));

	public static final RegistryObject<EntityType<KongStampProjectile>> GOMU_GOMU_NO_KONG_STAMP = WyRegistry.registerEntityType("Gomu Gomu no Kong Stamp", () -> WyRegistry.createEntityType(KongStampProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_true_kong_gun"));

	public static final RegistryObject<EntityType<JetRhinoSchneiderProjectile>> GOMU_GOMU_NO_JET_RHINO_SCHNEIDER = WyRegistry.registerEntityType("Gomu Gomu no Jet Rhino Schneider", () -> WyRegistry.createEntityType(JetRhinoSchneiderProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_jet_culverin"));

	public static final RegistryObject<EntityType<RhinoSchneiderProjectile>> GOMU_GOMU_NO_RHINO_SCHNEIDER = WyRegistry.registerEntityType("Gomu Gomu no Rhino Schneider", () -> WyRegistry.createEntityType(RhinoSchneiderProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_rhino_schneider"));

	public static final RegistryObject<EntityType<RhinoRexSchneiderProjectile>> GOMU_GOMU_NO_RHINO_REX_SCHNEIDER = WyRegistry.registerEntityType("Gomu Gomu no Rhino Rex Schneider", () -> WyRegistry.createEntityType(RhinoRexSchneiderProjectile::new).sized(5F, 5F).build("mineminenomi:gomu_gomu_no_rhino_rex_schneider"));

	public static final RegistryObject<EntityType<MolePistolProjectile>> GOMU_GOMU_NO_MOLE_PISTOL = WyRegistry.registerEntityType("Gomu Gomu no Mole Pistol", () -> WyRegistry.createEntityType(MolePistolProjectile::new).sized(1.25f, 2.25f).build("meh"));

	public static final RegistryObject<EntityType<KingBajrangGunProjectile>> GOMU_GOMU_NO_KING_BAJRANG_GUN = WyHelper.isAprilFirst() ? WyRegistry.registerEntityType("Gomu Gomu no King Bajrang Gun", () -> WyRegistry.createEntityType(KingBajrangGunProjectile::new).sized(0.5F, 0.5F).build("mineminenomi:gomu_gomu_no_king_bajrang_gun")) : null;

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerEntityRenderers(FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_KING_KONG_GUN.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel())).setStretchScale(25D, 25D, 10D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_BAJRANG_GUN.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel(), new EntityArmModel())).setStretchScale(4.5D, 4.5D).setScale(150D, 150D, 150D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_KING_KONG_STAMP.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel())).setStretchScale(25D, 25D, 10D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_KING_3_KONG_GUN.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel())).setStretchScale(75D, 75D, 10D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_KING_3_KONG_STAMP.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel())).setStretchScale(75D, 75D, 10D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_BAJRANG_STAMP_GUN.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel(), new TrueEntityLegModel())).setStretchScale(4.5D, 4.5D).setScale(150D, 150D, 150D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_ELEPHANT_GUN.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel(), new EntityArmModel())).setStretchScale(3.1D, 3.1D).setScale(20D, 20D, 10D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_GRIZZLY_MAGNUM.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel(), new EntityArmModel())).setStretchScale(3.1D, 3.1D).setScale(20D, 20D, 10D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_PISTOL.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel())).setStretchScale(3.1D, 3.1D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_BAZOOKA.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel())).setStretchScale(3.1D, 3.1D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_KONG_GUN.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel())).setStretchScale(4.5D, 4.5D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_JET_CULVERIN.get(), (new PythonProjectileRenderer.Factory(new EntityArmModel())).setScale(4.5D, 4.5D, 4.5));
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_LEO_BAZOOKA.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel())).setStretchScale(4.5D, 4.5D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_LEO_REX_BAZOOKA.get(), (new GomuProjectileRenderer.Factory(new EntityArmModel())).setStretchScale(25D, 25D, 10D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_ELEPHANT_STAMP.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel(), new TrueEntityLegModel())).setStretchScale(3.1D, 3.1D).setScale(20D, 20D, 10D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_GIGANT_YARI.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel(), new TrueEntityLegModel())).setStretchScale(3.1D, 3.1D).setScale(20D, 20D, 10D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_STAMP.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel())).setStretchScale(3.1D, 3.1D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_YARI.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel())).setStretchScale(3.1D, 3.1D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_KONG_STAMP.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel())).setStretchScale(4.5D, 4.5D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_JET_RHINO_SCHNEIDER.get(), (new PythonProjectileRenderer.Factory(new TrueEntityLegModel())).setScale(4.5D, 4.5D, 4.5));
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_RHINO_SCHNEIDER.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel())).setStretchScale(4.5D, 4.5D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_RHINO_REX_SCHNEIDER.get(), (new GomuProjectileRenderer.Factory(new TrueEntityLegModel())).setStretchScale(25D, 25D, 10D).setPlayerTexture());
		RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_MOLE_PISTOL.get(), MolePistolRenderer::new);
		if (WyHelper.isAprilFirst())
			RenderingRegistry.registerEntityRenderingHandler(GOMU_GOMU_NO_KING_BAJRANG_GUN.get(), (new AbilityProjectileRenderer.Factory(new KingBajrangGunModel())).setTexture(new ResourceLocation("hito_hito_no_mi_nika:textures/entities/king_bajrang_gun.png")));
	}
}
