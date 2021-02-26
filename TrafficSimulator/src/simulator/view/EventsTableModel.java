package simulator.view;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.events.Event;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver{
	private static final long serialVersionUID = 1L;

	private List<Event> events;
	private String[] colNames = { "Tiempo", "Descripcion" };
	
	public EventsTableModel(Controller c) {
		c.addObserver(this);
		events = new ArrayList<Event>();
	}
	
	public String getColumnName(int col) {
		return colNames[col];
	}
	
	@Override
	public int getRowCount() {
		return events == null ? 0 : events.size();
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
			s = events.get(rowIndex).getTime();
			break;
		case 1:
			s = events.get(rowIndex).toString();
			System.out.println(s);
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this.events = events;
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this.events = events;
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this.events = events;
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this.events = events;
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this.events = events;
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
