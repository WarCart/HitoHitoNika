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

public class BajrangStampGunProjectile extends NuProjectileEntity {
    protected final float size;

    public BajrangStampGunProjectile(EntityType type, Level world) {
        super(type, world);
        size = 30;
    }

    public BajrangStampGunProjectile(Level world, LivingEntity player, Ability ability, float size) {
        super(NikaProjectiles.GOMU_GOMU_NO_BAJRANG_STAMP_GUN.get(), world, player, ability, SourceElement.RUBBER, SourceHakiNature.HARDENING, SourceType.FIST, SourceType.PHYSICAL);
        this.setMaxLife(250);
        this.setDamage(250F);
        this.setPhysical();
        this.setEntityCollisionSize(15);
        this.setPassThroughEntities();
        this.size = size;
        this.addBlockHitEvent(100, TrueGomuHelper.onBlockImpactEvent(this, size, 200));
    }

    public BajrangStampGunProjectile(Level world, LivingEntity player, Ability ability) {
        this(world, player, ability, 30);
    }
}
