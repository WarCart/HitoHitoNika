package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;

public class GigantDawnBazookaProjectile extends TrueGrizzlyMagnumProjectile {

    public GigantDawnBazookaProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(9);
        this.setDamage(120f);
        this.setPassThroughEntities();
        this.setPassThroughBlocks();
        this.addEntityHitEvent(100, this::onEntityImpactEvent);
        this.addTickEvent(100, this::onTickEvent);
    }

    private void onEntityImpactEvent(EntityHitResult hitEntity) {
        Vec3 speed = this.getDeltaMovement().normalize().scale(15);
        AbilityHelper.setDeltaMovement(hitEntity.getEntity(), speed.x, 0.2, speed.z);
    }

    private void onTickEvent() {
        WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), this, this.getX(), this.getY(), this.getZ());
    }
}
