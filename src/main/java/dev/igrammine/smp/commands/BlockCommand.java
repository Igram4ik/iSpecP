package dev.igrammine.smp.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import dev.igrammine.smp.data.ModStates;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BlockCommand {
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        registerBlockCommand(dispatcher, registryAccess);
        registerUnBlockCommand(dispatcher, registryAccess);
    }

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

    public void registerUnBlockCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(CommandManager.literal("unblock")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("Player", EntityArgumentType.player())
                        .executes(commandContext -> {
                            unblock(commandContext.getSource(), EntityArgumentType.getPlayer(commandContext, "Player"));
                            return Command.SINGLE_SUCCESS;
                        })
                )
        );
    }

    private void block(ServerCommandSource source,  PlayerEntity player) {
        try {
            NbtCompound retrievedData = ModStates.getPlayerDataState((ServerWorld) player.getWorld()).getPlayerData(player.getUuid());
            if (retrievedData == null || !retrievedData.getBoolean("blocked")) {
                setData(player, true);
                dropNetherite(player);
                source.sendMessage(Text.of("Успешно заблокирован."));
            } else
                source.sendMessage(Text.of("Уже заблокирован."));
        } catch (Exception E) {
            source.sendError(Text.of("Произошла ошибка при добавлении."));
            E.printStackTrace();
        }
    }

    private void unblock(ServerCommandSource source,  PlayerEntity player) {
        try {
            setData(player, false);
            source.sendMessage(Text.of("Успешно разблокирован."));
        } catch (Exception E) {
            source.sendError(Text.of("Произошла ошибка при установки."));
            E.printStackTrace();
        }
    }

    public void setData(PlayerEntity player, boolean bool) {
        NbtCompound customData = new NbtCompound();
        customData.putBoolean("blocked", bool);
        ModStates.getPlayerDataState((ServerWorld) player.getWorld()).setPlayerData(player.getUuid(), customData);
    }

    public static void dropNetherite(PlayerEntity player) {
        if (player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.NETHERITE_CHESTPLATE) {
            var slot = player.getEquippedStack(EquipmentSlot.CHEST);
            if (!slot.isEmpty()) {
                player.dropItem(player.getEquippedStack(EquipmentSlot.CHEST).split(slot.getCount()), true, false);
            }
        }
        if (player.getEquippedStack(EquipmentSlot.HEAD).getItem() == Items.NETHERITE_HELMET) {
            var slot = player.getEquippedStack(EquipmentSlot.HEAD);
            if (!slot.isEmpty()) {
                player.dropItem(player.getEquippedStack(EquipmentSlot.HEAD).split(slot.getCount()), true, false);
            }
        }
        if (player.getEquippedStack(EquipmentSlot.FEET).getItem() == Items.NETHERITE_BOOTS) {
            var slot = player.getEquippedStack(EquipmentSlot.FEET);
            if (!slot.isEmpty()) {
                player.dropItem(player.getEquippedStack(EquipmentSlot.FEET).split(slot.getCount()), true, false);
            }
        }
        if (player.getEquippedStack(EquipmentSlot.LEGS).getItem() == Items.NETHERITE_LEGGINGS) {
            var slot = player.getEquippedStack(EquipmentSlot.LEGS);
            if (!slot.isEmpty()) {
                player.dropItem(player.getEquippedStack(EquipmentSlot.LEGS).split(slot.getCount()), true, false);
            }
        }
    }
}
