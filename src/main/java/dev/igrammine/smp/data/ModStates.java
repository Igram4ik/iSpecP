package dev.igrammine.smp.data;

import net.minecraft.server.world.ServerWorld;

public class ModStates {
    private static final String PLAYER_DATA_STATE_NAME = "ispecp_player_data";

    public static PlayerDataState getPlayerDataState(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                PlayerDataState::fromNbt,
                PlayerDataState::new,
                PLAYER_DATA_STATE_NAME
        );
    }
}
