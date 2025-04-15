package dev.igrammine.smp.events;

import dev.igrammine.smp.data.ModStates;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

public class AttackBlockEvent {

    public void register() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, block, blockEntity) -> {
            if (!player.isSpectator() && !player.isCreative()) {
                NbtCompound retrievedData = ModStates.getPlayerDataState((ServerWorld) player.getWorld()).getPlayerData(player.getUuid());
                var value = retrievedData.getBoolean("blocked");
                return !value;
            }
            return true;
        });
    }
}
