package net.warcar.hito_hito_nika.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import xyz.pixelatedw.mineminenomi.effects.BaseEffect;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;

public class SquishedEffect extends BaseEffect {
    public SquishedEffect() {
        super(MobEffectCategory.HARMFUL, 0);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "4bcf2978-a750-4231-814e-42042bc28a5e", -0.99, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(ModAttributes.JUMP_HEIGHT.get(), "65836e10-ecfb-4673-bff8-574750c34c68", -100, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "5fb0d432-45fe-4f6c-9af4-1250f30a0df6", -10, AttributeModifier.Operation.ADDITION);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, "e600f852-498c-4745-807a-e4defa1b9b1d", -0.99, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean shouldUpdateClient() {
        return true;
    }

    @Override
    public double getAttributeModifierValue(int amp, AttributeModifier modifier) {
        return modifier.getAmount();
    }
}
