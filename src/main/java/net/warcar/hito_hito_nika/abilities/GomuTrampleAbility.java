package net.warcar.hito_hito_nika.abilities;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.DealDamageComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.RangeComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.RequireMorphComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.RangeComponent.RangeType;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.MorphHelper;
import xyz.pixelatedw.mineminenomi.api.protection.block.FoliageBlockProtectionRule;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.init.ModMorphs;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;
import xyz.pixelatedw.mineminenomi.particles.effects.BreakingBlocksParticleEffect;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

public class GomuTrampleAbility extends PassiveAbility2 {
    private static final ITextComponent[] DESCRIPTION = AbilityHelper.registerDescriptionText("mineminenomi", "deka_trample", ImmutablePair.of("Running speed increases with acceleration trampling any nearby entity.", null));
    public static final AbilityCore<GomuTrampleAbility> INSTANCE;
    private final RangeComponent rangeComponent = new RangeComponent(this);
    private final DealDamageComponent dealDamageComponent = new DealDamageComponent(this);
    private final BreakingBlocksParticleEffect.Details details;
    public float speed;

    public GomuTrampleAbility(AbilityCore<GomuTrampleAbility> ability) {
        super(ability);
        this.details = new BreakingBlocksParticleEffect.Details(100);
        this.speed = 0.0F;
        this.addComponents(this.rangeComponent, this.dealDamageComponent);
        this.addDuringPassiveEvent(this::duringPassiveEvent);
        this.addCanUseCheck(this::canUse);
    }

    public void duringPassiveEvent(LivingEntity entity) {
        if (entity.isOnGround()) {
            if (!entity.isSprinting()) {
                this.speed = 0.0F;
            } else {
                List<LivingEntity> targets = this.rangeComponent.getTargetsInArea(entity, entity.blockPosition(), 5.0F);
                float acceleration = 0.004F;
                acceleration *= this.speed > 0.0F ? 1.0F - this.speed / 0.45F : 1.0F;
                if (!(entity.zza > 0.0F) || entity.horizontalCollision) {
                    acceleration = -0.044999998F;
                }

                this.speed = MathHelper.clamp(this.speed + acceleration, acceleration > 0.0F ? 0.022499999F : 0.0F, 0.45F);
                int d2 = entity.zza > 0.0F ? 1 : 0;
                Vector3d vec = entity.getLookAngle();
                double x = vec.x * (double) this.speed * (double) d2;
                double z = vec.z * (double) this.speed * (double) d2;
                AbilityHelper.setDeltaMovement(entity, x, entity.getDeltaMovement().y, z);
                if (!entity.level.isClientSide) {
                    List<BlockPos> blocks = WyHelper.getNearbyBlocks(entity.blockPosition(), entity.level, 7, 7, 7, (state) -> !state.getMaterial().equals(Material.AIR) && FoliageBlockProtectionRule.INSTANCE.isApproved(state));
                    List<BlockPos> positions = new ArrayList<>();

                    for (BlockPos pos : blocks) {
                        if (AbilityHelper.placeBlockIfAllowed(entity, pos, Blocks.AIR.defaultBlockState(), FoliageBlockProtectionRule.INSTANCE)) {
                            positions.add(pos);
                        }
                    }

                    if (!positions.isEmpty()) {
                        this.details.setPositions(positions);
                        WyHelper.spawnParticleEffect(ModParticleEffects.BREAKING_BLOCKS.get(), entity, 0.0F, 0.0F, 0.0F, this.details);
                    }

                    for (LivingEntity target : targets) {
                        if (this.dealDamageComponent.hurtTarget(entity, target, 8.0F)) {
                            Vector3d speed = WyHelper.propulsion(entity, 2.0F, 2.0F);
                            AbilityHelper.setDeltaMovement(target, speed.x, 0.2, speed.z);
                        }
                    }
                }

            }
        }
    }

    static {
        INSTANCE = new AbilityCore.Builder<>("Gomu Trample", AbilityCategory.DEVIL_FRUITS, AbilityType.PASSIVE, GomuTrampleAbility::new).addDescriptionLine(DESCRIPTION).addDescriptionLine(AbilityDescriptionLine.NEW_LINE, RequireMorphComponent.getTooltip()).addAdvancedDescriptionLine(AbilityDescriptionLine.NEW_LINE, RangeComponent.getTooltip(5.0F, RangeType.AOE), DealDamageComponent.getTooltip(8.0F)).setSourceHakiNature(SourceHakiNature.HARDENING).setSourceType(SourceType.FIST)
                .setHidden().build();
    }

    private AbilityUseResult canUse(LivingEntity entity, IAbility iAbility) {
        if (TrueGomuHelper.hasGigantActive(AbilityDataCapability.get(entity))) {
            return AbilityUseResult.success();
        }
        return AbilityUseResult.fail(null);
    }
}