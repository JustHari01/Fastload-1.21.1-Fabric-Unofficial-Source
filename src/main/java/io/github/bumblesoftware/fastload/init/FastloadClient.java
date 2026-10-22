package io.github.bumblesoftware.fastload.init;

import io.github.bumblesoftware.fastload.abstraction.AbstractClientCalls;
import io.github.bumblesoftware.fastload.abstraction.Client1211;
import io.github.bumblesoftware.fastload.client.FLClientEvents;
import io.github.bumblesoftware.fastload.client.FLClientHandler;
import io.github.bumblesoftware.fastload.config.FLConfig;
import io.github.bumblesoftware.fastload.config.FLMath;
import net.fabricmc.api.ClientModInitializer;

import static io.github.bumblesoftware.fastload.config.DefaultConfig.*;
import static io.github.bumblesoftware.fastload.config.FLMath.*;
import static io.github.bumblesoftware.fastload.init.Fastload.LOGGER;
import static io.github.bumblesoftware.fastload.init.Fastload.NAMESPACE;

public class FastloadClient implements ClientModInitializer {
    public static final AbstractClientCalls MINECRAFT_ABSTRACTION_HANDLER = new Client1211();

    static {
        FLConfig.init();
        FLClientEvents.init();
        FLClientHandler.init();

        LOGGER.info("Fastload Port Version: 1.21.1");
        LOGGER.info("Fastload Port Supported Versions: 1.21.1");
        LOGGER.info(logKey(DEBUG_KEY) + FLConfig.retrieveProperty(DEBUG_KEY).toUpperCase());
        LOGGER.info(logKey(CHUNK_TRY_LIMIT_KEY) + getChunkTryLimit());
        LOGGER.info(logKey(LOCAL_RENDER_RADIUS_KEY) + getLocalRenderChunkRadius());
        LOGGER.info(logKey(LOCAL_RENDER_AREA_KEY) + getLocalRenderChunkArea());
        LOGGER.info(logKey(SERVER_RENDER_RADIUS_KEY) + getServerRenderChunkRadius());
        LOGGER.info(logKey(SERVER_RENDER_AREA_KEY) + getServerRenderChunkArea());
    }

    @Override
    public void onInitializeClient() {}

    private static String logKey(String key) {
        return key.toUpperCase() + ": ";
    }
}
