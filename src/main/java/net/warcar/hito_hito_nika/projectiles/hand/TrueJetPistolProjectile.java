package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

public class TrueJetPistolProjectile extends TruePistolProjectile {
    public TrueJetPistolProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(9);
        this.setDamage(30f);
        this.setPhysical();
        this.addBlockHitEvent(100, TrueGomuHelper.onBlockImpactEvent(this, .3f, 1));
        this.addTickEvent(100, TrueGomuHelper.getG2Tick(this, 2));
    }
}
