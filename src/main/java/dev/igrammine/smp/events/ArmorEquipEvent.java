package dev.igrammine.smp.events;

import com.mojang.authlib.properties.Property;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ArmorEquipEvent {

    public void setupEvent() {

        UseItemCallback.EVENT.register(new UseItemCallback() {
            @Override
            public TypedActionResult<ItemStack> interact(PlayerEntity playerEntity, World world, Hand hand) {

                if (playerEntity.getGameProfile().getProperties().get("iSpecP").contains(new Property("block", "true"))) {
                    return TypedActionResult.fail(ItemStack.EMPTY);
                } else return TypedActionResult.success(ItemStack.EMPTY);
            }
        });
    }
}
