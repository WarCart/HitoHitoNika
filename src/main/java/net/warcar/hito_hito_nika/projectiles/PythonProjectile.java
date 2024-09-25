package net.warcar.hito_hito_nika.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.warcar.hito_hito_nika.projectiles.hand.JetCulverinProjectile;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModEntityPredicates;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;

public abstract class PythonProjectile extends AbilityProjectileEntity {
    protected static final DataParameter<Integer> NEXT_ID = EntityDataManager.defineId(PythonProjectile.class, DataSerializers.INT);
    protected static final DataParameter<Integer> PREV_ID = EntityDataManager.defineId(PythonProjectile.class, DataSerializers.INT);
    protected Ability master;
    protected float speed = 0f;
    protected int layer = 0;

    public PythonProjectile(EntityType type, World world) {
        super(type, world);
    }

    public PythonProjectile(EntityType<?> type, World world, LivingEntity player, Ability ability, float speed, int layer) {
        super(type, world, player, ability);
        this.onEntityImpactEvent = this::onEntityImpactEvent;
        master = ability;
        this.layer = layer;
        this.speed = speed;
    }

    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(NEXT_ID, -1);
        this.entityData.define(PREV_ID, -1);
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

    public void tick() {
        if (this.getLife() <= 0 && this.getMaxLife() != 1) {
            if (this.getThrower() == null || this.layer == 0) {
                this.kill();
                return;
            }
            PythonProjectile projectile = this.getNew();
            projectile.setDamage(this.getDamage() + 1f);
            Optional<LivingEntity> closest = WyHelper.getNearbyLiving(this.getThrower().position(), this.level, 1000, 1000, 1000, ModEntityPredicates.getEnemyFactions(this.getThrower())).stream().min(Comparator.comparing(this::distanceTo));
            if (!closest.isPresent() && this.getMaxLife() == 5) {
                super.tick();
                return;
            } else if (!closest.isPresent()) {
                this.setMaxLife(5);
                this.setLife(5);
                super.tick();
                return;
            }
            else {
                this.setMaxLife(1);
            }
            LivingEntity entity = closest.get();
            Vector3d vec = this.position().vectorTo(entity.position());
            projectile.shootFromRotation(this, 0, 0, 0, 0, 0);
            projectile.setDeltaMovement(vec.normalize().scale(this.speed));
            this.onEntityImpactEvent = (ent) -> {};
            this.level.addFreshEntity(projectile);
            this.setNext(projectile);
            projectile.setPrev(this);
            projectile.setPosAndOldPos(this.getX(), this.getY(), this.getZ());
            this.setEntityCollisionSize(0);
            this.setDeltaMovement(0, 0, 0);
        } else if (this.getMaxLife() == 1 && (this.getNext() == null || !this.getNext().isAlive())) {
            this.kill();
        } else if (this.getMaxLife() == 1) {
            this.setLife(1);
        }
        super.tick();
        if (this.getMaxLife() == 1 && this.getNext() != null && this.getNext().isAlive()) {
            Entity prev = this.getPrev();
            if (prev == null) {
                prev = this.getThrower();
            }
            assert prev != null;
            Vector3d vec = prev.position().vectorTo(this.position());
            float f = MathHelper.sqrt(getHorizontalDistanceSqr(vec));
            this.xRot = lerpRotation(this.xRotO, (float)(MathHelper.atan2(vec.y, f) * (double)(180F / (float)Math.PI)));
            this.yRot = lerpRotation(this.yRotO, (float)(MathHelper.atan2(vec.x, vec.z) * (double)(180F / (float)Math.PI)));
        }
    }
}
