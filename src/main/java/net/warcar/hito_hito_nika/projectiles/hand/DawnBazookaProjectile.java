package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;
import xyz.pixelatedw.mineminenomi.particles.effects.gomu.GearSecondParticleEffect;

import java.util.Objects;

public class DawnBazookaProjectile extends TrueBazookaProjectile {
    public DawnBazookaProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setDamage(85f);
        this.setMaxLife(5);
        this.setPassThroughEntities();
        this.addEntityHitEvent(100, this::onEntityImpactEvent);
        this.addTickEvent(100, this::onTickEvent);
    }

    private void onEntityImpactEvent(EntityHitResult hit) {
        Vec3 speed = this.getDeltaMovement().normalize().scale(10);
        AbilityHelper.setDeltaMovement(hit.getEntity(), speed.x, 0.2, speed.z);
    }

    private void onTickEvent() {
        WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), this, this.getX(), this.getY(), this.getZ());
    }
}
