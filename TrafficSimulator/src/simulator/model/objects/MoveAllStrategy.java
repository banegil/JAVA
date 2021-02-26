package simulator.model.objects;

import java.util.List;

import simulator.model.DequeuingStrategy;

public class MoveAllStrategy implements DequeuingStrategy{

	@Override
	public List<Vehicle> dequeue(List<Vehicle> q) {
		List<Vehicle> v = q;
		return v;
	}
}
