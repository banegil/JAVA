package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.events.Event;
import simulator.model.objects.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver{
	private static final long serialVersionUID = 1L;

	private String[] colNames = {"Id", "Estado", "Itinerario", "Clase CO2", "MaxVel", "Velocidad", "Total CO2", "Distancia"};
	List<Vehicle> vehiculos;
	
	public VehiclesTableModel(Controller c) {
		c.addObserver(this);
		vehiculos = new ArrayList<Vehicle>();
	}
	
	public String getColumnName(int col) {
		return colNames[col];
	}
	
	@Override
	public int getRowCount() {
		return vehiculos == null ? 0 : vehiculos.size();
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
			s = vehiculos.get(rowIndex).getId();
			break;
		case 1:
			if(vehiculos.get(rowIndex).getStatus().toString().equals("PENDING")) s = "Pending";
			else if(vehiculos.get(rowIndex).getStatus().toString().equals("TRAVELING"))
				s = vehiculos.get(rowIndex).getRoad().getId() + ":" + vehiculos.get(rowIndex).getLocation();
			else if(vehiculos.get(rowIndex).getStatus().toString().equals("WAITING"))
				s = "Waiting:" + vehiculos.get(rowIndex).getRoad().getDest();
			else s = "Arrived";
			break;
		case 2:
			s = vehiculos.get(rowIndex).getItinerary().toString();
			break;
		case 3:
			s = vehiculos.get(rowIndex).getContClass();
			break;
		case 4:
			s = vehiculos.get(rowIndex).getSpeedMax();
			break;
		case 5:
			s = vehiculos.get(rowIndex).getSpeed();
			break;
		case 6:
			s = vehiculos.get(rowIndex).getContTotal();
			break;
		case 7:
			s = vehiculos.get(rowIndex).getDistancia();
			break;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		vehiculos = map.getVehicle();
		fireTableDataChanged();
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		vehiculos = map.getVehicle();
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		vehiculos = map.getVehicle();
		fireTableDataChanged();
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		vehiculos = map.getVehicle();
		fireTableDataChanged();
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		vehiculos = map.getVehicle();
		fireTableDataChanged();
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
