package net.warcar.hito_hito_nika.animations;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import xyz.pixelatedw.mineminenomi.api.animations.Animation;
import xyz.pixelatedw.mineminenomi.api.animations.AnimationId;

public abstract class TimedAnimation<E extends LivingEntity, M extends EntityModel> extends Animation<E, M> {
    protected int maxTicks = 0;

    public <A extends Animation<E, M>> TimedAnimation(AnimationId<A> animId) {
        super(animId);
    }

    @Override
    public void start(LivingEntity entity, int animDuration) {
        super.start(entity, animDuration);
        this.maxTicks = animDuration;
    }

    public int getMaxTicks() {
        return maxTicks;
    }

    public double getPercentage() {
        if (this.getMaxTicks() == 0) {
            return 0;
        }
        return (double) this.getAnimationDuration() / this.getMaxTicks();
    }
}
