package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.particles.effects.gomu.GearSecondParticleEffect;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.Objects;

public class JetGrizzlyMagnumProjectile extends TrueGrizzlyMagnumProjectile {

    public JetGrizzlyMagnumProjectile(World world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(9);
        this.setDamage(28f);
        this.setPassThroughEntities();
        this.setDamageSource(this.getDamageSource().setPhysical());
        this.onEntityImpactEvent = this::onEntityImpactEvent;
        this.onTickEvent = this::onTickEvent;
    }

    private void onEntityImpactEvent(LivingEntity hitEntity) {
        Vector3d speed = WyHelper.propulsion(Objects.requireNonNull(this.getThrower()), 6D, 6D);
        hitEntity.setDeltaMovement(speed.x, 0.2D, speed.z);
        hitEntity.hurtMarked = true;
    }

    private void onTickEvent() {
        if (this.tickCount % 2 == 0)
            new GearSecondParticleEffect().spawn(this.level, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
    }
}
