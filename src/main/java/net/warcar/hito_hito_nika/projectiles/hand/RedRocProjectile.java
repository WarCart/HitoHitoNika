package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityExplosion;

public class RedRocProjectile extends TrueElephantGunProjectile {

    public RedRocProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(9);
        this.setDamage(90);
        this.addBlockHitEvent(200, this::onBlockImpactEvent);
        this.addTickEvent(100, TrueGomuHelper.getFlameTick(this, 10));
    }

    private void onBlockImpactEvent(BlockHitResult hit) {
        AbilityExplosion explosion = new AbilityExplosion(this.getOwner(), this.getParent().orElse(null), this.getX(), this.getY(), this.getZ(), 5F);
        explosion.setStaticDamage(10F);
        explosion.setExplosionSound(false);
        explosion.setDamageOwner(false);
        explosion.setDestroyBlocks(true);
        explosion.setFireAfterExplosion(true);
        explosion.setDamageEntities(false);
        explosion.explode();
    }
}
