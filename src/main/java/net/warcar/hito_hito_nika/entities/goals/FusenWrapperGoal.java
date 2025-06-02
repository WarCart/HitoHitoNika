package net.warcar.hito_hito_nika.entities.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.fluid.FluidState;
import net.warcar.hito_hito_nika.abilities.GomuFusenAbility;
import xyz.pixelatedw.mineminenomi.abilities.rokushiki.TekkaiAbility;
import xyz.pixelatedw.mineminenomi.api.entities.GoalUtil;
import xyz.pixelatedw.mineminenomi.api.entities.ai.AbilityWrapperGoal;

public class FusenWrapperGoal extends AbilityWrapperGoal<MobEntity, GomuFusenAbility> {
    private LivingEntity target;
    private double distance = (double)10.0F;
    private int lastHurtTicks = 60;
    private int hits = 0;
    private long lastHitTimestamp;

    public FusenWrapperGoal(MobEntity entity) {
        super(entity, GomuFusenAbility.INSTANCE);
    }

    public FusenWrapperGoal setMinDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public boolean canUseWrapper() {
        if (!GoalUtil.hasAliveTarget(this.entity)) {
            return false;
        } else {
            this.target = this.entity.getTarget();
            if (!GoalUtil.isWithinDistance(this.entity, this.target, this.distance)) {
                return false;
            } else if (this.hits < 3) {
                if (this.lastHitTimestamp == 0L) {
                    this.lastHitTimestamp = this.entity.level.getGameTime();
                }

                if (GoalUtil.lastHurtCheckBefore(this.entity, this.lastHurtTicks) && this.entity.level.getGameTime() > this.lastHitTimestamp + (long)this.lastHurtTicks) {
                    ++this.hits;
                    this.lastHitTimestamp = this.entity.level.getGameTime();
                }

                return false;
            } else {
                return true;
            }
        }
    }

    public boolean canContinueToUseWrapper() {
        if (!GoalUtil.isWithinDistance(this.entity, this.target, this.distance * (double)1.5F)) {
            return false;
        } else {
            return !GoalUtil.lastHurtCheckAfter(this.entity, (int)((double)this.lastHurtTicks / (double)1.5F));
        }
    }

    public void startWrapper() {
        this.hits = 0;
    }

    public void tickWrapper() {
        GoalUtil.lookAtEntity(this.entity, this.target);
    }

    public void stopWrapper() {
        this.hits = 0;
    }
}
