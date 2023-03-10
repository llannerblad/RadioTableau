package radio;

import org.json.simple.parser.ParseException;
import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Background thread for getting a channel's tableau.
 * @author Lee Lannerblad (ens19lld)
 * Course: Applikationsutveckling (Java)
 * Version information: 2023-02-07
 */
public class ProgramWorker extends SwingWorker<List<ProgramInfo>, Void> {
    private RadioInfoModel model;
    private String channelName;
    private RadioInfoView view;
    private CachedChannelTableaux cachedChannelTableaux;
    private boolean refresh;
    private String channelNameToUpdate;
    private Object lock;

    /**
     * Creates and initializes a new ProgramWorker object.
     * @param model the model object
     * @param currentChannelName the name of the channel that is currently dispayed in the view
     * @param cachedChannelTableaux the object containing all cached tableaux
     * @param view the view object
     * @param refresh if the tableau should be fetched even though if it is cached
     * @param channelNameToUpdate the name of the channel that is going to be updated
     */
    public ProgramWorker(RadioInfoModel model, String currentChannelName,
                         CachedChannelTableaux cachedChannelTableaux,
                         RadioInfoView view, boolean refresh,
                         String channelNameToUpdate, Object lock) {
        this.model = model;
        this.channelName = currentChannelName;
        this.cachedChannelTableaux = cachedChannelTableaux;
        this.view = view;
        this.refresh = refresh;
        this.channelNameToUpdate = channelNameToUpdate;
        this.lock = lock;
    }

    /**
     * If the tableau is cached that list will be returned, otherwise the model's method getChannelTableau is called
     * to fetch the tableau. If refresh is true new data will always be fetched.
     * @return the tableau as a list
     */
    @Override
    protected List<ProgramInfo> doInBackground() throws IOException, ParseException, InterruptedException, NullPointerException {
        List <ProgramInfo> list;
        synchronized (lock) {
            list = cachedChannelTableaux.getCachedTableau(channelName);
            if (list == null || refresh) {
                list = model.getChannelTableau(model.getChannelIdByName(channelName));
                cachedChannelTableaux.addTableau(list, channelName);

                if(list.isEmpty()) {
                    throw new NullPointerException("Hittar inga program f??r den valda kanalen: " + channelName);
                }
            }
        }
        return list;
    }

    /**
     * If the channel tableau that was fetched is the same as the one that is currently displayed in the view,
     * the view's table will be updated with the new data. Caches the tableau. If no data could be fetched,
     * an error message will be displayed in the view.
     */
    @Override
    protected void done() {
        List<ProgramInfo> resultData;
        try {
            resultData = get();
            if (channelNameToUpdate == channelName) {
                view.updateTableData(resultData);
            }
        } catch (InterruptedException e) {
            view.displayErrorMessage("Kunde inte h??mta resultat f??r: " + channelName);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ParseException) {
                view.displayErrorMessage("Kunde inte h??mta resultat f??r: " + channelName);
            }
            if (cause instanceof IOException) {
                view.displayErrorMessage("Kunde inte h??mta resultat f??r: " + channelName);
            }
            if (cause instanceof NullPointerException) {
                view.displayErrorMessage(cause.getMessage());
            }
        }
    }
}
