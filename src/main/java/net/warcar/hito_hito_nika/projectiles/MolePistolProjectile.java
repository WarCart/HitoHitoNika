package net.warcar.hito_hito_nika.projectiles;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.warcar.hito_hito_nika.abilities.MoguraPistolAbility;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModEffects;

public class MolePistolProjectile extends AbilityProjectileEntity {
    private static final DataParameter<Vector3d> TARGET_POS = EntityDataManager.defineId(MolePistolProjectile.class, TrueGomuHelper.VECTOR_SERIALIZER);

    public MolePistolProjectile(EntityType type, World world) {
        super(type, world);
    }

    public MolePistolProjectile(World world, LivingEntity player) {
        super(NikaProjectiles.GOMU_GOMU_NO_MOLE_PISTOL.get(), world, player, MoguraPistolAbility.INSTANCE);
        this.setDamage(50.0F);
        this.setMaxLife(8);
        this.setArmorPiercing(0.3f);
        this.setPassThroughBlocks();
        this.setPassThroughEntities();
        this.setEntityCollisionSize(1.75F, 5.0F, 1.75F);
        this.onEntityImpactEvent = this::onImpact;
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGET_POS, Vector3d.ZERO);
    }

    public BlockState getOutBlock() {
        return this.level.getBlockState(new BlockPos(this.getTargetPos()));
    }

    public Vector3d getTargetPos() {
        return this.entityData.get(TARGET_POS);
    }

    public void setTargetPos(Vector3d targetPos) {
        this.entityData.set(TARGET_POS, targetPos);
    }

    private void onImpact(LivingEntity target) {
        target.addEffect(new EffectInstance(ModEffects.DIZZY.get(), 1, 1));
    }
}