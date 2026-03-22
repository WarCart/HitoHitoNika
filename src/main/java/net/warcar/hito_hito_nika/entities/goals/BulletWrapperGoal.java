package net.warcar.hito_hito_nika.entities.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.warcar.hito_hito_nika.abilities.GomuBulletAbility;
import xyz.pixelatedw.mineminenomi.api.entities.ai.AbilityWrapperGoal;
import xyz.pixelatedw.mineminenomi.api.helpers.GoalHelper;

public class BulletWrapperGoal<E extends Mob> extends AbilityWrapperGoal<E, GomuBulletAbility> {
    private LivingEntity target;
    public BulletWrapperGoal(E entity) {
        super(entity, GomuBulletAbility.INSTANCE);
    }

    @Override
    public boolean canUseWrapper() {
        if (!GoalHelper.hasAliveTarget(entity)) {
            return false;
        }
        this.target = this.entity.getTarget();
        return GoalHelper.canSee(entity, target);
    }

    @Override
    public boolean canContinueToUseWrapper() {
        return true;
    }

    @Override
    public void startWrapper() {
        GoalHelper.lookAtEntity(entity, target);
    }

    @Override
    public void tickWrapper() {
        GoalHelper.lookAtEntity(entity, target);
    }

    @Override
    public void stopWrapper() {
        GoalHelper.lookAtEntity(entity, target);
    }
}
