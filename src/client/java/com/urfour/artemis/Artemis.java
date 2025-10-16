package com.urfour.artemis;

import com.urfour.artemis.server.Server;
import com.urfour.artemis.infos.MinecraftInfos;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class Artemis implements ModInitializer, ClientModInitializer{
	public static final String MOD_ID = "artemismc";
	public static Server server;
	private static ScheduledExecutorService scheduler;
	private static final Logger LOGGER = LogManager.getLogger("Artemis");

	@Override
	public void onInitialize() {
		server = null;
	}

	@Override
	public void onInitializeClient() {
		try {
			if (server == null) {
				server = new Server();
			}
			MinecraftInfos infos = new MinecraftInfos();
			server.setInfos(infos);
			scheduler = Executors.newScheduledThreadPool(1);
			scheduler.scheduleAtFixedRate(server, 0, 100, TimeUnit.MILLISECONDS);
		} catch (Throwable t) {
			LOGGER.error("Failed to initialize ArtemisMC", t);
			throw new RuntimeException(t);
		}
	}
}