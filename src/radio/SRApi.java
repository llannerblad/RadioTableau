package radio;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class SRApi {

    public String getChannels() throws IOException, InterruptedException, ParseException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .setHeader("Content-Type", "application/json")
                .uri(URI.create("https://api.sr.se/api/v2/channels?pagination=false&format=json"))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String getChannelTableau(int channelId) throws IOException, InterruptedException {
        String url = "https://api.sr.se/api/v2/scheduledepisodes?pagination=false&format=json&channelid="+ channelId;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        long timestamp = 1671490800000L;
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime local = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        Date date = Date.from(instant);
        //System.out.println(date);
        return response.body();
    }
}
