package dev.igrammine.smp.mixin;

import com.mojang.authlib.GameProfile;
import dev.igrammine.smp.data.ModStates;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class ArmorEquipPlayerEntityMixin {

    @Shadow @Nullable public abstract ItemEntity dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership);

    @Shadow @Final private GameProfile gameProfile;

    @Shadow public abstract void sendMessage(Text message, boolean overlay);

    @Inject(method = "equipStack", at = @At("HEAD"), cancellable = true)
    private void onSetStack(EquipmentSlot slot, ItemStack stack, CallbackInfo ci) {
        if (slot.isArmorSlot() &&
                (
                        stack.getItem() == Registries.ITEM.get(Identifier.tryParse("minecraft:netherite_chestplate")) ||
                                stack.getItem() == Registries.ITEM.get(Identifier.tryParse("minecraft:netherite_helmet")) ||
                                stack.getItem() == Registries.ITEM.get(Identifier.tryParse("minecraft:netherite_boots")) ||
                                stack.getItem() == Registries.ITEM.get(Identifier.tryParse("minecraft:netherite_leggings"))
                        )
        ) dropNetheriteItem(stack, ci);
    }

    @Inject(method = "giveItemStack", at = @At("HEAD"))
    private void test(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        System.out.println("test");
        //if (stack.getItem() == Registries.ITEM.get(Identifier.tryParse("minecraft:netherite_chestplate"))) {
        //    cir.setReturnValue(false);
        //}
    }
    public void dropNetheriteItem(ItemStack stack, CallbackInfo ci) {
        if (ModStates.getPlayerDataState((ServerWorld) ((LivingEntity)(Object)this).getWorld()).getPlayerData(gameProfile.getId()).getBoolean("blocked")) {
            dropItem(stack, true, false);
            ((LivingEntity)(Object)this).getWorld().playSound(
                    null,
                    ((LivingEntity) (Object) this).getBlockPos(),
                    SoundEvents.BLOCK_ANVIL_DESTROY, SoundCategory.PLAYERS,
                    0.5f,1.5f
            );
            sendMessage(Text.of("Броня слишком тяжелая..."), false);
            ci.cancel();
        }
    }
}
