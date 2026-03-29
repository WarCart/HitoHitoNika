package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;

public class King3KongStampProjectile extends NuProjectileEntity {
    public King3KongStampProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public King3KongStampProjectile(Level world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_KING_3_KONG_STAMP.get(), world, player, ability, SourceElement.RUBBER, SourceHakiNature.HARDENING, SourceType.FIST, SourceType.PHYSICAL);
        this.setMaxLife(40);
        this.setPhysical();
        this.setDamage(100);
        this.setEntityCollisionSize(7d);
        this.setPassThroughEntities();
        this.addBlockHitEvent(100, TrueGomuHelper.onBlockImpactEvent(this, 20, 120));
    }
}
