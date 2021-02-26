package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import simulator.model.objects.Junction;
import simulator.model.objects.Road;
import simulator.model.objects.Vehicle;

public class RoadMap {
	List<Vehicle> vehiculos;
	List<Road> roads;
	List<Junction> cruces;
	Map<String,Junction> mapCruces;
	Map<String, Road> mapRoads;
	Map<String, Vehicle> mapVehicles;
	
	public RoadMap() {
		vehiculos = new LinkedList<Vehicle>();
		roads = new LinkedList<Road>();
		cruces = new LinkedList<Junction>();
		mapCruces = new LinkedHashMap<String, Junction>();
		mapRoads = new LinkedHashMap<String, Road>();
		mapVehicles = new LinkedHashMap<String, Vehicle>();
	}
	
	public void addJUnction(Junction cruce) {
		boolean ok = false;
		for(Junction j: cruces) if(j.getId() == cruce.getId()) {ok = true; break;}
		if(!ok) cruces.add(cruce);
		mapCruces.put(cruce.getId(), cruce);
	}
	
	public void addRoad(Road road){
		int i = 0, c = 0;
		for(Road r: roads) if(r.getId().equals(road.getId())) c++;
		if(c == 0) {for(Junction j: cruces) if(road.getSource().getId().equals(j.getId()) || road.getDest().getId().equals(j.getId())) i++;
			if(i != 2) throw new IllegalArgumentException("Los cruces no existen");
			roads.add(road);
			mapRoads.put(road.getId(), road);}
	}
	
	public void addVehicle(Vehicle vehiculo) {
		int cont = 0, c = 0;
		for(Vehicle v: vehiculos) if(v.getId().equals(vehiculo.getId())) c++;
		if(c == 0) {
		for(int i = 0; i < vehiculo.getItinerary().size() - 1; i++) {
			for(Road r: roads){
				if(vehiculo.getItinerary().get(i) == r.getSource() && vehiculo.getItinerary().get(i + 1) == r.getDest()) break;
				else cont++;
			} 
			if(cont == roads.size()) {
				throw new IllegalArgumentException("La carretera entre los cruces "+ vehiculo.getItinerary().get(i).getId()
						+" y "+ vehiculo.getItinerary().get(i + 1).getId() +" no existe"); 
			}
			cont = 0;}
		}
		vehiculos.add(vehiculo);
		mapVehicles.put(vehiculo.getId(), vehiculo);
	}
	
	void reset() {
		vehiculos.clear();   mapVehicles.clear();
		roads.clear();       mapRoads.clear();
		cruces.clear();      mapCruces.clear();
	}
	
	public JSONObject report() {
		if(cruces.isEmpty()) return null;
		String j = "", r = "", v = "";
		for(Junction c: cruces) j += c.report() + ",";
		for(Road c: roads) r += c.report() + ",";
		for(Vehicle c: vehiculos) v += c.report() + ",";
		String jsonString = "{ \"junctions\": ["+ j.substring(0, j.length()-1) +"], \"roads\": "
				+ "["+ r.substring(0, r.length()-1) +"], \"vehicles\": ["+ v.substring(0, v.length()-1) +"] }";
		JSONObject jo = new JSONObject(jsonString);
		return jo;
	}
	
	public Vehicle getVechicle(String vehiculo) {
		if(mapVehicles.containsKey(vehiculo)) return mapVehicles.get(vehiculo);
		return null;
	}
	
	public List<Vehicle> getVehicle(){
		return Collections.unmodifiableList(new ArrayList<>(vehiculos));
	}
	
	public Road getRoad(String road) {
		if(mapRoads.containsKey(road)) return mapRoads.get(road);
		return null;
	}
	
	public List<Road> getRoads(){
		return Collections.unmodifiableList(new ArrayList<>(roads));
	}
	
	public Junction getJunction(String cruce) {
		if(mapCruces.containsKey(cruce)) return mapCruces.get(cruce);
		return null;
	}
	
	public List<Junction> getJunction(){
		return Collections.unmodifiableList(new ArrayList<>(cruces));
	}
}
