package net.warcar.hito_hito_nika.abilities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.AnimationComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChargeComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.CooldownComponent;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityUseConditions;
import xyz.pixelatedw.mineminenomi.api.util.Interval;
import xyz.pixelatedw.mineminenomi.api.util.Result;
import xyz.pixelatedw.mineminenomi.entities.projectiles.abilities.goro.ElThorProjectile;
import xyz.pixelatedw.mineminenomi.init.ModAnimations;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.init.i18n.ModI18nAbilities;

import java.awt.*;

public class GomuGomuNoKaminariAbility extends Ability {
    public static final Color BLUE_THUNDER = WyHelper.hexToRGB("#70EAFF22");
    private static final Component[] DESCRIPTION = TrueGomuHelper.registerDescriptionText("gomu_gomu_no_kaminari", ImmutablePair.of("User grabs a lightning from the sky and throws it at enemy", null), ImmutablePair.of("Requires Gear 5 and Thunderstorm", null));
    private static final int CHARGE_TIME = 80;
    private static final int COOLDOWN = 360;
    public static final RegistryObject<AbilityCore<GomuGomuNoKaminariAbility>> INSTANCE = TrueGomuGomuNoMi.registerAbility(new AbilityCore.Builder<>("gomu_gomu_no_kaminari", "Gomu Gomu no Kaminari", AbilityCategory.DEVIL_FRUITS, GomuGomuNoKaminariAbility::new)
            .addDescriptionLine(DESCRIPTION).addAdvancedDescriptionLine(AbilityDescriptionLine.NEW_LINE, CooldownComponent.getTooltip(COOLDOWN), ChargeComponent.getTooltip(CHARGE_TIME))
            .setSourceElement(SourceElement.LIGHTNING).setSourceHakiNature(SourceHakiNature.SPECIAL).setUnlockCheck(TrueGearFifthAbility::canUnlock)
            );
    private final ChargeComponent chargeComponent = (new ChargeComponent(this, (component) -> component.getChargeTime() >= 10.0F)).addStartEvent(this::onChargeStart).addTickEvent(this::onChargeTick).addEndEvent(this::onChargeEnd);
    private final AnimationComponent animationComponent = new AnimationComponent(this);
    private final Interval particleInterval = new Interval(2);

    public GomuGomuNoKaminariAbility(AbilityCore<GomuGomuNoKaminariAbility> core) {
        super(core);
        super.addComponents(this.chargeComponent, this.animationComponent);
        this.addCanUseCheck((e, a) -> AbilityUseConditions.requiresActiveAbility(e,a, TrueGearFifthAbility.INSTANCE.get()));
        this.addUseEvent(this::onUseEvent);
        this.addCanUseCheck(this::canUseCheck);
    }

    private Result canUseCheck(LivingEntity entity, IAbility iAbility) {
        if (!entity.level().isThundering()) {
            return Result.fail(ModI18nAbilities.MESSAGE_NEED_THUNDERSTORM);
        }
        return Result.success();
    }

    private void onUseEvent(LivingEntity entity, IAbility ability) {
        this.chargeComponent.startCharging(entity, CHARGE_TIME);
    }

    private void onChargeStart(LivingEntity entity, IAbility ability) {
        if (!entity.level().isClientSide) {
            this.particleInterval.restartIntervalToZero();
            this.animationComponent.start(entity, ModAnimations.RAISE_RIGHT_ARM);
        }
    }

    private void onChargeTick(LivingEntity entity, IAbility ability) {
        if (!entity.level().isClientSide) {
            AbilityHelper.slowEntityFall(entity);
            if (this.particleInterval.canTick()) {
                HitResult mop = WyHelper.rayTraceBlocksAndEntities(entity, 256.0F, 0.4F);
                double i = mop.getLocation().x;
                double j = mop.getLocation().y;
                double k = mop.getLocation().z;
                double particleAmount = this.chargeComponent.getChargeTime();

                for(int n = 0; (double)n < particleAmount; ++n) {
                    double offsetX = WyHelper.randomDouble() * (double)n * 0.225;
                    double offsetZ = WyHelper.randomDouble() * (double)n * 0.225;
                    if (entity instanceof Player) {
                        WyHelper.spawnParticleEffectForOwner(ModParticleEffects.EL_THOR_AIM.get(), (Player)entity, i + offsetX, j, k + offsetZ, null);
                    }
                }

            }
        }
    }

    private void onChargeEnd(LivingEntity entity, IAbility ability) {
        if (!entity.level().isClientSide) {
            HitResult mop = WyHelper.rayTraceBlocksAndEntities(entity);
            double time = this.chargeComponent.getChargePercentage();
            float multi = (float)((double)0.4F + time * (double)0.6F);

            Vec3 mopPos = mop.getLocation();
            BlockHitResult hitResult = entity.level().clip(new ClipContext(mopPos, mopPos.add(0.0F, 128.0F, 0.0F), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, entity));
            int targetY = hitResult.getType().equals(HitResult.Type.BLOCK) ? (int) hitResult.getLocation().y : AbilityHelper.CLOUD_HEIGHT;
            float travelLength = targetY + 16.0F * multi;
            Vec3 pos = new Vec3(mopPos.x, targetY, mopPos.z);
            ElThorProjectile lightning = new ElThorProjectile(entity.level(), entity, pos.x, pos.y, pos.z, targetY, travelLength, 1, this);
            entity.level().addFreshEntity(lightning);
            entity.level().playSound(null, new BlockPos((int) mopPos.x, (int) mopPos.y, (int) mopPos.z), ModSounds.EL_THOR_SFX.get(), SoundSource.PLAYERS, 20.0F, 1.0F);
            this.animationComponent.stop(entity);
            super.cooldownComponent.startCooldown(entity, COOLDOWN);
        }
    }
}
