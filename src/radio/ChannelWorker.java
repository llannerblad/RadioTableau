package radio;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChannelWorker extends SwingWorker<List<Map<Long, String>>, Void> {

    private RadioDataModel data;
    private List<Map<Long, String>> channelList;

    public ChannelWorker(RadioDataModel data){
        this.data = data;
    }
    @Override
    protected List<Map<Long, String>> doInBackground() throws Exception {
        this.channelList = new ArrayList<>();
        channelList.add(data.getPrimary());
        channelList.add(data.getP4());
        channelList.add(data.getExtra());
        channelList.add(data.getOthers());

        return channelList;
    }

    @Override
    protected void done() {
        super.done();
    }
}

