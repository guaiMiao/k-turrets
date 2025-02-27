package dev.buildtool.kturrets;

import dev.buildtool.satako.ItemHandler;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Extends Mob entity because of goals
 */
public abstract class Turret extends Mob implements RangedAttackMob, MenuProvider {
    private static final EntityDataAccessor<CompoundTag> TARGETS = SynchedEntityData.defineId(Turret.class, EntityDataSerializers.COMPOUND_TAG);
    private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(Turret.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Boolean> MOVEABLE = SynchedEntityData.defineId(Turret.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PROTECTION_FROM_PLAYERS = SynchedEntityData.defineId(Turret.class, EntityDataSerializers.BOOLEAN);
    /**
     * Players that are not allied to the owner
     */
    protected Predicate<LivingEntity> alienPlayers = livingEntity -> {
        if (getOwner().isPresent()) {
            return livingEntity instanceof Player && !livingEntity.getUUID().equals(getOwner().get()) && !livingEntity.isAlliedTo(level.getPlayerByUUID(getOwner().get()));
        }
        return false;
    };

    public Turret(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createDefaultAttributes() {
        return createLivingAttributes().add(Attributes.FOLLOW_RANGE, 32).add(Attributes.MOVEMENT_SPEED, 0).add(Attributes.MAX_HEALTH, 60).add(Attributes.ATTACK_DAMAGE, 4).add(Attributes.ARMOR, 3);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        CompoundTag compoundNBT = new CompoundTag();
        List<EntityType<?>> targets = ForgeRegistries.ENTITIES.getValues().stream().filter(entityType1 -> !entityType1.getCategory().isFriendly()).collect(Collectors.toList());
        for (int i = 0; i < targets.size(); i++) {
            compoundNBT.putString("Target#" + i, targets.get(i).getRegistryName().toString());
        }
        compoundNBT.putInt("Count", targets.size());
        entityData.define(TARGETS, compoundNBT);
        entityData.define(OWNER, Optional.empty());
        entityData.define(MOVEABLE, true);
        entityData.define(PROTECTION_FROM_PLAYERS, false);
    }

    public void setTargets(CompoundTag compoundNBT) {
        entityData.set(TARGETS, compoundNBT);
    }

    public CompoundTag getTargets() {
        return entityData.get(TARGETS);
    }

    public Optional<UUID> getOwner() {
        return entityData.get(OWNER);
    }

    public void setOwner(UUID owner) {
        entityData.set(OWNER, Optional.of(owner));
    }

    public void setMoveable(boolean moveable) {
        entityData.set(MOVEABLE, moveable);
    }

    public boolean isMoveable() {
        return entityData.get(MOVEABLE);
    }

    public void setProtectionFromPlayers(boolean protect) {
        entityData.set(PROTECTION_FROM_PLAYERS, protect);
    }

    public boolean isProtectingFromPlayers() {
        return entityData.get(PROTECTION_FROM_PLAYERS);
    }


    @Override
    protected abstract void registerGoals();

    @Override
    public boolean attackable() {
        return false;
    }

    /**
     * By player
     */
    @Override
    public boolean isAttackable() {
        return true;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot p_184582_1_) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot p_184201_1_, ItemStack p_184201_2_) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    protected double getRange() {
        return getAttributeValue(Attributes.FOLLOW_RANGE);
    }

    protected double getDamage() {
        return getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    @Override
    protected InteractionResult mobInteract(Player playerEntity, InteractionHand p_230254_2_) {
        if (canUse(playerEntity)) {
            if (level.isClientSide) {
                openTargetScreen();
            }
            return InteractionResult.SUCCESS;
        } else if (level.isClientSide)
            playerEntity.sendMessage(new TextComponent("k-turrets.turret.not.yours"), Util.NIL_UUID);
        return InteractionResult.PASS;
    }

    protected boolean canUse(Player playerEntity) {
        return !getOwner().isPresent() || getOwner().get().equals(playerEntity.getUUID());
    }

    @OnlyIn(Dist.CLIENT)
    private void openTargetScreen() {
        Minecraft.getInstance().setScreen(new TurretOptionsScreen(this));
    }

    //don't forget to save the inventory
    @Override
    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.put("Targets", getTargets());
        getOwner().ifPresent(uuid1 -> compoundNBT.putUUID("Owner", uuid1));
        compoundNBT.putBoolean("Mobile", isMoveable());
        compoundNBT.putBoolean("Player protection", isProtectingFromPlayers());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        setTargets(compoundNBT.getCompound("Targets"));
        if (compoundNBT.contains("Owner")) {
            UUID uuid = compoundNBT.getUUID("Owner");
            if (!uuid.equals(Util.NIL_UUID))
                setOwner(uuid);
        }
        setMoveable(compoundNBT.getBoolean("Mobile"));
        setProtectionFromPlayers(compoundNBT.getBoolean("Player protection"));
    }

    public List<EntityType<?>> decodeTargets(CompoundTag compoundNBT) {
        int count = compoundNBT.getInt("Count");
        List<EntityType<?>> list = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            String next = compoundNBT.getString("Target#" + i);
            list.add(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(next)));
        }
        return list;
    }

    public CompoundTag encodeTargets(List<EntityType<?>> list) {
        CompoundTag compoundNBT = new CompoundTag();
        for (int i = 0; i < list.size(); i++) {
            EntityType<?> entityType = list.get(i);
            compoundNBT.putString("Target#" + i, entityType.getRegistryName().toString());
        }
        compoundNBT.putInt("Count", list.size());
        return compoundNBT;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        getContainedItems().forEach(itemHandler -> Containers.dropContents(level, blockPosition(), itemHandler.getItems()));
    }

    protected abstract List<ItemHandler> getContainedItems();

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.SHIELD_BLOCK;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        MobEffect effect = effectInstance.getEffect();
        if (effect == MobEffects.POISON || effect == MobEffects.HEAL || effect == MobEffects.HEALTH_BOOST || effect == MobEffects.REGENERATION || effect == MobEffects.WITHER || effect == MobEffects.HUNGER)
            return false;
        return super.canBeAffected(effectInstance);
    }

    @Override
    public boolean canBeCollidedWith() {
        return !isMoveable();
    }

    @Override
    public boolean isPushable() {
        return isMoveable();
    }

    @Override
    public void knockback(double p_147241_, double p_147242_, double p_147243_) {
        if (isMoveable())
            super.knockback(p_147241_, p_147242_, p_147243_);
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.DRAGON_FIREBALL_EXPLODE;
    }

    @Override
    public boolean removeWhenFarAway(double p_213397_1_) {
        return false;
    }

    /**
     * @return appropriate spawn egg
     */
    public Item getSpawnItem() {
        return ForgeSpawnEggItem.fromEntityType(getType());
    }

    @Override
    public boolean isAlliedTo(Entity target) {
        return super.isAlliedTo(target) || (getOwner().isPresent() && (target.getUUID().equals(getOwner().get())) || target instanceof Turret turret && turret.getOwner().isPresent() && turret.getOwner().equals(getOwner()));
    }
}
