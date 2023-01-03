package radio;

import javax.swing.table.AbstractTableModel;
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
                "Sluttid",
                "Visa mer"};
        this.columnNames = columnNames;

        data = new ArrayList<>();
        data.add(new ProgramInfo("Namn", "desc", "date1", "date2", "img", "title"));

    }

   /* public void updateData(Map<Long, ProgramInfo> data){
        this.data = data;
    }*/

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
        return switch (columnIndex){
            case 0 -> data.get(rowIndex).getName();
            case 1 -> data.get(rowIndex).getStartDate();
            case 2 -> data.get(rowIndex).getEndDate();
            default -> "-";
        };
    }
    public String getColumnName(int column){
        return columnNames[column];
    }
}
