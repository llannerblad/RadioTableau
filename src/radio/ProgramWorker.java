package radio;

import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
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
    private RadioTableauModel model;
    private String channelName;
    private RadioTableauView view;
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
     * @param backgroundLock the lock object to ensure thread safety
     */
    public ProgramWorker(RadioTableauModel model, String currentChannelName,
                         CachedChannelTablueax cachedChannelTablueax,
                         RadioTableauView view, boolean refresh,
                         String channelNameToUpdate, Lock backgroundLock){
        this.model = model;
        this.channelName = currentChannelName;
        this.cachedChannelTablueax = cachedChannelTablueax;
        this.view = view;
        this.refresh = refresh;
        this.channelNameToUpdate = channelNameToUpdate;
        this.lock= backgroundLock;
    }

    /**
     * If the tableau is cached that list will be returned, otherwise the model's method getChannelTableau is called
     * to fetch the tableau. If refresh is true new data will always be fetched.
     * @return the tableau as a list
     */
    @Override
    protected List<ProgramInfo> doInBackground() {
        List<ProgramInfo> list = new ArrayList<>();

           try {
               if(lock.tryLock()) {
                   System.out.println(Thread.currentThread() + "Är i doinbakcground");
                   Thread.sleep(1);
                   list = cachedChannelTablueax.getCachedTableau(channelName);
                   System.out.println("i doinbackground");
                   if (list == null || refresh) {
                       System.out.println("Kommer vi hit?");
                       try {
                           list = model.getChannelTableau(model.getChannelIdByName(channelName));

                       } catch (IOException e) {
                           e.printStackTrace();
                       } catch (ParseException e) {
                           e.printStackTrace();
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
               }
           } catch (InterruptedException e) {
               throw new RuntimeException(e);
           } finally {
               System.out.println("Låserupp");
               lock.unlock();
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

        try {
            if(lock.tryLock()){
                List<ProgramInfo> resultData = get();
                try{
                    System.out.println("idone");
                    if(resultData.isEmpty()){
                        view.displayErrorMessage("Hittar inga program för den valda kanalen: " + channelName);
                    }
                    if(channelNameToUpdate == channelName) {
                        view.updateTableData(get());
                    }
                    cachedChannelTablueax.addTableau(get(), channelName);
                } finally {
                    lock.unlock();
                }
            }
            else{
                System.out.println("väntar i done" + Thread.currentThread());

            }
        } catch (InterruptedException | ExecutionException e) {
            view.displayErrorMessage("Kunde inte hämta resultat" + e.getMessage());
        }
    }
}
