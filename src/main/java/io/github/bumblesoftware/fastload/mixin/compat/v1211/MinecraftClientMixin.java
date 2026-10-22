package io.github.bumblesoftware.fastload.mixin.compat.v1211;

import io.github.bumblesoftware.fastload.client.FLClientEvents.Contexts.SetScreenEventContext;
import io.github.bumblesoftware.fastload.util.obj_holders.MutableObjectHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static io.github.bumblesoftware.fastload.client.FLClientEvents.Events.SET_SCREEN_EVENT;
import static io.github.bumblesoftware.fastload.client.FLClientEvents.Locations.*;
import static io.github.bumblesoftware.fastload.common.FLCommonEvents.Events.BOOLEAN_EVENT;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Inject(method = "setScreen", at = @At("HEAD"), cancellable = true)
    private void fastload$onSetScreen(@Nullable Screen screen, CallbackInfo ci) {
        if (SET_SCREEN_EVENT.isNotEmpty()) {
            SET_SCREEN_EVENT.execute(new SetScreenEventContext(screen, ci));
        }
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void fastload$onRender(boolean tick, CallbackInfo ci) {
        if (BOOLEAN_EVENT.isNotEmpty(RENDER_TICK)) {
            BOOLEAN_EVENT.execute(List.of(RENDER_TICK), new MutableObjectHolder<>(tick));
        }
    }

    @Redirect(
            method = "startIntegratedServer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V")
    )
    private void fastload$replaceLocalLoadingScreen(MinecraftClient client, Screen screen) {
        if (SET_SCREEN_EVENT.isNotEmpty(LLS_441_REDIRECT)) {
            SET_SCREEN_EVENT.execute(
                    List.of(LLS_441_REDIRECT),
                    new SetScreenEventContext(screen, null)
            );
        } else {
            client.setScreen(screen);
        }
    }

    @Redirect(
            method = "joinWorld",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;reset(Lnet/minecraft/client/gui/screen/Screen;)V")
    )
    private void fastload$replaceWorldEntryScreen(MinecraftClient client, Screen screen) {
        if (SET_SCREEN_EVENT.isNotEmpty(PROGRESS_SCREEN_JOIN_WORLD_REDIRECT)) {
            SET_SCREEN_EVENT.execute(
                    List.of(PROGRESS_SCREEN_JOIN_WORLD_REDIRECT),
                    new SetScreenEventContext(screen, null)
            );
        } else {
            ((MinecraftClientAccessor) (Object) client).fastload$invokeReset(screen);
        }
    }
}
