package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.projectiles.hand.ThorElephantGunProjectile;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

public class ThorElephantStampProjectile extends ElephantStampProjectile {
    public ThorElephantStampProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(9);
        this.setDamage(75);
        this.addEntityHitEvent(100, ThorElephantGunProjectile.onEntityImpactEvent(this));
    }
}
