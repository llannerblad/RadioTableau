package radio;

import javax.swing.*;
import java.util.List;

public class ProgramWorker extends SwingWorker<List<ProgramInfo>, Void> {

    private RadioDataModel data;

    private String channelName;
    public ProgramWorker(RadioDataModel data, String channelName){
        this.data = data;
        this.channelName = channelName;
    }
    @Override
    protected List<ProgramInfo> doInBackground() throws Exception {
        List<ProgramInfo> list = data.getChannelTableau(channelName);
            return list;
    }

    @Override
    protected void done() {
        super.done();
    }
}
