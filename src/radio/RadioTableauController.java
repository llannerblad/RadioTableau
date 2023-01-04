package radio;

import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RadioTableauController {
    private RadioTableauView view;
    private RadioDataModel data;

    public RadioTableauController() throws IOException, ParseException, InterruptedException {
        this.data = new RadioDataModel();

        SwingUtilities.invokeLater(() -> {
            ChannelWorker channelWorker = new ChannelWorker(data);
            channelWorker.execute();
            try {
                List<Map<Long, String>> channelList = channelWorker.get();
                this.view = new RadioTableauView(channelList.get(0),
                        channelList.get(1), channelList.get(2), channelList.get(3));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            //this.view = new RadioTableauView(data.getPrimary(),data.getP4(), data.getExtra(), data.getOthers());
            this.view.addChannelOptionListeners(this::onChannelOptionPress);
            this.view.show();
        });
    }

    private void onChannelOptionPress(ActionEvent e) {
        ProgramWorker worker = new ProgramWorker(data, e.getActionCommand());
        try {
            worker.execute();
            view.updateTableData(worker.get());
        } catch (ExecutionException | InterruptedException err) {
            System.out.println(err + "fel i optionPRess");
        }

    }
}
