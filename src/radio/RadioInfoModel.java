package radio;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


/**
 * Model for the application RadioInfo. Fetches data from Sveriges Radio's api using a SRParser object.
 * Divides channel's into four categories, primary, p4, extra and others.
 * @author Lee Lannerblad (ens19lld)
 * Course: Applikationsutveckling (Java)
 * Version information: 2023-01-09
 */
public class RadioInfoModel {

    private SRParser parser;
    Map<Long, String> primary;
    Map<Long, String> p4;
    Map<Long, String> extra;
    Map<Long, String> others;
    Map<Long, String> channels;

    /**
     * Creates a new RadioInfoModel object and initializes attributes.
     *
     */
    public RadioInfoModel() {
        this.parser = new SRParser();
        this.primary = new LinkedHashMap<>();
        this.p4 = new LinkedHashMap<>();
        this.extra = new LinkedHashMap<>();
        this.others = new LinkedHashMap<>();
    }

    /**
     * Returns the channelId for the channel with the name - name.
     * @param name the name of the channel
     * @return the channel's id, null if no channel with that name could be found
     */
    public Long getChannelIdByName(String name) {
        for(Map.Entry<Long,String>item:channels.entrySet()){
            if(item.getValue().equals(name)){
                return item.getKey();
            }
        }
        return null;
    }

    /**
     *Categorizes @this channels into four categories.
     * @throws IOException if data could not be parsed
     * @throws ParseException if data could not be parsed
     * @throws InterruptedException if data could not be parsed
     */
    public void categorizeChannels() throws IOException, ParseException, InterruptedException {
        this.channels = parser.getParsedChannels();

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

    /**
     * Returns @this extra
     * @return the Map that contains channels
     */
    public Map<Long, String> getExtra() {
        return extra;
    }

    /**
     * Returns @this others
     * @return the Map that contains channels
     */
    public Map<Long, String> getOthers() {
        return others;
    }

    /**
     * Returns @this p4
     * @return the Map that contains channels
     */
    public Map<Long, String> getP4() {
        return p4;
    }

    /**
     * Returns @this primary
     * @return the Map that contains channels
     */
    public Map<Long, String> getPrimary() {
        return primary;
    }

    /**
     * Fetches a channel's tableau and returns it in a form of a List of ProgramInfo objects.
     * @param channelId the id of the channel to be fetched
     * @return the tableau
     * @throws IOException if data could not be parsed
     * @throws InterruptedException if data could not be parsed
     * @throws ParseException if data could not be parsed
     */
    public List<ProgramInfo> getChannelTableau(Long channelId) throws IOException, InterruptedException, ParseException {
        List<ProgramInfo> sortedList = new ArrayList<>();
        List<ProgramInfo> programList = parser.getParsedChannelTableau(channelId);
          for (ProgramInfo program : programList) {
            LocalDateTime sixHoursBefore = LocalDateTime.now(ZoneId.systemDefault()).minusHours(6);
            LocalDateTime twelveHoursAfter = LocalDateTime.now(ZoneId.systemDefault()).plusHours(12);
            if(program.getStartDate().isAfter(sixHoursBefore) && program.getStartDate().isBefore(twelveHoursAfter)){
                sortedList.add(program);
            }
          }
        return sortedList;
    }

}
