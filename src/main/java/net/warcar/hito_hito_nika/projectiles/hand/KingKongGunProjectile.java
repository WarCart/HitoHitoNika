package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.ExplosionAbility;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;

public class KingKongGunProjectile extends AbilityProjectileEntity {
	public KingKongGunProjectile(EntityType type, World world) {
		super(type, world);
	}

	public KingKongGunProjectile(World world, LivingEntity player, Ability ability) {
		super(NikaProjectiles.GOMU_GOMU_NO_KING_KONG_GUN.get(), world, player, ability);
		this.setMaxLife(25);
		this.setPhysical();
		this.setDamage(100F);
		this.setEntityCollisionSize(4d);
		this.setPassThroughEntities();
		this.setBlocksAffectedLimit(100000);
		this.setDamageSource(this.getDamageSource().setPhysical());
		this.onBlockImpactEvent = this::onBlockImpactEvent;
	}

	private void onBlockImpactEvent(BlockPos hit) {
		ExplosionAbility explosion = AbilityHelper.newExplosion(this.getThrower(), this.level, this.getX(), this.getY(), this.getZ(), 13F);
		explosion.setStaticDamage(80.0F);
		explosion.setExplosionSound(false);
		explosion.setDamageOwner(false);
		explosion.setDestroyBlocks(true);
		explosion.setFireAfterExplosion(false);
		explosion.setDamageEntities(false);
		explosion.doExplosion();
	}
}
