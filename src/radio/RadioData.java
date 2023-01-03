package radio;

import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class RadioData {

    private SRParser parser;
    private SRApi api;
    Map<Long, String> primary;
    Map<Long, String> p4;
    Map<Long, String> extra;
    Map<Long, String> others;

    public RadioData() throws IOException, ParseException, InterruptedException {
        this.parser = new SRParser();
        this.api = new SRApi();
        categorizeChannels();
    }

    public Map getChannels() throws ParseException, IOException, InterruptedException {
        String json = api.getChannels();
        return parser.getParsedChannels(json);
    }

    public void categorizeChannels() throws IOException, ParseException, InterruptedException {
        String json = api.getChannels();
        Map<Long, String> channels = parser.getParsedChannels(json);
        this.primary = new LinkedHashMap<>();
        this.p4 = new LinkedHashMap<>();
        this.extra = new LinkedHashMap<>();
        this.others = new LinkedHashMap<>();


        for(Map.Entry<Long,String>item:channels.entrySet()){
            if(item.getValue().contains("P1") || item.getValue().contains("P2") || item.getValue().contains("P3")) {
                primary.put(item.getKey(), item.getValue());
            }
            else if(item.getValue().contains("P4"))
            {
                p4.put(item.getKey(), item.getValue());
            }
            else if(item.getValue().contains("Extra")){
                extra.put(item.getKey(),item.getValue());
            }
            else {
                others.put(item.getKey(),item.getValue());
            }
        }
    }

    public Map<Long, String> getExtra() {
        return extra;
    }

    public Map<Long, String> getOthers() {
        return others;
    }

    public Map<Long, String> getP4() {
        return p4;
    }

    public Map<Long, String> getPrimary() {
        return primary;
    }

    public Map<Long, ProgramInfo> getChannelTableau(int channelId) throws IOException, InterruptedException, ParseException {
        return parser.getParsedChannelTableau(api.getChannelTableau(channelId));

    }

}
