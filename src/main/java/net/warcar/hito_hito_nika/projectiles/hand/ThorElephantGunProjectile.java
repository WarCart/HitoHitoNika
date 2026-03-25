package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.entities.LightningDischargeEntity;

import java.awt.*;

public class ThorElephantGunProjectile extends TrueElephantGunProjectile {

    public ThorElephantGunProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(9);
        this.setDamage(75);
        this.addEntityHitEvent(100, onEntityImpactEvent(this));
    }
    
    public static IOnHitEntityEvent onEntityImpactEvent(NuProjectileEntity entity) {
        return hit -> {
            Vec2 vector = entity.getRotationVector();
            LightningDischargeEntity lightning = new LightningDischargeEntity(entity, entity.getX(), entity.getY(), entity.getZ(), vector.x, vector.y);
            lightning.setAliveTicks(20);
            lightning.setUpdateRate(4);
            lightning.setDetails(16);
            lightning.setColor(new Color(255, 255, 40));
            lightning.setOutlineColor(new Color(255, 255, 40, 50));
            lightning.setGlowingTag(true);
            lightning.setLightningLength(3);
            lightning.setDensity(15);
            lightning.setSize(3f);
            entity.level().addFreshEntity(lightning);
        };
    }
}
