package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import net.warcar.hito_hito_nika.projectiles.PythonProjectile;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

public class JetRhinoSchneiderProjectile extends PythonProjectile {
    public JetRhinoSchneiderProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public JetRhinoSchneiderProjectile(Level world, LivingEntity player, Ability ability, float speed, int layer) {
        super(NikaProjectiles.GOMU_GOMU_NO_JET_RHINO_SCHNEIDER.get(), world, player, ability, speed);
        this.setMaxLifetime(3);
        this.setMaxLife(layer * 3);
        this.setDamage(20f);
        this.setEntityCollisionSize(2.5d);
        this.setPassThroughEntities();
    }
}
