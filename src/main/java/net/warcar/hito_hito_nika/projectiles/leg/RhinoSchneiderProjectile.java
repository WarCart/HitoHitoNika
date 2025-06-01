package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceElement;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.Objects;

public class RhinoSchneiderProjectile extends AbilityProjectileEntity {
    public RhinoSchneiderProjectile(EntityType type, World world) {
        super(type, world);
    }

    public RhinoSchneiderProjectile(World world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_RHINO_SCHNEIDER.get(), world, player, ability);
        this.setDamage(40);
        this.setMaxLife(10);
        this.setAffectedByHardening();
        this.setPassThroughEntities();
        this.setHurtTime(10);
        this.setPassThroughBlocks();
        this.setDamageSource(this.getDamageSource().setSourceElement(SourceElement.RUBBER));
        this.onEntityImpactEvent = this::onEntityImpactEvent;
    }

    private void onEntityImpactEvent(LivingEntity hitEntity) {
        Vector3d speed = WyHelper.propulsion(Objects.requireNonNull(this.getThrower()), 7.0D, 7.0D);
        hitEntity.setDeltaMovement(speed.x, 0.8D, speed.z);
        hitEntity.hurtMarked = true;
    }
}
