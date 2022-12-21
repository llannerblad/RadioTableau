package radio;
import org.json.simple.parser.ParseException;
import java.io.IOException;

import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, ParseException {
	// write your code here
        RadioData data = new RadioData();
        Map<String, Long> channels = data.getChannels();
        data.getChannelTableau(164);

        for(Map.Entry<String,Long>it:channels.entrySet())
            System.out.println(it.getValue()+", " + it.getKey());
    }


}
