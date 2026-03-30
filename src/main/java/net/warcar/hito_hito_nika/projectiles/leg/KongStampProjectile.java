package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import net.warcar.hito_hito_nika.projectiles.TrueGomuProjectile;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;

public class KongStampProjectile extends TrueGomuProjectile {
    public KongStampProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public KongStampProjectile(Level world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_KONG_STAMP.get(), player, ability, SourceElement.RUBBER, SourceHakiNature.HARDENING, SourceType.FIST, SourceType.PHYSICAL);
        this.setMaxLife(12);
        this.setDamage(40f);
        this.setEntityCollisionSize(2.5d);
        this.setSize(4.5f);
        this.addBlockHitEvent(100, TrueGomuHelper.onBlockImpactEvent(this, 2.5f, 15));
    }
}
