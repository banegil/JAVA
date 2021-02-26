package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchingStrategy;
import simulator.model.objects.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchingStrategy>{
	
	public MostCrowdedStrategyBuilder(String type) {
		super(type);
	}

	@Override
	protected LightSwitchingStrategy createTheInstance(JSONObject data) {
		int time = 1;
		if(data.has("timeslot")) time = data.getInt("timeslot");
			return new MostCrowdedStrategy(time);
	}

}
