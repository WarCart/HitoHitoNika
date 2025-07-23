package net.warcar.hito_hito_nika.effects;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.warcar.hito_hito_nika.abilities.TrueGearFifthAbility;
import xyz.pixelatedw.mineminenomi.api.effects.ModEffect;
import xyz.pixelatedw.mineminenomi.config.CommonConfig;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncAbilityDataPacket;
import xyz.pixelatedw.mineminenomi.packets.server.ability.SUpdateEquippedAbilityPacket;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;
import xyz.pixelatedw.mineminenomi.wypi.WyRegistry;

public class GomuReviveEffect extends ModEffect {
    public static final RegistryObject<Effect> INSTANCE = WyRegistry.registerEffect("Dead", GomuReviveEffect::new);
    public GomuReviveEffect() {
        super(EffectType.HARMFUL, WyHelper.hexToRGB("#000000").getRGB());
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
    public void removeAttributeModifiers(LivingEntity entity, AttributeModifierManager manager, int amp) {
        super.removeAttributeModifiers(entity, manager, amp);
        if (!entity.level.isClientSide) {
            if (entity instanceof PlayerEntity) {
                IAbilityData props = AbilityDataCapability.get(entity);
                if (!props.hasEquippedAbility(TrueGearFifthAbility.INSTANCE)) {
                    TrueGearFifthAbility ability = TrueGearFifthAbility.INSTANCE.createAbility();
                    for (int i = 0; i < CommonConfig.INSTANCE.getAbilityBars() * 8; i++) {
                        if (props.getEquippedAbility(i) == null) {
                            props.setEquippedAbility(i, ability);
                            WyNetwork.sendToAllTrackingAndSelf(new SSyncAbilityDataPacket(entity.getId(), AbilityDataCapability.get(entity)), entity);
                            WyNetwork.sendToAllTrackingAndSelf(new SUpdateEquippedAbilityPacket(entity, ability), entity);
                        }
                    }
                }
                ((PlayerEntity) entity).closeContainer();
                if (props.hasEquippedAbility(TrueGearFifthAbility.INSTANCE)) {
                    props.getEquippedAbility(TrueGearFifthAbility.INSTANCE).use(entity);
                } else {
                    entity.level.playSound(null, entity, ModSounds.DRUMS_OF_LIBERATION_1.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
                }
            }
        }
    }

    public static void register() {
    }
}
