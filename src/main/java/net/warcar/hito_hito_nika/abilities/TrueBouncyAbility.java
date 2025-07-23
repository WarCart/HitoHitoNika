package net.warcar.hito_hito_nika.abilities;

import net.minecraft.block.SlimeBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import xyz.pixelatedw.mineminenomi.abilities.NoFallDamageAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityType;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;

public class TrueBouncyAbility extends NoFallDamageAbility {
    private static final ITextComponent[] DESCRIPTION = TrueGomuHelper.registerDescriptionText("bouncy", new Pair[]{ImmutablePair.of("Makes the user bounce upon landing", (Object)null)});
    public static final AbilityCore<TrueBouncyAbility> INSTANCE;
    private boolean touchedGround = true;
    private double bounceValue = 0;

    public TrueBouncyAbility(AbilityCore<TrueBouncyAbility> ability) {
        super(ability);
        super.damageTakenComponent.addOnAttackEvent(this::onDamageTaken);
        super.addDuringPassiveEvent(this::duringPassiveEvent);
    }

    protected void duringPassiveEvent(LivingEntity entity) {
        if (entity.fallDistance > 12 || !this.touchedGround) {
            this.touchedGround = false;
            if (!entity.isOnGround()) {
                this.bounceValue = entity.getDeltaMovement().y;
            }

            if (entity.isOnGround() && this.bounceValue < 0) {
                this.touchedGround = true;
                Vector3d reverse = new Vector3d(entity.getDeltaMovement().x, -bounceValue / 3, entity.getDeltaMovement().z);
                AbilityHelper.setDeltaMovement(entity, reverse);
            }
        }

    }

    private float onDamageTaken(LivingEntity entity, IAbility ability, DamageSource damageSource, float damage) {
        return damageSource == DamageSource.FLY_INTO_WALL ? 0.0F : damage;
    }

    static {
        INSTANCE = new AbilityCore.Builder<>("Bouncy", AbilityCategory.DEVIL_FRUITS, AbilityType.PASSIVE, TrueBouncyAbility::new).addDescriptionLine(DESCRIPTION).build();
    }
}