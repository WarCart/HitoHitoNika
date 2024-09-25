package net.warcar.hito_hito_nika.effects;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.abilities.TrueGearFifthAbility;
import xyz.pixelatedw.mineminenomi.api.effects.ModEffect;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;

import javax.annotation.ParametersAreNonnullByDefault;

public class GomuReviveEffect extends ModEffect {
    public static final RegistryObject<Effect> INSTANCE = WyRegistry.registerEffect("Dead", GomuReviveEffect::new);
    public GomuReviveEffect() {
        super(EffectType.HARMFUL, WyHelper.hexToRGB("#000000").getRGB());
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "042fdefa-052f-4d69-ab21-90b4bfba094e", -1000.0D, AttributeModifier.Operation.ADDITION).addAttributeModifier((Attribute) ModAttributes.JUMP_HEIGHT.get(), "dac953cd-3c25-463a-a748-8c49b059fc67", -256.0D, AttributeModifier.Operation.ADDITION);
    }

    public boolean isBlockingRotations() {
        return true;
    }

    public boolean isRemoveable() {
        return false;
    }

    public boolean isBlockingSwings() {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void removeAttributeModifiers(LivingEntity entity, AttributeModifierManager manager, int amp) {
        super.removeAttributeModifiers(entity, manager, amp);
        if (!entity.level.isClientSide) {
            if (entity instanceof PlayerEntity) {
                IAbilityData props = AbilityDataCapability.get(entity);
                /*if (!props.hasEquippedAbility(FifthGearAbility.INSTANCE)) {
                    Ability ability = FifthGearAbility.INSTANCE.createAbility();
                    props.addEquippedAbility(ability);
                    WyNetwork.sendToAllTrackingAndSelf(new SSyncAbilityDataPacket(entity.getId(), AbilityDataCapability.get(entity)), entity);
                    WyNetwork.sendToAllTrackingAndSelf(new SUpdateEquippedAbilityPacket((PlayerEntity) entity, ability), entity);
                }*/
                ((PlayerEntity) entity).closeContainer();
                if (props.hasEquippedAbility(TrueGearFifthAbility.INSTANCE)) {
                    props.getEquippedAbility(TrueGearFifthAbility.INSTANCE).use((PlayerEntity) entity);
                } else {
                    entity.level.playSound(null, entity, ModSounds.DRUMS_OF_LIBERATION_1.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
                }
                HitoHitoNoMiNikaMod.LOGGER.info(props.getEquippedAbilitySlot(props.getEquippedAbility(TrueGearFifthAbility.INSTANCE)));
            }
        }
    }

    public static void register(IEventBus eventBus) {
    }
}
