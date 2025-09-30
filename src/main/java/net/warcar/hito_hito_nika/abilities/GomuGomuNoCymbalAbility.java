package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.warcar.hito_hito_nika.init.GomuEffects;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.*;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.init.ModAbilityPools;
import xyz.pixelatedw.mineminenomi.init.ModDamageSource;

public class GomuGomuNoCymbalAbility extends Ability {
    public static final AbilityCore<GomuGomuNoCymbalAbility> INSTANCE = new AbilityCore.Builder<>("Gomu Gomu no Cymbal", AbilityCategory.DEVIL_FRUITS, GomuGomuNoCymbalAbility::new)
            .setSourceType(SourceType.FIST).setSourceElement(SourceElement.RUBBER).setSourceHakiNature(SourceHakiNature.HARDENING).build();

    private final GrabEntityComponent grabComponent = new GrabEntityComponent(this, true, false, 0);
    private final ChargeComponent chargeComponent = new ChargeComponent(this).addTickEvent(this::duringCharging).addEndEvent(this::endCharging);
    private final ContinuousComponent continuousComponent = new ContinuousComponent(this);
    private final DealDamageComponent damageComponent = new DealDamageComponent(this);
    private final HitTriggerComponent hitTriggerComponent = new HitTriggerComponent(this).addOnHitEvent(this::onHit);
    private final PoolComponent poolComponent = new PoolComponent(this, ModAbilityPools.GRAB_ABILITY);
    public GomuGomuNoCymbalAbility(AbilityCore<GomuGomuNoCymbalAbility> core) {
        super(core);
        this.isNew = true;
        this.addComponents(grabComponent, chargeComponent, continuousComponent, damageComponent, hitTriggerComponent, poolComponent);
        this.addUseEvent(this::onUse);
    }

    private void onUse(LivingEntity entity, IAbility iAbility) {
        if (!this.chargeComponent.isCharging() && !this.isContinuous()) {
            if (grabComponent.grabNearest(entity)) {
                this.chargeComponent.startCharging(entity, 30);
            } else {
                this.continuousComponent.startContinuity(entity);
            }
        } else if (this.isContinuous()) {
            this.continuousComponent.stopContinuity(entity);
        }
    }

    private boolean onHit(LivingEntity entity, LivingEntity target, ModDamageSource modDamageSource, IAbility iAbility) {
        if (this.continuousComponent.isContinuous()) {
            this.continuousComponent.stopContinuity(entity);
            this.grabComponent.grabManually(entity, target);
            this.chargeComponent.startCharging(entity, 30);
            return true;
        }
        return false;
    }

    private static void applySquished(LivingEntity livingEntity, LivingEntity target) {
        double angle = (Math.atan2(livingEntity.getX() - target.getX(), livingEntity.getZ() - target.getZ()) / Math.PI + 1.5) % 1;
        target.addEffect(new EffectInstance(GomuEffects.SQUISHED.get(), 100, (int) (angle * 256), false, false));
    }

    private void endCharging(LivingEntity entity, IAbility ability) {
        if (entity.level.isClientSide) {
            return;
        }
        if (this.grabComponent.hasGrabbedEntity()) {
            LivingEntity target = this.grabComponent.getGrabbedEntity();
            applySquished(entity, target);
            this.damageComponent.hurtTarget(entity, target, 15);
            this.grabComponent.release(entity);
            this.cooldownComponent.startCooldown(entity, 300);
        } else {
            this.cooldownComponent.startCooldown(entity, 30);
        }
    }

    private void duringCharging(LivingEntity entity, IAbility ability) {
        LivingEntity target = this.grabComponent.getGrabbedEntity();
        if (target == null) {
            return;
        }
        float t = this.chargeComponent.getChargeTime();
        double rot = entity.yHeadRot * Math.PI / 180;
        if (t < 25) {
            target.moveTo(entity.getX() - Math.sin(Math.PI * t / 25 + rot) * 5, entity.getY(), entity.getZ() + Math.cos(Math.PI * t / 25 + rot) * 5);
        } else {
            target.moveTo(entity.getX() - Math.sin(Math.PI * (30 - t) / 5 + rot) * 5, entity.getY(), entity.getZ() + Math.cos(Math.PI * (30 - t) / 5 + rot) * 5);
        }
    }
}
