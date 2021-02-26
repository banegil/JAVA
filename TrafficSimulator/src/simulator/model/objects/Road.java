package simulator.model.objects;

import java.util.List;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;
import simulator.model.Weather;
import simulator.model.exceptions.NegativeNumber;
import simulator.model.exceptions.NotPendingWaiting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class Road extends SimulatedObject{
	Junction cruceOrigen, cruceDestino;
	int longitud;
	int maxSpeed;
	int maxSpeedVehicle, ms;
	int limiteCont;
	Weather tiempo;
	int contTotal;
	List<Vehicle> vehiculos;

	
	public Road(String id, Junction srcJunc, Junction descJunc, int maxSpeed, int contLimit, int length, Weather weather) throws NegativeNumber {
		super(id);
		if(srcJunc == null) throw new NullPointerException("El cruce origen no puede ser null");
		if(descJunc == null) throw new NullPointerException("El cruce destino no puede ser null");
		if(weather == null) throw new NullPointerException("Weather no puede ser null");
		if(maxSpeed < 0) throw new NegativeNumber("La velocidad max no puede ser negativa");
		if(contLimit < 0) throw new NegativeNumber("El limite de contaminacion no puede ser negativa");
		if(length < 0) throw new NegativeNumber("La longitud no puede ser negativa");
		cruceOrigen = srcJunc;
		cruceDestino = descJunc;
		this.maxSpeed = ms = maxSpeedVehicle = maxSpeed;
		limiteCont = contLimit;
		longitud = length;
		tiempo = weather;
		cruceOrigen.addOutgoingRoad(this);
		cruceDestino.addIncomingRoad(this);
		vehiculos = new SortedArrayList<Vehicle>(new Comparator<Vehicle>() {
			@Override
			public int compare(Vehicle o1, Vehicle o2) {
				return Integer.valueOf(o2.getLocation()).compareTo(Integer.valueOf(o1.getLocation()));
			}});
		contTotal = 0;
	}
	
	public void setWeather(Weather w) {
		if(w == null) throw new NullPointerException("Weather no puede ser null");
		tiempo = w;
	}
	
	void addContamintation(int c) throws NegativeNumber {
		if(c < 0) throw new NegativeNumber("La contaminacion no puede ser negativa");
		contTotal += c;
	}
	
	void enter(Vehicle vehiculo) {
		if(vehiculo.getLocation() != 0 || vehiculo.getSpeed() != 0) throw new IllegalArgumentException("localizacion y velocidad deben ser 0");
		vehiculos.add(vehiculo);
	}
	
	void exit(Vehicle vehiculo) {
		vehiculos.remove(vehiculo);
	}
	
	abstract void reduceTotalContamination();
	abstract void updateSpeedLimit();
	abstract int calculateVehicleSpeed(Vehicle vehiculo) throws NegativeNumber;
	
	@Override
	public
	void advance(int time) throws NegativeNumber, NotPendingWaiting {
		int i = 0, c = 0;
		boolean ok = false;
		reduceTotalContamination();
		updateSpeedLimit();
		
		for(Vehicle v: vehiculos) {
			v.setSpeed(calculateVehicleSpeed(v));
			v.advance(time);
			if(v.getStatus().toString().equals("ARRIVED")) {c = i; ok = true;}
			i++;
		}
		
		if(ok) vehiculos.remove(c);
		
		Collections.sort(vehiculos, new Comparator<Vehicle>() {
			@Override
			public int compare(Vehicle o1, Vehicle o2) {
				return Integer.valueOf(o2.getLocation()).compareTo(Integer.valueOf(o1.getLocation()));
		}});
	}

	@Override
	public JSONObject report() {
		String jsonString = "{ \"id\": \""+ this.getId() +"\", \"speedlimit\": "+ maxSpeed +","
				+ "\"weather\": \""+ tiempo +"\", \"co2\": "+ contTotal +", \"vehicles\":"
				+ " ["+ listaVehiculos() +"]}";
		JSONObject jo = new JSONObject(jsonString);
		return jo;
	}
	
	public int getLength() {
		return longitud;
	}
	
	public Junction getDest() {
		return cruceDestino;
	}
	
	public Junction getSource() {
		return cruceOrigen;
	}
	
	public int getTotalCO2() {
		return contTotal;
	}
	
	public int getCO2Limit() {
		return limiteCont;
	}
	
	public String getWeather() {
		return tiempo.toString();
	}
	
	public Weather getClima() {
		return tiempo;
	}
	
	public int getVelMax() {
		return maxSpeed;
	}
	
	public int getVelActual() {
		return ms;
	}
	
	public List<Vehicle> getVehiculos() {
		return vehiculos;
	}
	
	public String listaVehiculos(){
		String s = "";
		List<Vehicle> l = Collections.unmodifiableList(new ArrayList<>(vehiculos));
		for(Vehicle v: l) {
			s += "\"" + v.getId() + "\",";
		}
		if(!l.isEmpty()) return s.substring(0, s.length()-1);
		return s;
	}
}
