package net.warcar.hito_hito_nika.init;

import net.minecraft.resources.ResourceLocation;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.animations.TrueGomuBazookaAnimation;
import net.warcar.hito_hito_nika.animations.TrueGomuBulletAnimation;
import xyz.pixelatedw.mineminenomi.api.animations.Animation;
import xyz.pixelatedw.mineminenomi.api.animations.AnimationId;

public class GomuAnimations {
    public static final AnimationId<TrueGomuBazookaAnimation> BAZOOKA = register("gomu_bazooka", TrueGomuBazookaAnimation::new);
    public static final AnimationId<TrueGomuBulletAnimation> BULLET = register("gomu_bullet",  TrueGomuBulletAnimation::new);

    private static <A extends Animation<?, ?>> AnimationId<A> register(String name, AnimationId.IAnimationFactory<A> factory) {
        return new AnimationId<>(ResourceLocation.fromNamespaceAndPath(HitoHitoNoMiNikaMod.MOD_ID, name),  factory);
    }

    public static void init() {}
}
