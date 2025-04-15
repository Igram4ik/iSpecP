package dev.igrammine.smp.commands;

import com.mojang.authlib.properties.Property;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.nbt.visitor.StringNbtWriter;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.NbtDataSource;
import net.minecraft.text.NbtTextContent;
import net.minecraft.text.Text;
import net.minecraft.util.collection.IndexedIterable;

public class BlockCommand {

    private final Property BLOCKED_PROPERTY = new Property("block", "true");
    public static final TrackedData<Boolean> key = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public void registerBlockCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(CommandManager.literal("block")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("Player", EntityArgumentType.player())
                        .executes(commandContext -> {
                            block(commandContext.getSource(), EntityArgumentType.getPlayer(commandContext, "Player"));
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
    }

    private void block(ServerCommandSource source,  PlayerEntity player) {
        try {
            // TODO:

            source.sendMessage(Text.of("Успешно  заблокирован."));
        } catch (Exception E) {
            source.sendError(Text.of("Произошла ошибка при добавлении."));
            E.printStackTrace();
        }
    }

    private void unblock(PlayerEntity player) {
        player.getGameProfile().getProperties().remove("iSpecP", BLOCKED_PROPERTY);
    }
}
