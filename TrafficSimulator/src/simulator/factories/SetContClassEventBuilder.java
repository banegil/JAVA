package simulator.factories;

import java.util.LinkedList;

import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.events.Event;
import simulator.model.events.NewSetContClassEvent;

public class SetContClassEventBuilder extends Builder<Event>{
	
	public SetContClassEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		LinkedList<Pair<String,Integer>> mylist = new LinkedList<Pair<String,Integer>>();
		
		if (data.has("time") && data.has("info")) {
			for(int i = 0; i < data.getJSONArray("info").length(); i++)
				mylist.add(new Pair<String, Integer>(data.getJSONArray("info").getJSONObject(i).getString("vehicle"),
						data.getJSONArray("info").getJSONObject(i).getInt("class")));
			
			return new NewSetContClassEvent(data.getInt("time"), mylist);
		}
		return null;
	}

}
