package simulator.model.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import simulator.model.VehicleStatus;
import simulator.model.exceptions.ContaminationClass;
import simulator.model.exceptions.JunctionList;
import simulator.model.exceptions.NegativeNumber;
import simulator.model.exceptions.NotPendingWaiting;

public class Vehicle  extends SimulatedObject{
	private List<Junction> itinerario;
	int cont;
	int velocidadMax;
	int velocidadActual;
	VehicleStatus estado;
	Road road;
	int localizacion;
	int gradoContaminacion;
	int contaminacionTotal;
	int distanciaRecorrida;
	
	public Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) throws NegativeNumber, ContaminationClass, JunctionList {
			super(id);
			cont = 0;
			if(maxSpeed < 0) throw new NegativeNumber("La velocidad max no puede ser negativa");
			if(contClass < 0 || contClass > 10) throw new ContaminationClass("El grado de contaminacion tiene que ser entre 0 y 10");
			if(itinerary.size() < 2) throw new JunctionList("La lista de itinerarios no puede ser menor que 2");
			velocidadMax = maxSpeed;
			gradoContaminacion = contClass;
			contaminacionTotal = 0;
			distanciaRecorrida = 0;
			velocidadActual = 0;
			estado = VehicleStatus.PENDING;
			itinerario = Collections.unmodifiableList(new ArrayList<>(itinerary));
			localizacion = 0;
			}
	
	void setSpeed(int s) throws NegativeNumber {
		if(s < 0) throw new NegativeNumber("La velocidad minima no puede ser negativa");
		if(s < velocidadMax) velocidadActual = s;
		else velocidadActual = velocidadMax;
	}
	
	public void setContClass(int c) throws ContaminationClass {
		if(c < 0 || c > 10) throw new ContaminationClass("El grado de contaminacion tiene que ser entre 0 y 10");
		gradoContaminacion = c;
	}
	
	public void moveToNextRoad() throws NotPendingWaiting, NegativeNumber {
		if(estado.toString().equals("TRAVELING") || estado.toString().equals("ARRIVED")) throw new NotPendingWaiting("El estado del vehículo debe ser waiting o pending");
		if(estado.toString().equals("WAITING")) road.exit(this);
		if(itinerario.size() != cont + 1) {
			road = itinerario.get(cont).roadTo(itinerario.get(cont + 1));
			velocidadActual = localizacion = 0;
			road.enter(this);
			estado = VehicleStatus.TRAVELING;
		}
		else {estado = VehicleStatus.ARRIVED;}
	}
	
	@Override
	void advance(int time) throws NegativeNumber, NotPendingWaiting {
		if(estado.toString().equals("TRAVELING")) {
			int loc = localizacion;
			if(localizacion + velocidadActual < road.getLength()) localizacion += velocidadActual;
			else localizacion = road.getLength();
			int c = gradoContaminacion * (localizacion - loc);
			contaminacionTotal += c;
			road.addContamintation(c);
			distanciaRecorrida += velocidadActual;
			
			if(localizacion >=  road.getLength()) {
				int h = distanciaRecorrida % road.getLength();
				distanciaRecorrida -= h;
				estado = VehicleStatus.WAITING;
				itinerario.get(cont + 1).enter(this);
				cont++;
			}
		}
	}

	@Override
	public JSONObject report() {
		if(!estado.toString().equalsIgnoreCase("TRAVELING") && velocidadActual != 0) velocidadActual = 0; 
		String jsonString = "{ \"id\": \""+ this.getId() +"\", \"speed\": "+ velocidadActual +", \"distance\": "+ distanciaRecorrida +
				", \"co2\": "+ contaminacionTotal +", \"class\": "+ gradoContaminacion +", \"status\": \""+ estado.toString() +"\"}";
		
		if(estado.toString().equalsIgnoreCase("TRAVELING") || estado.toString().equalsIgnoreCase("WAITING")) {jsonString = jsonString.substring(0, jsonString.length()-1);
		jsonString += ",\"road\": "+ road.getId() +", \"location\": "+ localizacion +"}";}
		JSONObject jo = new JSONObject(jsonString);
		return jo;
	}
	
	public int getLocation() {
		return localizacion;
	}
	
	public int getSpeed() {
		return velocidadActual;
	}
	
	public int getSpeedMax() {
		return velocidadMax;
	}
	
	public int getContClass() {
		return gradoContaminacion;
	}
	
	public int getContTotal() {
		return contaminacionTotal;
	}
	
	public int getDistancia(){
		return distanciaRecorrida;
	}
	
	public VehicleStatus getStatus() {
		return estado;
	}
	
	public List<Junction> getItinerary(){
		return itinerario;
	}
	
	public Road getRoad() {
		return road;
	}
}
