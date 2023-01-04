package radio;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

public class RadioDataModel {

    private SRParser parser;
    private SRApi api;
    Map<Long, String> primary;
    Map<Long, String> p4;
    Map<Long, String> extra;
    Map<Long, String> others;

    Map<Long, String> channels;

    public RadioDataModel() throws IOException, ParseException, InterruptedException {
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
        this.channels = parser.getParsedChannels(json);
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

    public List<ProgramInfo> getChannelTableau(String channelName) throws IOException, InterruptedException, ParseException {
        List<ProgramInfo> sortedList = new ArrayList<>();

        for(Map.Entry<Long,String>item:channels.entrySet()){
            if(item.getValue().contains(channelName)){
                List<ProgramInfo> programList = parser.getParsedChannelTableau(api.getChannelTableau(item.getKey()));
                  for (ProgramInfo program : programList) {
                    LocalDateTime sixHoursBefore = LocalDateTime.now(ZoneId.systemDefault()).minusHours(6);
                    LocalDateTime twelveHoursAfter = LocalDateTime.now(ZoneId.systemDefault()).plusHours(12);

                    System.out.println(sixHoursBefore);
                    if(program.getStartDate().isAfter(sixHoursBefore) && program.getStartDate().isBefore(twelveHoursAfter)){
                        sortedList.add(program);
                    }
                }
                return sortedList;
            }
        }
        return sortedList;
    }

}
