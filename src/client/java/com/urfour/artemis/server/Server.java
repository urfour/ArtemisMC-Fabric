package com.urfour.artemis.server;

import com.google.gson.Gson;
import com.urfour.artemis.infos.MinecraftInfos;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Server implements Runnable {

    private MinecraftInfos infos = new MinecraftInfos();
    private static final Logger LOGGER = LogManager.getLogger("Server");
    private static final String WEB_SERVER_FILE = System.getenv("ProgramData") + "/Artemis/webserver.txt";
    private String IP = "http://localhost:9696";
    private Gson gson =  new Gson();

    public Server() {
        try {
            InputStream file = new FileInputStream(WEB_SERVER_FILE);
            IP = new String(file.readAllBytes());
            LOGGER.info("Using IP " + IP + " to send in-game information.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            infos.update();
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(IP + "plugins/25dacd2d-9275-4d94-bc12-8761dedf0f1d/Minecraft");
            request.addHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(gson.toJson(infos)));
            httpClient.execute(request);
        } catch (Exception ex) {
            LOGGER.error(ex);
        }
    }
}
