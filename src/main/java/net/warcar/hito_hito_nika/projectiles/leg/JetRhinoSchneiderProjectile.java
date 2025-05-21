package net.warcar.hito_hito_nika.projectiles.leg;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import net.warcar.hito_hito_nika.projectiles.PythonProjectile;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

public class JetRhinoSchneiderProjectile extends PythonProjectile {
    public JetRhinoSchneiderProjectile(EntityType type, World world) {
        super(type, world);
    }

    public JetRhinoSchneiderProjectile(World world, LivingEntity player, Ability ability, float speed, int layer) {
        super(NikaProjectiles.GOMU_GOMU_NO_JET_RHINO_SCHNEIDER.get(), world, player, ability, speed, layer);
        this.setMaxLife(3);
        this.setDamage(30F);
        this.setEntityCollisionSize(2.5d);
        this.setAffectedByHardening();
        this.setPassThroughEntities();
        this.setDamageSource(this.getDamageSource().setPhysical());
    }
    public PythonProjectile getNew() {
        return new JetRhinoSchneiderProjectile(this.level, this.getThrower(), this.master, this.speed, this.layer - 1);
    }
}
