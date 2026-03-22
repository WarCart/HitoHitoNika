package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;

import java.util.Objects;

public class TrueBazookaProjectile extends NuProjectileEntity {
    public TrueBazookaProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public TrueBazookaProjectile(Level world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_BAZOOKA.get(), world, player, ability);
        this.setMaxLife(12);
        this.setPhysical();
        this.setDamage(30F);
        this.setEntityCollisionSize(2.5d);
        this.setPassThroughEntities();
        this.setPassThroughBlocks();
        this.addEntityHitEvent(100, this::onEntityImpactEvent);
    }

    private void onEntityImpactEvent(EntityHitResult hitEntity) {
        var speed = this.getDeltaMovement().normalize().scale(4);
        AbilityHelper.setDeltaMovement(hitEntity.getEntity(), speed.x, 0.4, speed.z);
    }
}
