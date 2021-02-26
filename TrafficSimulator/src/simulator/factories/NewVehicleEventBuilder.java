package simulator.factories;

import java.util.LinkedList;

import org.json.JSONObject;

import simulator.model.events.Event;
import simulator.model.events.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event>{
	
	public NewVehicleEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		LinkedList<String> mylist = new LinkedList<String>();
		
		if (data.has("time") && data.has("id")&& data.has("maxspeed")
				&& data.has("class") && data.has("itinerary")) {
			
			for(int i = 0; i < data.getJSONArray("itinerary").length(); i++)
				mylist.add(data.getJSONArray("itinerary").getString(i));
			
			return new NewVehicleEvent(data.getInt("time"),
					data.getString("id"), data.getInt("maxspeed"),
					data.getInt("class"), mylist);
		}
		return null;
	}

}
