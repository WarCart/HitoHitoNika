package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;

import java.util.UUID;

public class GomuFusenAbility extends StatsChangeAbility implements IAbilityMode {
	public static final AbilityCore<GomuFusenAbility> INSTANCE;
	private boolean gigant = false;

	public GomuFusenAbility(AbilityCore core) {
		super(core);
		this.setDisplayName("Gomu Gomu no Fusen");
		this.setCustomIcon("Fusen");
		this.onStartContinuityEvent = this::onStartContinuityEvent;
		this.afterContinuityStopEvent = this::afterContinuityStopEvent;
		this.setDynamicModifiers();
	}

	private void afterContinuityStopEvent(PlayerEntity player) {
		int cooldown = (int) Math.round((double) this.continueTime / 20.0D);
		this.setMaxCooldown(cooldown);
		IAbilityData props = AbilityDataCapability.get(player);
		((GomuMorphsAbility) props.getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
	}

	private boolean onStartContinuityEvent(PlayerEntity player) {
		IAbilityData props = AbilityDataCapability.get(player);
		double time = EntityStatsCapability.get(player).getDoriki() * .01d + 15d;
		if (time >= 500) {
			this.setThreshold(0f);
		} else {
			this.setThreshold(time);
		}
		this.addModifier(Attributes.MOVEMENT_SPEED, new AbilityAttributeModifier(UUID.fromString("0b034de2-6e61-4c55-b259-050dae546a48"), INSTANCE, "Fusen Speed Modifier", this.gigant ? -0.75d : -0.5d, Operation.MULTIPLY_TOTAL));
		this.addModifier(ModAttributes.JUMP_HEIGHT, new AbilityAttributeModifier(UUID.fromString("2a0bf1c6-0873-11ef-91ae-325096b39f47"), INSTANCE, "Fusen Jump Modifier", this.gigant ? -0.75d : -0.5d, Operation.MULTIPLY_TOTAL));
		this.addModifier(Attributes.KNOCKBACK_RESISTANCE, new AbilityAttributeModifier(UUID.fromString("2a0bf504-0873-11ef-8291-325096b39f47"), INSTANCE, "Fusen Knockback Resistance Modifier", 1, Operation.ADDITION));
		this.addModifier(ModAttributes.DAMAGE_REDUCTION, new AbilityAttributeModifier(UUID.fromString("2a0bf464-0873-11ef-b635-325096b39f47"), INSTANCE, "Fusen Resistance Damage Modifier", this.gigant ? 20d : 10d, Operation.ADDITION));
		((GomuMorphsAbility) props.getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
		return true;
	}

	public AbilityCore[] getParents() {
		return new AbilityCore[]{TrueGearThirdAbility.INSTANCE};
	}

	public void enableMode(Ability ign) {
		this.setDisplayName("Gomu Gomu no Gigant Fusen");
		this.gigant = true;
	}

	public void disableMode(Ability ign) {
		this.setDisplayName("Gomu Gomu no Fusen");
		this.gigant = false;
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gomu Fusen", AbilityCategory.DEVIL_FRUITS, GomuFusenAbility::new)).addDescriptionLine("").build();
	}
}
