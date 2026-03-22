package net.warcar.hito_hito_nika.projectiles;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.warcar.hito_hito_nika.abilities.MoguraPistolAbility;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import org.joml.Vector3d;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModEffects;

public class GomuGomuNoMoguraPistolProjectile extends NuProjectileEntity {
    private static final EntityDataAccessor<Vec3> TARGET_POS = SynchedEntityData.defineId(GomuGomuNoMoguraPistolProjectile.class, TrueGomuHelper.VECTOR_SERIALIZER);

    public GomuGomuNoMoguraPistolProjectile(EntityType type, Level Level) {
        super(type, Level);
    }

    public GomuGomuNoMoguraPistolProjectile(Level Level, LivingEntity player) {
        super(NikaProjectiles.GOMU_GOMU_NO_MOLE_PISTOL.get(), Level, player);
        this.setDamage(50.0F);
        this.setMaxLife(8);
        this.setArmorPiercing(0.3f);
        this.setPassThroughBlocks();
        this.setPassThroughEntities();
        this.setEntityCollisionSize(1.75F, 5.0F, 1.75F);
        this.addEntityHitEvent(100, this::onImpact);
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGET_POS, Vec3.ZERO);
    }

    public BlockState getOutBlock() {
        Vec3 vec = this.getTargetPos();
        return this.level().getBlockState(new BlockPos((int) vec.x, (int) vec.y, (int) vec.z));
    }

    public Vec3 getTargetPos() {
        return this.entityData.get(TARGET_POS);
    }

    public void setTargetPos(Vec3 targetPos) {
        this.entityData.set(TARGET_POS, targetPos);
    }

    private void onImpact(EntityHitResult target) {
        ((LivingEntity) target.getEntity()).addEffect(new MobEffectInstance(ModEffects.DIZZY.get(), 1, 1));
    }
}