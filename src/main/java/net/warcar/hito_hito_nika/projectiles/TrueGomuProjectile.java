package net.warcar.hito_hito_nika.projectiles;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import xyz.pixelatedw.mineminenomi.api.abilities.IAbility;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.api.entities.NuHorizontalLightningEntity;

public class TrueGomuProjectile extends NuHorizontalLightningEntity {
    public TrueGomuProjectile(EntityType type, Level world) {
        super(type, world);
    }

    public TrueGomuProjectile(EntityType<? extends NuHorizontalLightningEntity> type, LivingEntity thrower, @Nullable IAbility ability, SourceElement element, SourceHakiNature hakiNature, SourceType... types) {
        super(type, thrower, thrower.getX(), thrower.getY(), thrower.getZ(), 1000000, 0, ability, element, hakiNature, types);
        this.setRetracting();
        this.setDepth(1);
        this.setPassThroughEntities();
    }

    public Vec3 getMovement() {
        return this.lookVec;
    }

    @Override
    public void setSize(float size) {
        super.setSize(size / 100);
    }
}
