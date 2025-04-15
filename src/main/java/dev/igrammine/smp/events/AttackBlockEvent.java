package dev.igrammine.smp.events;

import dev.igrammine.smp.commands.BlockCommand;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

import java.util.concurrent.atomic.AtomicReference;

public class AttackBlockEvent {

    public void register() {
        AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
            var handStack = player.getMainHandStack();
            var block = world.getBlockState(pos);
            AtomicReference<ActionResult> result = new AtomicReference<>(ActionResult.PASS);

            player.getGameProfile().getProperties().forEach((name, prop) -> {
                if (player.getDataTracker().get(BlockCommand.key).equals(true) && !player.isSpectator() && !player.isCreative()) {
                    player.sendMessage(Text.of("blocked"));
                    result.set(ActionResult.SUCCESS);
                }
            });

            return result.get();
        });
    }
}
