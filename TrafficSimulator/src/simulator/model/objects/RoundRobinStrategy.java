package simulator.model.objects;

import java.util.List;

import simulator.model.LightSwitchingStrategy;

public class RoundRobinStrategy implements LightSwitchingStrategy{
	int time;
	public RoundRobinStrategy(int time) {
		this.time = time;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		if(roads.isEmpty()) return -1;
		if(currGreen == -1) return 0;
		if(currTime - lastSwitchingTime < time) return currGreen;
		return (currGreen + 1) % roads.size();
	}
}
