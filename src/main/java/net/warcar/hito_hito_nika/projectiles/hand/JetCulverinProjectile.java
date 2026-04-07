package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import net.warcar.hito_hito_nika.projectiles.PythonProjectile;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

public class JetCulverinProjectile extends PythonProjectile {
	public JetCulverinProjectile(EntityType type, Level world) {
		super(type, world);
	}

	public JetCulverinProjectile(Level world, LivingEntity player, Ability ability, float speed, int layer) {
		super(NikaProjectiles.GOMU_GOMU_NO_JET_CULVERIN.get(), world, player, ability, speed);
		this.setMaxLifetime(3);
		this.setMaxLife(layer * 3);
		this.setDamage(20f);
		this.setEntityCollisionSize(2.5d);
		this.setPassThroughEntities();
	}
}
