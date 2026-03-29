package net.warcar.hito_hito_nika.renderers.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.warcar.hito_hito_nika.entities.LuffyBoss;
import xyz.pixelatedw.mineminenomi.abilities.haki.HakiHelper;
import xyz.pixelatedw.mineminenomi.api.entities.NuLightningEntity;
import xyz.pixelatedw.mineminenomi.api.entities.NuLightningEntityRenderer;
import xyz.pixelatedw.mineminenomi.api.entities.NuProjectileRenderer;
import xyz.pixelatedw.mineminenomi.init.ModResources;

public class GomuLightningProjectileRenderer<M extends EntityModel<NuLightningEntity>> extends NuLightningEntityRenderer {
    private final boolean leg;

    public GomuLightningProjectileRenderer(EntityRendererProvider.Context renderManager, boolean leg) {
        super(renderManager);
        this.leg = leg;
    }

    public void render(NuLightningEntity entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(NuLightningEntity lightning) {
        ResourceLocation textureLocation = super.getTextureLocation(lightning);
        if (lightning.getOwner() != null) {
            LivingEntity entity = lightning.getOwner();
            if (HakiHelper.hasHardeningActive(entity, false, true)) {
                return ModResources.BUSOSHOKU_HAKI_ARM;
            } else if (lightning.getOwner() instanceof LuffyBoss luffy) {
                return luffy.getCurrentTexture();
            }
        }
        return textureLocation;
    }

    public static class Factory extends NuProjectileRenderer.Factory<NuLightningEntity> {
        private final boolean leg;

        public Factory(boolean leg) {
            this.leg = leg;
        }
        
        public EntityRenderer<NuLightningEntity> create(Context manager) {
            EntityModel<NuLightningEntity> model;
            if (this.model == null) {
                model = null;
            } else {
                model = this.model.apply(manager);
            }
            GomuLightningProjectileRenderer<?> renderer = new GomuLightningProjectileRenderer<>(manager, this.leg);
            renderer.setUseArmSkin();
            return renderer;
        }
    }
}
