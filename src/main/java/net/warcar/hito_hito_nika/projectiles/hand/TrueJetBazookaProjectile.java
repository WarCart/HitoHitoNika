package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.particles.effects.gomu.GearSecondParticleEffect;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.Objects;

public class TrueJetBazookaProjectile extends TrueBazookaProjectile {

    public TrueJetBazookaProjectile(World world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setDamage(30.0F);
        this.setMaxLife(5);
        this.setHurtTime(10);
        this.onTickEvent = this::onTickEvent;
        this.onEntityImpactEvent = this::onEntityImpactEvent;
    }

    private void onEntityImpactEvent(LivingEntity hitEntity) {
        Vector3d speed = WyHelper.propulsion(Objects.requireNonNull(this.getThrower()), 4.5D, 4.5D);
        hitEntity.setDeltaMovement(speed.x, 0.2D, speed.z);
        hitEntity.hurtMarked = true;
    }

    private void onTickEvent() {
        if (this.tickCount % 2 == 0)
            new GearSecondParticleEffect().spawn(this.level, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
    }
}
