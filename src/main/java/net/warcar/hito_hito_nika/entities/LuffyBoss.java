package net.warcar.hito_hito_nika.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.abilities.*;
import net.warcar.hito_hito_nika.entities.goals.*;
import net.warcar.hito_hito_nika.init.GomuEntities;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import xyz.pixelatedw.mineminenomi.abilities.brawler.BrawlerPassiveBonusesAbility;
import xyz.pixelatedw.mineminenomi.abilities.gomu.BouncyAbility;
import xyz.pixelatedw.mineminenomi.api.challenges.InProgressChallenge;
import xyz.pixelatedw.mineminenomi.api.challenges.OPBossEntity;
import xyz.pixelatedw.mineminenomi.api.entities.ai.NPCPhase;
import xyz.pixelatedw.mineminenomi.api.helpers.MobsHelper;
import xyz.pixelatedw.mineminenomi.entities.mobs.IRandomTexture;
import xyz.pixelatedw.mineminenomi.entities.mobs.OPEntity;
import xyz.pixelatedw.mineminenomi.entities.mobs.goals.ImprovedMeleeAttackGoal;
import xyz.pixelatedw.mineminenomi.entities.mobs.goals.JumpOutOfHoleGoal;
import xyz.pixelatedw.mineminenomi.entities.mobs.goals.SprintTowardsTargetGoal;
import xyz.pixelatedw.mineminenomi.entities.mobs.goals.abilities.AlwaysActiveAbilityWrapperGoal;
import xyz.pixelatedw.mineminenomi.entities.mobs.goals.abilities.TakedownKickWrapperGoal;
import xyz.pixelatedw.mineminenomi.entities.mobs.goals.abilities.haki.BusoshokuHakiHardeningWrapperGoal;
import xyz.pixelatedw.mineminenomi.entities.mobs.goals.abilities.haki.BusoshokuHakiInternalDestructionWrapperGoal;
import xyz.pixelatedw.mineminenomi.entities.mobs.goals.abilities.haki.HaoshokuHakiInfusionWrapperGoal;
import xyz.pixelatedw.mineminenomi.entities.mobs.phases.SimplePhase;

public class LuffyBoss extends OPBossEntity<LuffyBoss> implements IRandomTexture {
    private static final DataParameter<Boolean> POST_TS = EntityDataManager.defineId(LuffyBoss.class, DataSerializers.BOOLEAN);

    private final NPCPhase<LuffyBoss> firstPhase = new SimplePhase<>("First phase", this);
    private final NPCPhase<LuffyBoss> secondPhase = new SimplePhase<>("Second phase", this);
    private final NPCPhase<LuffyBoss> thirdPhase = new SimplePhase<>("Third phase", this);
    private final NPCPhase<LuffyBoss> lastPhase = new SimplePhase<>("Last phase", this);

    public LuffyBoss(InProgressChallenge inProgressChallenge) {
        super(GomuEntities.LUFFY, inProgressChallenge);
        this.setPostTs(!inProgressChallenge.isStandardDifficulty());
        this.devilFruitData.setDevilFruit(TrueGomuGomuNoMi.HITO_HITO_NO_MI_NIKA);
        if (inProgressChallenge.isStandardDifficulty()) {
            this.entityStats.setDoriki(3500);
        } else if (inProgressChallenge.isHardDifficulty()) {
            this.getAttribute(Attributes.ARMOR).setBaseValue(10);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(7);
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(500);
            this.entityStats.setDoriki(7000);
            this.hakiCapability.setBusoshokuHakiExp(55);
            this.hakiCapability.setKenbunshokuHakiExp(50);
        } else {
            this.getAttribute(Attributes.ARMOR).setBaseValue(20);
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(12);
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(750);
            this.entityStats.setDoriki(10000);
            this.hakiCapability.setBusoshokuHakiExp(100);
            this.hakiCapability.setKenbunshokuHakiExp(100);
        }
        MobsHelper.addBasicNPCGoals(this);
        this.goalSelector.addGoal(0, new LuffyPhaseSwitcherGoal(this));
        this.goalSelector.addGoal(0, new AlwaysActiveAbilityWrapperGoal<>(this, GomuMorphsAbility.INSTANCE));
        this.goalSelector.addGoal(0, new AlwaysActiveAbilityWrapperGoal<>(this, BrawlerPassiveBonusesAbility.INSTANCE));
        this.goalSelector.addGoal(0, new AlwaysActiveAbilityWrapperGoal<>(this, BouncyAbility.INSTANCE));
        this.goalSelector.addGoal(0, new JumpOutOfHoleGoal(this));
        this.goalSelector.addGoal(1, new SprintTowardsTargetGoal(this));
        this.goalSelector.addGoal(1, new ImprovedMeleeAttackGoal(this, 1, true));
        this.goalSelector.addGoal(2, new TakedownKickWrapperGoal(this));
        if (inProgressChallenge.isStandardDifficulty()) {
            this.secondPhase.addGoal(2, new BulletWrapperGoal<>(this));
            this.secondPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearSecondAbility.INSTANCE));
            this.thirdPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearThirdAbility.INSTANCE));
            this.thirdPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearSecondAbility.INSTANCE));
            this.thirdPhase.addGoal(2, new BulletWrapperGoal<>(this));
            this.goalSelector.addGoal(3, new FusenWrapperGoal(this));
        } else if (inProgressChallenge.isHardDifficulty()) {
            this.goalSelector.addGoal(0, new BusoshokuHakiHardeningWrapperGoal(this));
            this.goalSelector.addGoal(2, new BulletWrapperGoal<>(this));
            this.secondPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearSecondAbility.INSTANCE));
            this.secondPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearThirdAbility.INSTANCE));
            this.thirdPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearFourthAbility.INSTANCE));
        } else {
            this.goalSelector.addGoal(0, new BusoshokuHakiHardeningWrapperGoal(this));
            this.goalSelector.addGoal(0, new BusoshokuHakiInternalDestructionWrapperGoal(this));
            this.goalSelector.addGoal(2, new BulletWrapperGoal<>(this));
            this.firstPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearSecondAbility.INSTANCE));
            this.firstPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearThirdAbility.INSTANCE));
            this.secondPhase.addGoal(0, new HaoshokuHakiInfusionWrapperGoal(this));
            this.secondPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearSecondAbility.INSTANCE));
            this.secondPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearThirdAbility.INSTANCE));
        }
        this.goalSelector.addGoal(2, new PistolWrapperGoal<>(this));
        this.goalSelector.addGoal(2, new GatlingWrapperGoal<>(this));
        this.goalSelector.addGoal(2, new BazookaWrapperGoal<>(this));
        this.getPhaseManager().setPhase(firstPhase);
    }

    public LuffyBoss(EntityType type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return OPEntity.createAttributes().add(Attributes.MAX_HEALTH, 300).add(Attributes.ATTACK_DAMAGE, 5);
    }

    @Override
    public ResourceLocation getCurrentTexture() {
        if (!this.isPostTs()) {
            return new ResourceLocation(HitoHitoNoMiNikaMod.MOD_ID, "textures/entities/luffy_pre_ts.png");
        }
        return new ResourceLocation(HitoHitoNoMiNikaMod.MOD_ID, "textures/entities/luffy_post_ts.png");
    }

    @Override
    public ResourceLocation getDefaultTexture() {
        return new ResourceLocation(HitoHitoNoMiNikaMod.MOD_ID, "textures/entities/luffy_pre_ts.png");
    }

    public boolean isFirstPhaseActive() {
        return this.firstPhase.isActive(this);
    }

    public void startSecondPhase() {
        this.getPhaseManager().setPhase(secondPhase);
    }

    public boolean isSecondPhaseActive() {
        return this.secondPhase.isActive(this);
    }

    public void startThirdPhase() {
        this.getPhaseManager().setPhase(thirdPhase);
    }

    public boolean isThirdPhaseActive() {
        return this.thirdPhase.isActive(this);
    }

    public void startLastPhase() {
        this.getPhaseManager().setPhase(lastPhase);
    }

    public boolean isPostTs() {
        return this.entityData.get(POST_TS);
    }

    public void setPostTs(boolean postTs) {
        this.entityData.set(POST_TS, postTs);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(POST_TS, false);
    }
}
