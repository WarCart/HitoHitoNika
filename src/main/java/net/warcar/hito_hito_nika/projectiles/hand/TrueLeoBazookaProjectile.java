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

public class TrueLeoBazookaProjectile extends AbilityProjectileEntity {
    public TrueLeoBazookaProjectile(EntityType type, World world) {
        super(type, world);
    }

    public TrueLeoBazookaProjectile(World world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_LEO_BAZOOKA.get(), world, player, ability);
        this.setDamage(70.0F);
        this.setMaxLife(10);
        this.setAffectedByHardening();
        this.setPassThroughEntities();
        this.setPassThroughBlocks();
        this.setEntityCollisionSize(2.5D);
        this.setHurtTime(10);
        this.setDamageSource(this.getDamageSource().setSourceElement(SourceElement.RUBBER));
        this.onEntityImpactEvent = this::onEntityImpactEvent;
    }

    private void onEntityImpactEvent(LivingEntity hitEntity) {
        Vector3d speed = WyHelper.propulsion(Objects.requireNonNull(this.getThrower()), 7.0D, 7.0D);
        hitEntity.setDeltaMovement(speed.x, 0.8D, speed.z);
        hitEntity.hurtMarked = true;
    }
}
