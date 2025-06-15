package net.warcar.hito_hito_nika.entities.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.warcar.hito_hito_nika.abilities.TrueGomuPistol;
import xyz.pixelatedw.mineminenomi.api.entities.GoalUtil;
import xyz.pixelatedw.mineminenomi.api.entities.ai.AbilityWrapperGoal;

public class PistolWrapperGoal<E extends MobEntity> extends AbilityWrapperGoal<E, TrueGomuPistol> {
    private LivingEntity target;
    public PistolWrapperGoal(E entity) {
        super(entity, TrueGomuPistol.INSTANCE);
    }

    @Override
    public boolean canUseWrapper() {
        if (!GoalUtil.hasAliveTarget(entity)) {
            return false;
        }
        this.target = this.entity.getTarget();
        return GoalUtil.canSee(entity, target);
    }

    @Override
    public boolean canContinueToUseWrapper() {
        return true;
    }

    @Override
    public void startWrapper() {
        GoalUtil.lookAtEntity(entity, target);
    }

    @Override
    public void tickWrapper() {
        GoalUtil.lookAtEntity(entity, target);
    }

    @Override
    public void stopWrapper() {
        GoalUtil.lookAtEntity(entity, target);
    }
}
