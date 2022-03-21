package ui;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CustomTable extends JTable{
private static final long serialVersionUID = 1L;
	
	public CustomTable() {
		
	
		setRowHeight(25);
		getTableHeader().setReorderingAllowed(false);
		getTableHeader().setBackground(new Color(255,250,210));
		setShowVerticalLines(true);
		setShowHorizontalLines(true);
		setAutoCreateRowSorter(true);
		setFont(UIConstant.NORMAL_FONT);
		
	}
	
	public CustomTable(DefaultTableModel tableModel) {
		this();
		setModel(tableModel);
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		// TODO Auto-generated method stub
		return false;
	}
}