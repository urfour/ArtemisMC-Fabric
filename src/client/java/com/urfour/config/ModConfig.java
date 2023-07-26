package com.urfour.config;

import com.mojang.datafixers.util.Pair;
import com.urfour.Artemis;

public class ModConfig {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;

    public static int PORT;

    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(Artemis.MOD_ID + "config").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        // configs.addKeyValuePair(new Pair<>("port", 9696), "int");
    }

    private static void assignConfigs() {
        // PORT = CONFIG.getOrDefault("port", 9696);
    }
}