package net.warcar.hito_hito_nika.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.data.entity.projectilesextra.IProjectileExtras;
import xyz.pixelatedw.mineminenomi.data.entity.projectilesextra.ProjectileExtrasCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModAbilityKeys;
import xyz.pixelatedw.mineminenomi.init.ModEntityPredicates;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;

public abstract class PythonProjectile extends AbilityProjectileEntity {
    protected static final DataParameter<Integer> NEXT_ID = EntityDataManager.defineId(PythonProjectile.class, DataSerializers.INT);
    protected static final DataParameter<Integer> PREV_ID = EntityDataManager.defineId(PythonProjectile.class, DataSerializers.INT);
    protected static final DataParameter<Boolean> IS_STATIC = EntityDataManager.defineId(PythonProjectile.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Integer> LAYER = EntityDataManager.defineId(PythonProjectile.class, DataSerializers.INT);
    protected Ability master;
    protected float speed = 0f;
    private boolean sneakyStatic;

    public PythonProjectile(EntityType type, World world) {
        super(type, world);
    }

    public PythonProjectile(EntityType<?> type, World world, LivingEntity player, Ability ability, float speed, int layer) {
        super(type, world, player, ability);
        this.onEntityImpactEvent = this::onEntityImpactEvent;
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

    private void onEntityImpactEvent(LivingEntity hitEntity) {
        this.kill();
    }

    @Nullable
    public Entity getPrev() {
        return this.level.getEntity(this.entityData.get(PREV_ID));
    }

    @Nullable
    public Entity getNext() {
        return this.level.getEntity(this.entityData.get(NEXT_ID));
    }

    public void setPrev(Entity ent) {
        this.entityData.set(PREV_ID, ent.getId());
    }

    public void setNext(Entity ent) {
        this.entityData.set(NEXT_ID, ent.getId());
    }

    public abstract PythonProjectile getNew();

    @Override
    public void onModHit(RayTraceResult hit) {
        if (this.isStatic()) {
            return;
        }
        if (hit instanceof EntityRayTraceResult && ((EntityRayTraceResult) hit).getEntity() instanceof AbilityProjectileEntity) {
            return;
        }
        boolean wasInfused = false;
        IProjectileExtras extras = ProjectileExtrasCapability.get(this);
        if (extras.isProjectileHaoshokuInfused()) {
            wasInfused = true;
            extras.setProjectileHaoshokuInfused(false);
        }
        super.onModHit(hit);
        if (wasInfused) {
            extras.setProjectileHaoshokuInfused(true);
        }
    }

    public void tick() {
        if (!this.level.isClientSide() && this.master != null) {
            Optional<ContinuousComponent> component = this.master.getComponent(ModAbilityKeys.CONTINUOUS);
            if (component.isPresent() && !component.get().isContinuous()) {
                this.remove();
                return;
            }
        }
        if (this.getLife() <= 0 && !this.isStatic() && !this.level.isClientSide) {
            if (this.getThrower() == null || this.getLayer() == 0) {
                this.remove();
                return;
            }
            Optional<LivingEntity> closest = WyHelper.getNearbyLiving(this.getThrower().position(), this.level, 1000, 1000, 1000, ModEntityPredicates.getEnemyFactions(this.getThrower())).stream().min(Comparator.comparing(this::distanceTo));
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
                Vector3d vec = this.position().vectorTo(entity.position());
                projectile.setDamage(this.getDamage());
                projectile.shootFromRotation(this, 0, 0, 0, 0, 0);
                projectile.setDeltaMovement(vec.normalize().scale(this.speed));
                this.level.addFreshEntity(projectile);
                this.setNext(projectile);
                projectile.setPrev(this);
                projectile.setPosAndOldPos(this.getX(), this.getY(), this.getZ());
                this.setDeltaMovement(0, 0, 0);
            }
        }
        if (this.isStatic()) {
            if ((this.getNext() == null || !this.getNext().isAlive())) {
                this.remove();
            }
            return;
        }
        super.tick();
        if (this.getNext() != null && this.getNext().isAlive()) {
            Entity prev = this.getPrev();
            if (prev == null) {
                prev = this.getThrower();
            }
            Vector3d vec = prev.position().vectorTo(this.position());
            float f = MathHelper.sqrt(getHorizontalDistanceSqr(vec));
            this.xRot = (float)(MathHelper.atan2(vec.y, f) * (double)(180F / (float)Math.PI));
            this.xRotO = xRot;
            this.yRot = (float)(MathHelper.atan2(vec.x, vec.z) * (double)(180F / (float)Math.PI));
            this.yRotO = yRot;
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
    public void remove() {
        if (!this.isStatic() || this.getLayer() == 0 || this.getNext() == null) {
            super.remove();
        }
    }
}
