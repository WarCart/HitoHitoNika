package net.warcar.hito_hito_nika.projectiles.hand;

import xyz.pixelatedw.mineminenomi.entities.projectiles.gomu.GomuGomuNoRocketProjectile;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;
import net.warcar.hito_hito_nika.abilities.TrueGomuRocket;

import java.util.Objects;

public class TrueGomuRocketProjectile extends GomuGomuNoRocketProjectile {
	public IOnBlockImpact oldOnImpact;
	public Ability master;

	public TrueGomuRocketProjectile(World world, LivingEntity player, Ability ability) {
		super(world, player, ability);
		this.oldOnImpact = this.onBlockImpactEvent;
		this.onBlockImpactEvent = this::onBlockImpact;
		this.onEntityImpactEvent = this::onEntityImpact;
		this.setPhysical();
		this.setDamage(0f);
		this.master = ability;
	}

	private void onBlockImpact(BlockPos pos) {
		this.oldOnImpact.onImpact(pos);
		((TrueGomuRocket) this.master).setFlying();
	}

	private void onEntityImpact(Entity ent) {
		this.onBlockImpact(new BlockPos(ent.getX(), ent.getY() + (double) ent.getEyeHeight() / 2, ent.getZ()));
	}
}
