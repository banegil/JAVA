package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import simulator.control.Controller;
import simulator.model.events.Event;
import simulator.model.objects.Vehicle;

public class VehicleObserver implements TrafficSimObserver{
	
	private Map<Vehicle, String> llegada;
	
	public VehicleObserver(Controller ctrl){
		ctrl.addObserver(this);
		llegada = new HashMap<Vehicle, String>();
	}
	
	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {	
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		for(Vehicle v : map.getVehicle()) {
			if(v.getStatus() == VehicleStatus.ARRIVED) 
				llegada.put(v, Integer.toString(time));
			else llegada.put(v, "NO");
		}
	}



	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {	
	}

	@Override
	public void onError(String err) {	
	}
	
	@Override
	public String toString() {
		String ret = "\nLlegadas: ";
		for(Map.Entry<Vehicle, String> entry : llegada.entrySet()) {
			ret+= "(" + entry.getKey() + ", " + entry.getValue() + ")";
		}
		return ret;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}
	
}
