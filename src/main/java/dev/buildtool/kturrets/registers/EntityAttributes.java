package dev.buildtool.kturrets.registers;

import dev.buildtool.kturrets.KTurrets;
import dev.buildtool.kturrets.Turret;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttributes {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent attributeCreationEvent) {
        attributeCreationEvent.put(TEntities.ARROW_TURRET.get(), Turret.createDefaultAttributes().add(Attributes.FOLLOW_RANGE, KTurrets.ARROW_TURRET_RANGE.get()).add(Attributes.MAX_HEALTH, KTurrets.ARROW_TURRET_HEALTH.get()).add(Attributes.ARMOR, KTurrets.ARROW_TURRET_ARMOR.get()).build());
        attributeCreationEvent.put(TEntities.BULLET_TURRET.get(), Turret.createDefaultAttributes().add(Attributes.FOLLOW_RANGE, KTurrets.BULLET_TURRET_RANGE.get()).add(Attributes.MAX_HEALTH, KTurrets.BULLET_TURRET_HEALTH.get()).add(Attributes.ARMOR, KTurrets.BULLET_TURRET_ARMOR.get()).build());
        attributeCreationEvent.put(TEntities.FIRE_CHARGE_TURRET.get(), Turret.createDefaultAttributes().add(Attributes.FOLLOW_RANGE, KTurrets.CHARGE_TURRET_RANGE.get()).add(Attributes.MAX_HEALTH, KTurrets.CHARGE_TURRET_HEALTH.get()).add(Attributes.ARMOR, KTurrets.CHARGE_TURRET_ARMOR.get()).build());
        attributeCreationEvent.put(TEntities.BRICK_TURRET.get(), Turret.createDefaultAttributes().add(Attributes.FOLLOW_RANGE, KTurrets.BRICK_TURRET_RANGE.get()).add(Attributes.MAX_HEALTH, KTurrets.BRICK_TURRET_HEALTH.get()).add(Attributes.ARMOR, KTurrets.BRICK_TURRET_ARMOR.get()).build());
        attributeCreationEvent.put(TEntities.GAUSS_TURRET.get(), Turret.createDefaultAttributes().add(Attributes.FOLLOW_RANGE, KTurrets.GAUSS_TURRET_RANGE.get()).add(Attributes.MAX_HEALTH, KTurrets.GAUSS_TURRET_HEALTH.get()).add(Attributes.ARMOR, KTurrets.GAUSS_TURRET_ARMOR.get()).build());
        attributeCreationEvent.put(TEntities.COBBLE_TURRET.get(), Turret.createDefaultAttributes().add(Attributes.FOLLOW_RANGE, KTurrets.COBBLE_TURRET_RANGE.get()).add(Attributes.MAX_HEALTH, KTurrets.COBBLE_TURRET_HEALTH.get()).add(Attributes.ARMOR, KTurrets.COBBLE_TURRET_ARMOR.get()).build());
    }
}
