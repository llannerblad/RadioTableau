package radio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachedChannelTablueax {
    private Map<String, List<ProgramInfo>> list;

    public CachedChannelTablueax(){
        list = new HashMap<>();
    }

    public void addTableau(List<ProgramInfo> tableau, String channelName){
        list.put(channelName,tableau);
    }

    public List<ProgramInfo> getCachedTableu(String channelName){
        return list.get(channelName);
    }
}
