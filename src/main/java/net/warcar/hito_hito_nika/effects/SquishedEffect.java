package net.warcar.hito_hito_nika.effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import xyz.pixelatedw.mineminenomi.effects.BaseEffect;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;

public class SquishedEffect extends BaseEffect {
    public SquishedEffect() {
        super(MobEffectCategory.HARMFUL, 0);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(HitoHitoNoMiNikaMod.MOD_ID, "4bcf2978-a750-4231-814e-42042bc28a5e"), -0.99, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        this.addAttributeModifier(ModAttributes.JUMP_HEIGHT, ResourceLocation.fromNamespaceAndPath(HitoHitoNoMiNikaMod.MOD_ID, "65836e10-ecfb-4673-bff8-574750c34c68"), -100, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(HitoHitoNoMiNikaMod.MOD_ID, "5fb0d432-45fe-4f6c-9af4-1250f30a0df6"), -10, AttributeModifier.Operation.ADD_VALUE);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(HitoHitoNoMiNikaMod.MOD_ID, "e600f852-498c-4745-807a-e4defa1b9b1d"), -0.99, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
    }

    @Override
    public boolean shouldUpdateClient() {
        return true;
    }
}
