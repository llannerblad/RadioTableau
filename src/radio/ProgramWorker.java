package radio;

import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class ProgramWorker extends SwingWorker<List<ProgramInfo>, Void> {

    private RadioDataModel data;
    private String channelName;
    private RadioTableauView view;
    private CachedChannelTablueax cachedChannelTablueax;
    private boolean refresh;
    private String channelNameToUpdate;
    private Lock lock;

    public ProgramWorker(RadioDataModel data, String channelName,
                         CachedChannelTablueax cachedChannelTablueax,
                         RadioTableauView view, boolean refresh,
                         String channelNameToUpdate, Lock backgroundLock){
        this.data = data;
        this.channelName = channelName;
        this.cachedChannelTablueax = cachedChannelTablueax;
        this.view = view;
        this.refresh = refresh;
        this.channelNameToUpdate = channelNameToUpdate;
        this.lock= backgroundLock;
    }

    @Override
    protected List<ProgramInfo> doInBackground() throws InterruptedException {
        List<ProgramInfo> list = new ArrayList<>();

           try {
               if(lock.tryLock()) {
                   System.out.println("kollar cached");
                   list = cachedChannelTablueax.getCachedTableu(channelName);
                   System.out.println("i doinbackground");
                   if (list == null || refresh) {
                       System.out.println("Kommer vi hit?");
                       try {
                           list = data.getChannelTableau(data.getChannelIdByName(channelName));

                       } catch (IOException e) {
                           e.printStackTrace();
                       } catch (ParseException e) {
                           e.printStackTrace();
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }
                   }
               }

           } finally {
               System.out.println("Låserupp");
               lock.unlock();
           }
        return list;
    }

    @Override
    protected void done() {
        try {
            List<ProgramInfo> resultData = get();
            if(lock.tryLock()){
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
                System.out.println("väntar i done");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
