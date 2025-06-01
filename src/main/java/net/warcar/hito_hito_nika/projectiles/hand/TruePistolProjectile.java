package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.entities.projectiles.AbilityProjectileEntity;

public class TruePistolProjectile extends AbilityProjectileEntity {
    public TruePistolProjectile(EntityType type, World world) {
        super(type, world);
    }

    public TruePistolProjectile(World world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_PISTOL.get(), world, player, ability);
        this.setMaxLife(12);
        this.setAffectedByHardening();
        this.setDamage(8F);
        this.setPassThroughEntities();
        this.setDamageSource(this.getDamageSource().setPhysical());
    }
}
