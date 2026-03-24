package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;

import java.util.Objects;

public class TrueLeoBazookaProjectile extends NuProjectileEntity {
    public TrueLeoBazookaProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public TrueLeoBazookaProjectile(Level world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_LEO_BAZOOKA.get(), world, player, ability, SourceElement.RUBBER, SourceHakiNature.HARDENING, SourceType.FIST, SourceType.PHYSICAL);
        this.setDamage(80f);
        this.setMaxLife(10);
        this.setPassThroughEntities();
        this.setPassThroughBlocks();
        this.setEntityCollisionSize(2.5D);
        this.addEntityHitEvent(100, this::onEntityImpactEvent);
    }

    private void onEntityImpactEvent(EntityHitResult hitEntity) {
        Vec3 speed = this.getDeltaMovement().normalize().scale(7);
        AbilityHelper.setDeltaMovement(hitEntity.getEntity(), speed.x, 0.8, speed.z);
    }
}
