package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

public class GigantDawnPistolProjectile extends TrueElephantGunProjectile {

    public GigantDawnPistolProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setDamage(40f);
        this.setMaxLife(9);
        this.addBlockHitEvent(100, TrueGomuHelper.onBlockImpactEvent(this, 3, 13));
        this.addTickEvent(100, TrueGomuHelper.getG2Tick(this));
    }
}
