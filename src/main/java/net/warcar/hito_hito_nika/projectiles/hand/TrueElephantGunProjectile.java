package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.ExplosionAbility;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;

public class TrueElephantGunProjectile extends AbilityProjectileEntity {
    public TrueElephantGunProjectile(EntityType type, World world) {
        super(type, world);
    }

    public TrueElephantGunProjectile(World world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_ELEPHANT_GUN.get(), world, player, ability);
        this.setMaxLife(12);
        this.setAffectedByHardening();
        this.setDamage(24F);
        this.setEntityCollisionSize(2.5d);
        this.setPassThroughEntities();
        this.setCanGetStuckInGround();
        this.setEntityCollisionSize(5.0, 3.0, 5.0);
        this.setDamageSource(this.getDamageSource().setPhysical());
        this.onBlockImpactEvent = this::onBlockImpactEvent;
    }

    private void onBlockImpactEvent(BlockPos hit) {
        ExplosionAbility explosion = AbilityHelper.newExplosion(this.getThrower(), this.level, this.getX(), this.getY(), this.getZ(), 2F);
        explosion.setStaticDamage(80.0F);
        explosion.setExplosionSound(false);
        explosion.setDamageOwner(false);
        explosion.setDestroyBlocks(true);
        explosion.setFireAfterExplosion(false);
        explosion.setDamageEntities(false);
        explosion.doExplosion();
    }
}
