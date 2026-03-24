package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;

public class JetGrizzlyMagnumProjectile extends TrueGrizzlyMagnumProjectile {

    public JetGrizzlyMagnumProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(9);
        this.setDamage(80f);
        this.setPassThroughEntities();
        this.addEntityHitEvent(100, this::onEntityImpactEvent);
        this.addTickEvent(100, this::onTickEvent);
    }

    private void onEntityImpactEvent(EntityHitResult hitEntity) {
        Vec3 speed = this.getDeltaMovement().normalize().scale(6);
        AbilityHelper.setDeltaMovement(hitEntity.getEntity(), speed.x, 0.2, speed.z);
    }

    private void onTickEvent() {
        if (this.tickCount % 2 == 0)
            WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), this, this.getX(), this.getY(), this.getZ());
    }
}
