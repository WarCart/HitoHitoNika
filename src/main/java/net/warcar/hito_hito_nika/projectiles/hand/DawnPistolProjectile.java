package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

public class DawnPistolProjectile extends TruePistolProjectile {

	public DawnPistolProjectile(Level world, LivingEntity player, Ability ability) {
		super(world, player, ability);
		this.setMaxLife(6);
		this.setDamage(25f);
		this.addBlockHitEvent(100, TrueGomuHelper.onBlockImpactEvent(this, 1, 5));
		this.addTickEvent(100, TrueGomuHelper.getG2Tick(this));
	}
}
