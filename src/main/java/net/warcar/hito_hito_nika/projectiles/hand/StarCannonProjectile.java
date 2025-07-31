package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.ExplosionAbility;
import xyz.pixelatedw.mineminenomi.api.effects.ModEffect;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.particles.effects.gomu.GearSecondParticleEffect;

public class StarCannonProjectile extends TrueKongGunProjectile {
    public StarCannonProjectile(World world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(300);
        this.setDamage(125f);
        this.setDamageSource(this.getDamageSource().setPhysical());
        this.setBlocksAffectedLimit(250);
        this.onBlockImpactEvent = this::onBlockImpactEvent;
        this.onEntityImpactEvent = this::onEntityImpact;
        this.onTickEvent = this::onTickEvent;
    }

    private void onEntityImpact(LivingEntity entity) {
        entity.addEffect(new EffectInstance(ModEffects.DIZZY.get(), 60, 1));
    }

    private void onBlockImpactEvent(BlockPos hit) {
        ExplosionAbility explosion = AbilityHelper.newExplosion(this.getThrower(), this.level, this.getX(), this.getY(), this.getZ(), 3F);
        explosion.setStaticDamage(15F);
        explosion.setDamageOwner(false);
        explosion.setExplosionSound(false);
        explosion.setDestroyBlocks(true);
        explosion.setFireAfterExplosion(false);
        explosion.setDamageEntities(false);
        explosion.doExplosion();
    }

    private void onTickEvent() {
        new GearSecondParticleEffect().spawn(this.level, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
    }
}
