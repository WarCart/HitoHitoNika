package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityExplosion;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;


public class BajrangGunProjectile extends NuProjectileEntity {
	protected float size = 30f;

	public BajrangGunProjectile(EntityType type, Level world) {
		super(type, world);
	}

	public BajrangGunProjectile(Level world, LivingEntity player, Ability ability) {
		super(NikaProjectiles.GOMU_GOMU_NO_BAJRANG_GUN.get(), world, player, ability, SourceElement.RUBBER, SourceHakiNature.HARDENING, SourceType.FIST, SourceType.PHYSICAL);
		this.setMaxLife(250);
		this.setDamage(250F);
		this.setPhysical();
		this.setEntityCollisionSize(15);
		this.setPassThroughEntities();
		this.addBlockHitEvent(100, this::onBlockImpactEvent);
		//this.onEntityImpactEvent = this::onEntityImpactEvent;
	}

	public BajrangGunProjectile(Level world, LivingEntity player, Ability ability, float size) {
		this(world, player, ability);
		this.size = size;
	}

	private void onBlockImpactEvent(BlockHitResult hit) {
		AbilityExplosion explosion = new AbilityExplosion(this.getOwner(), this.getParent().orElse(null), this.getX(), this.getY(), this.getZ(), this.size);
		explosion.setStaticDamage(280.0F);
		explosion.setExplosionSound(false);
		explosion.setDamageOwner(false);
		explosion.setDestroyBlocks(true);
		explosion.setFireAfterExplosion(false);
		explosion.setDamageEntities(false);
		explosion.explode();
	}

	private void onEntityImpactEvent(EntityHitResult hitEnt) {
		AbilityHelper.setDeltaMovement(hitEnt.getEntity(), hitEnt.getEntity().getDeltaMovement().add(this.getDeltaMovement().normalize()));
	}

}
