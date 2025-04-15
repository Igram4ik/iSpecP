package dev.igrammine.smp.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerPropertyUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerSyncHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ArmorSlotsServerPlayerMixin {

    @Mutable
    @Final
    @Shadow
    private ScreenHandlerSyncHandler screenHandlerSyncHandler;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onConstructorEnd(MinecraftServer server, ServerWorld world, GameProfile profile, CallbackInfo ci) {
        // Изменение значения final поля в конструкторе
        this.screenHandlerSyncHandler = new ScreenHandlerSyncHandler() {
            public void updateState(ScreenHandler handler, DefaultedList<ItemStack> stacks, ItemStack cursorStack, int[] properties) {
                networkHandler.sendPacket(new InventoryS2CPacket(handler.syncId, handler.nextRevision(), stacks, cursorStack));

                for(int i = 0; i < properties.length; ++i) {
                    this.sendPropertyUpdate(handler, i, properties[i]);
                }

            }

            public void updateSlot(ScreenHandler handler, int slot, ItemStack stack) {
                networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(handler.syncId, handler.nextRevision(), slot, stack));

                if (slot == 5 && stack.getItem() == Registries.ITEM.get(Identifier.tryParse("minecraft:netherite_helmet"))) {
                    System.out.println("drop");


                    if (((Object) this) instanceof Entity entity) {
                        System.out.println("instanceof");

                    }
                }
            }

            public void updateCursorStack(ScreenHandler handler, ItemStack stack) {
                networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(-1, handler.nextRevision(), -1, stack));
            }

            public void updateProperty(ScreenHandler handler, int property, int value) {
                this.sendPropertyUpdate(handler, property, value);
            }

            private void sendPropertyUpdate(ScreenHandler handler, int property, int value) {
                networkHandler.sendPacket(new ScreenHandlerPropertyUpdateS2CPacket(handler.syncId, property, value));
            }
        };
    }

    @Shadow
    public ServerPlayNetworkHandler networkHandler;
}
