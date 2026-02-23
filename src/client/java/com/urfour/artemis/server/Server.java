package com.urfour.artemis.server;

import com.google.gson.Gson;
import com.urfour.artemis.infos.MinecraftInfos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
            
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(IP + "plugins/25dacd2d-9275-4d94-bc12-8761dedf0f1d/Minecraft"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            client.send(request, HttpResponse.BodyHandlers.discarding());

        } catch (Throwable t) {
            LOGGER.error("Error in Server.run()", t);
        }
    }
}
