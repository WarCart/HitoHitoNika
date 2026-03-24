package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityExplosion;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;
import xyz.pixelatedw.mineminenomi.particles.effects.gomu.GearSecondParticleEffect;

public class TrueJetPistolProjectile extends TruePistolProjectile {
    public TrueJetPistolProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(9);
        this.setDamage(30f);
        this.setPhysical();
        this.addBlockHitEvent(100, this::onBlockImpactEvent);
        this.addTickEvent(100, this::onTickEvent);
    }

    private void onBlockImpactEvent(BlockHitResult hit) {
        AbilityExplosion explosion = new AbilityExplosion(this.getOwner(), this.getParent().orElse(null), this.getX(), this.getY(), this.getZ(), .3F);
        explosion.setStaticDamage(1F);
        explosion.setExplosionSound(false);
        explosion.setDamageOwner(false);
        explosion.setDestroyBlocks(true);
        explosion.setFireAfterExplosion(false);
        explosion.setDamageEntities(false);
        explosion.explode();
    }

    private void onTickEvent() {
        if (this.tickCount % 2 == 0)
            WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), this, this.getX(), this.getY(), this.getZ());
    }
}
