package radio;

import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RadioTableauController {
    private static final int HOUR = 3600000;
    private RadioTableauView view;
    private RadioDataModel dataModel;
    private List<ProgramInfo> currentTableau;
    private String currentChannelName;
    private CachedChannelTablueax cachedChannelTablueax;
    private Map<String, Thread> threadPool;
    private Lock lock;

    public RadioTableauController() throws IOException, ParseException, InterruptedException {
        this.dataModel = new RadioDataModel();
        currentChannelName= null;
        this.threadPool = new HashMap<>();
        this.lock = new ReentrantLock();
        this.cachedChannelTablueax = new CachedChannelTablueax();

        SwingUtilities.invokeLater(() -> {
            this.view = new RadioTableauView();
            ChannelWorker channelWorker = new ChannelWorker(dataModel, view, this::onChannelOptionPress);
            channelWorker.execute();
            this.view.addTableMouseListener(new ProgramClickAdapter());
            this.view.addRefreshButtonListener(this::onRefreshButtonPress);
            this.view.show();
        });
    }

    private void onChannelOptionPress(ActionEvent e) {

        currentChannelName = e.getActionCommand();
        runProgramThread(false, currentChannelName);
        if(threadPool.get(currentChannelName) == null){
            System.out.println("Skapar ny tråd för kanal: " + currentChannelName);
            TimerThread t = new TimerThread(currentChannelName);
            threadPool.put(currentChannelName,t);
            t.start();
        }
    }

    private void onRefreshButtonPress(ActionEvent e) {
        if(currentTableau != null) {
            runProgramThread(true, currentChannelName);
            System.out.println("Refreshing");
        }

    }

    private void timer(String channelNameToUpdate){
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("i run i TimertASk");
                runProgramThread(true, channelNameToUpdate);
            }
        }, HOUR, HOUR);
    }

    private class TimerThread extends Thread {
        private String channelNameToUpdate;
        public TimerThread(String channelNameToUpdate){
            this.channelNameToUpdate = channelNameToUpdate;
        }
        public void run(){
            timer(this.channelNameToUpdate);
        }
    }

    private void runProgramThread(boolean refresh, String channelNameToUpdate) {
        ProgramWorker worker = new ProgramWorker(dataModel, currentChannelName,
                cachedChannelTablueax, view, refresh, channelNameToUpdate, lock);
        try {
            worker.execute();
            currentTableau = worker.get();
        } catch (ExecutionException | InterruptedException err) {
            System.out.println(err + "Kunde inte hämta resultat.");
        }

    }

    class ProgramClickAdapter extends MouseAdapter {
        public void mouseClicked(MouseEvent e){
            view.getTable().rowAtPoint(e.getPoint());
            ProgramInfo prog = currentTableau.get(view.getTable().rowAtPoint(e.getPoint()));
            Image image;
            ImageIcon icon;
            String description = prog.getDescription();
            System.out.println(description);
            if(prog.getDescription().equals("")){
                description = "Ingen tillgänglig beskrivning";
            }

            try {
                if(!prog.getImageUrl().equals("-")){
                    URL url = new URL(prog.getImageUrl());
                    image = ImageIO.read(url);
                    Image scaledImage = image.getScaledInstance(350,200, Image.SCALE_SMOOTH);
                    icon= new ImageIcon(scaledImage);

                }
                else{
                    icon= new ImageIcon("noimage.png");
                }
                view.showProgramDialog(icon, description, prog.getTitle());
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
