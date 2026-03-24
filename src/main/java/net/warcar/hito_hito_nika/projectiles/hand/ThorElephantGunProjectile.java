package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.entities.LightningDischargeEntity;

import java.awt.*;

public class ThorElephantGunProjectile extends TrueElephantGunProjectile {

    public ThorElephantGunProjectile(Level world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(9);
        this.setDamage(75);
        this.addEntityHitEvent(100, this::onEntityImpactEvent);
    }
    
    public void onEntityImpactEvent(EntityHitResult target){
        Vec2 vector = this.getRotationVector();
        LightningDischargeEntity lightning = new LightningDischargeEntity(this, this.getX(), this.getY(), this.getZ(), vector.x, vector.y);
        lightning.setAliveTicks(20);
        lightning.setUpdateRate(4);
        lightning.setDetails(16);
        lightning.setColor(new Color(255, 255, 40));
        lightning.setOutlineColor(new Color(255, 255, 40, 50));
        lightning.setGlowingTag(true);
        lightning.setLightningLength(3);
        lightning.setDensity(15);
        lightning.setSize(3f);
        this.level().addFreshEntity(lightning);
    }
}
