package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import net.warcar.hito_hito_nika.projectiles.PythonProjectile;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

public class JetCulverinProjectile extends PythonProjectile {
	public JetCulverinProjectile(EntityType type, World world) {
		super(type, world);
	}

	public JetCulverinProjectile(World world, LivingEntity player, Ability ability, float speed, int layer) {
		super(NikaProjectiles.GOMU_GOMU_NO_JET_CULVERIN.get(), world, player, ability, speed, layer);
		this.setMaxLife(3);
		this.setDamage(20f);
		this.setEntityCollisionSize(2.5d);
		this.setAffectedByHardening();
		this.setPassThroughEntities();
		this.setBlocksAffectedLimit(100000);
		this.setDamageSource(this.getDamageSource().setPhysical());
	}
	public PythonProjectile getNew() {
		return new JetCulverinProjectile(this.level, this.getThrower(), this.master, this.speed, this.getLayer() - 1);
	}
}
