package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.events.Event;
import simulator.model.objects.Junction;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver{
	private static final long serialVersionUID = 1L;

	private List<Junction> cruces;
	private String[] colNames = { "Id", "Verde", "Colas"};
	
	public JunctionsTableModel(Controller c) {
		c.addObserver(this);
		cruces = new ArrayList<Junction>();
	}
	
	public String getColumnName(int col) {
		return colNames[col];
	}
	
	@Override
	public int getRowCount() {
		return cruces == null ? 0 : cruces.size();
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object s = null;
		switch (columnIndex) {
		case 0:
			s = cruces.get(rowIndex).getId();
			break;
		case 1:
			s = cruces.get(rowIndex).getGreen();
			break;
		case 2:
			s = cruces.get(rowIndex).getColas();
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		cruces = map.getJunction();
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		cruces = map.getJunction();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		cruces = map.getJunction();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		cruces = map.getJunction();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		cruces = map.getJunction();
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
