package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.ExplosionAbility;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;

public class KingKongStampProjectile extends AbilityProjectileEntity {
    public KingKongStampProjectile(EntityType type, World world) {
        super(type, world);
    }

    public KingKongStampProjectile(World world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_KING_KONG_STAMP.get(), world, player, ability);
        this.setMaxLife(25);
        this.setPhysical();
        this.setDamage(120F);
        this.setEntityCollisionSize(4d);
        this.setPassThroughEntities();
        this.setCanGetStuckInGround();
        this.setDamageSource(this.getDamageSource().getSource());
        this.onBlockImpactEvent = this::onBlockImpactEvent;
    }

    private void onBlockImpactEvent(BlockPos hit) {
        ExplosionAbility explosion = AbilityHelper.newExplosion(this.getThrower(), this.level, this.getX(), this.getY(), this.getZ(), 13F);
        explosion.setExplosionSound(false);
        explosion.setStaticDamage(80.0F);
        explosion.setDamageOwner(false);
        explosion.setDestroyBlocks(true);
        explosion.setFireAfterExplosion(false);
        explosion.setDamageEntities(false);
        explosion.doExplosion();
    }
}
