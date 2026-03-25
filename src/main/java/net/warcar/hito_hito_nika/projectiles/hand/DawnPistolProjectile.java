package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityExplosion;

public class DawnPistolProjectile extends TruePistolProjectile {

	public DawnPistolProjectile(Level world, LivingEntity player, Ability ability) {
		super(world, player, ability);
		this.setMaxLife(6);
		this.setDamage(25f);
		this.addBlockHitEvent(100, this::onBlockImpactEvent);
		this.addTickEvent(100, TrueGomuHelper.getG2Tick(this));
	}

	private void onBlockImpactEvent(BlockHitResult hit) {
		AbilityExplosion explosion = new AbilityExplosion(this.getOwner(), this.getParent().orElse(null), this.getX(), this.getY(), this.getZ(), 1F);
		explosion.setStaticDamage(5F);
		explosion.setExplosionSound(false);
		explosion.setDamageOwner(false);
		explosion.setDestroyBlocks(true);
		explosion.setFireAfterExplosion(false);
		explosion.setDamageEntities(false);
		explosion.explode();
	}
}
