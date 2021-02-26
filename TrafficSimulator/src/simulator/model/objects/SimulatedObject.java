package simulator.model.objects;

import org.json.JSONObject;

import simulator.model.exceptions.NegativeNumber;
import simulator.model.exceptions.NotPendingWaiting;

public abstract class SimulatedObject {

	protected String _id;

	SimulatedObject(String id) {
		_id = id;
	}

	public String getId() {
		return _id;
	}

	@Override
	public String toString() {
		return _id;
	}

	abstract void advance(int time) throws NegativeNumber, NotPendingWaiting;

	abstract public JSONObject report();
}
