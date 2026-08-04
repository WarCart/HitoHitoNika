package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import net.warcar.hito_hito_nika.projectiles.TrueGomuProjectile;
import net.warcar.hito_hito_nika.renderers.projectiles.GomuProjectileRenderer;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;

public class BajrangGunProjectile extends TrueGomuProjectile {
	protected final float size;

	public BajrangGunProjectile(EntityType type, Level world) {
		super(type, world);
		size = 30;
	}

	public BajrangGunProjectile(Level world, LivingEntity player, Ability ability, float size) {
		super(NikaProjectiles.GOMU_GOMU_NO_BAJRANG_GUN.get(), player, ability, SourceElement.RUBBER, SourceHakiNature.HARDENING, SourceType.FIST, SourceType.PHYSICAL);
		this.setMaxLife(250);
		this.setDamage(250F);
		this.setEntityCollisionSize(15);
		this.setSize(size * 2);
		this.addBlockHitEvent(100, TrueGomuHelper.onBlockImpactEvent(this, size, 200));
		this.size = size;
		this.setDeformation(GomuProjectileRenderer.Deformation.ELEPHANT);
	}

	public BajrangGunProjectile(Level world, LivingEntity player, Ability ability) {
		this(world, player, ability, 30);
	}
}
