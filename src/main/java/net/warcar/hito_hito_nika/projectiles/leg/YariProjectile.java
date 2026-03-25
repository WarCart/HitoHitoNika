package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;

public class YariProjectile extends NuProjectileEntity {
    public YariProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public YariProjectile(Level world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_YARI.get(), world, player, ability);
        this.setMaxLife(12);
        this.setDamage(30F);
        this.setEntityCollisionSize(2.5d);
        this.setPassThroughEntities();
        this.setPassThroughBlocks();
        this.addEntityHitEvent(100, TrueGomuHelper.getBazookaOnEntityImpactEvent(this, 4));
    }
}
