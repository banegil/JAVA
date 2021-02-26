package simulator.model.events;

import simulator.model.RoadMap;
import simulator.model.exceptions.NegativeNumber;

public abstract class NewRoadEvent extends Event{

	NewRoadEvent(int time) {
		super(time);
		// TODO Auto-generated constructor stub
	}

	@Override
	public abstract void execute(RoadMap map) throws NegativeNumber;
}
