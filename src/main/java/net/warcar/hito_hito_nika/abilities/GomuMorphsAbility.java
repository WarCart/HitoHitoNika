package net.warcar.hito_hito_nika.abilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import net.warcar.hito_hito_nika.init.TrueMorphs;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityType;
import xyz.pixelatedw.mineminenomi.api.abilities.PassiveAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.components.MorphComponent;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.init.ModAbilityComponents;
import xyz.pixelatedw.mineminenomi.init.ModNetwork;
import xyz.pixelatedw.mineminenomi.packets.server.ability.SUpdateAbilityNBTPacket;

import javax.annotation.Nullable;

public class GomuMorphsAbility extends PassiveAbility {
	public static final RegistryObject<AbilityCore<GomuMorphsAbility>> INSTANCE = TrueGomuGomuNoMi.registerAbility(new AbilityCore.Builder<>("gomu_transformations", "Gomu Transformations", AbilityCategory.DEVIL_FRUITS, AbilityType.PASSIVE, GomuMorphsAbility::new)
			.setHidden());

	private int needsUpdate = 0;

	private final MorphComponent morphComponent;

	public GomuMorphsAbility(AbilityCore<GomuMorphsAbility> core) {
		super(core);
		this.morphComponent = new MorphComponent(this);
		this.getComponents().remove(ModAbilityComponents.DISABLE.get());
		this.addComponents(morphComponent);
		this.addDuringPassiveEvent(this::update);
	}

	private void update(LivingEntity player) {
		if (this.needsUpdate > 0) {
			if (this.morphComponent.isMorphed()) {
				this.morphComponent.stopMorph(player);
			}
			MorphInfo morphInfo = this.getTransformation(player);
			if (morphInfo != null) {
				this.morphComponent.startMorph(player, morphInfo);
			}
			this.needsUpdate--;
		}
	}

	public void updateModes(LivingEntity entity) {
		this.needsUpdate = 2;
		if (!entity.level().isClientSide) {
			ModNetwork.sendToAllTrackingAndSelf(new SUpdateAbilityNBTPacket(entity, this), entity);
		}
	}

	@Nullable
	public MorphInfo getTransformation(LivingEntity target) {
		IAbilityData props = AbilityCapability.get(target).get();
		if (TrueGomuHelper.hasGearFourthActive(props)) {
			TrueGearFourthAbility g4 = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE.get());
			if (g4.isSnakeman()) {
				return TrueMorphs.SNAKEMAN.get();
			} else if (g4.isBoundman()) {
				return TrueMorphs.BOUNDMAN.get();
			} else if (g4.isTankman()) {
				return TrueMorphs.TANKMAN.get();
			}
		} else if (TrueGomuHelper.hasGigantActive(props)) {
			return TrueMorphs.GIANT.get();
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE)) {
			return TrueMorphs.GIANT_FUSEN.get();
		} else if (TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE) || (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasAbilityActive(props, TrueGomuRocket.INSTANCE))) {
			return TrueMorphs.FUSEN.get();
		} else if (TrueGomuHelper.isSmall(props)) {
			return TrueMorphs.SMALL.get();
		}
		return null;
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		nbt.putInt("updateTicks", needsUpdate);
	}

	@Override
	public void loadAdditional(CompoundTag nbt) {
		this.needsUpdate = nbt.getInt("updateTicks");
	}
}
