package dev.igrammine.smp.events;

import dev.igrammine.smp.data.ModStates;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

public class AttackBlockEvent {

    public void register() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, block, blockEntity) -> {
            if (!player.isSpectator() && !player.isCreative() && (
                     player.getMainHandStack().getItem() == Items.AIR ||
                     player.getMainHandStack().getItem() == Items.WOODEN_AXE ||
                     player.getMainHandStack().getItem() == Items.WOODEN_HOE ||
                     player.getMainHandStack().getItem() == Items.WOODEN_PICKAXE ||
                     player.getMainHandStack().getItem() == Items.WOODEN_SHOVEL ||
                     player.getMainHandStack().getItem() == Items.WOODEN_SWORD
                    )) {
                NbtCompound retrievedData = ModStates.getPlayerDataState((ServerWorld) player.getWorld()).getPlayerData(player.getUuid());
                var value = retrievedData.getBoolean("blocked");
                if (value) {
                    var slot = player.getEquippedStack(EquipmentSlot.MAINHAND);
                    if (!slot.isEmpty()) {
                        player.dropItem(player.getEquippedStack(EquipmentSlot.MAINHAND).split(slot.getCount()), true, false);
                    }
                    //player.dropItem(player.getMainHandStack(), true, false);
                }
                return !value;
            }
            return true;
        });
    }
}
