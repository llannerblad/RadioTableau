package radio;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * Table model used to be able to present a channel's tableau in a JTable object. Has three columns, the name,
 * the start time, and the end time of the program.
 * @author Lee Lannerblad (ens19lld)
 * Course: Applikationsutveckling (Java)
 * Version information: 2023-01-09
 */
public class TableauTableModel extends AbstractTableModel {
    private String[] columnNames;
    private List<ProgramInfo> tableau;

    /**
     * Creates a new MyTableModel object and initializes @this attributes.
     */
    public TableauTableModel(){
        String[] columnNames ={"Program",
                "Starttid",
                "Sluttid"};
        this.columnNames = columnNames;
        tableau = new ArrayList<>();
    }

    /**
     * Sets @this tableau.
     * @param tableau the data
     */
    public void setTableau(List<ProgramInfo> tableau) {
        this.tableau = tableau;
    }

    /**
     * Returns number of rows in tableau
     * @return number of rows
     */
    @Override
    public int getRowCount() {
        return tableau.size();
    }

    /**
     * Returns number of columns in tableau, always 3
     * @return number of columns
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Returns the value in tableau at rowIndex and columnIndex
     * @param rowIndex        the row whose value is to be queried
     * @param columnIndex     the column whose value is to be queried
     * @return the value
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

        return switch (columnIndex){
            case 0 -> tableau.get(rowIndex).getName();
            case 1 -> tableau.get(rowIndex).getStartDate().format(formatter);
            case 2 -> tableau.get(rowIndex).getEndDate().format(formatter);
            default -> "-";
        };
    }

    /**
     * Returns the name of the column
     * @param column  the column being queried
     * @return the name of the column as a String
     */
    public String getColumnName(int column){
        return columnNames[column];
    }

    /**
     * Tableau is not editable therefore the cells are not editable.
     * @param rowIndex  the row being queried
     * @param columnIndex the column being queried
     * @return false
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public List<ProgramInfo> getTableau(){
        return tableau;
    }

}
