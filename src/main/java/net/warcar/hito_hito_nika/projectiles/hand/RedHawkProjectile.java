package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.ExplosionAbility;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

public class RedHawkProjectile extends TruePistolProjectile {

    public RedHawkProjectile(World world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(9);
        this.setDamage(70f);
        this.setDamageSource(this.getDamageSource().setPhysical());
        this.setBlocksAffectedLimit(75);
        this.onBlockImpactEvent = this::onBlockImpactEvent;
        this.onTickEvent = this::onTickEvent;
    }
    private void onBlockImpactEvent(BlockPos pos) {
        ExplosionAbility explosion = AbilityHelper.newExplosion(this.getThrower(), this.level, this.getX(), this.getY(), this.getZ(), 1F);
        explosion.setStaticDamage(3.0F);
        explosion.setExplosionSound(false);
        explosion.setDamageOwner(false);
        explosion.setDestroyBlocks(true);
        explosion.setFireAfterExplosion(true);
        explosion.setDamageEntities(true);
        explosion.doExplosion();
    }
    private void onTickEvent() {
        for (int i = 0; i < 5; i++) {
            WyHelper.spawnParticleEffect(ModParticleEffects.DAI_ENKAI_1.get(), this, this.getX(), this.getY(), this.getZ());
        }
    }
}
