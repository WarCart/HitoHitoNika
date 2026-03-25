package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;

public class ElephantStampProjectile extends NuProjectileEntity {
    public ElephantStampProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public ElephantStampProjectile(Level world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_ELEPHANT_STAMP.get(), world, player, ability);
        this.setMaxLife(12);
        this.setDamage(40f);
        this.setEntityCollisionSize(2.5d);
        this.setPassThroughEntities();
        this.setEntityCollisionSize(5.0, 3.0, 5.0);
        this.addBlockHitEvent(300, TrueGomuHelper.onBlockImpactEvent(this, 2, 80));
    }
}
