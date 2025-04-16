package dev.igrammine.smp.armor;
import dev.igrammine.smp.commands.BlockCommand;
import dev.igrammine.smp.data.ModStates;
import dev.igrammine.smp.iSpecP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ArmorTracker {
    private final Map<ServerPlayerEntity, Map<EquipmentSlot, ItemStack>> playerArmorMap = new HashMap<>();

    public void updatePlayerArmor(ServerPlayerEntity player) {
        Map<EquipmentSlot, ItemStack> previousArmor = playerArmorMap.getOrDefault(player, new HashMap<>());
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack currentStack = player.getEquippedStack(slot);
                ItemStack previousStack = previousArmor.getOrDefault(slot, ItemStack.EMPTY);
                if (!ItemStack.areEqual(currentStack, previousStack)) {
                    // Armor changed in this slot
                    if (currentStack.isEmpty()) {
                        // Armor was unequipped
                        onArmorUnequipped(player, slot, previousStack);
                    } else {
                        // Armor was equipped
                        onArmorEquipped(player, slot, currentStack);
                    }
                }
            }
        }
        // Update the stored armor state
        Map<EquipmentSlot, ItemStack> currentArmor = new HashMap<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                currentArmor.put(slot, player.getEquippedStack(slot).copy());
            }
        }
        playerArmorMap.put(player, currentArmor);
    }

    private void onArmorEquipped(ServerPlayerEntity player, EquipmentSlot slot, ItemStack stack) {
        if (ModStates.getPlayerDataState((ServerWorld) player.getWorld()).getPlayerData(player.getUuid()).getBoolean("blocked")) {
            player.sendMessage(Text.of("§7Броня слишком тяжелая..."));
            player.getWorld().playSound(
                    null,
                    player.getBlockPos(),
                    SoundEvents.BLOCK_ANVIL_DESTROY,
                    SoundCategory.PLAYERS,
                    0.5F, 1.5F
            );
            BlockCommand.dropNetherite(player);
        }
        //CompletableFuture.runAsync(() -> CompletableFuture.delayedExecutor(3, TimeUnit.SECONDS).execute(() -> {
        //    if (slot == EquipmentSlot.LEGS && stack.getItem() == Registries.ITEM.get(Identifier.tryParse("minecraft:netherite_leggings"))) {
        //        player.sendMessage(Text.of("Броня слишком тяжелая..."));
        //        player.getWorld().playSound(
        //                null,
        //                player.getBlockPos(),
        //                SoundEvents.BLOCK_ANVIL_DESTROY,
        //                SoundCategory.PLAYERS,
        //                0.5F, 1.5F
        //        );
        //        //player.dropItem(stack, false);
        //        player.sendMessage(Text.of(String.valueOf(slot.ordinal())));
        //        //player.dropItem(stack, true, true);
        //        //player.getArmorItems()
        //        var nslot = player.getEquippedStack(EquipmentSlot.LEGS);
        //        if (!nslot.isEmpty()) {
        //            player.dropItem(player.getEquippedStack(EquipmentSlot.LEGS).split(nslot.getCount()), true, false);
        //        }
        //    }
        //}));
    }

    private void onArmorUnequipped(ServerPlayerEntity player, EquipmentSlot slot, ItemStack stack) {
        // Handle armor unequipped logic here
    }
}

