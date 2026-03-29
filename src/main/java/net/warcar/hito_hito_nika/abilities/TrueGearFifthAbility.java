package net.warcar.hito_hito_nika.abilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.RegistryObject;
import net.warcar.hito_hito_nika.config.CommonConfig;
import net.warcar.hito_hito_nika.helpers.EquationHelper;
import net.warcar.hito_hito_nika.helpers.TrueGomuHelper;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.joml.Math;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChangeStatsComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.SkinOverlayComponent;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.init.ModAbilityComponents;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.init.i18n.ModI18nAbilities;

import java.util.HashMap;
import java.util.UUID;

import static xyz.pixelatedw.mineminenomi.api.abilities.AbilityOverlay.RenderType.ENERGY;

public class TrueGearFifthAbility extends Ability {
	private static final Component[] DESCRIPTION = TrueGomuHelper.registerDescriptionText("gear_fifth", new Pair[]{ImmutablePair.of("The absolute peak bringing joy and freedom to those around them.", (Object)null)});
	public static final RegistryObject<AbilityCore<TrueGearFifthAbility>> INSTANCE = TrueGomuGomuNoMi.registerAbility(new AbilityCore.Builder<>("gear_fifth", "Gear Fifth", AbilityCategory.DEVIL_FRUITS, TrueGearFifthAbility::new).setUnlockCheck(TrueGearFifthAbility::canUnlock)
            .addDescriptionLine(DESCRIPTION).addAdvancedDescriptionLine(AbilityDescriptionLine.NEW_LINE, ChangeStatsComponent.getTooltip()));
	private final ContinuousComponent continuousComponent;
	private final ChangeStatsComponent statsComponent;
	private final SkinOverlayComponent overlayComponent;
	private static final AbilityAttributeModifier DAMAGE_REDUCTION_MODIFIER;
	private static final AbilityAttributeModifier STRENGTH_MODIFIER;
	private static final AbilityAttributeModifier REGEN;
	private static final AbilityAttributeModifier GRAVITY_REDUCTION_MODIFIER;
	private boolean playJumpSound = false;

	public TrueGearFifthAbility(AbilityCore<TrueGearFifthAbility> core) {
		super(core);
		overlayComponent = new SkinOverlayComponent(this, new AbilityOverlay.Builder().setColor("#ffffff30").setRenderType(ENERGY).build());
		continuousComponent = new ContinuousComponent(this, true);
		this.continuousComponent.addEndEvent(this::afterContinuityStop);
		this.addUseEvent(this::onStartContinuity);
		this.continuousComponent.addTickEvent(this::duringContinuity);
		this.continuousComponent.addTickEvent(TrueGomuHelper.getSpeedEvent(0.875f));
		statsComponent = new ChangeStatsComponent(this);
		this.statsComponent.addAttributeModifier(ModAttributes.PUNCH_DAMAGE, STRENGTH_MODIFIER);
		this.statsComponent.addAttributeModifier(ModAttributes.TOUGHNESS, DAMAGE_REDUCTION_MODIFIER);
		this.statsComponent.addAttributeModifier(ModAttributes.REGEN_RATE, REGEN);
		this.statsComponent.addAttributeModifier(ForgeMod.ENTITY_GRAVITY, GRAVITY_REDUCTION_MODIFIER);
		this.addComponents(continuousComponent, statsComponent, overlayComponent);
	}

	private void duringContinuity(LivingEntity entity, IAbility ability) {
		if (entity.onGround() && !this.playJumpSound) {
			this.playJumpSound = true;
		} else if (!entity.onGround() && this.playJumpSound) {
			SoundEvent sfx;
			if (entity.getRandom().nextBoolean()) {
				sfx = ModSounds.BOUNCE_2.get();
			} else {
				sfx = ModSounds.BOUNCE_1.get();
			}
			entity.level().playSound(null, entity.blockPosition(), sfx, SoundSource.PLAYERS, 2.0F, 0.75F + entity.getRandom().nextFloat() / 2.0F);
			this.playJumpSound = false;
		}
	}

	private void onStartContinuity(LivingEntity player, IAbility ability) {
		if (this.continuousComponent.isContinuous()) {
			this.continuousComponent.stopContinuity(player);
			return;
		}
		if (!TrueGomuHelper.canActivateGear(AbilityCapability.get(player).get(), this)) {
			player.sendSystemMessage(ModI18nAbilities.MESSAGE_GEAR_ACTIVE);
			return;
		}
		IAbilityData props = AbilityCapability.get(player).get();
		GomuMorphsAbility morphs = props.getPassiveAbility(GomuMorphsAbility.INSTANCE.get());
		if (morphs != null)
			morphs.updateModes(player);
		if (player instanceof Player && player.level().isClientSide) {
			this.startPlayingDrums((AbstractClientPlayer) player, true);
		}
		this.statsComponent.applyModifiers(player);
		this.continuousComponent.startContinuity(player, (float) EquationHelper.parseEquation(CommonConfig.INSTANCE.getG5Length(), player, new HashMap<>()).getValue() * 20);
		this.overlayComponent.showAll(player);
	}

	private void afterContinuityStop(LivingEntity player, IAbility abl) {
		float time = (float) EquationHelper.parseEquation(CommonConfig.INSTANCE.getG5Cooldown(), player, TrueGomuHelper.getBasicBonusData(this.continuousComponent.getContinueTime())).getValue();
		player.addEffect(new MobEffectInstance(ModEffects.UNCONSCIOUS.get(), (int) (time * 0.25f), 1, true, true));
		this.cooldownComponent.startCooldown(player, time * 0.75f);
		IAbilityData props = AbilityCapability.get(player).get();
		GomuMorphsAbility morphs = props.getPassiveAbility(GomuMorphsAbility.INSTANCE.get());
		if (morphs != null)
			morphs.updateModes(player);
		if (player instanceof Player && player.level().isClientSide) {
			this.startPlayingDrums((AbstractClientPlayer) player, false);
		}
		this.statsComponent.removeModifiers(player);
		this.overlayComponent.hideAll(player);
	}

	protected static boolean canUnlock(LivingEntity user) {
		IDevilFruit fruit = DevilFruitCapability.get(user).get();
		return fruit.hasAwakenedFruit() && fruit.hasDevilFruit(TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA);
	}

	@OnlyIn(Dist.CLIENT)
	private void startPlayingDrums(AbstractClientPlayer player, boolean isStarting) {
		Minecraft mc = Minecraft.getInstance();
		if (isStarting) {
			mc.getSoundManager().play(new DrumsOfLiberation(ModSounds.DRUMS_OF_LIBERATION_1.get(), SoundSource.PLAYERS, player, this));
		} else {
			mc.getSoundManager().stop(ModSounds.DRUMS_OF_LIBERATION_1.get().getLocation(), SoundSource.PLAYERS);
			mc.getSoundManager().stop(ModSounds.DRUMS_OF_LIBERATION_2.get().getLocation(), SoundSource.PLAYERS);
		}
	}

	static {
        STRENGTH_MODIFIER = new AbilityAttributeModifier(UUID.fromString("5fc1a28f-7e59-44bf-9d7a-36953e9c700d"), TrueGearFifthAbility.INSTANCE, "Gear Fifth Attack Damage Modifier", 20.0, AttributeModifier.Operation.ADDITION);
		DAMAGE_REDUCTION_MODIFIER = new AbilityAttributeModifier(UUID.fromString("2efdb212-33d0-4fad-b806-4d39d7091ffd"), TrueGearFifthAbility.INSTANCE, "Gear Fifth Resistance Damage Modifier", 2, AttributeModifier.Operation.ADDITION);
		REGEN = new AbilityAttributeModifier(UUID.fromString("e6a409f2-5c6a-409e-a9f3-5b74899d8129"), TrueGearFifthAbility.INSTANCE, "Gear Fifth Regen Modifier", 5, AttributeModifier.Operation.MULTIPLY_TOTAL);
		GRAVITY_REDUCTION_MODIFIER = new AbilityAttributeModifier(UUID.fromString("2efdb212-33d0-7fad-b806-4d39d7091ffd"), TrueGearFifthAbility.INSTANCE, "Gear Fifth Gravity Damage Modifier", -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@OnlyIn(Dist.CLIENT)
	static class DrumsOfLiberation extends AbstractTickableSoundInstance {
		protected AbstractClientPlayer player;
		protected TrueGearFifthAbility ability;


		protected DrumsOfLiberation(SoundEvent event, SoundSource category, AbstractClientPlayer player, TrueGearFifthAbility ability) {
			super(event, category, RandomSource.create());
			this.player = player;
			this.ability = ability;
		}

		@Override
		public void tick() {
			if (player.isAlive() && ability.isContinuous()) {
				float continueTime = ability.getComponent(ModAbilityComponents.CONTINUOUS.get()).map(ContinuousComponent::getContinueTime).orElse(0.0F);
				if (continueTime > 60.0F) {
					this.stop();
					Minecraft.getInstance().getSoundManager().play(new DrumsOfLiberation(ModSounds.DRUMS_OF_LIBERATION_2.get(), SoundSource.PLAYERS, player, ability));
				} else {
					this.volume = Math.clamp(continueTime / 120.0F, 0.0F, 0.5F);
				}
			} else {
				this.stop();
			}
		}

	}
}
