package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import net.warcar.hito_hito_nika.projectiles.TrueGomuProjectile;
import net.warcar.hito_hito_nika.renderers.projectiles.GomuProjectileRenderer;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;

public class TrueGrizzlyMagnumProjectile extends TrueGomuProjectile {
    public TrueGrizzlyMagnumProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public TrueGrizzlyMagnumProjectile(Level world, LivingEntity player, Ability ability) {
        super(NikaProjectiles.GOMU_GOMU_NO_GRIZZLY_MAGNUM.get(), player, ability, SourceElement.RUBBER, SourceHakiNature.HARDENING, SourceType.FIST, SourceType.PHYSICAL);
        this.setDamage(80f);
        this.setMaxLife(10);
        this.setPassThroughBlocks();
        this.setEntityCollisionSize(2.5D);
        this.setSize(15.5f);
        this.addEntityHitEvent(100, TrueGomuHelper.getBazookaOnEntityImpactEvent(this, 5));
        this.setDeformation(GomuProjectileRenderer.Deformation.ELEPHANT);
    }
}
