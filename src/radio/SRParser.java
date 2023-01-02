package radio;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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
                String name = validateKeyAndReturnValue(channel, "name");
                Long channelId = (Long) channel.get("id");
                map.put(channelId, name);
            }
        }
        return map;
    }

    public Map getParsedChannelTableau(String json) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(json);
        JSONArray schedule = (JSONArray) jsonObject.get("schedule");
        Map<Long, ProgramInfo> map = new LinkedHashMap();

        if(schedule != null) {
            map = parseProgramsIntoMap(schedule);
        }
        return map;
    }

    private String validateKeyAndReturnValue(JSONObject program, String key) {
        if(program.containsKey(key)){
            return program.get(key).toString();
        }
        return null;
    }

    private Map<Long, ProgramInfo> parseProgramsIntoMap(JSONArray data){
        Map<Long, ProgramInfo> map = new LinkedHashMap();
        for(int i = 0; i < data.size(); i++) {
            JSONObject info = (JSONObject) data.get(i);
            JSONObject program = (JSONObject) info.get("program");
            String name = validateKeyAndReturnValue(program, "name");
            String imageUrl = validateKeyAndReturnValue(info, "imageurltemplate");
            String description = validateKeyAndReturnValue(info, "description");
            String title = validateKeyAndReturnValue(info, "title");
            String startDate = validateKeyAndReturnValue(info, "starttimeutc");
            String endDate = validateKeyAndReturnValue(info, "endtimeutc");
            Long episodeId = (Long) info.get("episodeid");

            ProgramInfo programInfo = new ProgramInfo(name, description, startDate, endDate, imageUrl, title);
            map.put(episodeId,programInfo);
        }
        return map;
    }
}
