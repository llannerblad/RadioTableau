package radio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GUI of the application called RadioTableau. Displays data from the RadioTableau provided by the RadioTableauController.
 * Sends the user's actions through the RadioTableauController to the RadioTableauModel. Has a JMenuBar containing
 * a JMenu that's containing channels, a JTable for displaying a channel's tableau and a JButton for refreshing
 * the current tableau that is displayed in the gui. Uses a JOptionPane to show additional information about a specific
 * program and a JOptionPane to show error messages to the user.
 * @author Lee Lannerblad (ens19lld)
 * Course: Applikationsutveckling (Java)
 * Version information: 2023-01-09
 */
public class RadioTableauView {
    private JFrame frame;
    private JTable table;
    private JButton refreshButton;
    private JLabel displayField;
    private List<JMenuItem> channelOptions;
    private JMenuBar menuBar;
    private JMenu menu;
    private MyTableModel model;

    /**
     * Creates and initializes a new RadioTableauView object.
     */
    public RadioTableauView(){
        channelOptions = new ArrayList<>();
        frame = new JFrame("SR RadioInfo");
        refreshButton = new JButton("Uppdatera tablå");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(650,550));
        frame.setJMenuBar(buildMenuBar());
        frame.add(buildCenterPanel(), BorderLayout.CENTER);
        frame.add(refreshButton, BorderLayout.SOUTH);
        frame.add(buildTopPanel(), BorderLayout.NORTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    /**
     * Initializes @this menuBar and menu
     * @return @this menuBar containing @this menu
     */
    private JMenuBar buildMenuBar() {
        menuBar= new JMenuBar();
        menu = new JMenu("Välj tablå");
        menuBar.add(menu);
        return menuBar;
    }

    /**
     * Sets the content of @this menuBar. The menu bar needs to contain primary, p4, extra and others channels.
     * @param primary the primary channels
     * @param p4 all p4 channels
     * @param extra extra channels
     * @param others all other channels that does not fit in any of the other categories
     */
    public void setMenuBar(Map<Long, String> primary, Map<Long, String> p4,
                           Map<Long, String> extra, Map<Long, String> others){

        addPrimaryChannelsToMenu(primary);
        addSubMenu("P4-kanaler", p4);
        addSubMenu("Extra-kanaler", extra);
        addSubMenu("Övriga-kanaler", others);
    }

    /**
     * Adds the primary channels, P1, P2, P3 to @this menu
     * @param channels the map that is containing the channels to be added
     */
    private void addPrimaryChannelsToMenu(Map<Long, String> channels) {
        for(Map.Entry<Long,String>it:channels.entrySet()){
            JMenuItem item = new JMenuItem(it.getValue());
            menu.add(item);
            channelOptions.add(item);
        }
    }

    /**
     * Adds a sub menu containing channels to @this menu and adds each channel to @this channelOptions.
     * @param subMenuName name of the sub menu
     * @param channels the map that is containing the channels to be added to the sub menu
     */
    private void addSubMenu(String subMenuName, Map<Long, String> channels){
        JMenu submenu = new JMenu(subMenuName);
        menu.add(submenu);
        for(Map.Entry<Long,String>it:channels.entrySet()){
            JMenuItem item = new JMenuItem(it.getValue());
            submenu.add(item);
            channelOptions.add(item);
        }
    }

    /**
     * Adds a ActionListener to @this refreshButton
     * @param listener ActionListener to add
     */
    public void addRefreshButtonListener(ActionListener listener){
        refreshButton.addActionListener(listener);
    }

    /**
     * Builds a new JTable with a MyTableModel object as table model and puts it in a JScrollPane.
     * @return JScrollPane containing content for the Center panel in @this frame
     */
   private JScrollPane buildCenterPanel(){
        this.model = new MyTableModel();
        table = new JTable(model);
        return new JScrollPane(table);
    }

    /**
     * Used to show additional information about a program when the user clicks on a row in the table.
     * Shows a JOptionPane containing an icon, a description and a title
     * @param icon Icon of the program
     * @param description description of the program
     * @param title title of the program
     */
    public void showProgramDialog(Icon icon, String description, String title){
        JOptionPane.showMessageDialog(null, description, title, JOptionPane.INFORMATION_MESSAGE, icon);

    }

    /**
     * Returns @this table
     * @return JTable table
     */
    public JTable getTable() {
        return table;
    }

    /**
     *
     * @return
     */
    private JPanel buildTopPanel() {
        JPanel topPanel = new JPanel();
        ImageIcon img = new ImageIcon("startbild.png");
        displayField = new JLabel(img);
        topPanel.setLayout(new BorderLayout());
        topPanel.add(displayField, BorderLayout.CENTER);
        return topPanel;
    }

    /**
     * Show the gui, should only be called on EDT
     */
    public void show(){
        frame.setVisible(true);
    }

    /**
     * Adds an ActionListener to all JMenuItem's that represents a channel.
     * @param listener ActionListener
     */
    public void addChannelOptionListeners(ActionListener listener) {
        for (JMenuItem channel : channelOptions) {
            channel.addActionListener(listener);
        }
    }

    /**
     * Updates @this table's data
     * @param data the data to be displayed in the table
     */
    public void updateTableData(List<ProgramInfo> data){
        model.setTableau(data);
        model.fireTableDataChanged();
    }

    /**
     * Adds a MouseListener to @this table
     * @param listener the MouseListener
     */
    public void addTableMouseListener(MouseListener listener) {
        table.addMouseListener(listener);
    }

    /**
     * Displays an error message to the user in a popup dialog.
     * @param errorMessage the error message to be displayed
     */
    public void displayErrorMessage(String errorMessage){
        JOptionPane.showMessageDialog(frame,errorMessage);
    }

}
