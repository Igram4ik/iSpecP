package dev.igrammine.smp;

import dev.igrammine.smp.commands.BlockCommand;
import dev.igrammine.smp.commands.SpeedCommand;
import dev.igrammine.smp.config.ConfigManager;
import dev.igrammine.smp.config.iSpecConfig;
import dev.igrammine.smp.events.AttackBlockEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class iSpecP implements ModInitializer {
    public static final iSpecConfig CONFIG = new iSpecConfig();

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) -> {
                    new SpeedCommand().registerSpeedCommand(dispatcher, registryAccess);
                    new BlockCommand().registerBlockCommand(dispatcher, registryAccess);
                }
        );

        ConfigManager.loadConfig();

        new AttackBlockEvent().register();
    }
}
