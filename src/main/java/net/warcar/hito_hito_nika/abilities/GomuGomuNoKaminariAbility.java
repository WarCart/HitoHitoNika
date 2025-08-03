package net.warcar.hito_hito_nika.abilities;

import java.awt.Color;
import javax.annotation.Nullable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.*;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.util.Interval;
import xyz.pixelatedw.mineminenomi.entities.projectiles.goro.LightningEntity;
import xyz.pixelatedw.mineminenomi.init.*;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

public class GomuGomuNoKaminariAbility extends Ability {
    public static final Color BLUE_THUNDER = WyHelper.hexToRGB("#70EAFF22");
    private static final ITextComponent[] DESCRIPTION = TrueGomuHelper.registerDescriptionText("gomu_gomu_no_kaminari", ImmutablePair.of("User grabs a lightning from the sky and throws it at enemy", null), ImmutablePair.of("Requires Gear 5 and Thunderstorm", null));
    private static final int CHARGE_TIME = 80;
    private static final int COOLDOWN = 360;
    public static final AbilityCore<GomuGomuNoKaminariAbility> INSTANCE;
    private final ChargeComponent chargeComponent = (new ChargeComponent(this, (component) -> component.getChargeTime() >= 10.0F)).addStartEvent(this::onChargeStart).addTickEvent(this::onChargeTick).addEndEvent(this::onChargeEnd);
    private final AnimationComponent animationComponent = new AnimationComponent(this);
    private final RequireAbilityComponent abilityComponent = new RequireAbilityComponent(this, new RequireAbilityComponent.CheckData(TrueGearFifthAbility.INSTANCE, RequireAbilityComponent.IS_ACTIVE));
    private final Interval particleInterval = new Interval(2);

    public GomuGomuNoKaminariAbility(AbilityCore<GomuGomuNoKaminariAbility> core) {
        super(core);
        super.isNew = true;
        super.addComponents(this.chargeComponent, this.animationComponent, this.abilityComponent);
        super.addUseEvent(this::onUseEvent);
        this.addCanUseCheck(this::canUseCheck);
    }

    private AbilityUseResult canUseCheck(LivingEntity entity, IAbility iAbility) {
        if (!entity.level.isThundering()) {
            return AbilityUseResult.fail(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_NEED_THUNDERSTORM));
        }
        return AbilityUseResult.success();
    }

    private void onUseEvent(LivingEntity entity, IAbility ability) {
        this.chargeComponent.startCharging(entity, CHARGE_TIME);
    }

    private void onChargeStart(LivingEntity entity, IAbility ability) {
        if (!entity.level.isClientSide) {
            this.particleInterval.restartIntervalToZero();
            this.animationComponent.start(entity, ModAnimations.RAISE_RIGHT_ARM);
        }
    }

    private void onChargeTick(LivingEntity entity, IAbility ability) {
        if (!entity.level.isClientSide) {
            AbilityHelper.slowEntityFall(entity);
            if (this.particleInterval.canTick()) {
                RayTraceResult mop = WyHelper.rayTraceBlocksAndEntities(entity, 256.0F, 0.4F);
                double i = mop.getLocation().x;
                double j = mop.getLocation().y;
                double k = mop.getLocation().z;
                double particleAmount = this.chargeComponent.getChargeTime();

                for(int n = 0; (double)n < particleAmount; ++n) {
                    double offsetX = WyHelper.randomDouble() * (double)n * 0.225;
                    double offsetZ = WyHelper.randomDouble() * (double)n * 0.225;
                    if (entity instanceof PlayerEntity) {
                        WyHelper.spawnParticleEffectForOwner(ModParticleEffects.EL_THOR_AIM.get(), (PlayerEntity)entity, i + offsetX, j, k + offsetZ, null);
                    }
                }

            }
        }
    }

    private void onChargeEnd(LivingEntity entity, IAbility ability) {
        if (!entity.level.isClientSide) {
            RayTraceResult mop = WyHelper.rayTraceBlocksAndEntities(entity);
            double time = this.chargeComponent.getChargePercentage();
            float multi = (float)((double)0.4F + time * (double)0.6F);

            Vector3d mopPos = mop.getLocation();
            BlockRayTraceResult hitResult = entity.level.clip(new RayTraceContext(mopPos, mopPos.add(0.0F, 128.0F, 0.0F), BlockMode.COLLIDER, FluidMode.ANY, entity));
            float targetY = hitResult.getType().equals(Type.BLOCK) ? (float)hitResult.getLocation().y : 128.0F;
            float travelLength = targetY + 16.0F * multi;
            Vector3d pos = new Vector3d(mopPos.x, targetY, mopPos.z);
            LightningEntity boltInner = new LightningEntity(entity, pos.x, pos.y, pos.z, 0.0F, 90.0F, travelLength, 24.0F, this.getCore());
            LightningEntity boltOuter = new LightningEntity(entity, pos.x, pos.y, pos.z, 0.0F, 90.0F, travelLength, 24.0F, this.getCore());
            this.setBoltPropieties(boltInner, 2.0F, 0.0F, 90, 40, false, Color.WHITE, multi);
            this.setBoltPropieties(boltOuter, 2.5F, 50.0F, 100, 9999, true, BLUE_THUNDER, multi);
            boltOuter.seed = boltInner.seed;
            entity.level.addFreshEntity(boltInner);
            entity.level.addFreshEntity(boltOuter);
            entity.level.playSound(null, new BlockPos(mopPos), ModSounds.EL_THOR_SFX.get(), SoundCategory.PLAYERS, 20.0F, 1.0F);
            this.animationComponent.stop(entity);
            super.cooldownComponent.startCooldown(entity, COOLDOWN);
        }
    }

    public void setBoltPropieties(LightningEntity bolt, float size, float damage, int timeAlive, int resetTime, boolean explodes, @Nullable Color color, float multiplier) {
        bolt.setBlocksAffectedLimit(8150);
        bolt.setAngle(160);
        bolt.setBranches(1);
        bolt.setSegments(1);
        bolt.setSize(size * multiplier);
        bolt.setBoxSizeDivision(0.225F);
        bolt.setLightningMovement(false);
        bolt.setExplosion(explodes ? (int)(10.0F * multiplier) : 0, true, 0.25F);
        if (color != null) {
            bolt.setColor(color);
        }

        bolt.setMaxLife(timeAlive);
        bolt.setDamage(damage * multiplier);
        bolt.setTargetTimeToReset(resetTime);
    }

    static {
        INSTANCE = (new AbilityCore.Builder<>("Gomu Gomu no Kaminari", AbilityCategory.DEVIL_FRUITS, GomuGomuNoKaminariAbility::new)).addDescriptionLine(DESCRIPTION).addAdvancedDescriptionLine(AbilityDescriptionLine.NEW_LINE, CooldownComponent.getTooltip(COOLDOWN), ChargeComponent.getTooltip(CHARGE_TIME)).setSourceElement(SourceElement.LIGHTNING).setSourceHakiNature(SourceHakiNature.SPECIAL)
                .setUnlockCheck(TrueGearFifthAbility::canUnlock).build();
    }
}
