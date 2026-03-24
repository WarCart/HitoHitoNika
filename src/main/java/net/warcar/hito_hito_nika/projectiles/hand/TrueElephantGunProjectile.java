package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityExplosion;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;

public class TrueElephantGunProjectile extends NuProjectileEntity {
    public TrueElephantGunProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public TrueElephantGunProjectile(Level world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_ELEPHANT_GUN.get(), world, player, ability, SourceElement.RUBBER, SourceHakiNature.HARDENING, SourceType.FIST, SourceType.PHYSICAL);
        this.setMaxLife(12);
        this.setDamage(40f);
        this.setEntityCollisionSize(2.5d);
        this.setPassThroughEntities();
        this.setEntityCollisionSize(5.0, 3.0, 5.0);
        this.addBlockHitEvent(100, this::onBlockImpactEvent);
    }

    private void onBlockImpactEvent(BlockHitResult hit) {
        AbilityExplosion explosion = new AbilityExplosion(this.getOwner(), this.getParent().orElse(null), this.getX(), this.getY(), this.getZ(), 2F);
        explosion.setStaticDamage(80.0F);
        explosion.setExplosionSound(false);
        explosion.setDamageOwner(false);
        explosion.setDestroyBlocks(true);
        explosion.setFireAfterExplosion(false);
        explosion.setDamageEntities(false);
        explosion.explode();
    }
}
