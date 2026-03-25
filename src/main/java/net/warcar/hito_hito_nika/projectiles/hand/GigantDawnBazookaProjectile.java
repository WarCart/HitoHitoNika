package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

public class GigantDawnBazookaProjectile extends TrueGrizzlyMagnumProjectile {

    public GigantDawnBazookaProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(9);
        this.setDamage(120f);
        this.setPassThroughEntities();
        this.setPassThroughBlocks();
        this.addEntityHitEvent(100, TrueGomuHelper.getBazookaOnEntityImpactEvent(this, 15));
        this.addTickEvent(100, TrueGomuHelper.getG2Tick(this));
    }
}
