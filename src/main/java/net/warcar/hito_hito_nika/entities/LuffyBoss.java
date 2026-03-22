package net.warcar.hito_hito_nika.entities;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.warcar.hito_hito_nika.HitoHitoNoMiNikaMod;
import net.warcar.hito_hito_nika.abilities.*;
import net.warcar.hito_hito_nika.entities.goals.*;
import net.warcar.hito_hito_nika.init.GomuEffects;
import net.warcar.hito_hito_nika.init.GomuEntities;
import net.warcar.hito_hito_nika.init.TrueGomuGomuNoMi;
import xyz.pixelatedw.mineminenomi.abilities.brawler.BrawlerPassiveBonusesAbility;
import xyz.pixelatedw.mineminenomi.abilities.gomu.BouncyAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiHardeningAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.BusoshokuHakiInternalDestructionAbility;
import xyz.pixelatedw.mineminenomi.abilities.haki.HaoshokuHakiInfusionAbility;
import xyz.pixelatedw.mineminenomi.api.challenges.InProgressChallenge;
import xyz.pixelatedw.mineminenomi.api.entities.ai.NPCPhase;
import xyz.pixelatedw.mineminenomi.api.entities.ai.SimplePhase;
import xyz.pixelatedw.mineminenomi.api.helpers.MobsHelper;
import xyz.pixelatedw.mineminenomi.entities.ai.goals.ImprovedMeleeAttackGoal;
import xyz.pixelatedw.mineminenomi.entities.ai.goals.JumpOutOfHoleGoal;
import xyz.pixelatedw.mineminenomi.entities.ai.goals.SprintTowardsTargetGoal;
import xyz.pixelatedw.mineminenomi.entities.ai.goals.abilities.ActiveGuardAbilityWrapperGoal;
import xyz.pixelatedw.mineminenomi.entities.ai.goals.abilities.AlwaysActiveAbilityWrapperGoal;
import xyz.pixelatedw.mineminenomi.entities.ai.goals.abilities.HakiAbilityWrapperGoal;
import xyz.pixelatedw.mineminenomi.entities.mobs.OPBossEntity;
import xyz.pixelatedw.mineminenomi.entities.mobs.OPEntity;
import xyz.pixelatedw.mineminenomi.init.ModEffects;

public class LuffyBoss extends OPBossEntity {
    private static final EntityDataAccessor<Boolean> POST_TS = SynchedEntityData.defineId(LuffyBoss.class, EntityDataSerializers.BOOLEAN);

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
        this.goalSelector.addGoal(0, new AlwaysActiveAbilityWrapperGoal<>(this, BrawlerPassiveBonusesAbility.INSTANCE.get()));
        this.goalSelector.addGoal(0, new AlwaysActiveAbilityWrapperGoal<>(this, BouncyAbility.INSTANCE.get()));
        this.goalSelector.addGoal(0, new JumpOutOfHoleGoal(this));
        this.goalSelector.addGoal(1, new SprintTowardsTargetGoal(this));
        this.goalSelector.addGoal(1, new ImprovedMeleeAttackGoal(this, 1, true));
        if (inProgressChallenge.isStandardDifficulty()) {
            this.secondPhase.addGoal(2, new BulletWrapperGoal<>(this));
            this.secondPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearSecondAbility.INSTANCE));
            this.thirdPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearThirdAbility.INSTANCE));
            this.thirdPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearSecondAbility.INSTANCE));
            this.thirdPhase.addGoal(2, new BulletWrapperGoal<>(this));
            this.goalSelector.addGoal(3, new ActiveGuardAbilityWrapperGoal<>(this, GomuFusenAbility.INSTANCE));
        } else if (inProgressChallenge.isHardDifficulty()) {
            this.goalSelector.addGoal(0, new HakiAbilityWrapperGoal<>(this, BusoshokuHakiHardeningAbility.INSTANCE.get()));
            this.goalSelector.addGoal(2, new BulletWrapperGoal<>(this));
            this.secondPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearSecondAbility.INSTANCE));
            this.secondPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearThirdAbility.INSTANCE));
            this.thirdPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearFourthAbility.INSTANCE));
        } else {
            this.goalSelector.addGoal(0, new HakiAbilityWrapperGoal<>(this, BusoshokuHakiHardeningAbility.INSTANCE.get()));
            this.goalSelector.addGoal(0, new HakiAbilityWrapperGoal<>(this, BusoshokuHakiInternalDestructionAbility.INSTANCE.get()));
            this.goalSelector.addGoal(2, new BulletWrapperGoal<>(this));
            this.firstPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearSecondAbility.INSTANCE));
            this.firstPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearThirdAbility.INSTANCE));
            this.goalSelector.addGoal(0, new HakiAbilityWrapperGoal<>(this, HaoshokuHakiInfusionAbility.INSTANCE.get()));
            this.secondPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearSecondAbility.INSTANCE));
            this.secondPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearThirdAbility.INSTANCE));
            this.secondPhase.addGoal(1, new GearFourthWrapperGoal(this, 0.75f));
            this.thirdPhase.addGoal(1, new GearFourthWrapperGoal(this, 0));
            this.lastPhase.addGoal(1, new GearWrapperGoal<>(this, TrueGearFifthAbility.INSTANCE));
        }
        this.goalSelector.addGoal(2, new PistolWrapperGoal<>(this));
        this.goalSelector.addGoal(2, new GatlingWrapperGoal<>(this));
        this.goalSelector.addGoal(2, new BazookaWrapperGoal<>(this));
        this.getPhaseManager().setPhase(firstPhase);
    }

    public LuffyBoss(EntityType type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return OPEntity.createAttributes().add(Attributes.MAX_HEALTH, 300).add(Attributes.ATTACK_DAMAGE, 5);
    }

    public ResourceLocation getCurrentTexture() {
        if (!this.isPostTs()) {
            return ResourceLocation.fromNamespaceAndPath(HitoHitoNoMiNikaMod.MOD_ID, "textures/entities/luffy_pre_ts.png");
        }
        return ResourceLocation.fromNamespaceAndPath(HitoHitoNoMiNikaMod.MOD_ID, "textures/entities/luffy_post_ts.png");
    }

    public ResourceLocation getDefaultTexture() {
        return ResourceLocation.fromNamespaceAndPath(HitoHitoNoMiNikaMod.MOD_ID, "textures/entities/luffy_pre_ts.png");
    }

    @Override
    public void die(DamageSource p_70645_1_) {
        if (this.getChallengeInfo().isDifficultyUltimate() && !this.isLastPhase()) {
            devilFruitData.setAwakenedFruit(true);
            this.setHealth(5);
            this.addEffect(new MobEffectInstance(GomuEffects.GOMU_REVIVE.get(), 600, 1, true, false));
            this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 12, true, true));
            this.addEffect(new MobEffectInstance(ModEffects.UNCONSCIOUS.get(), 600, 1, true, true));
            this.startLastPhase();
        } else {
            super.die(p_70645_1_);
        }
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

    public boolean isLastPhase() {
        return this.lastPhase.isActive(this);
    }
}
