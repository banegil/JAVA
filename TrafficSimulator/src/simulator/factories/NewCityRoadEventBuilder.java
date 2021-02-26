package simulator.factories;

import org.json.JSONObject;

import simulator.model.Weather;
import simulator.model.events.Event;
import simulator.model.events.NewCityRoadEvent;

public class NewCityRoadEventBuilder extends Builder<Event>{
	
	public NewCityRoadEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		if (data.has("time") && data.has("id") && data.has("src") && data.has("dest")
				&& data.has("length") && data.has("co2limit") && data.has("maxspeed") && data.has("weather")) {
			return new NewCityRoadEvent(data.getInt("time"), data.getString("id"),
				data.getString("src"), data.getString("dest"), 
				data.getInt("length"), data.getInt("co2limit"),
				data.getInt("maxspeed"), Weather.tiempo(data.get("weather").toString().toUpperCase()));
		}
		return null;
	}

}
