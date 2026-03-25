package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;

public class RhinoRexSchneiderProjectile extends NuProjectileEntity {
    public RhinoRexSchneiderProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public RhinoRexSchneiderProjectile(Level world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_RHINO_REX_SCHNEIDER.get(), world, player, ability);
        this.setDamage(120f);
        this.setMaxLife(10);
        this.setEntityCollisionSize(4d);
        this.setPassThroughEntities();
        this.addEntityHitEvent(100, TrueGomuHelper.getBazookaOnEntityImpactEvent(this, 15));
        this.addBlockHitEvent(100, TrueGomuHelper.onBlockImpactEvent(this, 11, 80));
    }
}
