package net.warcar.hito_hito_nika.projectiles.hand;

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

public class TrueGrizzlyMagnumProjectile extends AbilityProjectileEntity {
    public TrueGrizzlyMagnumProjectile(EntityType type, World world) {
        super(type, world);
    }

    public TrueGrizzlyMagnumProjectile(World world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_GRIZZLY_MAGNUM.get(), world, player, ability);
        this.setDamage(80f);
        this.setMaxLife(10);
        this.setAffectedByHardening();
        this.setPassThroughEntities();
        this.setPassThroughBlocks();
        this.setHurtTime(10);
        this.setEntityCollisionSize(2.5D);
        this.setDamageSource(this.getDamageSource().setSourceElement(SourceElement.RUBBER));
        this.onEntityImpactEvent = this::onEntityImpactEvent;
    }

    private void onEntityImpactEvent(LivingEntity hitEntity) {
        Vector3d speed = WyHelper.propulsion(Objects.requireNonNull(this.getThrower()), 5.0D, 5.0D);
        hitEntity.setDeltaMovement(speed.x, 0.5D, speed.z);
        hitEntity.hurtMarked = true;
    }
}
