package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.init.ModEffects;

public class StarStampCannonProjectile extends KongStampProjectile {
    public StarStampCannonProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(300);
        this.setDamage(125F);
        this.addBlockHitEvent(100, TrueGomuHelper.onBlockImpactEvent(this, 3, 15));
        this.addTickEvent(100, TrueGomuHelper.getG2Tick(this));
        this.addEntityHitEvent(100, this::onEntityImpact);
    }

    private void onEntityImpact(EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity entity) {
            entity.addEffect(new MobEffectInstance(ModEffects.DIZZY.get(), 60, 1));
        }
    }
}
