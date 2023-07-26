package com.urfour;

import com.urfour.config.ModConfig;
import com.urfour.server.Server;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Environment(EnvType.CLIENT)
public class Artemis implements ClientModInitializer{
	public static final String MOD_ID = "artemismc";

	@Override
	public void onInitializeClient() {
		Server server = new Server();
		ModConfig.registerConfigs();
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(server, 0, 100, TimeUnit.MILLISECONDS);
	}
}