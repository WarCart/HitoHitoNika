package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.particles.effects.gomu.GearSecondParticleEffect;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.Objects;

public class GigantDawnBazookaProjectile extends TrueGrizzlyMagnumProjectile {

    public GigantDawnBazookaProjectile(World world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(9);
        this.setDamage(180f);
        this.setPassThroughEntities();
        this.setDamageSource(this.getDamageSource().setPhysical());
        this.onEntityImpactEvent = this::onEntityImpactEvent;
        this.onTickEvent = this::onTickEvent;
    }

    private void onEntityImpactEvent(LivingEntity hitEntity) {
        Vector3d speed = WyHelper.propulsion(Objects.requireNonNull(this.getThrower()), 15D, 15D);
        hitEntity.setDeltaMovement(speed.x, 0.2D, speed.z);
        hitEntity.hurtMarked = true;
    }

    private void onTickEvent() {
        new GearSecondParticleEffect().spawn(this.level, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
    }
}
