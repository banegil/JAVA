package simulator.factories;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.LightSwitchingStrategy;
import simulator.model.events.*;

public class NewJunctionEventBuilder extends Builder<Event>{
	Factory<LightSwitchingStrategy> lssFactory;
	Factory<DequeuingStrategy> dqsFactory;
	
	public NewJunctionEventBuilder(String type, Factory<LightSwitchingStrategy>
	lssFactory, Factory<DequeuingStrategy> dqsFactory) {
		super(type);
		this.lssFactory = lssFactory;
		this.dqsFactory = dqsFactory;
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if (data.has("time") && data.has("id") && data.has("coor") 
				&& data.has("ls_strategy") && data.has("dq_strategy")) {
			return new NewJunctionEvent(data.getInt("time"), data.getString("id"), lssFactory.createInstance(data.getJSONObject("ls_strategy")),
			dqsFactory.createInstance(data.getJSONObject("dq_strategy")), data.getJSONArray("coor").getInt(0),
			data.getJSONArray("coor").getInt(1));
		}
		return null;
	}

}
