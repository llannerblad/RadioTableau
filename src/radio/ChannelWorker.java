package radio;

import org.json.simple.parser.ParseException;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Background thread for getting a List of the channels of Sveriges Radio.
 * @author Lee Lannerblad (ens19lld)
 * Course: Applikationsutveckling (Java)
 * Version information: 2023-02-07
 */
public class ChannelWorker extends SwingWorker<List<Map<Long, String>>, Void> {
    private RadioInfoModel model;
    private List<Map<Long, String>> channelList;
    private RadioInfoView view;
    private ActionListener listener;

    /**
     * Creates a new ChannelWorker object
     * @param model the model object used to fetch data
     * @param view the view object to be updated
     * @param listener ActionListener to be added to the view's channel options
     */
    public ChannelWorker(RadioInfoModel model, RadioInfoView view, ActionListener listener){
        this.model = model;
        this.view = view;
        this.listener = listener;
    }

    /**
     * Creates an ArrayList containing the different categories of channels
     * @return the list of categorized channels
     */
    @Override
    protected List<Map<Long, String>> doInBackground() throws IOException, ParseException, InterruptedException, NullPointerException {
        this.channelList = new ArrayList<>();
        model.categorizeChannels();
        channelList.add(model.getPrimary());
        channelList.add(model.getP4());
        channelList.add(model.getExtra());
        channelList.add(model.getOthers());
        if(channelList.isEmpty()){
            throw new NullPointerException("Inga kanaler fanns att hämta.");
        }
        return channelList;
    }

    /**
     * If channelList is not empty the view's menu bar is updated with the channels, otherwise
     * an error message is displayed to the user.
     */
    @Override
    protected void done() {
        try {
            List<Map<Long, String>> channelList = get();
            view.setMenuBar(channelList.get(0), channelList.get(1), channelList.get(2), channelList.get(3));
            view.addChannelOptionListeners(listener);
        } catch (InterruptedException e) {
            view.displayErrorMessage("Kunde inte hämta kanaler (InterruptedException): " + e.getMessage());
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if(cause instanceof ParseException) {
                view.displayErrorMessage("Kunde inte hämta kanaler (ParseException):  " + e.getMessage());
            }
            if(cause instanceof IOException) {
                view.displayErrorMessage("Kunde inte hämta kanaler (IOException):  " + e.getMessage());
            }
            if (cause instanceof NullPointerException) {
                view.displayErrorMessage(cause.getMessage());
            }
        }
    }
}

