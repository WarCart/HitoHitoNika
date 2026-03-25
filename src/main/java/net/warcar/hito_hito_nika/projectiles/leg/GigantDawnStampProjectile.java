package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

public class GigantDawnStampProjectile extends ElephantStampProjectile {
    public GigantDawnStampProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setDamage(40);
        this.setMaxLife(9);
        this.addBlockHitEvent(100, TrueGomuHelper.onBlockImpactEvent(this, 3, 13));
        this.addTickEvent(100, TrueGomuHelper.getG2Tick(this));
    }
}
