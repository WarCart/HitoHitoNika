package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityExplosion;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;

public class GigantDawnPistolProjectile extends TrueElephantGunProjectile {

    public GigantDawnPistolProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setDamage(40f);
        this.setMaxLife(9);
        this.addBlockHitEvent(100, this::onBlockImpactEvent);
        this.addTickEvent(100, this::onTickEvent);
    }

    private void onBlockImpactEvent(BlockHitResult pos) {
        AbilityExplosion explosion = new AbilityExplosion(this.getOwner(), this.getParent().orElse(null), this.getX(), this.getY(), this.getZ(), 3.0F);
        explosion.setStaticDamage(13F);
        explosion.setExplosionSound(false);
        explosion.setDamageOwner(true);
        explosion.setDestroyBlocks(true);
        explosion.setFireAfterExplosion(false);
        explosion.setDamageEntities(false);
        explosion.explode();
    }

    private void onTickEvent() {
        WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), this, this.getX(), this.getY(), this.getZ());
    }
}
