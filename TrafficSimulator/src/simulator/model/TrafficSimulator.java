package simulator.model;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;
import simulator.model.events.Event;
import simulator.model.exceptions.ContaminationClass;
import simulator.model.exceptions.JunctionList;
import simulator.model.exceptions.NegativeNumber;
import simulator.model.exceptions.NotPendingWaiting;
import simulator.model.objects.Junction;
import simulator.model.objects.Road;

public class TrafficSimulator implements Observable<TrafficSimObserver>{
	RoadMap map;
	List<Event> listaEventos;
	private LinkedList<TrafficSimObserver> observadores = new LinkedList<TrafficSimObserver>();
	int time;
	
	public TrafficSimulator() {
		time = 0;
		map = new RoadMap();
		listaEventos = new SortedArrayList<Event>(new Comparator<Event>() {
			@Override
			public int compare(Event o1, Event o2) {
				return ((Comparable<Integer>)o1.getTime()).compareTo(o2.getTime());
			}
		});
	}
	
	public void addEvent(Event e)throws ContaminationClass, JunctionList, NotPendingWaiting, NegativeNumber {
		try {
			listaEventos.add(e);
			notifyEvent(e);
		} catch (Exception ex) {
			this.notifyError(ex.getMessage());
			throw ex;
		  }
	}
	
	public void advance() throws NegativeNumber, ContaminationClass, JunctionList, NotPendingWaiting {
		try {
			time++;
			notifyObserver("onAdvanceStart");
			
			for(int i = 0; i < listaEventos.size(); i++) {
				if(listaEventos.get(i).getTime() == time) {
					listaEventos.get(i).execute(map);
					listaEventos.remove(i);
					i--;
				}
			}
			for(Junction j: map.getJunction()) j.advance(time);
			for(Road r: map.getRoads()) r.advance(time);
		
			this.notifyObserver("onAdvanceEnd");
		} catch (Exception ex) {
			notifyError(ex.getMessage());
			throw ex;
		  }
	}
	
	public void reset() {
		time = 0;
		map = new RoadMap();
		listaEventos.clear();
		notifyObserver("onReset");
	}
	
	public JSONObject report() {
		String jsonString = "{ \"time\": "+ time +", \"state\": "+ map.report() +"}";
		JSONObject jo = new JSONObject(jsonString);
		return jo;
	}

	@Override
	public void addObserver(TrafficSimObserver o) {
		observadores.add(o);
		o.onRegister(map, listaEventos, time);
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		observadores.remove(o);
	}

	@Override
	public void notifyObserver(String s) {
		for(TrafficSimObserver t: observadores) {
			if(s.equals("onAdvanceStart")) t.onAdvanceStart(map, listaEventos, time);
			if(s.equals("onAdvanceEnd")) t.onAdvanceEnd(map, listaEventos, time);
			if(s.equals("onReset")) t.onReset(map, listaEventos, time);
		}
	}
	
	public void notifyEvent(Event event) {
		for(TrafficSimObserver t: observadores) t.onEventAdded(map, listaEventos, event, time);
	}
	
	public void notifyError(String error) {
		for(TrafficSimObserver t: observadores) t.onError(error);
	}
}
