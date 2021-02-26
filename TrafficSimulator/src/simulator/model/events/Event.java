package simulator.model.events;

import simulator.model.RoadMap;
import simulator.model.exceptions.ContaminationClass;
import simulator.model.exceptions.JunctionList;
import simulator.model.exceptions.NegativeNumber;
import simulator.model.exceptions.NotPendingWaiting;

public abstract class Event implements Comparable<Event> {

	protected int _time;

	Event(int time) {
		if (time < 1)
			throw new IllegalArgumentException("Time debe ser posivitvo, time: (" + time + ")");
		else
			_time = time;
	}

	public int getTime() {
		return _time;
	}

	@Override
	public int compareTo(Event o) {
		return this.equals(o) ? 1:0;
	}

	public abstract void execute(RoadMap map) throws NegativeNumber, ContaminationClass, JunctionList, NotPendingWaiting;
	public abstract String toString();
}
