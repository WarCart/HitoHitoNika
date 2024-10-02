package net.warcar.hito_hito_nika.abilities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.warcar.fruit_progression.data.entity.awakening.AwakeningDataCapability;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.init.*;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.Objects;
import java.util.UUID;

public class FifthGearAbility extends ContinuousAbility implements IBodyOverlayAbility, IParallelContinuousAbility, IAbilityModeSwitcher {
	public static final AbilityCore<FifthGearAbility> INSTANCE;
	private static final AbilityOverlay OVERLAY;
	private static final AbilityAttributeModifier DAMAGE_REDUCTION_MODIFIER;
	private static final AbilityAttributeModifier STRENGTH_MODIFIER;
	private static final AbilityAttributeModifier REGEN;

	public FifthGearAbility(AbilityCore core) {
		super(core);
		this.onStartContinuityEvent = this::onStartContinuity;
		this.duringContinuityEvent = this::duringContinuity;
		this.afterContinuityStopEvent = this::afterContinuityStop;
	}

	private void duringContinuity(PlayerEntity player, int passiveTimer) {
		if (passiveTimer % 4 == 0) {
			WyHelper.spawnParticleEffect(ModParticleEffects.GEAR_SECOND.get(), player, player.getX(), player.getY(), player.getZ());
		}
	}

	private boolean onStartContinuity(PlayerEntity player) {
		double time = EntityStatsCapability.get(player).getDoriki() * .003d;
		if (!TrueGomuHelper.canActivateGear(AbilityDataCapability.get(player), INSTANCE)) {
			player.sendMessage(new TranslationTextComponent(ModI18n.ABILITY_MESSAGE_GEAR_ACTIVE), Util.NIL_UUID);
			return false;
		} else if (!canUnlock(player)) {
			player.sendMessage(new TranslationTextComponent("text.mineminenomi.too_weak"), Util.NIL_UUID);
			return false;
		}
		player.getAttribute(ModAttributes.PUNCH_DAMAGE.get()).addTransientModifier(STRENGTH_MODIFIER);
		player.getAttribute(ModAttributes.DAMAGE_REDUCTION.get()).addTransientModifier(DAMAGE_REDUCTION_MODIFIER);
		player.getAttribute(ModAttributes.REGEN_RATE.get()).addTransientModifier(REGEN);
		if (time >= 500) {
			this.setThreshold(0f);
		} else {
			this.setThreshold(time);
		}
		this.enableModes(player, this);
		IAbilityData props = AbilityDataCapability.get(player);
		((GomuMorphsAbility) props.getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
		player.level.playSound(null, player, TrueGomuGomuNoMi.DRUMS_OF_LIBERATION.get(), SoundCategory.PLAYERS, 0.5F, 1.0F);
		return true;
	}

	private void afterContinuityStop(PlayerEntity player) {
		player.getAttribute(ModAttributes.PUNCH_DAMAGE.get()).removeModifier(STRENGTH_MODIFIER);
		player.getAttribute(ModAttributes.DAMAGE_REDUCTION.get()).removeModifier(DAMAGE_REDUCTION_MODIFIER);
		player.getAttribute(ModAttributes.REGEN_RATE.get()).removeModifier(REGEN);
		AbilityHelper.disableAbilities(player, this.continueTime / 4, Objects::nonNull);
		player.addEffect(new EffectInstance(ModEffects.PARALYSIS.get(), this.continueTime / 4, 1, true, true));
		this.setMaxCooldown((float) this.getContinueTime() / 20);
		this.disableModes(player, this);
		IAbilityData props = AbilityDataCapability.get(player);
		((GomuMorphsAbility) props.getUnlockedAbility(GomuMorphsAbility.INSTANCE)).updateModes();
	}

	public AbilityOverlay getBodyOverlay(LivingEntity entity) {
		return OVERLAY;
	}

	protected static boolean canUnlock(LivingEntity user) {
		return EntityStatsCapability.get(user).getDoriki() * .003d >= 25d && AwakeningDataCapability.get(user).isAwakened();
	}

	static {
		INSTANCE = new AbilityCore.Builder<>("Gear Fifth", AbilityCategory.DEVIL_FRUITS, FifthGearAbility::new).setUnlockCheck(FifthGearAbility::canUnlock)
				.addDescriptionLine("Awakening ability, that makes you insanely powerful").build();
		OVERLAY = new AbilityOverlay.Builder().setColor("#ffffff55").build();//.setTexture(new ResourceLocation("hito_hito_no_mi_nika", "textures/models/gear_5_overlay.png"))
		STRENGTH_MODIFIER = new AbilityAttributeModifier(UUID.fromString("5fc1a28f-7e59-44bf-9d7a-36953e9c700d"), FifthGearAbility.INSTANCE, "Gear Fifth Attack Damage Modifier", 20.0, AttributeModifier.Operation.ADDITION);
		DAMAGE_REDUCTION_MODIFIER = new AbilityAttributeModifier(UUID.fromString("2efdb212-33d0-4fad-b806-4d39d7091ffd"), FifthGearAbility.INSTANCE, "Gear Fifth Resistance Damage Modifier", 40, AttributeModifier.Operation.ADDITION);
		REGEN = new AbilityAttributeModifier(UUID.fromString("e6a409f2-5c6a-409e-a9f3-5b74899d8129"), FifthGearAbility.INSTANCE, "Gear Fifth Regen Modifier", 5, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}
}
