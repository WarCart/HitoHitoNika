package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.gen.Heightmap.Type;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.projectiles.GomuGomuNoMoguraPistolProjectile;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.*;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesource.SourceType;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.ModParticleEffects;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

public class MoguraPistolAbility extends Ability {
    private static final ITextComponent[] DESCRIPTION = TrueGomuHelper.registerDescriptionText("gomu_gomu_no_mogura_pistol", ImmutablePair.of("User pushes their hand through blocks to attack enemy from below.", null));
    private static final int COOLDOWN = 400;
    private static final int CHARGE_TIME = 10;
    public static final AbilityCore<MoguraPistolAbility> INSTANCE;
    private final ChargeComponent chargeComponent = (new ChargeComponent(this)).addTickEvent(this::duringChargeEvent).addEndEvent(this::endChargeEvent);
    private final ProjectileComponent projectileComponent = new ProjectileComponent(this, this::createProjectile);
    private final RequireAbilityComponent requireAbilityComponent = new RequireAbilityComponent(this, new RequireAbilityComponent.CheckData(TrueGearFifthAbility.INSTANCE, RequireAbilityComponent.IS_ACTIVE));
    private Vector3d targetPos;

    public MoguraPistolAbility(AbilityCore<MoguraPistolAbility> core) {
        super(core);
        super.isNew = true;
        this.addComponents(this.projectileComponent, this.chargeComponent, this.requireAbilityComponent);
        this.addCanUseCheck(AbilityHelper::requiresOnGround);
        this.addUseEvent(this::useEvent);
    }

    private void useEvent(LivingEntity entity, IAbility ability) {
        this.chargeComponent.startCharging(entity, 10.0F);
    }

    private void duringChargeEvent(LivingEntity entity, IAbility ability) {
        if (this.targetPos == null) {
            RayTraceResult mop = WyHelper.rayTraceBlocksAndEntities(entity, 64.0F);
            double i = mop.getLocation().x;
            double k = mop.getLocation().z;
            int y = entity.level.getHeight(Type.MOTION_BLOCKING, (int)i, (int)k) - 1;
            this.setTargetPos(new Vector3d(i, y, k));
        }

        if (!entity.level.isClientSide) {
            WyHelper.spawnParticleEffect(ModParticleEffects.SAND_BLADE_IDLE.get(), entity, this.targetPos.x, this.targetPos.y, this.targetPos.z);
        }

        entity.addEffect(new EffectInstance(ModEffects.MOVEMENT_BLOCKED.get(), 2, 1, false, false));
    }

    private void endChargeEvent(LivingEntity entity, IAbility ability) {
        if (!entity.level.isClientSide) {
            GomuGomuNoMoguraPistolProjectile pillar = this.projectileComponent.getNewProjectile(entity);
            pillar.moveTo(this.targetPos.x, this.targetPos.y, this.targetPos.z, 0.0F, 0.0F);
            pillar.shoot(0.0F, 0.7, 0.0F, 1.4F, 0.0F);
            entity.level.addFreshEntity(pillar);
        }

        this.cooldownComponent.startCooldown(entity, COOLDOWN);
        this.targetPos = null;
    }

    private AbilityProjectileEntity createProjectile(LivingEntity entity) {
        GomuGomuNoMoguraPistolProjectile projectile = new GomuGomuNoMoguraPistolProjectile(entity.level, entity);
        projectile.setTargetPos(this.targetPos);
        return projectile;
    }

    public void setTargetPos(Vector3d vec) {
        this.targetPos = vec;
    }

    static {
        INSTANCE = new AbilityCore.Builder<>("Gomu Gomu no Mogura Pistol", AbilityCategory.DEVIL_FRUITS, MoguraPistolAbility::new)
                .addDescriptionLine(DESCRIPTION).addAdvancedDescriptionLine(AbilityDescriptionLine.NEW_LINE, CooldownComponent.getTooltip(COOLDOWN), ChargeComponent.getTooltip(CHARGE_TIME))
                .addAdvancedDescriptionLine(ProjectileComponent.getProjectileTooltips()).setSourceHakiNature(SourceHakiNature.HARDENING)
                .setSourceType(SourceType.FIST).build();
    }
}
