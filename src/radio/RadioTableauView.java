package radio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

public class RadioTableauView {
    private JFrame frame;
    private ImageIcon imageStart;

    private JTable table;
    private JButton button;
    private JLabel displayField;
    public RadioTableauView(Map<Long, String> primary, Map<Long, String> p4,
                            Map<Long, String> extra, Map<Long, String> others){

        Map<Long, ProgramInfo> map = new LinkedHashMap<>();
        map.put(1L, new ProgramInfo("test", "Beskrivning", "2022", "20023", "url", "P3"));
        frame = new JFrame("SR RadioInfo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(650,550));
        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.add(buildCenterPanel());

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu menu = new JMenu("Välj tablå");
        menuBar.add(menu);
        for(Map.Entry<Long,String>it:primary.entrySet()){
            JMenuItem item = new JMenuItem(it.getValue());
            menu.add(item);
        }
        JMenu p4Channels = new JMenu("P4-kanaler");
        menu.add(p4Channels);
        for(Map.Entry<Long,String>it:p4.entrySet()){
            JMenuItem item = new JMenuItem(it.getValue());
            p4Channels.add(item);
        }
        JMenu extraChannels = new JMenu("Extra-kanaler");
        menu.add(extraChannels);
        for(Map.Entry<Long,String>it:extra.entrySet()){
            JMenuItem item = new JMenuItem(it.getValue());
            extraChannels.add(item);
        }
        JMenu otherChannels = new JMenu("Övriga kanaler");
        menu.add(otherChannels);
        for(Map.Entry<Long,String>it:others.entrySet()){
            JMenuItem item = new JMenuItem(it.getValue());
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("press");
                    displayField.setVisible(false);
                }
            });
            otherChannels.add(item);
        }

        buildTable(map);

        button = new JButton("Ta bort bild");
        button.addActionListener(this::onPressButtonListener);
        frame.add(button, BorderLayout.SOUTH);

    }
    private void onPressButtonListener(ActionEvent e) {
        displayField.setVisible(false);
    }

   private void buildTable(Map<Long, ProgramInfo> map){

        Map<Long, ProgramInfo> data = map;
        MyTableModel model = new MyTableModel();
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        frame.add(sp);
    }

    private JPanel buildCenterPanel(){
        JPanel centerPanel = new JPanel();
        imageStart = new ImageIcon("startbild.png");
        displayField = new JLabel(imageStart);
        centerPanel.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(displayField);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        return centerPanel;
    }

    public void show(){
        frame.setVisible(true);
    }
}
