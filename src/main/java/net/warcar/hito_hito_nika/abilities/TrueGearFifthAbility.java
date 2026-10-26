package net.warcar.hito_hito_nika.abilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
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
import xyz.pixelatedw.mineminenomi.data.entity.AbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.DevilFruitData;
import xyz.pixelatedw.mineminenomi.init.ModAbilityComponents;
import xyz.pixelatedw.mineminenomi.init.ModAttributes;
import xyz.pixelatedw.mineminenomi.init.ModEffects;
import xyz.pixelatedw.mineminenomi.init.ModSounds;
import xyz.pixelatedw.mineminenomi.init.i18n.ModI18nAbilities;

import java.util.HashMap;

import static xyz.pixelatedw.mineminenomi.api.abilities.AbilityOverlay.RenderType.ENERGY;

public class TrueGearFifthAbility extends Ability {
	private static final Component[] DESCRIPTION = TrueGomuHelper.registerDescriptionText("gear_fifth", new Pair[]{ImmutablePair.of("The absolute peak bringing joy and freedom to those around them.", (Object)null)});
	public static final DeferredHolder<AbilityCore<?>, AbilityCore<TrueGearFifthAbility>> INSTANCE = TrueGomuGomuNoMi.registerAbility(new AbilityCore.Builder<>("gear_fifth", "Gear Fifth", AbilityCategory.DEVIL_FRUITS, TrueGearFifthAbility::new).setUnlockCheck(TrueGearFifthAbility::canUnlock)
            .addDescriptionLine(DESCRIPTION).addAdvancedDescriptionLine(AbilityDescriptionLine.NEW_LINE, ChangeStatsComponent.getTooltip()));
	private final ContinuousComponent continuousComponent;
	private final ChangeStatsComponent statsComponent;
	private final SkinOverlayComponent overlayComponent;
	private static final AttributeModifier DAMAGE_REDUCTION_MODIFIER;
	private static final AttributeModifier STRENGTH_MODIFIER;
	private static final AttributeModifier REGEN;
	private static final AttributeModifier GRAVITY_REDUCTION_MODIFIER;
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
		this.statsComponent.addAttributeModifier(Attributes.GRAVITY, GRAVITY_REDUCTION_MODIFIER);
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
		if (!TrueGomuHelper.canActivateGear(AbilityData.get(player).get(), this)) {
			player.sendSystemMessage(ModI18nAbilities.MESSAGE_GEAR_ACTIVE);
			return;
		}
		AbilityData props = AbilityData.get(player).get();
		GomuMorphsAbility morphs = props.getPassiveAbility(GomuMorphsAbility.INSTANCE.get());
		if (morphs != null)
			morphs.updateModes(player);
		if (player instanceof Player && player.level().isClientSide) {
			this.startPlayingDrums((AbstractClientPlayer) player, true);
		}
		this.statsComponent.applyModifiers(player);
		this.continuousComponent.startContinuity(player, (float) EquationHelper.parseEquation(CommonConfig.INSTANCE.getG5Length(), player, new HashMap<>()).getValue() * 20);
		this.overlayComponent.showAll(player);
		if (player instanceof ServerPlayer sp) {
			TrueGomuHelper.unlockAdvancement(sp, "the_peak", "gear_fifth");
		}
	}

	private void afterContinuityStop(LivingEntity player, IAbility abl) {
		float time = (float) EquationHelper.parseEquation(CommonConfig.INSTANCE.getG5Cooldown(), player, TrueGomuHelper.getBasicBonusData(this.continuousComponent.getContinueTime())).getValue();
		player.addEffect(new MobEffectInstance(ModEffects.UNCONSCIOUS, (int) (time * 0.25f), 1, true, true));
		this.cooldownComponent.startCooldown(player, time * 0.75f);
		AbilityData props = AbilityData.get(player).get();
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
		DevilFruitData fruit = DevilFruitData.get(user).get();
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
        STRENGTH_MODIFIER = new AttributeModifier(INSTANCE.getId(), 20.0, AttributeModifier.Operation.ADD_VALUE);
		DAMAGE_REDUCTION_MODIFIER = new AttributeModifier(INSTANCE.getId(), 2, AttributeModifier.Operation.ADD_VALUE);
		REGEN = new AttributeModifier(INSTANCE.getId(), 5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
		GRAVITY_REDUCTION_MODIFIER = new AttributeModifier(INSTANCE.getId(), -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
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
				if (continueTime > 60) {
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
