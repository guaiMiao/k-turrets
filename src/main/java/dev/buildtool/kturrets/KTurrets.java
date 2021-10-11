package dev.buildtool.kturrets;

import dev.buildtool.kturrets.packets.DismantleTurret;
import dev.buildtool.kturrets.packets.TurretTargets;
import dev.buildtool.kturrets.registers.TContainers;
import dev.buildtool.kturrets.registers.TEntities;
import dev.buildtool.kturrets.registers.TItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod(KTurrets.ID)
public class KTurrets {
    public static final String ID = "k-turrets";
    static private final String NP = "1.0";
    public static SimpleChannel channel;

    public KTurrets() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        TEntities.ENTITIES.register(eventBus);
        TItems.ITEMS.register(eventBus);
        TContainers.CONTAINERS.register(eventBus);
        channel = NetworkRegistry.newSimpleChannel(new ResourceLocation(ID, "network"), () -> NP, NP::equals, NP::equals);
        int packetIndex = 0;
        channel.registerMessage(packetIndex++, TurretTargets.class, (turretTargets, packetBuffer) -> {
            packetBuffer.writeInt(turretTargets.turretID);
            packetBuffer.writeNbt(turretTargets.targets);
        }, packetBuffer -> {
            int id = packetBuffer.readInt();
            CompoundNBT compoundNBT = packetBuffer.readNbt();
            return new TurretTargets(compoundNBT, id);
        }, (turretTargets, contextSupplier) -> {
            NetworkEvent.Context context = contextSupplier.get();
            ServerWorld serverWorld = context.getSender().getLevel();
            Entity entity = serverWorld.getEntity(turretTargets.turretID);
            if (entity instanceof Turret) {
                Turret turret = (Turret) entity;
                turret.setTargets(turretTargets.targets);
                context.setPacketHandled(true);
            }
        });
        channel.registerMessage(packetIndex, DismantleTurret.class, (dismantleTurret, packetBuffer) -> packetBuffer.writeInt(dismantleTurret.id),
                packetBuffer -> new DismantleTurret(packetBuffer.readInt()),
                (dismantleTurret, contextSupplier) -> {
                    ServerWorld serverWorld = contextSupplier.get().getSender().getLevel();
                    Entity entity = serverWorld.getEntity(dismantleTurret.id);
                    if (entity instanceof Turret) {
                        Turret turret = (Turret) entity;
                        turret.getContainedItems().forEach(itemHandler -> InventoryHelper.dropContents(serverWorld, turret.blockPosition(), itemHandler.getItems()));
                        turret.remove();
                        SpawnEggItem eggItem = SpawnEggItem.byId(turret.getType());
                        serverWorld.addFreshEntity(new ItemEntity(serverWorld, turret.getX(), turret.getY(), turret.getZ(), new ItemStack(eggItem)));
                        contextSupplier.get().setPacketHandled(true);
                    }
                });
    }
}
