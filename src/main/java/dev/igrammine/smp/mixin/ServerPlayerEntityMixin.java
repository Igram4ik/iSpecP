package dev.igrammine.smp.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayerEntity.class)
public interface ServerPlayerEntityMixin {

    @Invoker("dropItem")
    ItemEntity invokeDropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership);
}
