package net.warcar.hito_hito_nika.entities.goals;

import net.warcar.hito_hito_nika.entities.LuffyBoss;
import xyz.pixelatedw.mineminenomi.api.entities.GoalUtil;
import xyz.pixelatedw.mineminenomi.api.entities.ai.TickedGoal;

public class LuffyPhaseSwitcherGoal extends TickedGoal<LuffyBoss> {
    public LuffyPhaseSwitcherGoal(LuffyBoss entity) {
        super(entity);
    }

    @Override
    public boolean canUse() {
        if (!GoalUtil.hasAliveTarget(this.entity) || this.entity.isLastPhase()) {
            return false;
        } else if (this.entity.isFirstPhaseActive() && this.trySwitchToSecondPhase()) {
            this.entity.startSecondPhase();
            return true;
        } else if (this.entity.isSecondPhaseActive() && this.trySwitchToThirdPhase()) {
            this.entity.startThirdPhase();
            return true;
        } else {
            return false;
        }
    }


    private boolean trySwitchToSecondPhase() {
        return !GoalUtil.hasHealthAbovePercentage(this.entity, 50);
    }

    private boolean trySwitchToThirdPhase() {
        return !GoalUtil.hasHealthAbovePercentage(this.entity, 25);
    }
}
