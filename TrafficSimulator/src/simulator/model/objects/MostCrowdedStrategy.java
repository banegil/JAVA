package simulator.model.objects;

import java.util.List;

import simulator.model.LightSwitchingStrategy;

public class MostCrowdedStrategy implements LightSwitchingStrategy{
	int time;
	public MostCrowdedStrategy(int time) {
		this.time = time;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		int i = 0, cont = 0, circle = qs.size();
		if(roads.isEmpty()) return -1;
		if(currGreen == -1) {
			for(int c = 0; c < qs.size(); c++) {
				if(qs.get(c).size() > i) {i = qs.get(c).size(); cont = c;}
			} return cont;
		}
		if(currTime - lastSwitchingTime < time) return currGreen;
		for(int c = (currGreen + 1) % roads.size(); c < circle; c++) {
			if(qs.get(c).size() > i) {i = qs.get(c).size(); cont = c;}
			if(c + 1 == qs.size()) {c = -1; circle = currGreen + 1 % roads.size();}
		} return cont;
	}
}
