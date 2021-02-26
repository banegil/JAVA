package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.events.Event;
import simulator.model.objects.Road;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver{
	private static final long serialVersionUID = 1L;
	
	private List<Road> roads;
	private String[] colNames = { "Id", "Longitud" , "Clima", "VelMax", "VelActual", "CO2Total", "LimiteCO2"};
	
	public RoadsTableModel(Controller c) {
		c.addObserver(this);
		roads = new ArrayList<Road>();
	}
	
	public String getColumnName(int col) {
		return colNames[col];
	}
	
	@Override
	public int getRowCount() {
		return roads == null ? 0 : roads.size();
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
			s = roads.get(rowIndex).getId();
			break;
		case 1:
			s = roads.get(rowIndex).getLength();
			break;
		case 2:
			s = roads.get(rowIndex).getWeather();
			break;
		case 3:
			s = roads.get(rowIndex).getVelActual();
			break;
		case 4:
			s = roads.get(rowIndex).getVelMax();
			break;
		case 5:
			s = roads.get(rowIndex).getTotalCO2();
			break;
		case 6:
			s = roads.get(rowIndex).getCO2Limit();
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		roads = map.getRoads();
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
