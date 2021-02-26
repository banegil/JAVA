
package simulator.model.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.LightSwitchingStrategy;
import simulator.model.exceptions.NegativeNumber;
import simulator.model.exceptions.NotPendingWaiting;

public class Junction extends SimulatedObject {
	List<Road> roads;
	Map<Junction, Road> carreterasSalientes;
	List<List<Vehicle>> colasCarreteras;
	Map<Road,List<Vehicle>> carreteraCola;
	int greenLight;
	int ultPasoSem;
	LightSwitchingStrategy lsStrategy;
	DequeuingStrategy dqStrategy;
	int x, y, cont;

	public Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor) throws NegativeNumber {
		super(id);
		if(lsStrategy == null || dqStrategy == null) throw new IllegalArgumentException("lsStrategy y dqStrategy no pueden ser null");
		if(xCoor < 0 || yCoor < 0) throw new NegativeNumber("xCoor e yCoor no pueden ser negativos");
		roads = new LinkedList<Road>();
		carreterasSalientes = new LinkedHashMap<Junction, Road>();
		colasCarreteras = new LinkedList<List<Vehicle>>();
		carreteraCola = new LinkedHashMap<Road, List<Vehicle>>();
		this.lsStrategy = lsStrategy;
		this.dqStrategy = dqStrategy;
		ultPasoSem = 0;
		x = xCoor;
		y = yCoor;
		cont = 0;
	}
	
	void addIncomingRoad(Road r) {
		if(!this.equals(r.getDest())) throw new IllegalArgumentException("El cruce actual "+this.getId()+" y el cruce destino "+r.getDest()+" deben ser el mismo");
		roads.add(r);
		colasCarreteras.add(new LinkedList<Vehicle>());
		carreteraCola.put(r, new LinkedList<Vehicle>());
	}
	
	void addOutgoingRoad(Road r) {
		if(!this.equals(r.getSource())) throw new IllegalArgumentException("El cruce actual y el cruce origen deben ser el mismo");
		for(Road c: roads) {
			if(c.getSource().equals(this) && c.getDest().equals(r.getDest())) throw new IllegalArgumentException("Esta carretera ya existe");
		}
		carreterasSalientes.put(r.getDest(), r);
	}
	
	void enter(Vehicle v) throws NotPendingWaiting {
		Set<Road> keys = carreteraCola.keySet();
		List<Road> listKeys = new LinkedList<Road>( keys );
		colasCarreteras.get(listKeys.indexOf(v.getRoad())).add(v);
		carreteraCola.get(v.getRoad()).add(v);
	}
	
	Road roadTo(Junction j){
		return carreterasSalientes.get(j);
	}

	@Override
	public
	void advance(int time) throws NotPendingWaiting, NegativeNumber {
		cont++;
		
		if(!colasCarreteras.isEmpty() && greenLight != -1 && !colasCarreteras.get(greenLight).isEmpty()) {
			List<Vehicle> nueva = dqStrategy.dequeue(colasCarreteras.get(greenLight));
			for(Vehicle v: nueva) if(v.getStatus().toString().equals("WAITING") || v.getStatus().toString().equals("PENDING")) v.moveToNextRoad();
			Road r = (Road) carreteraCola.keySet().toArray()[greenLight];
			carreteraCola.put(r, nueva);
			colasCarreteras.get(greenLight).clear();
		}
		
		int cambio = lsStrategy.chooseNextGreen(roads, colasCarreteras, greenLight, ultPasoSem, time - 1);
		if(cambio != greenLight) {greenLight = cambio; ultPasoSem = time - 1;}
	}

	@Override
	public JSONObject report() {
		String s = "none";
		if(greenLight != -1 && !roads.isEmpty() && greenLight < roads.size()) s = roads.get(greenLight).toString();
		String jsonString = "{ \"id\": \""+ this.getId() +"\", \"green\": \""+ s +"\", \"queues\": ["+ queues() +"]}";
		JSONObject jo = new JSONObject(jsonString);
		return jo;
	}
	
	public String queues() {
		String s = "", s2 = "";
		int i = 0;
		List<List<Vehicle>> l = Collections.unmodifiableList(new ArrayList<>(colasCarreteras));
		if(!l.isEmpty()) {for(List<Vehicle> v: l) {
			if(!v.isEmpty())for(Vehicle v2: v) { 
				s2 += "\""+ v2.getId() +"\",";
				if(!v.iterator().hasNext()) s2 = s2.substring(0, s2.length()-1);
			}
			s += "{\"road\": \"" + roads.get(i) + "\", \"vehicles\": ["+ s2 +"]},";
			i++;
			if(!v.isEmpty()) s2 = "";
			}return s.substring(0, s.length()-1);	
		}return "";
	}
	
	public List<Road> getInRoads(){
		return roads;
	}
	
	public int getGreenLightIndex() {
		return greenLight;
	}
	
	public String getGreen() {
		if(greenLight == -1) return "NONE";
		else return roads.get(greenLight).toString();
	}
	
	public String getColas() {
		String s = "", s2 = "";
		int i = 0, j = 0;
		List<List<Vehicle>> l = Collections.unmodifiableList(new ArrayList<>(colasCarreteras));
		if(!l.isEmpty()) {for(List<Vehicle> v: l) {
			if(!v.isEmpty())for(Vehicle v2: v) { 
				s2 += v2.getId() +",";
				if(v.size() - 1 == j) s2 = s2.substring(0, s2.length()-1);
				j++;
			}
			s += roads.get(i) + ":[" + s2 + "] ";
			j = 0;
			i++;
			if(!v.isEmpty()) s2 = "";
			}return s;	
		}return "";
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}