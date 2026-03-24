package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityExplosion;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;

public class LeoRexBazookaProjectile extends NuProjectileEntity {
    public LeoRexBazookaProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public LeoRexBazookaProjectile(Level world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_LEO_REX_BAZOOKA.get(), world, player, ability, SourceElement.RUBBER, SourceHakiNature.HARDENING, SourceType.FIST, SourceType.PHYSICAL);
        this.setDamage(120f);
        this.setMaxLife(10);
        this.setEntityCollisionSize(4d);
        this.setPassThroughEntities();
        this.addEntityHitEvent(100, this::onEntityImpactEvent);
        this.addBlockHitEvent(100, this::onBlockImpactEvent);
    }

    private void onEntityImpactEvent(EntityHitResult hit) {
        var hitEntity = hit.getEntity();
        Vec3 speed = this.getDeltaMovement().normalize().scale(15);
        AbilityHelper.setDeltaMovement(hitEntity, speed.x, 0.8, speed.z);
    }

    private void onBlockImpactEvent(BlockHitResult hit) {
        AbilityExplosion explosion = new AbilityExplosion(this.getOwner(), this.getParent().orElse(null), this.getX(), this.getY(), this.getZ(), 11F);
        explosion.setStaticDamage(80.0F);
        explosion.setExplosionSound(false);
        explosion.setDamageOwner(false);
        explosion.setDestroyBlocks(true);
        explosion.setFireAfterExplosion(false);
        explosion.setDamageEntities(false);
        explosion.explode();
    }
}
