package io.github.bumblesoftware.fastload.mixin.compat.v1211;

import io.github.bumblesoftware.fastload.client.BuildingTerrainScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientCommonNetworkHandler.class)
public abstract class ClientCommonNetworkHandlerMixin {
    @Shadow @Final protected MinecraftClient client;

    @Inject(
            method = "onResourcePackSend",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void fastload$deferResourcePackPrompt(ResourcePackSendS2CPacket packet, CallbackInfo ci) {
        if (client.currentScreen instanceof BuildingTerrainScreen screen) {
            screen.setClose(() -> client.execute(() ->
                    ((ClientCommonNetworkHandler) (Object) this).onResourcePackSend(packet)
            ));
            ci.cancel();
        }
    }
}
