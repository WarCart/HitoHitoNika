package net.warcar.hito_hito_nika.projectiles.hand;

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

public class TrueElephantGunProjectile extends TrueGomuProjectile {
    public TrueElephantGunProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public TrueElephantGunProjectile(Level world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_ELEPHANT_GUN.get(), player, ability, SourceElement.RUBBER, SourceHakiNature.HARDENING, SourceType.FIST, SourceType.PHYSICAL);
        this.setMaxLife(12);
        this.setDamage(40f);
        this.setEntityCollisionSize(2.5d);
        this.setSize(15.5f);
        this.setEntityCollisionSize(5.0, 3.0, 5.0);
        this.addBlockHitEvent(100, TrueGomuHelper.onBlockImpactEvent(this, 2, 80));
    }
}
