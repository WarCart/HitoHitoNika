package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChargeComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.DamageTakenComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.DealDamageComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.GrabEntityComponent;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;

public class GomuGomuNoCannonballAbility extends PassiveAbility2 {
    public static final AbilityCore<GomuGomuNoCannonballAbility> INSTANCE = new AbilityCore.Builder<>("Gomu Gomu no Cannonball", AbilityCategory.DEVIL_FRUITS, AbilityType.PASSIVE, GomuGomuNoCannonballAbility::new)
            .setUnlockCheck(GomuGomuNoCannonballAbility::canUnlock).build();

    private final ChargeComponent chargeComponent = new ChargeComponent(this).addEndEvent(this::onChargeEnd);
    private final GrabEntityComponent grabEntityComponent = new GrabEntityComponent(this, true, true, true, 3).addGrabEvent(this::onGrabStart);
    private final DamageTakenComponent damageTakenComponent = new DamageTakenComponent(this).addOnDamageEvent(this::onDamageTaken);
    private final DealDamageComponent damageComponent = new DealDamageComponent(this);

    private float onDamageTaken(LivingEntity entity, IAbility iAbility, DamageSource source, float amount) {
        if (this.isPaused() || !TrueGomuHelper.hasGearFourthTankmanActive(AbilityDataCapability.get(entity)) || this.chargeComponent.isCharging()) {
            return amount;
        }
        if (!source.isProjectile() || (source.getDirectEntity() instanceof AbilityProjectileEntity && ((AbilityProjectileEntity) source.getDirectEntity()).isPhysical())) {
            if (source.getEntity() instanceof LivingEntity) {
                this.grabEntityComponent.grabManually(entity, (LivingEntity) source.getEntity());
            }
        }
        return amount;
    }

    private boolean onGrabStart(LivingEntity entity, LivingEntity target, IAbility iAbility) {
        this.chargeComponent.startCharging(entity, 60);
        return true;
    }

    private void onChargeEnd(LivingEntity entity, IAbility iAbility) {
        if (this.grabEntityComponent.hasGrabbedEntity()) {
            LivingEntity grabbedEntity = this.grabEntityComponent.getGrabbedEntity();
            this.damageComponent.hurtTarget(entity, grabbedEntity, 30);
            Vector3d position = new Vector3d(entity.position().x, entity.getEyeY(), entity.position().z);
            Vector3d velocity = position.vectorTo(grabbedEntity.position()).normalize().scale(5);
            AbilityHelper.setDeltaMovement(grabbedEntity, velocity.x, 1, velocity.z);
            this.grabEntityComponent.release(entity);
        }
    }

    public GomuGomuNoCannonballAbility(AbilityCore<GomuGomuNoCannonballAbility> core) {
        super(core);
        this.addComponents(chargeComponent, grabEntityComponent, damageTakenComponent, damageComponent);
    }

    private static boolean canUnlock(LivingEntity entity) {
        return TrueGearFourthAbility.canUnlock(entity);
    }
}
