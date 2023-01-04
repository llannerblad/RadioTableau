package radio;
import org.json.simple.parser.ParseException;
import java.io.IOException;

import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
	// write your code here
        RadioDataModel data = new RadioDataModel();
        Map<Long, String> channels = data.getChannels();
        //List<ProgramInfo> list = data.getChannelTableau(164);

        /*for(Map.Entry<Long,ProgramInfo>it:map.entrySet()){
            System.out.println(it.getValue());
            System.out.println(it.getValue().toString());
        }
        for(Map.Entry<Long,String>it:channels.entrySet())
            System.out.println(it.getValue()+", " + it.getKey()); */

    }


}
