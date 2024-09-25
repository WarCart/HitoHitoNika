package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.entities.LightningDischargeEntity;

import java.awt.*;

public class ThorElephantGunProjectile extends TrueElephantGunProjectile {

    public ThorElephantGunProjectile(World world, LivingEntity player, Ability ability) {
        super(world, player, ability);
        this.setMaxLife(9);
        this.setDamage(56f);
        this.setDamageSource(this.getDamageSource().setPhysical());
        this.onEntityImpactEvent = this::onEntityImpactEvent;
    }
    
    public void onEntityImpactEvent(Entity target){
        LightningDischargeEntity lightning = new LightningDischargeEntity(this, this.getX(), this.getY(), this.getZ(), this.xRot, this.yRot);
        lightning.setAliveTicks(20);
        lightning.setUpdateRate(4);
        lightning.setDetails(16);
        lightning.setColor(new Color(255, 255, 40));
        lightning.setOutlineColor(new Color(255, 255, 40, 50));
        lightning.setRenderTransparent();
        lightning.setLightningLength(3);
        lightning.setDensity(15);
        lightning.setSize(3f);
        this.level.addFreshEntity(lightning);
    }
}
