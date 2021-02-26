package simulator.control;

import simulator.model.events.*;
import simulator.model.exceptions.ContaminationClass;
import simulator.model.exceptions.JunctionList;
import simulator.model.exceptions.NegativeNumber;
import simulator.model.exceptions.NotPendingWaiting;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	TrafficSimulator ts;
	Factory<Event> ef;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		if(sim == null || eventsFactory == null) throw new IllegalArgumentException("sim y eventsFacotry no pueden ser null");
		ts = sim;
		ef = eventsFactory;
	}
	
	public void loadEvents(InputStream in) throws NegativeNumber, ContaminationClass, JunctionList, NotPendingWaiting {
		JSONObject jsonInput = new JSONObject(new JSONTokener(in));
		JSONArray arrayOfEvents;
		JSONObject bb;
		Event t;
		arrayOfEvents = jsonInput.getJSONArray("events");
		for(int i = 0; i < arrayOfEvents.length(); i++)
		{
			bb = arrayOfEvents.getJSONObject(i);
			t = this.ef.createInstance(bb);
			this.ts.addEvent(t);
		}
	}
	
	public void run(int n, OutputStream out) throws NegativeNumber, ContaminationClass, JunctionList, NotPendingWaiting, IOException {
		if(out != null) {
			PrintStream p = new PrintStream(out);
			p.println("{ \n  \"states\": [");
			for(int i = 0; i < n; i++) {
				this.ts.advance();
				if(i + 1 == n) p.println(ts.report().toString());
				else p.println(ts.report().toString() + ",");
			}
			p.println("] \n}");
		} else for(int i = 0; i < n; i++) this.ts.advance();
	}
	
	public void reset() {
		ts.reset();
	}
	
	public void addObserver(TrafficSimObserver o){
		ts.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		ts.removeObserver(o);
	}
	
	public void addEvent(Event e) throws ContaminationClass, JunctionList, NotPendingWaiting, NegativeNumber {
		ts.addEvent(e);
	}
}
