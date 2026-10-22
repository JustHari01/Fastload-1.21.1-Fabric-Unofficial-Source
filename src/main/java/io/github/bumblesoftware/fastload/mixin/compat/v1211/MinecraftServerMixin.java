package io.github.bumblesoftware.fastload.mixin.compat.v1211;

import io.github.bumblesoftware.fastload.common.FLCommonEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.PROGRESS_LISTENER_EVENT;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Locations.PREPARE_START_REGION;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Redirect(
            method = "prepareStartRegion",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getInt(Lnet/minecraft/world/GameRules$Key;)I")
    )
    private int fastload$skipSpawnChunkRadiusWait(GameRules gameRules, GameRules.Key<GameRules.IntRule> key) {
        if (key == GameRules.SPAWN_CHUNK_RADIUS) return 0;
        return gameRules.getInt(key);
    }

    @Redirect(
            method = "prepareStartRegion",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/WorldGenerationProgressListener;start(Lnet/minecraft/util/math/ChunkPos;)V")
    )
    private void fastload$stopVanillaProgressListener(WorldGenerationProgressListener listener, ChunkPos spawnChunk) {
        if (PROGRESS_LISTENER_EVENT.isNotEmpty(PREPARE_START_REGION)) {
            PROGRESS_LISTENER_EVENT.execute(
                    new FLCommonEvents.Contexts.ProgressListenerContext(listener, spawnChunk)
            );
        } else {
            listener.start(spawnChunk);
        }
    }
}
