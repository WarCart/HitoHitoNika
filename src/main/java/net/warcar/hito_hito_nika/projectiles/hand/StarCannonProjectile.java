package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityExplosion;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;

public class StarCannonProjectile extends TrueKongGunProjectile {
    public StarCannonProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(300);
        this.setDamage(125f);
        this.addBlockHitEvent(100, this::onBlockImpactEvent);
        this.addTickEvent(100, this::onTickEvent);
        this.addEntityHitEvent(100, this::onEntityImpact);
    }

    private void onEntityImpact(EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity entity) {
            entity.addEffect(new MobEffectInstance(ModEffects.DIZZY.get(), 60, 1));
        }
    }

    private void onBlockImpactEvent(BlockHitResult hit) {
        AbilityExplosion explosion = new AbilityExplosion(this.getOwner(), this.getParent().orElse(null), this.getX(), this.getY(), this.getZ(), 3F);
        explosion.setStaticDamage(15F);
        explosion.setDamageOwner(false);
        explosion.setExplosionSound(false);
        explosion.setDestroyBlocks(true);
        explosion.setFireAfterExplosion(false);
        explosion.setDamageEntities(false);
        explosion.explode();
    }

    private void onTickEvent() {
        WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), this, this.getX(), this.getY(), this.getZ());
    }
}
