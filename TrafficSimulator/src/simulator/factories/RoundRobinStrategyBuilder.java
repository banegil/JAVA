package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.objects.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchingStrategy>{
	
	public RoundRobinStrategyBuilder(String type) {
		super(type);
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		int time = 1;
		if(data.has("timeslot")) time = data.getInt("timeslot");
			return new RoundRobinStrategy(time);
	}
}
