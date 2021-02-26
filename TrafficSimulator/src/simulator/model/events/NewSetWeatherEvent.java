package simulator.model.events;

import java.util.List;

import simulator.misc.Pair;
import simulator.model.RoadMap;
import simulator.model.Weather;
import simulator.model.exceptions.ContaminationClass;
import simulator.model.exceptions.JunctionList;
import simulator.model.exceptions.NegativeNumber;
import simulator.model.exceptions.NotPendingWaiting;

public class NewSetWeatherEvent extends Event{
	List<Pair<String,Weather>> ws;
	
	public NewSetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		if(ws == null) throw new NullPointerException("ws no puede ser null");
		this.ws = ws;
	}

	@Override
	public
	void execute(RoadMap map) throws NegativeNumber, ContaminationClass, JunctionList, NotPendingWaiting {
		for(Pair<String,Weather> w: ws) {
			if(map.getRoad(w.getFirst()) == null) throw new IllegalArgumentException("La carretera "+w.getFirst()+" no existe");
			map.getRoad(w.getFirst()).setWeather(w.getSecond());
		}	
	}

	@Override
	public String toString() {
		return "(New SetWeather: [" + ws.get(0).getFirst() +","+ ws.get(0).getSecond() + "])";
	}
}
