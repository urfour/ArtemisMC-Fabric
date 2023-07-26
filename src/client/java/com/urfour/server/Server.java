package com.urfour.server;

import com.google.gson.Gson;
import com.urfour.config.ModConfig;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server implements Runnable {

    private MinecraftInfos infos = new MinecraftInfos();
    private static final Logger LOGGER = LogManager.getLogger("Server");
    private Gson gson =  new Gson();

    public void run() {
        try {
            infos.update();
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost("http://localhost:" + ModConfig.PORT + "/plugins/25dacd2d-9275-4d94-bc12-8761dedf0f1d/Minecraft");
            request.addHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(gson.toJson(infos)));
            httpClient.execute(request);
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
    }
}
