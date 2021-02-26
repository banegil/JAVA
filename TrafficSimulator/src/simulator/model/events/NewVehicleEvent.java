package simulator.model.events;

import java.util.LinkedList;
import java.util.List;

import simulator.model.RoadMap;
import simulator.model.exceptions.ContaminationClass;
import simulator.model.exceptions.JunctionList;
import simulator.model.exceptions.NegativeNumber;
import simulator.model.exceptions.NotPendingWaiting;
import simulator.model.objects.Junction;
import simulator.model.objects.Vehicle;

public class NewVehicleEvent extends Event{
	int time, maxSpeed, contClass;
	String id;
	List<String> itinerary;
	
	public NewVehicleEvent(int time, String id, int maxSpeed, int
		contClass, List<String> itinerary) {
		super(time);
		this.time = time;
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.contClass = contClass;
		this.itinerary = itinerary;
	}

	@Override
	public
	void execute(RoadMap map) throws NegativeNumber, ContaminationClass, JunctionList, NotPendingWaiting {
		List<Junction> l = new LinkedList<Junction>();
		for(int i = 0; i < itinerary.size(); i++) l.add(map.getJunction(itinerary.get(i)));
		Vehicle v = new Vehicle(id, maxSpeed, contClass, l);
		v.moveToNextRoad();
		map.addVehicle(v);
	}
	
	@Override
	public String toString() {
	return "New Vehicle '" + id + "'";
	}
}
