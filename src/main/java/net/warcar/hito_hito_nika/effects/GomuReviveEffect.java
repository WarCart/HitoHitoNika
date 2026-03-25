package net.warcar.hito_hito_nika.effects;


import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.warcar.hito_hito_nika.abilities.TrueGearFifthAbility;
import xyz.pixelatedw.mineminenomi.api.WyHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.effects.BaseEffect;
import xyz.pixelatedw.mineminenomi.init.ModNetwork;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncAbilityDataPacket;
import xyz.pixelatedw.mineminenomi.packets.server.ability.SUpdateEquippedAbilityPacket;

public class GomuReviveEffect extends BaseEffect {
    public GomuReviveEffect() {
        super(MobEffectCategory.BENEFICIAL, WyHelper.hexToRGB("#000000").getRGB());
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
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap manager, int amp) {
        super.removeAttributeModifiers(entity, manager, amp);
        if (!entity.level().isClientSide) {
            if (entity instanceof Player) {
                IAbilityData props = AbilityCapability.get(entity).get();
                if (!props.hasEquippedAbility(TrueGearFifthAbility.INSTANCE.get())) {
                    TrueGearFifthAbility ability = TrueGearFifthAbility.INSTANCE.get().createAbility();
                    for (int i = 0; i < 2 * 8; i++) {
                        if (props.getEquippedAbility(i) == null) {
                            props.setEquippedAbility(i, ability);
                            ModNetwork.sendToAllTrackingAndSelf(new SSyncAbilityDataPacket(entity, AbilityCapability.get(entity)), entity);
                            ModNetwork.sendToAllTrackingAndSelf(new SUpdateEquippedAbilityPacket(entity, ability), entity);
                        }
                    }
                }
                ((Player) entity).closeContainer();
                if (props.hasEquippedAbility(TrueGearFifthAbility.INSTANCE.get())) {
                    props.getEquippedAbility(TrueGearFifthAbility.INSTANCE.get()).use(entity);
                } else {
                    entity.level().playSound(null, entity, ModSounds.DRUMS_OF_LIBERATION_1.get(), SoundSource.PLAYERS, 0.5F, 1.0F);
                }
            }
        }
    }
}
