package net.warcar.hito_hito_nika.abilities;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import xyz.pixelatedw.mineminenomi.ModMain;
import xyz.pixelatedw.mineminenomi.abilities.NoFallDamageAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityType;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;

public class TrueBouncyAbility extends NoFallDamageAbility {
    private static final Component[] DESCRIPTION = TrueGomuHelper.registerDescriptionText("bouncy", ImmutablePair.of("Makes the user bounce upon landing", null));
    public static final AbilityCore<TrueBouncyAbility> INSTANCE = new AbilityCore.Builder<>("bouncy", "Bouncy", AbilityCategory.DEVIL_FRUITS, AbilityType.PASSIVE, TrueBouncyAbility::new)
            .addDescriptionLine(DESCRIPTION).build();
    private boolean touchedGround = true;
    private double bounceValue = 0;

    public TrueBouncyAbility(AbilityCore<TrueBouncyAbility> ability) {
        super(ability);
        this.setDisplayIcon(TrueGomuHelper.getIcon(ModMain.PROJECT_ID, "Bouncy"));
        super.damageTakenComponent.addOnAttackEvent(this::onDamageTaken);
        super.addDuringPassiveEvent(this::duringPassiveEvent);
    }

    protected void duringPassiveEvent(LivingEntity entity) {
        if (entity.fallDistance > 12 || !this.touchedGround) {
            this.touchedGround = false;
            if (entity.isFallFlying()) {
                this.bounceValue = entity.getDeltaMovement().y;
            }

            if (!entity.isFallFlying() && this.bounceValue < 0) {
                this.touchedGround = true;
                Vec3 reverse = new Vec3(entity.getDeltaMovement().x, -bounceValue / 3, entity.getDeltaMovement().z);
                AbilityHelper.setDeltaMovement(entity, reverse);
            }
        }

    }

    private float onDamageTaken(LivingEntity entity, IAbility ability, DamageSource damageSource, float damage) {
        return damageSource.is(DamageTypes.FLY_INTO_WALL) ? 0.0F : damage;
    }

}