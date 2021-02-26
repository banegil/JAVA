package simulator.model.events;

import simulator.model.DequeuingStrategy;
import simulator.model.LightSwitchingStrategy;
import simulator.model.RoadMap;
import simulator.model.exceptions.NegativeNumber;
import simulator.model.objects.Junction;

public class NewJunctionEvent extends Event{
	int time, x, y;
	String id;
	LightSwitchingStrategy lsStrategy;
	DequeuingStrategy dqStrategy;
	
	public NewJunctionEvent(int time, String id, LightSwitchingStrategy
		lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		super(time);
		this.time = time;
		this.id = id;
		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		x = xCoor;
		y = yCoor;
	}

	@Override
	public
	void execute(RoadMap map) throws NegativeNumber {
		map.addJUnction(new Junction(id, lsStrategy, dqStrategy, x, y));
	}
	
	@Override
	public String toString() {
	return "New JunctionEvent'" + id + "'";
	}
}
