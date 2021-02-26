package simulator.factories;

import java.util.LinkedList;

import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Weather;
import simulator.model.events.Event;
import simulator.model.events.NewSetWeatherEvent;

public class SetWeatherEventBuilder extends Builder<Event>{
	
	public SetWeatherEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		LinkedList<Pair<String,Weather>> mylist = new LinkedList<Pair<String,Weather>>();
		
		if (data.has("time") && data.has("info")) {
			for(int i = 0; i < data.getJSONArray("info").length(); i++)
				mylist.add(new Pair<String, Weather>(data.getJSONArray("info").getJSONObject(i).getString("road"),
						Weather.tiempo(data.getJSONArray("info").getJSONObject(i).get("weather").toString().toUpperCase())));
			
			return new NewSetWeatherEvent(data.getInt("time"), mylist);
		}
		return null;
	}

}
