package net.warcar.hito_hito_nika.projectiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.EntityHitResult;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileEntity;
import xyz.pixelatedw.mineminenomi.api.abilities.Ability;

public class KingBajrangGunProjectile extends NuProjectileEntity {
	public KingBajrangGunProjectile(EntityType type, Level world) {
		super(type, world);
	}

	public KingBajrangGunProjectile(Level world, LivingEntity player, Ability ability) {
		super(NikaProjectiles.GOMU_GOMU_NO_KING_BAJRANG_GUN.get(), world, player, ability);
		this.setMaxLife(250);
		this.setDamage(0F);
		this.setEntityCollisionSize(5);
		this.setPassThroughEntities();
		this.setPassThroughBlocks();
		this.addTickEvent(100, this::onTickEvent);
		this.addEntityHitEvent(100, this::onEntityImpactEvent);
	}

	private void onTickEvent() {
		for (BlockPos pos : WyHelper.getNearbyBlocks(this, 5)) {
			this.level().setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
		}
	}

	private void onEntityImpactEvent(EntityHitResult hitEnt) {
		hitEnt.getEntity().remove(RemovalReason.KILLED);
	}
}
