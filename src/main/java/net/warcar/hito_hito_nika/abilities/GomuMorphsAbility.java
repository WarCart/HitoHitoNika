package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.init.TrueMorphs;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityType;
import xyz.pixelatedw.mineminenomi.api.abilities.PassiveAbility2;
import xyz.pixelatedw.mineminenomi.api.abilities.components.MorphComponent;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.init.ModAbilityKeys;
import xyz.pixelatedw.mineminenomi.init.ModMorphs;

import javax.annotation.Nullable;

public class GomuMorphsAbility extends PassiveAbility2 {
	public static final AbilityCore<GomuMorphsAbility> INSTANCE;

	private int needsUpdate = 0;

	private final MorphComponent morphComponent;

	public GomuMorphsAbility(AbilityCore<GomuMorphsAbility> core) {
		super(core);
		this.morphComponent = new MorphComponent(this);
		this.getComponents().remove(ModAbilityKeys.DISABLE);
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

	public void updateModes() {
		this.needsUpdate = 2;
	}

	@Nullable
	public MorphInfo getTransformation(LivingEntity target) {
		IAbilityData props = AbilityDataCapability.get(target);
		if (TrueGomuHelper.hasGearFourthActive(props)) {
			TrueGearFourthAbility g4 = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE);
			if (g4.isSnakeman()) {
				return TrueMorphs.SNAKEMAN.get();
			} else if (g4.isBoundman()) {
				return TrueMorphs.BOUNDMAN.get();
			}
		} else if (TrueGomuHelper.hasGigantActive(props)) {
			return TrueMorphs.GIGANT.get();
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE)) {
			return TrueMorphs.GIGANT_FUSEN.get();
		} else if (TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE) || (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasAbilityActive(props, TrueGomuRocket.INSTANCE))) {
			return TrueMorphs.FUSEN.get();
		} else if (TrueGomuHelper.isSmall(props)) {
			return TrueMorphs.SMALL.get();
		}
		return null;
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gomu Transformations", AbilityCategory.DEVIL_FRUITS, AbilityType.PASSIVE, GomuMorphsAbility::new)).setHidden().build();
	}
}
