package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

public class RedRocProjectile extends TrueElephantGunProjectile {

    public RedRocProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(9);
        this.setDamage(90);
        this.addBlockHitEvent(100, TrueGomuHelper.onBlockImpactEvent(this, 5, 10, true));
        this.addTickEvent(100, TrueGomuHelper.getFlameTick(this, 10));
    }
}
