package radio;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Fetches data from Sveriges Radio's api. The response format is in JSON.
 * @author Lee Lannerblad (ens19lld)
 * Course: Applikationsutveckling (Java)
 * Version information: 2023-01-09
 */
public class SRApi {

    /**
     * Fetches all available channels.
     * @return the response
     * @throws IOException if data could not be fetched
     * @throws InterruptedException if data could not be fetched
     */
    public String getChannels() throws IOException, InterruptedException{
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

    /**
     * Fetches the channel with the channelId's tableau. It fetches data from the date that's six hours before current
     * time and twelve hours after.
     * @param channelId the channel's id
     * @return the response
     * @throws IOException if data could not be fetched
     * @throws InterruptedException if data could not be fetched
     */
    public String getChannelTableau(Long channelId) throws IOException, InterruptedException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime fromDate= LocalDateTime.now().minusHours(6);
        LocalDateTime toDate = LocalDateTime.now().plusHours(12);
        String url = "https://api.sr.se/api/v2/scheduledepisodes?pagination=false&format=json&channelid="+
                channelId + "&fromdate=" +fromDate.format(formatter)+ "&todate=" +toDate.format(formatter);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

}
