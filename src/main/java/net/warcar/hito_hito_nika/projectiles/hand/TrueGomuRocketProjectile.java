package net.warcar.hito_hito_nika.projectiles.hand;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.warcar.hito_hito_nika.abilities.TrueGomuRocket;
import net.warcar.hito_hito_nika.projectiles.NikaProjectiles;
import net.warcar.hito_hito_nika.projectiles.TrueGomuProjectile;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceElement;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceHakiNature;
import xyz.pixelatedw.mineminenomi.api.damagesources.SourceType;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;

public class TrueGomuRocketProjectile extends TrueGomuProjectile {
	public TrueGomuRocketProjectile(EntityType<TrueGomuRocketProjectile> entityType, Level world) {
		super(entityType, world);
	}

	public TrueGomuRocketProjectile(Level world, LivingEntity player, Ability ability) {
		super(NikaProjectiles.GOMU_GOMU_NO_ROCKET.get(), player, ability, SourceElement.RUBBER, SourceHakiNature.HARDENING, SourceType.FIST, SourceType.FRIENDLY, SourceType.PHYSICAL);
		this.setDamage(0f);
		this.setMaxLife(24);
		this.setSize(3.1f);
		this.addEntityHitEvent(100, this::onEntityImpact);
		this.addBlockHitEvent(100, this::onBlockHit);
		this.setTravelSpeed(3);
	}

	private void flying(BlockPos pos) {
		var owner = this.getOwner();
		((TrueGomuRocket) this.getParent().get()).setFlying();

		BlockPos distance = pos.subtract(owner.blockPosition());

		AbilityHelper.setDeltaMovement(owner, distance.getX() * 0.35, 0.3 + distance.getY() * 0.35, distance.getZ() * 0.35);
	}

	private void onBlockHit(BlockHitResult result) {
		this.flying(result.getBlockPos());
	}

	private void onEntityImpact(EntityHitResult result) {
		var ent = result.getEntity();
		this.flying(new BlockPos(ent.getBlockX(), Math.round(ent.getBlockY() + ent.getEyeHeight() / 2), ent.getBlockZ()));
	}
}
