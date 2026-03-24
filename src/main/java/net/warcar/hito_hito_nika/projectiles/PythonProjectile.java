package net.warcar.hito_hito_nika.projectiles;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.data.entity.projectileextra.IProjectileExtras;
import xyz.pixelatedw.mineminenomi.data.entity.projectileextra.ProjectileExtrasCapability;
import xyz.pixelatedw.mineminenomi.init.ModAbilityComponents;
import xyz.pixelatedw.mineminenomi.init.ModEntityPredicates;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;

public abstract class PythonProjectile extends NuProjectileEntity {
    protected static final EntityDataAccessor<Integer> NEXT_ID = SynchedEntityData.defineId(PythonProjectile.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> PREV_ID = SynchedEntityData.defineId(PythonProjectile.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> IS_STATIC = SynchedEntityData.defineId(PythonProjectile.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Integer> LAYER = SynchedEntityData.defineId(PythonProjectile.class, EntityDataSerializers.INT);
    protected Ability master;
    protected float speed = 0f;
    private boolean sneakyStatic;

    public PythonProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public PythonProjectile(EntityType<? extends PythonProjectile> type, Level world, LivingEntity player, Ability ability, float speed, int layer) {
        super(type, world, player, ability, SourceElement.RUBBER, SourceHakiNature.HARDENING, SourceType.FIST, SourceType.PHYSICAL);
        this.addEntityHitEvent(100, this::onEntityImpactEvent);
        master = ability;
        this.setLayer(layer);
        this.setUnavoidable();
        this.speed = speed;
        this.setPassThroughBlocks();
    }

    private void setLayer(int layer) {
        this.entityData.set(LAYER, layer);
    }

    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(NEXT_ID, -1);
        this.entityData.define(PREV_ID, -1);
        this.entityData.define(IS_STATIC, false);
        this.entityData.define(LAYER, 0);
    }

    private void onEntityImpactEvent(EntityHitResult hitEntity) {
        this.kill();
    }

    @Nullable
    public Entity getPrev() {
        return this.level().getEntity(this.entityData.get(PREV_ID));
    }

    @Nullable
    public Entity getNext() {
        return this.level().getEntity(this.entityData.get(NEXT_ID));
    }

    public void setPrev(Entity ent) {
        this.entityData.set(PREV_ID, ent.getId());
    }

    public void setNext(Entity ent) {
        this.entityData.set(NEXT_ID, ent.getId());
    }

    public abstract PythonProjectile getNew();

    /*@Override
    public void onModHit(RayTraceResult hit) {
        if (this.isStatic()) {
            return;
        }
        if (hit instanceof EntityRayTraceResult && ((EntityRayTraceResult) hit).getEntity() instanceof AbilityProjectileEntity) {
            return;
        }
        boolean wasInfused = false;
        IProjectileExtras extras = ProjectileExtrasCapability.get(this).get();
        if (extras.isProjectileHaoshokuInfused()) {
            wasInfused = true;
            extras.setProjectileHaoshokuInfused(false);
        }
        super.onModHit(hit);
        if (wasInfused) {
            extras.setProjectileHaoshokuInfused(true);
        }
    }*/

    public void tick() {
        if (!this.level().isClientSide() && this.master != null) {
            Optional<ContinuousComponent> component = this.master.getComponent(ModAbilityComponents.CONTINUOUS.get());
            if (component.isPresent() && !component.get().isContinuous()) {
                this.remove(RemovalReason.DISCARDED);
                return;
            }
        }
        if (this.getLife() <= 0 && !this.isStatic() && !this.level().isClientSide) {
            if (this.getOwner() == null || this.getLayer() == 0) {
                this.remove(RemovalReason.DISCARDED);
                return;
            }
            Optional<LivingEntity> closest = WyHelper.getNearbyLiving(this.getOwner().position(), this.level(), 1000, 1000, 1000, ModEntityPredicates.getEnemyFactions(this.getOwner())).stream().min(Comparator.comparing(this::distanceTo));
            if (!closest.isPresent() && this.getMaxLife() == 5) {
                super.tick();
                return;
            } else if (!closest.isPresent()) {
                this.setMaxLife(5);
                super.tick();
                return;
            } else {
                PythonProjectile projectile = this.getNew();
                this.setStatic(true);
                this.setMaxLife(100000000);
                this.setPassThroughBlocks();
                LivingEntity entity = closest.get();
                Vec3 vec = this.position().vectorTo(entity.position());
                projectile.setDamage(this.getDamage());
                projectile.shootFromRotation(this, 0, 0, 0, 0, 0);
                projectile.setDeltaMovement(vec.normalize().scale(this.speed));
                this.level().addFreshEntity(projectile);
                this.setNext(projectile);
                projectile.setPrev(this);
                projectile.setPos(this.getX(), this.getY(), this.getZ());
                this.setDeltaMovement(0, 0, 0);
            }
        }
        if (this.isStatic()) {
            if ((this.getNext() == null || !this.getNext().isAlive())) {
                this.remove(RemovalReason.DISCARDED);
            }
            return;
        }
        super.tick();
        if (this.getNext() != null && this.getNext().isAlive()) {
            Entity prev = this.getPrev();
            if (prev == null) {
                prev = this.getOwner();
            }
            Vec3 vec = prev.position().vectorTo(this.position());
            double f = this.position().distanceTo(vec);
            var xRot = (float)(Math.atan2(vec.y, f) * (double)(180F / (float)Math.PI));
            var yRot = (float)(Math.atan2(vec.x, vec.z) * (double)(180F / (float)Math.PI));
            this.setRot(yRot,  xRot);
        }
    }

    protected void setStatic(boolean b) {
        this.sneakyStatic = b;
        //this.entityData.set(IS_STATIC, b);
    }

    protected boolean isStatic() {
        return sneakyStatic;
        //return this.entityData.get(IS_STATIC);
    }

    protected int getLayer() {
        return this.entityData.get(LAYER);
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!this.isStatic() || this.getLayer() == 0 || this.getNext() == null) {
            super.remove(reason);
        }
    }
}
