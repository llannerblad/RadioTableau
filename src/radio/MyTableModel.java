package radio;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyTableModel extends AbstractTableModel {
    private String[] columnNames;
    //private Map<Long, ProgramInfo> data;
    private List<ProgramInfo> data;

    public MyTableModel(){
        String[] columnNames ={"Program",
                "Starttid",
                "Sluttid"};
        this.columnNames = columnNames;

        data = new ArrayList<>();

    }

    public void setData(List<ProgramInfo> data) {
        this.data = data;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

        return switch (columnIndex){
            case 0 -> data.get(rowIndex).getName();
            case 1 -> data.get(rowIndex).getStartDate().format(formatter);
            case 2 -> data.get(rowIndex).getEndDate().format(formatter);
            default -> "-";
        };
    }
    public String getColumnName(int column){
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

}
