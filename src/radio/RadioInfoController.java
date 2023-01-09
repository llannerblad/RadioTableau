package radio;

import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

/**
 * Acts as a controller for the application RadioTableau. Controls the communication between
 * RadioTableauModel- and RadioTableauView object.
 * @author Lee Lannerblad (ens19lld)
 * Course: Applikationsutveckling (Java)
 * Version information: 2023-01-09
 */
public class RadioInfoController {
    private static final int HOUR = 3600000;
    private RadioInfoView view;
    private RadioInfoModel model;
    private List<ProgramInfo> currentTableau;
    private String currentChannelName;
    private CachedChannelTablueax cachedChannelTablueax;
    private Map<String, Thread> threadPool;

    /**
     * Creates a new RadioTableauController object and initializes its attributes.
     */
    public RadioInfoController()  {
        try {
            this.model = new RadioInfoModel();
        } catch (IOException | ParseException  | InterruptedException e) {
            view.displayErrorMessage("Kunde inte hämta kanaler.");
        }
        currentChannelName= null;
        this.threadPool = new HashMap<>();
        this.cachedChannelTablueax = new CachedChannelTablueax();

        SwingUtilities.invokeLater(() -> {
            this.view = new RadioInfoView();
            ChannelWorker channelWorker = new ChannelWorker(model, view, this::onChannelOptionPress);
            channelWorker.execute();
            this.view.addTableMouseListener(new ProgramClickAdapter());
            this.view.addRefreshButtonListener(this::onRefreshButtonPress);
            this.view.show();
        });
    }

    /**
     * When a channel is pressed in the JMenuBar, its tableau is displayed in the gui.
     * @param e the event to process
     */
    private void onChannelOptionPress(ActionEvent e) {
        currentChannelName = e.getActionCommand();
        runProgramThread(false, currentChannelName);
        handleTimerThread();

    }

    /**
     * If a TimerThread object does not exist for the currentChannel, a new TimerThread is created.
     */
    private synchronized void handleTimerThread(){
        if(threadPool.get(currentChannelName) == null) {
            TimerThread t = new TimerThread(currentChannelName);
            threadPool.put(currentChannelName,t);
            t.start();
        }
    }

    /**
     * When the refresh button is pressed in the gui, and there is a tableau in the table,
     * it will refresh that tableau.
     * @param e the event to process
     */
    private void onRefreshButtonPress(ActionEvent e) {
        if(currentTableau != null) {
            runProgramThread(true, currentChannelName);
        }
    }



    /**
     * A thread used for updating a channel's tableau automatically every hour.
     */
    private class TimerThread extends Thread {
        private String channelNameToUpdate;

        /**
         * Creates a new TimerThread object.
         * @param channelNameToUpdate the name of the channel to monitor
         */
        public TimerThread(String channelNameToUpdate){
            this.channelNameToUpdate = channelNameToUpdate;
        }

        /**
         * Runs the timer method when the thread is executed.
         */
        public void run(){
            timer();
        }

        /**
         * Creates a new timer object that runs the method runProgramThread every hour.
         */
        private void timer(){
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    runProgramThread(true, channelNameToUpdate);
                }
            }, HOUR, HOUR);
        }
    }


    /**
     * Runs the background thread that is responsible for updating and/or show a channel's tableau
     * in the gui. Can only be called by one thread at a time.
     * @param refresh if new data should be fetched from the api
     * @param channelNameToUpdate the name of the channel
     */
    private synchronized void runProgramThread(boolean refresh, String channelNameToUpdate) {
        ProgramWorker worker = new ProgramWorker(model, currentChannelName,
                cachedChannelTablueax, view, refresh, channelNameToUpdate);
        try {
            worker.execute();
            currentTableau = worker.get();
        } catch (ExecutionException | InterruptedException err) {
            System.out.println(err + "Kunde inte hämta resultat.");
        }

    }

    /**
     * Listener that listens for mouse click in the view's Jtable. When the user clicks a row,
     * that represents a program, additional information is about that program is shown in the
     * view. An image, a title and a description is provided.
     */
    class ProgramClickAdapter extends MouseAdapter {
        private ProgramInfo program;
        private String description;
        private String title;
        private Icon icon;

        /**
         * When the user clicks the JTable in the view. Checks which program was clicked and
         * gets additional information and then displays it to the user.
         * @param e the event to be processed
         */
        public void mouseClicked(MouseEvent e){
            view.getTable().setEnabled(false);
            view.getTable().rowAtPoint(e.getPoint());
            this.program = currentTableau.get(view.getTable().rowAtPoint(e.getPoint()));

            showAdditionalInformationAboutProgram();

        }

        /**
         * Shows additional information about a program. If no available image exists, "noimage.png" is shown instead.
         * If description is missing, "Ingen tillgänglig beskrivning" will be shown as the desciption.
         */
        private void showAdditionalInformationAboutProgram(){
            this.description = program.getDescription();
            this.title = program.getTitle();

            if(this.program.getDescription().equals("")){
                description = "Ingen tillgänglig beskrivning";
            }
            try {
                if(!program.getImageUrl().equals("-")){
                    URL url = new URL(program.getImageUrl());
                    Image image = ImageIO.read(url);
                    Image scaledImage = image.getScaledInstance(350,200, Image.SCALE_SMOOTH);
                    this.icon= new ImageIcon(scaledImage);
                }
                else{
                    this.icon= new ImageIcon("noimage.png");
                }
                view.showProgramDialog(this.icon, this.description, this.title);
                view.getTable().setEnabled(true);

            } catch (IOException err) {
                view.displayErrorMessage("Kunde inte hämta visa ytterligare information");
            }
        }
    }
}
