package net.warcar.hito_hito_nika.entities.goals;

import net.minecraft.entity.MobEntity;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.entities.GoalUtil;
import xyz.pixelatedw.mineminenomi.api.entities.ai.AbilityWrapperGoal;

public class GearWrapperGoal<E extends MobEntity, G extends Ability> extends AbilityWrapperGoal<E, G> {
    public GearWrapperGoal(E entity, AbilityCore<G> core) {
        super(entity, core);
    }

    @Override
    public boolean canUseWrapper() {
        return GoalUtil.hasAliveTarget(this.entity);
    }

    @Override
    public boolean canContinueToUseWrapper() {
        return true;
    }

    @Override
    public void startWrapper() {
    }

    @Override
    public void tickWrapper() {
    }

    @Override
    public void stopWrapper() {
    }
}
