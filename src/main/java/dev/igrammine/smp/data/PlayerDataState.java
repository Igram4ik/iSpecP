package dev.igrammine.smp.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataState extends PersistentState {

    private final Map<UUID, NbtCompound> playerData = new HashMap<>();

    public void setPlayerData(UUID playerUUID, NbtCompound data) {
        playerData.put(playerUUID, data);
        markDirty();
    }

    public NbtCompound getPlayerData(UUID playerUUID) {
        return playerData.getOrDefault(playerUUID, new NbtCompound());
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList list = new NbtList();
        for (Map.Entry<UUID, NbtCompound> entry : playerData.entrySet()) {
            NbtCompound data = new NbtCompound();
            data.putUuid("UUID", entry.getKey());
            data.put("Data", entry.getValue());
            list.add(data);
        }
        nbt.put("PlayerData", list);
        return nbt;
    }

    public static PlayerDataState fromNbt(NbtCompound nbt) {
        PlayerDataState state = new PlayerDataState();
        NbtList list = nbt.getList("PlayerData", NbtElement.COMPOUND_TYPE);
        for (NbtElement element : list) {
            NbtCompound data = (NbtCompound) element;
            UUID uuid = data.getUuid("UUID");
            NbtCompound playerNbt = data.getCompound("Data");
            state.playerData.put(uuid, playerNbt);
        }
        return state;
    }
}
