package net.warcar.hito_hito_nika.entities.goals;

import net.warcar.hito_hito_nika.abilities.TrueGearFourthAbility;
import net.warcar.hito_hito_nika.entities.LuffyBoss;

public class GearFourthWrapperGoal extends GearWrapperGoal<TrueGearFourthAbility> {
    private final float snakemanChance;

    public GearFourthWrapperGoal(LuffyBoss entity, float snakemanChance) {
        super(entity, TrueGearFourthAbility.INSTANCE.get());
        this.snakemanChance = snakemanChance;
    }

    @Override
    public void startWrapper() {
        if (entity.getRandom().nextFloat() < snakemanChance) {
            this.getAbility().setSnakeman(entity);
        } else {
            this.getAbility().setBoundman(entity);
        }
    }
}
