package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

import net.warcar.hito_hito_nika.abilities.TrueGomuRocket;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;

public class TrueGomuRocketProjectile extends NuProjectileEntity {
	public Ability master;

	public TrueGomuRocketProjectile(Level world, LivingEntity player, Ability ability) {
		super(null, world, player, ability);
		this.setPhysical();
		this.setDamage(0f);
		this.master = ability;
		this.addEntityHitEvent(100, this::onEntityImpact);
		this.addBlockHitEvent(100, this::onBlockHit);
	}

	private void onBlockImpact(BlockPos pos) {
		var owner = this.getOwner();
		((TrueGomuRocket) this.master).setFlying();

		BlockPos distance = pos.subtract(owner.blockPosition());

		AbilityHelper.setDeltaMovement(owner, distance.getX() * 0.35, 0.3 + distance.getY() * 0.35, distance.getZ() * 0.35);
	}

	private void onBlockHit(BlockHitResult result) {
		this.onBlockImpact(result.getBlockPos());
	}

	private void onEntityImpact(EntityHitResult result) {
		var ent = result.getEntity();
		this.onBlockImpact(new BlockPos(ent.getBlockX(), Math.round(ent.getBlockY() + ent.getEyeHeight() / 2), ent.getBlockZ()));
	}
}
