package radio;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Parser used to parse json response from Sveriges Radio's api. The data is fetches using a SRApi object.
 * @author Lee Lannerblad (ens19lld)
 * Course: Applikationsutveckling (Java)
 * Version information: 2023-01-09
 */
public class SRParser {
    private SRApi api;

    /**
     * Creates a new SRParser object and initializes a new SRApi object.
     */
    public SRParser() {
        this.api = new SRApi();
    }

    /**
     * Fetches Sveriges Radio's channel's using an SRApi object, then parses the data into a HashMap where the key
     * is the channel's id and the value is the name of the channel
     * @return a Map containing channels
     * @throws ParseException if data could not be parsed to an JSONObject
     * @throws IOException if data could not be fetched
     * @throws InterruptedException if data could not be fetched
     */
    public Map getParsedChannels() throws ParseException, IOException, InterruptedException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(api.getChannels());
        JSONArray channels = (JSONArray) jsonObject.get("channels");
        Map<Long, String> map = new LinkedHashMap();

        if(channels != null) {
            for(int i = 0; i < channels.size(); i++) {
                JSONObject channel = (JSONObject) channels.get(i);
                String name = (String)validateKeyAndReturnValue(channel, "name");
                Long channelId = (Long) channel.get("id");
                map.put(channelId, name);
            }
        }
        return map;
    }

    /**
     * Fetches the channel's tableau and parses the data into an Arraylist. If the response from the api is
     * empty null is returned.
     * @param channelId the channel's channel id
     * @return a list of ProgramInfo's or null
     * @throws ParseException if data could not be parsed
     * @throws IOException if data could not be fetched
     * @throws InterruptedException if data could not be fetched
     */
    public List getParsedChannelTableau(Long channelId) throws ParseException, IOException, InterruptedException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(api.getChannelTableau(channelId));
        JSONArray schedule = (JSONArray) jsonObject.get("schedule");
        List<ProgramInfo> list = new ArrayList<>();

        if(schedule != null) {
            list = parseProgramsIntoList(schedule);
        }
        return list;
    }

    /**
     * Checks if the JSONObject program contains the key. If yes the value is returned, otherwise '-' is
     * returned
     * @param program JSONObject to be checked
     * @param key the key to be checked
     * @return the corresponding value to the key, or '-'
     */
    private Object validateKeyAndReturnValue(JSONObject program, String key) {
        if(program.containsKey(key)){
            return program.get(key).toString();
        }
        return "-";
    }

    /**
     * Parses a channel's tableau into a List of ProgramInfo-objects.
     * @param data channel tableau
     * @return a List of ProgramInfo's
     */
    private List<ProgramInfo> parseProgramsIntoList(JSONArray data){
        List<ProgramInfo> list = new ArrayList<>();
        for(int i = 0; i < data.size(); i++) {
            JSONObject info = (JSONObject) data.get(i);
            JSONObject program = (JSONObject) info.get("program");
            String name = (String)validateKeyAndReturnValue(program, "name");
            String imageUrl = (String)validateKeyAndReturnValue(info, "imageurltemplate");
            String description = (String)validateKeyAndReturnValue(info, "description");
            String title = (String)validateKeyAndReturnValue(info, "title");
            String tempStartDate = (String)validateKeyAndReturnValue(info, "starttimeutc");
            String tempEndDate = (String)validateKeyAndReturnValue(info, "endtimeutc");
            Long episodeId = (Long) info.get("episodeid");

            LocalDateTime startDate = convertDate(tempStartDate);
            LocalDateTime endDate = convertDate(tempEndDate);
            ProgramInfo programInfo = new ProgramInfo(name, description, startDate, endDate, imageUrl, title, episodeId);
            list.add(programInfo);
        }
        return list;
    }

    /**
     * Converts the string date to a LocalDateTime object based on the system's default time zone
     * @param date to be converted
     * @return LocalDateTime object from date
     */
    private LocalDateTime convertDate(String date){
        long timestamp = parseDateToOnlyNumbers(date);
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * Removes all characters except for numbers from date and converts string to Long
     * @param date the date to be parsed
     * @return date in Long
     */
    private Long parseDateToOnlyNumbers(String date){
        String str = date.replaceAll("[^\\d.]", "");
        return Long.parseLong(str);
    }
}
