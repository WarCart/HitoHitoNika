package net.warcar.hito_hito_nika.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityDescriptionLine;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.RangeComponent.RangeType;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.util.Result;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.stats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.init.ModAnimations;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;

import java.util.UUID;

public class GomuUfoAbility extends Ability {
    private static final Component[] DESCRIPTION = TrueGomuHelper.registerDescriptionText("gomu_gomu_no_ufo", ImmutablePair.of("Launches the user forward hitting everybody in their path.", null));
    private static final int COOLDOWN = 300;
    private static final int HOLD_TIME = 100;
    private static final float RANGE = 2.5F;
    private static final int DAMAGE = 5;
    private static final int G2_DAMAGE = 10;
    private static final int G5_DAMAGE = 20;
    public static final Component UFO = TrueGomuHelper.getName("Gomu Gomu no Ufo");
    public static final Component JET_UFO = TrueGomuHelper.getName("Gomu Gomu no Jet Ufo");
    public static final Component DAWN_WHIP = TrueGomuHelper.getName("Gomu Gomu no Dawn Whip");
    public static final RegistryObject<AbilityCore<GomuUfoAbility>> INSTANCE = TrueGomuGomuNoMi.registerAbility(new AbilityCore.Builder<>("gomu_gomu_no_ufo", "Gomu Gomu no UFO", AbilityCategory.DEVIL_FRUITS, GomuUfoAbility::new)
            .addDescriptionLine(DESCRIPTION).addAdvancedDescriptionLine(AbilityDescriptionLine.NEW_LINE, CooldownComponent.getTooltip(COOLDOWN), ContinuousComponent.getTooltip(HOLD_TIME), RangeComponent.getTooltip(RANGE, RangeType.AOE), DealDamageComponent.getTooltip(DAMAGE, G5_DAMAGE))
            .setSourceHakiNature(SourceHakiNature.HARDENING).setSourceType(SourceType.PHYSICAL).setUnlockCheck(GomuUfoAbility::canUnlock));
    private final ContinuousComponent continuousComponent = (new ContinuousComponent(this)).addStartEvent(this::startContinuityEvent).addTickEvent(this::duringContinuityEvent).addEndEvent(this::endContinuityEvent);
    private final HitTrackerComponent hitTrackerComponent = new HitTrackerComponent(this);
    private final AnimationComponent animationComponent = new AnimationComponent(this);
    private final RangeComponent rangeComponent = new RangeComponent(this);
    private final DealDamageComponent dealDamageComponent = new DealDamageComponent(this);
    private final ChangeStatsComponent statsComponent = new ChangeStatsComponent(this).addAttributeModifier(ModAttributes.JUMP_HEIGHT, new AttributeModifier(UUID.fromString("a18d1c38-16f4-4ce0-8801-cf90538aaa97"), "Gomu Ufo jump boost", 3, AttributeModifier.Operation.ADDITION),
            entity -> !TrueGomuHelper.hasGearSecondActive(AbilityCapability.get(entity).get()) && this.continuousComponent.isContinuous());

    public GomuUfoAbility(AbilityCore<GomuUfoAbility> core) {
        super(core);
        this.addComponents(this.continuousComponent, this.hitTrackerComponent, this.animationComponent, this.rangeComponent, this.dealDamageComponent, this.statsComponent);
        this.addUseEvent(this::useEvent);
        this.addTickEvent(this::onTick);
        this.addCanUseCheck(this::canUse);
    }

    private void onTick(LivingEntity entity, IAbility ability) {
        IAbilityData props = AbilityCapability.get(entity).get();
        if (TrueGomuHelper.hasGearSecondActive(props)) {
            this.setDisplayName(JET_UFO);
            this.setDisplayIcon(TrueGomuHelper.getIcon("Gomu Gomu no Ufo"));
        } else if (TrueGomuHelper.hasGearFifthActive(props)) {
            this.setDisplayName(DAWN_WHIP);
            this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Gomu Gomu no Dawn Whip"));
        } else {
            this.setDisplayName(UFO);
            this.setDisplayIcon(TrueGomuHelper.getIcon("Gomu Gomu no Ufo"));
        }
    }

    private void useEvent(LivingEntity entity, IAbility ability) {
        this.continuousComponent.triggerContinuity(entity, HOLD_TIME);
    }

    private void startContinuityEvent(LivingEntity entity, IAbility ability) {
        this.hitTrackerComponent.clearHits();
        this.animationComponent.start(entity, ModAnimations.GOMU_DAWN_WHIP);
    }

    private void duringContinuityEvent(LivingEntity entity, IAbility ability) {
        if (this.continuousComponent.getContinueTime() % 20.0F == 0.0F) {
            this.hitTrackerComponent.clearHits();
        }
        IAbilityData props = AbilityCapability.get(entity).get();

        double speedMultiplier;
        if (TrueGomuHelper.hasGearFifthActive(props)) {
            speedMultiplier = 1.35;
        } else if (TrueGomuHelper.hasGearSecondActive(props)) {
            speedMultiplier = 2;
        } else {
            speedMultiplier = 0.75;
        }
        Vec3 speed = entity.getLookAngle().multiply(speedMultiplier, speedMultiplier, speedMultiplier);
        double yComponent;
        if (TrueGomuHelper.hasGearSecondActive(props)) {
            yComponent = (entity.getDeltaMovement().y + speed.y) / 2;
        } else {
            yComponent = entity.getDeltaMovement().y;
        }
        AbilityHelper.setDeltaMovement(entity, speed.x, yComponent, speed.z);

        for(LivingEntity target : this.rangeComponent.getTargetsInArea(entity, RANGE)) {
            if (this.hitTrackerComponent.canHit(target)) {
                if (TrueGomuHelper.hasGearFifthActive(props)) {
                    this.dealDamageComponent.hurtTarget(entity, target, G5_DAMAGE);
                } else if (TrueGomuHelper.hasGearSecondActive(props)) {
                    this.dealDamageComponent.hurtTarget(entity, target, G2_DAMAGE);
                } else {
                    this.dealDamageComponent.hurtTarget(entity, target, DAMAGE);
                }
            }
        }

    }

    private void endContinuityEvent(LivingEntity entity, IAbility ability) {
        this.animationComponent.stop(entity);
        this.cooldownComponent.startCooldown(entity, COOLDOWN);
    }

    private static boolean canUnlock(LivingEntity user) {
        return EntityStatsCapability.get(user).get().getDoriki() > 3500 && DevilFruitCapability.get(user).get().hasDevilFruit(TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA);
    }

    private Result canUse(LivingEntity entity, IAbility iAbility) {
        IAbilityData props = AbilityCapability.get(entity).get();
        if (TrueGomuHelper.hasGearFourthActive(props) || TrueGomuHelper.hasGearThirdActive(props)) {
            return Result.fail(TrueGomuHelper.TOO_HEAVY);
        }
        return Result.success();
    }

}