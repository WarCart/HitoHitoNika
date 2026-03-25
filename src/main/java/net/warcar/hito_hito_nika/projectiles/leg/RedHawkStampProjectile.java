package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

public class RedHawkStampProjectile extends StampProjectile {
    public RedHawkStampProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(9);
        this.setDamage(50f);
        this.addBlockHitEvent(100, TrueGomuHelper.onFlamingBlockImpactEvent(this, 1, 3));
        this.addTickEvent(100, TrueGomuHelper.getFlameTick(this, 5));
    }
}
