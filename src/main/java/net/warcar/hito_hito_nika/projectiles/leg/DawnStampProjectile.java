package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.ExplosionAbility;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.particles.effects.gomu.GearSecondParticleEffect;

public class DawnStampProjectile extends StampProjectile {
    public DawnStampProjectile(World world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(6);
        this.setDamage(25f);
        this.setDamageSource(this.getDamageSource().setPhysical());
        this.onBlockImpactEvent = this::onBlockImpactEvent;
        this.onTickEvent = this::onTickEvent;
    }

    private void onBlockImpactEvent(BlockPos hit) {
        ExplosionAbility explosion = AbilityHelper.newExplosion(this.getThrower(), this.level, this.getX(), this.getY(), this.getZ(), 1F);
        explosion.setStaticDamage(5F);
        explosion.setDestroyBlocks(true);
        explosion.setExplosionSound(false);
        explosion.setDamageOwner(false);
        explosion.setFireAfterExplosion(false);
        explosion.setDamageEntities(false);
        explosion.doExplosion();
    }

    private void onTickEvent() {
        new GearSecondParticleEffect().spawn(this.level, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
    }
}
