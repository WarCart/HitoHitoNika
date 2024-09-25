package net.warcar.hito_hito_nika.abilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.common.Mod;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import xyz.pixelatedw.mineminenomi.api.abilities.*;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ChangeStatsComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.ContinuousComponent;
import xyz.pixelatedw.mineminenomi.api.abilities.components.SkinOverlayComponent;
import xyz.pixelatedw.mineminenomi.api.helpers.AbilityHelper;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.entitystats.EntityStatsCapability;
import xyz.pixelatedw.mineminenomi.init.*;
import xyz.pixelatedw.mineminenomi.wypi.WyHelper;

import java.util.UUID;

import static xyz.pixelatedw.mineminenomi.api.abilities.AbilityOverlay.RenderType.ENERGY;

public class TrueGearFifthAbility extends Ability {
	public static final AbilityCore<TrueGearFifthAbility> INSTANCE;
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
		this.isNew = true;
		overlayComponent = new SkinOverlayComponent(this, new AbilityOverlay.Builder().setColor("#ffffff30").setRenderType(ENERGY).build());
		continuousComponent = new ContinuousComponent(this, true);
		this.continuousComponent.addEndEvent(this::afterContinuityStop);
		this.addUseEvent(this::onStartContinuity);
		this.continuousComponent.addTickEvent(this::duringContinuity);
		statsComponent = new ChangeStatsComponent(this);
		this.statsComponent.addAttributeModifier(ModAttributes.PUNCH_DAMAGE, STRENGTH_MODIFIER);
		this.statsComponent.addAttributeModifier(ModAttributes.DAMAGE_REDUCTION, DAMAGE_REDUCTION_MODIFIER);
		this.statsComponent.addAttributeModifier(ModAttributes.REGEN_RATE, REGEN);
		this.statsComponent.addAttributeModifier(ForgeMod.ENTITY_GRAVITY, GRAVITY_REDUCTION_MODIFIER);
		this.addComponents(continuousComponent, statsComponent, overlayComponent);
	}

	private void duringContinuity(LivingEntity entity, IAbility ability) {
		if (entity.isOnGround() && !this.playJumpSound) {
			this.playJumpSound = true;
		} else if (!entity.isOnGround() && this.playJumpSound) {
			SoundEvent sfx;
			if (entity.getRandom().nextBoolean()) {
				sfx = ModSounds.BOUNCE_2.get();
			} else {
				sfx = ModSounds.BOUNCE_1.get();
			}
			entity.level.playSound(null, entity.blockPosition(), sfx, SoundCategory.PLAYERS, 2.0F, 0.75F + entity.getRandom().nextFloat() / 2.0F);
			this.playJumpSound = false;
		}
	}

	private void onStartContinuity(LivingEntity player, IAbility ability) {
		if (this.continuousComponent.isContinuous()) {
			this.continuousComponent.stopContinuity(player);
			return;
		}
		if (!TrueGomuHelper.canActivateGear(AbilityDataCapability.get(player), INSTANCE)) {
			player.sendMessage(ModI18n.ABILITY_MESSAGE_GEAR_ACTIVE, Util.NIL_UUID);
			return;
		} else if (!canUnlock(player)) {
			player.sendMessage(new TranslationTextComponent("text.mineminenomi.too_weak"), Util.NIL_UUID);
			return;
		}
		IAbilityData props = AbilityDataCapability.get(player);
		GomuMorphsAbility morphs = props.getPassiveAbility(GomuMorphsAbility.INSTANCE);
		if (morphs != null)
			morphs.updateModes();
		if (player instanceof ClientPlayerEntity) {
			this.startPlayingDrums((ClientPlayerEntity) player, true);
		}
		this.continuousComponent.startContinuity(player, (float) (EntityStatsCapability.get(player).getDoriki()) * .06f);
		this.overlayComponent.showAll(player);
	}

	private void afterContinuityStop(LivingEntity player, IAbility abl) {
		AbilityHelper.disableAbilities(player, (int) this.continuousComponent.getContinueTime(), ability -> ability != this);
		player.addEffect(new EffectInstance(ModEffects.PARALYSIS.get(), (int) (this.continuousComponent.getContinueTime() / 4), 1, true, true));
		this.cooldownComponent.startCooldown(player, this.continuousComponent.getContinueTime());
		IAbilityData props = AbilityDataCapability.get(player);
		GomuMorphsAbility morphs = props.getPassiveAbility(GomuMorphsAbility.INSTANCE);
		if (morphs != null)
			morphs.updateModes();
		if (player instanceof ClientPlayerEntity) {
			this.startPlayingDrums((ClientPlayerEntity) player, false);
		}
		this.overlayComponent.hideAll(player);
	}

	protected static boolean canUnlock(LivingEntity user) {
		return EntityStatsCapability.get(user).getDoriki() * .003d >= 25d && DevilFruitCapability.get(user).hasAwakenedFruit() && TrueGomuHelper.hasFruit(user, new ResourceLocation("mineminenomi", "gomu_gomu_no_mi"));
	}

	@OnlyIn(Dist.CLIENT)
	private void startPlayingDrums(ClientPlayerEntity player, boolean isStarting) {
		Minecraft mc = Minecraft.getInstance();
		if (isStarting) {
			mc.getSoundManager().play(new DrumsOfLiberation(ModSounds.DRUMS_OF_LIBERATION_1.get(), SoundCategory.PLAYERS, player, this));
		} else {
			mc.getSoundManager().stop(ModSounds.DRUMS_OF_LIBERATION_1.get().getLocation(), SoundCategory.PLAYERS);
			mc.getSoundManager().stop(ModSounds.DRUMS_OF_LIBERATION_2.get().getLocation(), SoundCategory.PLAYERS);
		}
	}

	static {
		INSTANCE = new AbilityCore.Builder<>("Gear Fifth", AbilityCategory.DEVIL_FRUITS, TrueGearFifthAbility::new).setUnlockCheck(TrueGearFifthAbility::canUnlock)
				.addDescriptionLine("Awakening ability, that makes you insanely powerful").build();
		STRENGTH_MODIFIER = new AbilityAttributeModifier(UUID.fromString("5fc1a28f-7e59-44bf-9d7a-36953e9c700d"), TrueGearFifthAbility.INSTANCE, "Gear Fifth Attack Damage Modifier", 20.0, AttributeModifier.Operation.ADDITION);
		DAMAGE_REDUCTION_MODIFIER = new AbilityAttributeModifier(UUID.fromString("2efdb212-33d0-4fad-b806-4d39d7091ffd"), TrueGearFifthAbility.INSTANCE, "Gear Fifth Resistance Damage Modifier", 40, AttributeModifier.Operation.ADDITION);
		REGEN = new AbilityAttributeModifier(UUID.fromString("e6a409f2-5c6a-409e-a9f3-5b74899d8129"), TrueGearFifthAbility.INSTANCE, "Gear Fifth Regen Modifier", 5, AttributeModifier.Operation.MULTIPLY_TOTAL);
		GRAVITY_REDUCTION_MODIFIER = new AbilityAttributeModifier(UUID.fromString("2efdb212-33d0-7fad-b806-4d39d7091ffd"), TrueGearFifthAbility.INSTANCE, "Gear Fifth Gravity Damage Modifier", -0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	static class DrumsOfLiberation extends TickableSound {
		protected ClientPlayerEntity player;
		protected TrueGearFifthAbility ability;


		protected DrumsOfLiberation(SoundEvent event, SoundCategory category, ClientPlayerEntity player, TrueGearFifthAbility ability) {
			super(event, category);
			this.player = player;
			this.ability = ability;
		}

		@Override
		public void tick() {
			if (player.isAlive() && ability.isContinuous()) {
				float continueTime = ability.getComponent(ModAbilityKeys.CONTINUOUS).map(ContinuousComponent::getContinueTime).orElse(0.0F);
				if (continueTime > 60.0F) {
					this.stop();
					Minecraft.getInstance().getSoundManager().play(new DrumsOfLiberation(ModSounds.DRUMS_OF_LIBERATION_2.get(), SoundCategory.PLAYERS, player, ability));
				} else {
					this.volume = MathHelper.clamp(continueTime / 120.0F, 0.0F, 0.5F);
				}
			} else {
				HitoHitoNoMiNikaMod.LOGGER.info("T");
				this.stop();
			}
		}

	}
}
