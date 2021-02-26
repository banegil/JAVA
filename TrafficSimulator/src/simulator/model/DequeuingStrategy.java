package simulator.model;

import java.util.List;

import simulator.model.objects.Vehicle;

public interface DequeuingStrategy {
	List<Vehicle> dequeue(List<Vehicle> q);
}
