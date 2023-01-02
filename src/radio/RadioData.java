package radio;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Map;

public class RadioData {

    private SRParser parser;
    private SRApi api;
    public RadioData(){
        this.parser = new SRParser();
        this.api = new SRApi();
    }

    public Map getChannels() throws ParseException, IOException, InterruptedException {
        String json = api.getChannels();
        return parser.getParsedChannels(json);
    }

    public Map<Long, ProgramInfo> getChannelTableau(int channelId) throws IOException, InterruptedException, ParseException {
        return parser.getParsedChannelTableau(api.getChannelTableau(channelId));

    }

}
