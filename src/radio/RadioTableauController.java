package radio;

import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.IOException;

public class RadioTableauController {
    private RadioTableauView view;
    private RadioData data;

    public RadioTableauController() throws IOException, ParseException, InterruptedException {
        this.data = new RadioData();

        SwingUtilities.invokeLater(() -> {
            this.view = new RadioTableauView(data.getPrimary(),data.getP4(), data.getExtra(), data.getOthers());
            this.view.show();
        });
    }
}
