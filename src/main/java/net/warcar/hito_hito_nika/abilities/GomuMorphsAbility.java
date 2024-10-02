package net.warcar.hito_hito_nika.abilities;

import net.warcar.fruit_progression.models.IMultiModelMorph;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import net.warcar.hito_hito_nika.models.*;
import net.warcar.hito_hito_nika.morphs.*;
import xyz.pixelatedw.mineminenomi.wypi.WyNetwork;
import xyz.pixelatedw.mineminenomi.packets.server.ability.SRecalculateEyeHeightPacket;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncDevilFruitPacket;
import xyz.pixelatedw.mineminenomi.packets.server.SSyncAbilityDataPacket;
import xyz.pixelatedw.mineminenomi.packets.client.CSyncZoanPacket;
import xyz.pixelatedw.mineminenomi.items.AkumaNoMiItem;
import xyz.pixelatedw.mineminenomi.entities.zoan.MegaMorphInfo;
import xyz.pixelatedw.mineminenomi.entities.zoan.GearFourthMorphInfo;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.IDevilFruit;
import xyz.pixelatedw.mineminenomi.data.entity.devilfruit.DevilFruitCapability;
import xyz.pixelatedw.mineminenomi.data.entity.ability.IAbilityData;
import xyz.pixelatedw.mineminenomi.data.entity.ability.AbilityDataCapability;
import xyz.pixelatedw.mineminenomi.api.morph.MorphModel;
import xyz.pixelatedw.mineminenomi.api.morph.MorphInfo;
import xyz.pixelatedw.mineminenomi.api.abilities.PassiveAbility;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCore;
import xyz.pixelatedw.mineminenomi.api.abilities.AbilityCategory;

import net.minecraftforge.event.entity.EntityEvent.Size;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class GomuMorphsAbility extends PassiveAbility implements IMultiModelMorph {
	public static final AbilityCore<GomuMorphsAbility> INSTANCE;
	private static final EntitySize STANDING_SIZE_SNAKEMAN = EntitySize.scalable(0.7F, 1.9875F);
	private static final EntitySize CROUCHING_SIZE_SNAKEMAN = EntitySize.scalable(0.8F, 1.8F);
	private static final EntitySize STANDING_SIZE_FUSEN = EntitySize.scalable(1.6F, 2.8F);
	private static final EntitySize CROUCHING_SIZE_FUSEN = EntitySize.scalable(1.7F, 2.6F);
	private static final EntitySize STANDING_SIZE_SMALL = EntitySize.scalable(0.2F, 1.1F);
	private static final EntitySize CROUCHING_SIZE_SMALL = EntitySize.scalable(0.3F, 1F);
	private static final EntitySize STANDING_SIZE_GIGANT_FUSEN = EntitySize.scalable(3.6F, 4.8F);
	private static final EntitySize CROUCHING_SIZE_GIGANT_FUSEN = EntitySize.scalable(3.7F, 4.6F);

	private static final MorphInfo GIGANT = new GomuGigantMorph();

	private static final MorphInfo BOUNDMAN = new GearFourthBoundmanMorph();

	private static final MorphInfo SNAKEMAN = new GearFourthSnakemanMorph();

	private static final MorphInfo FUSEN = new FusenMorph();

	private static final MorphInfo SMALL = new SmallMorph();

	private static final MorphInfo GIGANT_FUSEN = new GigantFusenMorph();

	private boolean needsUpdate = false;

	public GomuMorphsAbility(AbilityCore core) {
		super(core);
		this.duringPassiveEvent = this::update;
	}

	private void update(PlayerEntity player) {
		if (this.needsUpdate) {
			IDevilFruit props = DevilFruitCapability.get(player);
			IAbilityData abilityProps = AbilityDataCapability.get(player);
			if (props.getZoanPoint().isEmpty()) {
				props.setZoanPoint("");
			}
			if (this.isTransformationActive(player)) {
				props.setZoanPoint(this.getTransformation(player).getForm());
			} else {
				props.setZoanPoint("");
			}
			WyNetwork.sendToAll(new SSyncDevilFruitPacket(player.getId(), props));
			WyNetwork.sendToAll(new SSyncAbilityDataPacket(player.getId(), abilityProps));
			WyNetwork.sendToAll(new CSyncZoanPacket(player.getId()));
			MinecraftForge.EVENT_BUS.post(new Size(player, player.getPose(), player.getDimensions(player.getPose()), player.getEyeHeight()));
			WyNetwork.sendTo(new SRecalculateEyeHeightPacket(player.getId()), player);
			player.refreshDimensions();
			this.needsUpdate = false;
		}
	}

	public void updateModes() {
		this.needsUpdate = true;
	}

	public boolean isTransformationActive(LivingEntity target) {
		IAbilityData props = AbilityDataCapability.get(target);
		return (TrueGomuHelper.isSmall(props) || TrueGomuHelper.hasGigantActive(props)|| TrueGomuHelper.hasGearFourthActive(props) || TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE)
				|| (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasAbilityActive(props, TrueGomuRocket.INSTANCE))) && !this.isPaused();
	}

	public MorphInfo getTransformation(LivingEntity target) {
		IAbilityData props = AbilityDataCapability.get(target);
		if (TrueGomuHelper.hasGearFourthActive(props)) {
			TrueGearFourthAbility g4 = props.getEquippedAbility(TrueGearFourthAbility.INSTANCE);
			if (g4.isSnakeman()) {
				return SNAKEMAN;
			} else if (g4.isBoundman()) {
				return BOUNDMAN;
			}
		} else if (TrueGomuHelper.hasGigantActive(props)) {
			return GIGANT;
		} else if (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE)) {
			return GIGANT_FUSEN;
		} else if (TrueGomuHelper.hasAbilityActive(props, GomuFusenAbility.INSTANCE) || (TrueGomuHelper.hasGearThirdActive(props) && TrueGomuHelper.hasAbilityActive(props, TrueGomuRocket.INSTANCE))) {
			return FUSEN;
		} else if (TrueGomuHelper.isSmall(props)) {
			return SMALL;
		}
		return null;
	}

	static {
		INSTANCE = (new AbilityCore.Builder<>("Gomu Transformations", AbilityCategory.DEVIL_FRUITS, GomuMorphsAbility::new)).setHidden().build();
	}
}
