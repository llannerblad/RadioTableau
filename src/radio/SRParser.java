package radio;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class SRParser {
    private SRApi api;

    public SRParser() {
        this.api = new SRApi();
    }

    public Map getParsedChannels(String json) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(json);
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

    public List getParsedChannelTableau(String json) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(json);
        JSONArray schedule = (JSONArray) jsonObject.get("schedule");
        List<ProgramInfo> list = new ArrayList<>();

        if(schedule != null) {
            list = parseProgramsIntoMap(schedule);
        }
        return list;
    }

    private Object validateKeyAndReturnValue(JSONObject program, String key) {
        if(program.containsKey(key)){
            return program.get(key).toString();
        }
        return "-";
    }

    private List<ProgramInfo> parseProgramsIntoMap(JSONArray data){
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

    private LocalDateTime convertDate(String d){
        long timestamp = parseDate(d);
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime local = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return local;
    }

    private Long parseDate(String d){
        String str = d.replaceAll("[^\\d.]", "");
        return Long.parseLong(str);
    }
}
