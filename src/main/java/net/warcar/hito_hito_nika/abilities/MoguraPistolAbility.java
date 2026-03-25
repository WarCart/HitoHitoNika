package net.warcar.hito_hito_nika.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import net.warcar.hito_hito_nika.projectiles.GomuGomuNoMoguraPistolProjectile;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.*;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityUseConditions;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;

public class MoguraPistolAbility extends Ability {
    private static final Component[] DESCRIPTION = TrueGomuHelper.registerDescriptionText("gomu_gomu_no_mogura_pistol", ImmutablePair.of("User pushes their hand through blocks to attack enemy from below.", null));
    private static final int COOLDOWN = 400;
    private static final int CHARGE_TIME = 10;
    public static final RegistryObject<AbilityCore<MoguraPistolAbility>> INSTANCE = TrueGomuGomuNoMi.registerAbility(new AbilityCore.Builder<>("gomu_gomu_no_mogura_pistol", "Gomu Gomu no Mogura Pistol", AbilityCategory.DEVIL_FRUITS, MoguraPistolAbility::new)
            .addDescriptionLine(DESCRIPTION).addAdvancedDescriptionLine(AbilityDescriptionLine.NEW_LINE, CooldownComponent.getTooltip(COOLDOWN), ChargeComponent.getTooltip(CHARGE_TIME))
            .addAdvancedDescriptionLine(ProjectileComponent.getProjectileTooltips()).setSourceHakiNature(SourceHakiNature.HARDENING)
            .setSourceType(SourceType.FIST).setUnlockCheck(TrueGearFifthAbility::canUnlock)
            );
    private final ChargeComponent chargeComponent = (new ChargeComponent(this)).addTickEvent(this::duringChargeEvent).addEndEvent(this::endChargeEvent);
    private final ProjectileComponent projectileComponent = new ProjectileComponent(this, this::createProjectile);
    private Vec3 targetPos;

    public MoguraPistolAbility(AbilityCore<MoguraPistolAbility> core) {
        super(core);
        this.addComponents(this.projectileComponent, this.chargeComponent);
        this.addCanUseCheck(AbilityUseConditions::requiresOnGround);
        this.addUseEvent(this::useEvent);
    }

    private void useEvent(LivingEntity entity, IAbility ability) {
        this.chargeComponent.startCharging(entity, 10.0F);
    }

    private void duringChargeEvent(LivingEntity entity, IAbility ability) {
        if (this.targetPos == null) {
            HitResult mop = WyHelper.rayTraceBlocksAndEntities(entity, 64.0F);
            double i = mop.getLocation().x;
            double k = mop.getLocation().z;
            int y = entity.level().getHeight(Heightmap.Types.MOTION_BLOCKING, (int)i, (int)k) - 1;
            this.setTargetPos(new Vec3(i, y, k));
        }

        if (!entity.level().isClientSide) {
            WyHelper.spawnParticleEffect(ModParticleEffects.SAND_BLADE_IDLE.get(), entity, this.targetPos.x, this.targetPos.y, this.targetPos.z);
        }

        entity.addEffect(new MobEffectInstance(ModEffects.MOVEMENT_BLOCKED.get(), 2, 1, false, false));
    }

    private void endChargeEvent(LivingEntity entity, IAbility ability) {
        if (!entity.level().isClientSide) {
            GomuGomuNoMoguraPistolProjectile pillar = this.projectileComponent.getNewProjectile(entity);
            pillar.moveTo(this.targetPos.x, this.targetPos.y, this.targetPos.z, 0.0F, 0.0F);
            pillar.shoot(0.0F, 0.7, 0.0F, 1.4F, 0.0F);
            entity.level().addFreshEntity(pillar);
        }

        this.cooldownComponent.startCooldown(entity, COOLDOWN);
        this.targetPos = null;
    }

    private GomuGomuNoMoguraPistolProjectile createProjectile(LivingEntity entity) {
        GomuGomuNoMoguraPistolProjectile projectile = new GomuGomuNoMoguraPistolProjectile(entity.level(), entity);
        projectile.setTargetPos(this.targetPos);
        return projectile;
    }

    public void setTargetPos(Vec3 vec) {
        this.targetPos = vec;
    }

}
