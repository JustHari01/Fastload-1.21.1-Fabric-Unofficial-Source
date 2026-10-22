package io.github.bumblesoftware.fastload.compat.modmenu;

import io.github.bumblesoftware.fastload.config.FLConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;

import static io.github.bumblesoftware.fastload.config.DefaultConfig.*;

/** Simple editor for the Fastload toggles and numeric settings. */
public final class FastloadConfigScreen extends Screen {
    private final Screen parent;
    private final List<Setting> settings = List.of(
            Setting.bool(DEBUG_KEY),
            Setting.bool(INSTANT_LOAD_KEY),
            Setting.number(LOCAL_RENDER_RADIUS_KEY, 0, LOCAL_CHUNK_RADIUS_BOUND.max()),
            Setting.number(SERVER_RENDER_RADIUS_KEY, SERVER_CHUNK_RADIUS_BOUND.min(), SERVER_CHUNK_RADIUS_BOUND.max()),
            Setting.number(CHUNK_TRY_LIMIT_KEY, 1, CHUNK_TRY_LIMIT_BOUND.max())
    );

    public FastloadConfigScreen(Screen parent) {
        super(Text.translatable("fastload.screen.config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int rowY = 44;
        for (Setting setting : settings) {
            final int y = rowY;
            if (setting.booleanSetting) {
                boolean value = Boolean.parseBoolean(FLConfig.retrieveProperty(setting.key));
                ButtonWidget button = ButtonWidget.builder(label(setting, value), b -> {
                    boolean next = !Boolean.parseBoolean(FLConfig.retrieveProperty(setting.key));
                    FLConfig.storeProperty(setting.key, Boolean.toString(next));
                    b.setMessage(label(setting, next));
                }).dimensions(width / 2 - 100, y, 200, 20).build();
                addDrawableChild(button);
            } else {
                addDrawableChild(ButtonWidget.builder(Text.literal("−"), b -> change(setting, -1))
                        .dimensions(width / 2 + 40, y, 28, 20).build());
                addDrawableChild(ButtonWidget.builder(Text.literal("+"), b -> change(setting, 1))
                        .dimensions(width / 2 + 72, y, 28, 20).build());
                addDrawableChild(ButtonWidget.builder(valueLabel(setting), b -> {})
                        .dimensions(width / 2 - 100, y, 136, 20).build());
            }
            rowY += 27;
        }
        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.done"), b -> close())
                .dimensions(width / 2 - 100, height - 32, 200, 20).build());
    }

    private static Text label(Setting setting, boolean value) {
        return Text.translatable("fastload.button." + setting.key)
                .append(Text.literal(": " + (value ? "ON" : "OFF")));
    }

    private static Text valueLabel(Setting setting) {
        return Text.translatable("fastload.button." + setting.key)
                .append(Text.literal(": " + FLConfig.retrieveProperty(setting.key)));
    }

    private void change(Setting setting, int amount) {
        int value;
        try {
            value = Integer.parseInt(FLConfig.retrieveProperty(setting.key));
        } catch (NumberFormatException ignored) {
            value = setting.minimum;
        }
        value = Math.max(setting.minimum, Math.min(setting.maximum, value + amount));
        FLConfig.storeProperty(setting.key, Integer.toString(value));
        clearAndInit();
    }

    @Override
    public void close() {
        FLConfig.writeToDisk();
        client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 18, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    private record Setting(String key, boolean booleanSetting, int minimum, int maximum) {
        static Setting bool(String key) {
            return new Setting(key, true, 0, 1);
        }

        static Setting number(String key, int minimum, int maximum) {
            return new Setting(key, false, minimum, maximum);
        }
    }
}
