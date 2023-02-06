package radio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Object that is used to maintain cached tableax for channels. Consists of a HashMap where
 * the key is the channel name and the value is a List of ProgramInfo objects (the tableau)
 * @author Lee Lannerblad (ens19lld)
 * Course: Applikationsutveckling (Java)
 * Version information: 2023-01-09
 */
public class CachedChannelTableaux {
    private Map<String, List<ProgramInfo>> cache;

    /**
     * Creates a new CachedChannelTableaux object and initializes @this list.
     */
    public CachedChannelTableaux(){
        cache = new HashMap<>();
    }

    /**
     * Adds a tableau to cache for the channel with the name channelName.
     * @param tableau the tableau to cache
     * @param channelName name of the channel
     */
    public void addTableau(List<ProgramInfo> tableau, String channelName){
        cache.put(channelName,tableau);
    }

    /**
     * Returns the cached tableau for the channel
     * @param channelName the name of the channel
     * @return the cached tableau
     */
    public List<ProgramInfo> getCachedTableau(String channelName){
        return cache.get(channelName);
    }

    public void print(){
        for(String info: cache.keySet()){
            String key = info.toString();
            String p = cache.get(key).toString();
            System.out.println(key + " " + p);
        }
    }
}
