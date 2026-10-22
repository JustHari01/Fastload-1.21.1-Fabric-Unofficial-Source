package io.github.bumblesoftware.fastload.abstraction;

import io.github.bumblesoftware.fastload.api.abstraction.core.handler.MethodAbstractionApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;

/** Version-specific calls used by the loading and configuration screens. */
public interface AbstractClientCalls extends MethodAbstractionApi {
    MinecraftClient getClientInstance();
    ClientWorld getClientWorld();
    Screen newFastloadConfigScreen(Screen parent);
    Screen newBuildingTerrainScreen(int loadingAreaGoal);
    Screen getCurrentScreen();
    Text newTranslatableText(String key);
    Text newLiteralText(String value);
    void reset(Screen screen);
    void setScreen(Screen screen);
    void renderScreenBackgroundTexture(Screen screen, DrawContext context);
    void drawCenteredText(DrawContext context, TextRenderer renderer, Text text, int centerX, int y, int color);
    void drawCenteredText(DrawContext context, TextRenderer renderer, String text, int centerX, int y, int color);
    int getLoadedChunkCount();
    int getCompletedChunkCount();
    int getViewDistance();
    boolean isWindowFocused();
    boolean isSingleplayer();
    boolean forCurrentScreen(ScreenProvider screenProvider);
    boolean isBuildingTerrainScreen(Screen screen);
    boolean isGameMenuScreen(Screen screen);
    boolean isDownloadingTerrainScreen(Screen screen);
}
