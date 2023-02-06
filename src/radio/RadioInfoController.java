package radio;

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
    private CachedChannelTableaux cachedChannelTableaux;
    private ChannelWorker channelWorker;
    private Object lock;

    /**
     * Creates a new RadioTableauController object and initializes its attributes.
     */
    public RadioInfoController()  {
        this.lock = new Object();
        this.model = new RadioInfoModel();
        currentChannelName= null;
        this.cachedChannelTableaux = new CachedChannelTableaux();

        SwingUtilities.invokeLater(() -> {
            this.view = new RadioInfoView();
            this.view.show();
            channelWorker = new ChannelWorker(model, view, this::onChannelOptionPress);
            channelWorker.execute();
            this.view.addTableMouseListener(new ProgramClickAdapter());
            this.view.addRefreshButtonListener(this::onRefreshButtonPress);
        });
    }

    /**
     * When a channel is pressed in the JMenuBar, its tableau is displayed in the gui.
     * @param e the event to process
     */
    private void onChannelOptionPress(ActionEvent e) {
        currentChannelName = e.getActionCommand();
        runProgramThread(false, currentChannelName);
        handleTimer();
    }

    /**
     * If a timer object does not exist for the currentChannel, a new TimerThread is created.
     */
    private void handleTimer() {
        synchronized (lock) {
            String temp = currentChannelName;
            if(cachedChannelTableaux.getCachedTableau(currentChannelName) == null) {
                timer(temp);
            }
        }
    }

    /**
     * Creates a new Timer object for the specified channel
     * @param channelToUpdate the name of the channel
     */
    private void timer(String channelToUpdate){
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                runProgramThread(true, channelToUpdate);
            }
        }, HOUR, HOUR);
    }

    /**
     * When the refresh button is pressed in the gui, and there is a tableau in the table,
     * it will refresh that tableau.
     * @param e the event to process
     */
    private void onRefreshButtonPress(ActionEvent e) {
        if(currentChannelName != null) {
            runProgramThread(true, currentChannelName);
        }
    }

    /**
     * Runs the background thread that is responsible for updating and/or show a channel's tableau
     * in the gui. Can only be called by one thread at a time.
     * @param refresh if new data should be fetched from the api
     * @param channelNameToUpdate the name of the channel
     */
    private void runProgramThread(boolean refresh, String channelNameToUpdate) {
        ProgramWorker worker = new ProgramWorker(model, currentChannelName,
                cachedChannelTableaux, view, refresh, channelNameToUpdate, lock);
        worker.execute();
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
            currentTableau = view.getTableData();
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
                view.displayErrorMessage("Kunde inte visa ytterligare information");
            }
        }
    }
}
