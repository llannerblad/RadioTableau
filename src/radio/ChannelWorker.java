package radio;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ChannelWorker extends SwingWorker<List<Map<Long, String>>, Void> {

    private RadioDataModel data;
    private List<Map<Long, String>> channelList;
    private RadioTableauView view;
    private ActionListener l;

    public ChannelWorker(RadioDataModel data, RadioTableauView view, ActionListener e){
        this.data = data;
        this.view = view;
        this.l = e;
    }
    @Override
    protected List<Map<Long, String>> doInBackground() {
        this.channelList = new ArrayList<>();
        channelList.add(data.getPrimary());
        channelList.add(data.getP4());
        channelList.add(data.getExtra());
        channelList.add(data.getOthers());
        return channelList;
    }

    @Override
    protected void done() {
        try {
            List<Map<Long, String>> channelList = get();
            if(channelList.isEmpty()){
                view.displayErrorMessage("Kunde inte hämta kanaler.");
            }
            else{
                view.updateMenuBar(channelList.get(0), channelList.get(1), channelList.get(2), channelList.get(3));
                view.addChannelOptionListeners(l);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

