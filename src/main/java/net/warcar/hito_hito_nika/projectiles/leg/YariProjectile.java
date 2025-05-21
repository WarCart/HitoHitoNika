package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.Objects;

public class YariProjectile extends AbilityProjectileEntity {
    public YariProjectile(EntityType type, World world) {
        super(type, world);
    }

    public YariProjectile(World world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_YARI.get(), world, player, ability);
        this.setMaxLife(12);
        this.setAffectedByHardening();
        this.setDamage(20F);
        this.setEntityCollisionSize(2.5d);
        this.setPassThroughEntities();
        this.setPassThroughBlocks();
        this.setDamageSource(this.getDamageSource().setPhysical());
        this.onEntityImpactEvent = this::onEntityImpactEvent;
    }

    private void onEntityImpactEvent(LivingEntity hitEntity) {
        Vector3d speed = WyHelper.propulsion(Objects.requireNonNull(this.getThrower()), 4.0D, 4.0D);
        hitEntity.setDeltaMovement(speed.x, 0.4D, speed.z);
        hitEntity.hurtMarked = true;
    }
}
