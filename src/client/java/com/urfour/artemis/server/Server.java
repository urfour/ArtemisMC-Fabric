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
import java.util.Map;

public class Server implements Runnable {

    private static String getArtemisFolder() {
        if (System.getProperty("os.name").contains("Windows")) {
            return System.getenv("ProgramData");
        }
        else {
            return System.getProperty("user.home") + "/.local/share";
        }
    }
    private MinecraftInfos infos;
    private static final Logger LOGGER = LogManager.getLogger("Server");
    private static final String WEB_SERVER_FILE = getArtemisFolder() + "/Artemis/webserver.txt";
    private String IP;
    private Gson gson =  new Gson();

    public Server() {
        try {
            InputStream file = new FileInputStream(WEB_SERVER_FILE);
            IP = new String(file.readAllBytes());
            LOGGER.info("Using IP " + IP + " to send in-game information.");
        } catch (IOException e) {
            // If the file is missing or unreadable, continue without crashing.
            IP = "";
            LOGGER.warn("Could not read web server file: " + WEB_SERVER_FILE + ". Artemis will not send data.", e);
            infos = null;
        }
    }

    /**
     * Inject a MinecraftInfos instance to be used by this Server.
     * Allows Artemis to create and register the infos on the client thread
     * where Minecraft classes are available.
     */
    public void setInfos(MinecraftInfos infos) {
        this.infos = infos;
    }

    public void run() {
        try {
            // Initialize MinecraftInfos lazily on the first run where Minecraft classes are available.
            if (infos == null) {
                infos = new MinecraftInfos();
            }
            
            infos.update();
            
            if (IP == null || IP.isEmpty()) {
                return;
            }
            
            String json = gson.toJson(infos);
            
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(IP + "plugins/25dacd2d-9275-4d94-bc12-8761dedf0f1d/Minecraft");
            request.addHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(json));
            httpClient.execute(request);
        } catch (Throwable t) {
            LOGGER.error("Error in Server.run()", t);
        }
    }
}
