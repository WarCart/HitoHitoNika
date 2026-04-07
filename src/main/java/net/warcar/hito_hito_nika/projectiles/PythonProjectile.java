package net.warcar.hito_hito_nika.projectiles;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModAbilityComponents;
import xyz.pixelatedw.mineminenomi.init.ModEntityPredicates;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public abstract class PythonProjectile extends NuProjectileEntity {
    protected Ability master;
    protected float speed = 0f;
    protected int maxLifetime;
    protected int lifetime;
    protected boolean retracting = false;

    private static final EntityDataAccessor<ArrayList<Vec3>> TURNS = SynchedEntityData.defineId(PythonProjectile.class, TrueGomuHelper.TURNS_SERIALIZER);

    public PythonProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public PythonProjectile(EntityType<? extends PythonProjectile> type, Level world, LivingEntity player, Ability ability, float speed) {
        super(type, world, player, ability, SourceElement.RUBBER, SourceHakiNature.HARDENING, SourceType.FIST, SourceType.PHYSICAL);
        this.addEntityHitEvent(100, this::onEntityImpactEvent);
        master = ability;
        this.setUnavoidable();
        this.speed = speed;
        this.setPassThroughBlocks();
    }

    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TURNS, new ArrayList<>());
    }

    private void onEntityImpactEvent(EntityHitResult hitEntity) {
        this.retracting = true;
    }

    public void setMaxLifetime(int maxLifetime) {
        this.maxLifetime = maxLifetime;
        this.lifetime = maxLifetime;
    }

    public void tick() {
        if (this.getOwner() == null) {
            this.remove(RemovalReason.DISCARDED);
            return;
        }
        if (!this.level().isClientSide() && this.master != null) {
            Optional<ContinuousComponent> component = this.master.getComponent(ModAbilityComponents.CONTINUOUS.get());
            if (component.isPresent() && !component.get().isContinuous()) {
                this.remove(RemovalReason.DISCARDED);
                return;
            }
        }
        super.tick();
        if (this.level().isClientSide()) {
            return;
        } else if (this.retracting) {
            ArrayList<Vec3> turns = entityData.get(TURNS);
            if (turns.isEmpty()) {
                this.remove(RemovalReason.DISCARDED);
                return;
            } else {
                var newPos = turns.remove(turns.size() - 1);
                this.entityData.set(TURNS, turns);
                var pos = this.position();
                this.setPos(newPos);
                this.xOld = pos.x;
                this.yOld = pos.y;
                this.zOld = pos.z;
            }
            return;
        }
        this.lifetime--;
        if (this.lifetime <= 0) {
            Optional<LivingEntity> closest = WyHelper.getNearbyLiving(this.getOwner().position(), this.level(), 1000, 1000, 1000, ModEntityPredicates.getEnemyFactions(this.getOwner())).stream().min(Comparator.comparing(this::distanceTo));
            closest.ifPresent(entity -> {
                Vec3 vec = this.position().vectorTo(entity.position());
                this.setDeltaMovement(vec.normalize().scale(this.speed));
            });
            this.lifetime =  this.maxLifetime;
            ArrayList<Vec3> allTurns = this.getAllTurns();
            HitoHitoNoMiNikaMod.LOGGER.info(allTurns);
            this.getEntityData().set(TURNS, allTurns);
        }
    }

    public ArrayList<Vec3> getAllTurns() {
        ArrayList<Vec3> list = new ArrayList<>(this.getEntityData().get(TURNS));
        list.add(this.position());
        return list;
    }

    public ArrayList<Vec3> getAllTurns(float partialTicks) {
        ArrayList<Vec3> list = new ArrayList<>(this.getEntityData().get(TURNS));
        list.add(this.getPosition(partialTicks));
        return list;
    }

    public int getSegments() {
        return this.getAllTurns().size() - 1;
    }

    @Override
    public void shootFromRotation(Entity thrower, float pX, float pY, float pZ, float velocity, float inaccuracy) {
        super.shootFromRotation(thrower, pX, pY, pZ, velocity, inaccuracy);
        ArrayList<Vec3> s = new ArrayList<>();
        s.add(this.position());
        this.getEntityData().set(TURNS, s);
    }
}
