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
        Map<String, Long> map = new LinkedHashMap();

        if(channels != null) {
            for(int i = 0; i < channels.size(); i++) {
                JSONObject channel = (JSONObject) channels.get(i);
                String name = (String) channel.get("name");
                Long channel_id = (Long) channel.get("id");
                map.put(name, channel_id);
            }
        }
        return map;
    }

    public Map getParsedChannelTableau(String json) throws ParseException {
        JSONParser parser = new JSONParser();
        System.out.println(json);
        JSONObject jsonObject = (JSONObject) parser.parse(json);
        JSONArray schedule = (JSONArray) jsonObject.get("schedule");
        Map<ProgramInfo, Long> map = new LinkedHashMap();

        if(schedule != null) {
            for(int i = 0; i < schedule.size(); i++) {
                JSONObject program = (JSONObject) schedule.get(i);
                String name = (String) program.get("program");
                String description = (String) program.get("description");
                String title = (String) program.get("title");
                String imageUrl = (String) program.get("imageurltemplate");
                String startDate = (String) program.get("starttimeutc");
                String endDate = (String) program.get("endtimeutc");
                Long episodeId = (Long) program.get("episodeid");
                System.out.println(program);
                System.out.printf("Hej");
            }
        }
        return map;
    }

}
