package radio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RadioTableauView {
    private JFrame frame;
    private ImageIcon imageStart;
    private JTable table;
    private JButton button;
    private JLabel displayField;
    private List<JMenuItem> channelOptions;

    private MyTableModel model;
    public RadioTableauView(Map<Long, String> primary, Map<Long, String> p4,
                            Map<Long, String> extra, Map<Long, String> others){

        channelOptions = new ArrayList<>();

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
            channelOptions.add(item);
        }
        JMenu p4Channels = new JMenu("P4-kanaler");
        menu.add(p4Channels);
        for(Map.Entry<Long,String>it:p4.entrySet()){
            JMenuItem item = new JMenuItem(it.getValue());
            p4Channels.add(item);
            channelOptions.add(item);
        }
        JMenu extraChannels = new JMenu("Extra-kanaler");
        menu.add(extraChannels);
        for(Map.Entry<Long,String>it:extra.entrySet()){
            JMenuItem item = new JMenuItem(it.getValue());
            extraChannels.add(item);
            channelOptions.add(item);
        }
        JMenu otherChannels = new JMenu("Övriga kanaler");
        menu.add(otherChannels);
        for(Map.Entry<Long,String>it:others.entrySet()){
            JMenuItem item = new JMenuItem(it.getValue());
            otherChannels.add(item);
            channelOptions.add(item);
        }

        buildTable();

        button = new JButton("Ta bort bild");
        button.addActionListener(this::onPressButtonListener);
        //frame.add(button, BorderLayout.SOUTH);
        frame.add(new JLabel("Tryck på ett program för att visa mer information"), BorderLayout.SOUTH);
        frame.add(displayField, BorderLayout.NORTH);
    }
    private void onPressButtonListener(ActionEvent e) {
        displayField.setVisible(false);
    }

   private void buildTable(){
        this.model = new MyTableModel();
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        frame.add(sp);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("row" + table.rowAtPoint(e.getPoint()));
                System.out.println("column" + table.columnAtPoint(e.getPoint()));
            }
        });
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

    public void addChannelOptionListeners(ActionListener listener) {
        for (JMenuItem channel : channelOptions) {
            channel.addActionListener(listener);
        }
    }

    public void updateTableData(List<ProgramInfo> data){
        model.setData(data);
        model.fireTableDataChanged();
    }

    public void addTableMouseListener(MouseListener listener) {
        table.addMouseListener(listener);
    }
}
