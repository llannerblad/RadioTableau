package radio;

import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;

/**
 * Background thread for getting a channel's tableau.
 * @author Lee Lannerblad (ens19lld)
 * Course: Applikationsutveckling (Java)
 * Version information: 2023-01-09
 */
public class ProgramWorker extends SwingWorker<List<ProgramInfo>, Void> {
    private RadioInfoModel model;
    private String channelName;
    private RadioInfoView view;
    private CachedChannelTablueax cachedChannelTablueax;
    private boolean refresh;
    private String channelNameToUpdate;
    private Lock lock;

    /**
     * Creates and initializes a new ProgramWorker object.
     * @param model the model object
     * @param currentChannelName the name of the channel that is currently dispayed in the view
     * @param cachedChannelTablueax the object containing all cached tableaux
     * @param view the view object
     * @param refresh if the tableau should be fetched even though if it is cached
     * @param channelNameToUpdate the name of the channel that is going to be updated
     */
    public ProgramWorker(RadioInfoModel model, String currentChannelName,
                         CachedChannelTablueax cachedChannelTablueax,
                         RadioInfoView view, boolean refresh,
                         String channelNameToUpdate){
        this.model = model;
        this.channelName = currentChannelName;
        this.cachedChannelTablueax = cachedChannelTablueax;
        this.view = view;
        this.refresh = refresh;
        this.channelNameToUpdate = channelNameToUpdate;
    }

    /**
     * If the tableau is cached that list will be returned, otherwise the model's method getChannelTableau is called
     * to fetch the tableau. If refresh is true new data will always be fetched.
     * @return the tableau as a list
     */
    @Override
    protected List<ProgramInfo> doInBackground() {

       System.out.println(Thread.currentThread() + "Är i doinbakcground");

       List <ProgramInfo> list = cachedChannelTablueax.getCachedTableau(channelName);
       System.out.println("i doinbackground");
       if (list == null || refresh) {
           System.out.println("Kommer vi hit?");
           try {
               list = model.getChannelTableau(model.getChannelIdByName(channelName));
           } catch (IOException | ParseException | InterruptedException e) {
               view.displayErrorMessage("Kunde inte hämta tablå för: " + channelName);
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
            System.out.println("idone");
            if(resultData.isEmpty()){
                view.displayErrorMessage("Hittar inga program för den valda kanalen: " + channelName);
            }
            if(channelNameToUpdate == channelName) {
                view.updateTableData(get());
            }
            cachedChannelTablueax.addTableau(get(), channelName);
        } catch (InterruptedException | ExecutionException e) {
            view.displayErrorMessage("Kunde inte hämta resultat för: " + channelName);
        }
        }

    }
