package net.warcar.hito_hito_nika.projectiles.hand;

import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.init.ModDamageSource;
import xyz.pixelatedw.mineminenomi.entities.projectiles.brawler.BrawlerProjectiles;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.ExplosionAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.vector.Vector3d;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.Objects;

public class BajrangGunProjectile extends AbilityProjectileEntity {
	protected float size = 30f;

	public BajrangGunProjectile(EntityType type, World world) {
		super(type, world);
	}

	public BajrangGunProjectile(World world, LivingEntity player, Ability ability) {
		super(NikaProjectiles.GOMU_GOMU_NO_BAJRANG_GUN.get(), world, player, ability);
		this.setMaxLife(250);
		this.setDamage(2000F);
		this.setPhysical();
		this.setEntityCollisionSize(15);
		this.setPassThroughEntities();
		this.setCanGetStuckInGround();
		this.setDamageSource(this.getDamageSource().setPhysical());
		this.onBlockImpactEvent = this::onBlockImpactEvent;
		//this.onEntityImpactEvent = this::onEntityImpactEvent;
	}

	public BajrangGunProjectile(World world, LivingEntity player, Ability ability, float size) {
		this(world, player, ability);
		this.size = size;
	}

	private void onBlockImpactEvent(BlockPos hit) {
		ExplosionAbility explosion = AbilityHelper.newExplosion(this.getThrower(), this.level, this.getX(), this.getY(), this.getZ(), this.size);
		explosion.setStaticDamage(280.0F);
		explosion.setExplosionSound(false);
		explosion.setDamageOwner(false);
		explosion.setDestroyBlocks(true);
		explosion.setFireAfterExplosion(false);
		explosion.setDamageEntities(false);
		explosion.doExplosion();
	}

	private void onEntityImpactEvent(LivingEntity hitEnt) {
		Vector3d speed = WyHelper.propulsion(Objects.requireNonNull(this.getThrower()), 2000, 2000, 2000);
		hitEnt.push(speed.x, speed.y, speed.z);
	}

}
