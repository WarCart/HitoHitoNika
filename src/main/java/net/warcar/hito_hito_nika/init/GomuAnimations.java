package net.warcar.hito_hito_nika.init;

import net.minecraft.util.ResourceLocation;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.animations.TrueGomuBazookaAnimation;
import net.warcar.hito_hito_nika.animations.TrueGomuBulletAnimation;
import xyz.pixelatedw.mineminenomi.api.animations.Animation;
import xyz.pixelatedw.mineminenomi.api.animations.AnimationId;

public class GomuAnimations {
    public static final AnimationId<TrueGomuBazookaAnimation> BAZOOKA = register("gomu_bazooka");
    public static final AnimationId<TrueGomuBulletAnimation> BULLET = register("gomu_bullet");

    public static void clientInit() {
        AnimationId.register(new TrueGomuBazookaAnimation(BAZOOKA));
        AnimationId.register(new TrueGomuBulletAnimation(BULLET));
    }

    private static <A extends Animation<?, ?>> AnimationId<A> register(String name) {
        return new AnimationId<>(new ResourceLocation(HitoHitoNoMiNikaMod.MOD_ID, name));
    }
}
