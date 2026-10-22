package io.github.bumblesoftware.fastload.mixin.compat.v1211;

import io.github.bumblesoftware.fastload.client.FLClientEvents.Contexts.PlayerJoinEventContext;
import io.github.bumblesoftware.fastload.client.FLClientEvents.Contexts.SetScreenEventContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.PLAYER_JOIN_EVENT;
import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.SET_SCREEN_EVENT;
import static io.github.bumblesoftware.fastload.client.FLClientEvents.Locations.DTS_GAME_JOIN_REDIRECT;
import static io.github.bumblesoftware.fastload.client.FLClientEvents.Locations.RESPAWN_DTS_REDIRECT;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Unique
    private static boolean fastload$respawning;

    @Inject(
            method = "onGameJoin",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;joinWorld(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/gui/screen/DownloadingTerrainScreen$WorldEntryReason;)V")
    )
    private void fastload$onPlayerJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        if (PLAYER_JOIN_EVENT.isNotEmpty()) {
            PLAYER_JOIN_EVENT.execute(new PlayerJoinEventContext(packet));
        }
    }

    @Inject(
            method = "onPlayerRespawn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;startWorldLoading(Lnet/minecraft/client/network/ClientPlayerEntity;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/gui/screen/DownloadingTerrainScreen$WorldEntryReason;)V",
                    shift = At.Shift.BEFORE
            )
    )
    private void fastload$markRespawnLoading(PlayerRespawnS2CPacket packet, CallbackInfo ci) {
        fastload$respawning = true;
    }

    @Redirect(
            method = "startWorldLoading",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V")
    )
    private void fastload$replaceDownloadingTerrainScreen(MinecraftClient client, Screen screen) {
        String location = fastload$respawning ? RESPAWN_DTS_REDIRECT : DTS_GAME_JOIN_REDIRECT;
        fastload$respawning = false;
        if (SET_SCREEN_EVENT.isNotEmpty(location)) {
            SET_SCREEN_EVENT.execute(
                    List.of(location),
                    new SetScreenEventContext(screen, null)
            );
        } else {
            client.setScreen(screen);
        }
    }
}
