package net.warcar.hito_hito_nika.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.GomuEffects;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.*;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.api.util.Result;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.init.ModAbilityPools;

public class GomuGomuNoCymbalAbility extends Ability {
    private static final Component[] DESCRIPTION = TrueGomuHelper.registerDescriptionText("gomu_gomu_no_cymbal", ImmutablePair.of("User grabs enemy, stretches their hands back and then forcefully slams them into opponent flattening them into thin disc", null),
            ImmutablePair.of("Requires Gear 5 and Giant form", null));
    public static final RegistryObject<AbilityCore<GomuGomuNoCymbalAbility>> INSTANCE = TrueGomuGomuNoMi.registerAbility(new AbilityCore.Builder<>("gomu_gomu_no_cymbal", "Gomu Gomu no Cymbal", AbilityCategory.DEVIL_FRUITS, GomuGomuNoCymbalAbility::new)
            .setSourceType(SourceType.FIST).setSourceElement(SourceElement.RUBBER).addDescriptionLine(DESCRIPTION)
            .addAdvancedDescriptionLine(ChargeComponent.getTooltip(30), CooldownComponent.getTooltip(300), DealDamageComponent.getTooltip(15))
            .setSourceHakiNature(SourceHakiNature.HARDENING).setUnlockCheck(TrueGearFifthAbility::canUnlock));

    private final GrabEntityComponent grabComponent = new GrabEntityComponent(this, true, false, 0);
    private final ChargeComponent chargeComponent = new ChargeComponent(this).addTickEvent(this::duringCharging).addEndEvent(this::endCharging);
    private final ContinuousComponent continuousComponent = new ContinuousComponent(this);
    private final DealDamageComponent damageComponent = new DealDamageComponent(this);
    private final HitTriggerComponent hitTriggerComponent = new HitTriggerComponent(this).addTryHitEvent(this::tryHit).addOnHitEvent(this::onHit);
    private final PoolComponent poolComponent = new PoolComponent(this, ModAbilityPools.GRAB_ABILITY);
    public GomuGomuNoCymbalAbility(AbilityCore<GomuGomuNoCymbalAbility> core) {
        super(core);
        this.addComponents(grabComponent, chargeComponent, continuousComponent, damageComponent, hitTriggerComponent, poolComponent);
        this.addUseEvent(this::onUse);
        this.addCanUseCheck(this::canUse);
    }

    private void onUse(LivingEntity entity, IAbility iAbility) {
        if (!this.chargeComponent.isCharging() && !this.isContinuous()) {
            if (grabComponent.grabNearest(entity, false)) {
                this.chargeComponent.startCharging(entity, 30);
            } else {
                this.continuousComponent.startContinuity(entity);
            }
        } else if (this.isContinuous()) {
            this.continuousComponent.stopContinuity(entity);
        }
    }

    private boolean onHit(LivingEntity entity, LivingEntity target, DamageSource modDamageSource, IAbility iAbility) {
        this.continuousComponent.stopContinuity(entity);
        this.grabComponent.grabManually(entity, target);
        this.chargeComponent.startCharging(entity, 30);
        return true;
    }

    private static void applySquished(LivingEntity livingEntity, LivingEntity target) {
        double angle = (Math.atan2(livingEntity.getX() - target.getX(), livingEntity.getZ() - target.getZ()) / Math.PI + 1.5) % 1;
        target.addEffect(new MobEffectInstance(GomuEffects.SQUISHED.get(), 100, (int) (angle * 256), false, false));
    }

    private void endCharging(LivingEntity entity, IAbility ability) {
        if (entity.level().isClientSide) {
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

    private Result canUse(LivingEntity entity, IAbility ability) {
        IAbilityData props = AbilityCapability.get(entity).get();
        if (TrueGomuHelper.hasGigantActive(props)) {
            return Result.success();
        }
        return Result.fail(null);
    }

    private HitTriggerComponent.HitResult tryHit(LivingEntity entity, LivingEntity target, DamageSource source, IAbility iAbility) {
        if (this.isContinuous()) {
            return HitTriggerComponent.HitResult.HIT;
        }
        return HitTriggerComponent.HitResult.PASS;
    }
}
