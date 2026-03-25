package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;

public class GigantYariProjectile extends NuProjectileEntity {
    public GigantYariProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public GigantYariProjectile(Level world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_GIGANT_YARI.get(), world, player, ability);
        this.setDamage(75f);
        this.setMaxLife(10);
        this.setPassThroughEntities();
        this.setPassThroughBlocks();
        this.setEntityCollisionSize(2.5D);
        this.addEntityHitEvent(100, TrueGomuHelper.getBazookaOnEntityImpactEvent(this, 5));
    }
}
