package io.github.bumblesoftware.fastload.abstraction;

import io.github.bumblesoftware.fastload.client.BuildingTerrainScreen;
import io.github.bumblesoftware.fastload.compat.modmenu.FastloadConfigScreen;
import io.github.bumblesoftware.fastload.mixin.compat.v1211.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.text.Text;

public final class Client1211 implements AbstractClientCalls {
    @Override
    public String[] getSupportedVersions() {
        return new String[] {"1.21.1"};
    }

    @Override
    public MinecraftClient getClientInstance() {
        return MinecraftClient.getInstance();
    }

    @Override
    public ClientWorld getClientWorld() {
        return getClientInstance().world;
    }

    @Override
    public Screen newFastloadConfigScreen(Screen parent) {
        return new FastloadConfigScreen(parent);
    }

    @Override
    public Screen newBuildingTerrainScreen(int loadingAreaGoal) {
        return new BuildingTerrainScreen(loadingAreaGoal);
    }

    @Override
    public Screen getCurrentScreen() {
        return getClientInstance().currentScreen;
    }

    @Override
    public Text newTranslatableText(String key) {
        return Text.translatable(key);
    }

    @Override
    public Text newLiteralText(String value) {
        return Text.literal(value);
    }

    @Override
    public void reset(Screen screen) {
        ((MinecraftClientAccessor) (Object) getClientInstance()).fastload$invokeReset(screen);
    }

    @Override
    public void setScreen(Screen screen) {
        getClientInstance().setScreen(screen);
    }

    @Override
    public void renderScreenBackgroundTexture(Screen screen, DrawContext context) {
        screen.renderBackground(context, 0, 0, 0.0F);
    }

    @Override
    public void drawCenteredText(DrawContext context, TextRenderer renderer, Text text, int centerX, int y, int color) {
        context.drawCenteredTextWithShadow(renderer, text, centerX, y, color);
    }

    @Override
    public void drawCenteredText(DrawContext context, TextRenderer renderer, String text, int centerX, int y, int color) {
        context.drawCenteredTextWithShadow(renderer, text, centerX, y, color);
    }

    @Override
    public int getLoadedChunkCount() {
        ClientWorld world = getClientWorld();
        return world == null ? 0 : world.getChunkManager().getLoadedChunkCount();
    }

    @Override
    public int getCompletedChunkCount() {
        return getClientInstance().worldRenderer.getCompletedChunkCount();
    }

    @Override
    public int getViewDistance() {
        MinecraftClient client = getClientInstance();
        // Fastload logs its configured radius from the client entrypoint, before
        // MinecraftClient finishes constructing GameOptions.
        return client.options == null ? 32 : client.options.getViewDistance().getValue();
    }

    @Override
    public boolean isWindowFocused() {
        return getClientInstance().isWindowFocused();
    }

    @Override
    public boolean isSingleplayer() {
        return getClientInstance().isInSingleplayer();
    }

    @Override
    public boolean forCurrentScreen(ScreenProvider screenProvider) {
        return screenProvider.getCurrent(getCurrentScreen());
    }

    @Override
    public boolean isBuildingTerrainScreen(Screen screen) {
        return screen instanceof BuildingTerrainScreen;
    }

    @Override
    public boolean isGameMenuScreen(Screen screen) {
        return screen instanceof GameMenuScreen;
    }

    @Override
    public boolean isDownloadingTerrainScreen(Screen screen) {
        return screen instanceof DownloadingTerrainScreen;
    }
}
