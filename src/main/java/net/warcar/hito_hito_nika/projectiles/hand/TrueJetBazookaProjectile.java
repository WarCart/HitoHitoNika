package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;

public class TrueJetBazookaProjectile extends TrueBazookaProjectile {

    public TrueJetBazookaProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setDamage(60f);
        this.setMaxLife(5);
        this.addTickEvent(100, this::onTickEvent);
        this.addEntityHitEvent(100, this::onEntityImpactEvent);
    }

    private void onEntityImpactEvent(EntityHitResult hitEntity) {
        Vec3 speed = this.getDeltaMovement().normalize().scale(4.5);
        AbilityHelper.setDeltaMovement(hitEntity.getEntity(), speed.x, 0.5, speed.z);
    }

    private void onTickEvent() {
        if (this.tickCount % 2 == 0)
            WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), this, this.getX(), this.getY(), this.getZ());
    }
}
