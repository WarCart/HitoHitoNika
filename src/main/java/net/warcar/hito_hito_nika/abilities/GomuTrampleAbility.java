package net.warcar.hito_hito_nika.abilities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import net.warcar.hito_hito_nika.init.TrueMorphs;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.api.NuWorld;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.DealDamageComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.RangeComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.RangeComponent.RangeType;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityTooltipsHelper;
import xyz.pixelatedw.mineminenomi.api.protection.DefaultProtectionRules;
import xyz.pixelatedw.mineminenomi.api.util.Result;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;
import xyz.pixelatedw.mineminenomi.init.ModTags;
import xyz.pixelatedw.mineminenomi.particles.effects.BreakingBlocksParticleEffect;

import java.util.ArrayList;
import java.util.List;

public class GomuTrampleAbility extends PassiveAbility {
    private static final Component[] DESCRIPTION = AbilityHelper.registerDescriptionText(HitoHitoNoMiNikaMod.MOD_ID, "gomu_trample", ImmutablePair.of("Running speed increases with acceleration trampling any nearby entity.", null));
    public static final RegistryObject<AbilityCore<GomuTrampleAbility>> INSTANCE = TrueGomuGomuNoMi.registerAbility(new AbilityCore.Builder<>("gomu_trample", "Gomu Trample", AbilityCategory.DEVIL_FRUITS, AbilityType.PASSIVE, GomuTrampleAbility::new)
            .addDescriptionLine(DESCRIPTION).addDescriptionLine(AbilityDescriptionLine.NEW_LINE, AbilityTooltipsHelper.getRequiredMorphTooltip(TrueMorphs.GIANT))
            .addAdvancedDescriptionLine(AbilityDescriptionLine.NEW_LINE, RangeComponent.getTooltip(5.0F, RangeType.AOE), DealDamageComponent.getTooltip(8.0F))
            .setSourceHakiNature(SourceHakiNature.HARDENING).setSourceType(SourceType.FIST).setUnlockCheck(TrueGearFifthAbility::canUnlock));
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
        if (entity.onGround()) {
            if (!entity.isSprinting()) {
                this.speed = 0.0F;
            } else {
                List<LivingEntity> targets = this.rangeComponent.getTargetsInArea(entity, entity.blockPosition(), 5.0F);
                float acceleration = 0.004F;
                acceleration *= this.speed > 0.0F ? 1.0F - this.speed / 0.45F : 1.0F;
                if (!(entity.zza > 0.0F) || entity.horizontalCollision) {
                    acceleration = -0.044999998F;
                }

                this.speed = clamp(this.speed + acceleration, acceleration > 0.0F ? 0.022499999F : 0.0F, 0.45F);
                int d2 = entity.zza > 0.0F ? 1 : 0;
                Vec3 vec = entity.getLookAngle();
                double x = vec.x * (double) this.speed * (double) d2;
                double z = vec.z * (double) this.speed * (double) d2;
                AbilityHelper.setDeltaMovement(entity, x, entity.getDeltaMovement().y, z);
                if (!entity.level().isClientSide) {
                    List<BlockPos> blocks = WyHelper.getNearbyBlocks(entity.blockPosition(), entity.level(), 7, 7, 7, (state) -> !state.isAir() && state.is(ModTags.Blocks.BLOCK_PROT_FOLIAGE));
                    List<BlockPos> positions = new ArrayList<>();

                    for (BlockPos pos : blocks) {
                        if (NuWorld.setBlockState(entity, pos, Blocks.AIR.defaultBlockState(), 3, DefaultProtectionRules.FOLIAGE)) {
                            positions.add(pos);
                        }
                    }

                    if (!positions.isEmpty()) {
                        this.details.setPositions(positions);
                        WyHelper.spawnParticleEffect(ModParticleEffects.BREAKING_BLOCKS.get(), entity, 0.0F, 0.0F, 0.0F, this.details);
                    }

                    for (LivingEntity target : targets) {
                        if (this.dealDamageComponent.hurtTarget(entity, target, 8.0F)) {
                            Vec3 speed = entity.getLookAngle().scale(2);
                            AbilityHelper.setDeltaMovement(target, speed.x, 0.2, speed.z);
                        }
                    }
                }

            }
        }
    }

    private float clamp(float v, float v1, float v2) {
        if (v > v2) {
            return v2;
        }
        return Math.max(v, v1);
    }

    private Result canUse(LivingEntity entity, IAbility iAbility) {
        if (TrueGomuHelper.hasGigantActive(AbilityCapability.get(entity).get())) {
            return Result.success();
        }
        return Result.fail(null);
    }
}