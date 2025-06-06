package dev.igrammine.smp;

import dev.igrammine.smp.armor.ArmorTracker;
import dev.igrammine.smp.commands.BlockCommand;
import dev.igrammine.smp.commands.SpeedCommand;
import dev.igrammine.smp.config.ConfigManager;
import dev.igrammine.smp.config.iSpecConfig;
import dev.igrammine.smp.events.AttackBlockEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class iSpecP implements ModInitializer {
    public static final iSpecConfig CONFIG = new iSpecConfig();

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {
                    try {
                        new SpeedCommand().registerSpeedCommand(dispatcher, registryAccess);
                        new BlockCommand().registerCommands(dispatcher, registryAccess);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

        ConfigManager.loadConfig();
        config = ConfigManager.getConfig();

        new AttackBlockEvent().register();

        ServerTickEvents.END_SERVER_TICK.register(this::onServerTick);
    }

    public static iSpecConfig config;

    private final ArmorTracker armorTracker = new ArmorTracker();

    private void onServerTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            armorTracker.updatePlayerArmor(player);
        }
    }
}
