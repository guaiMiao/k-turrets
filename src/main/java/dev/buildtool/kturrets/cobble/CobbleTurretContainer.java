package dev.buildtool.kturrets.cobble;

import dev.buildtool.kturrets.registers.TContainers;
import dev.buildtool.satako.Container2;
import dev.buildtool.satako.ItemHandlerSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CobbleTurretContainer extends Container2 {
    public CobbleTurretContainer(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        super(TContainers.COBBLE_TURRET, i);
        CobbleTurret cobbleTurret = (CobbleTurret) playerInventory.player.level.getEntity(packetBuffer.readInt());
        int index = 0;
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 9; k++) {
                addSlot(new ItemHandlerSlot(cobbleTurret.cobblestone, index++, k * 18, j * 18));
            }
        }

        addPlayerInventory(0, 4 * 18, playerInventory);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemStack = getSlot(index).getItem();
        if (index > 26) {
            if (itemStack.getItem() == Items.COBBLESTONE && !moveItemStackTo(itemStack, 0, 27, false))
                return ItemStack.EMPTY;
        } else {
            if (!moveItemStackTo(itemStack, 27, 63, false))
                return ItemStack.EMPTY;
        }
        return super.quickMoveStack(playerIn, index);
    }
}
