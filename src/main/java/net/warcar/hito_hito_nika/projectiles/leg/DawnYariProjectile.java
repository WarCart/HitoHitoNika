package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

public class DawnYariProjectile extends YariProjectile {
    public DawnYariProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setDamage(85f);
        this.setMaxLife(5);
        this.setPassThroughEntities();
        this.addTickEvent(100, TrueGomuHelper.getG2Tick(this));
        this.addEntityHitEvent(100, TrueGomuHelper.getBazookaOnEntityImpactEvent(this, 10));
    }
}
