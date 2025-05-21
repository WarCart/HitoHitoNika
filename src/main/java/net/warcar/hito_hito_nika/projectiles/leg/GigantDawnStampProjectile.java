package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.ExplosionAbility;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.particles.effects.gomu.GearSecondParticleEffect;

public class GigantDawnStampProjectile extends ElephantStampProjectile {
    public GigantDawnStampProjectile(World world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setDamage(150);
        this.setMaxLife(9);
        this.setBlocksAffectedLimit(200);
        this.onBlockImpactEvent = this::onBlockImpactEvent;
        this.setDamageSource(this.getDamageSource().setPhysical());
        this.onTickEvent = this::onTickEvent;
    }

    private void onBlockImpactEvent(BlockPos pos) {
        ExplosionAbility explosion = AbilityHelper.newExplosion(this.getThrower(), this.level, this.getX(), this.getY(), this.getZ(), 3.0F);
        explosion.setStaticDamage(13F);
        explosion.setExplosionSound(false);
        explosion.setDamageOwner(true);
        explosion.setDestroyBlocks(true);
        explosion.setFireAfterExplosion(false);
        explosion.setDamageEntities(false);
        explosion.doExplosion();
    }

    private void onTickEvent() {
        new GearSecondParticleEffect().spawn(this.level, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
    }
}
