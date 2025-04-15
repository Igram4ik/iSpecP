package dev.igrammine.smp.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class SpeedCommand {

    public void registerSpeedCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(CommandManager.literal("speed")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .then(CommandManager.argument("speed", FloatArgumentType.floatArg(0.0f, 10.0f))
                                .executes(context ->
                                        setSpeed(
                                                EntityArgumentType.getPlayer(context, "player"),
                                                FloatArgumentType.getFloat(context, "speed")
                                        )
                                )
                        )
                )
        );
    }
    private int setSpeed(PlayerEntity player, float speed) {
        player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1f * speed);
        return 1;
    }

}
