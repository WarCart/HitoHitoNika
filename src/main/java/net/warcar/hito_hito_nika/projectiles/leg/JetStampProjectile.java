package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

public class JetStampProjectile extends StampProjectile {
    public JetStampProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(9);
        this.setDamage(30f);
        this.setPhysical();
        this.addBlockHitEvent(100, TrueGomuHelper.onBlockImpactEvent(this, 0.3f, 1));
        this.addTickEvent(100, TrueGomuHelper.getG2Tick(this, 2));
    }
}
