package net.warcar.hito_hito_nika.projectiles;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.warcar.hito_hito_nika.renderers.projectiles.GomuProjectileRenderer;
import org.jetbrains.annotations.Nullable;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.api.entities.NuHorizontalLightningEntity;

public class TrueGomuProjectile extends NuHorizontalLightningEntity {
    protected static final EntityDataAccessor<Integer> DEFORMATION = SynchedEntityData.defineId(TrueGomuProjectile.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> LEG = SynchedEntityData.defineId(TrueGomuProjectile.class, EntityDataSerializers.BOOLEAN);
    public TrueGomuProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public TrueGomuProjectile(EntityType<? extends NuHorizontalLightningEntity> type, LivingEntity thrower, @Nullable IAbility ability, SourceElement element, SourceHakiNature hakiNature, SourceType... types) {
        super(type, thrower, thrower.getX(), thrower.getY(), thrower.getZ(), 1000000, 0, ability, element, hakiNature, types);
        this.setRetracting();
        this.setDepth(1);
        this.setPassThroughEntities();
    }

    public Vec3 getMovement() {
        return this.lookVec;
    }

    @Override
    public void setSize(float size) {
        super.setSize(size / 100);
    }

    public GomuProjectileRenderer.Deformation getDeformation() {
        return GomuProjectileRenderer.Deformation.values()[this.entityData.get(DEFORMATION)];
    }

    public boolean isLeg() {
        return this.entityData.get(LEG);
    }

    protected void setDeformation(GomuProjectileRenderer.Deformation newDeformation) {
        this.entityData.set(DEFORMATION, newDeformation.ordinal());
    }

    protected void setLeg(boolean leg) {
        this.entityData.set(LEG, leg);
    }

    protected void setLeg() {
        this.setLeg(true);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DEFORMATION, 0);
        this.entityData.define(LEG, false);
    }
}
