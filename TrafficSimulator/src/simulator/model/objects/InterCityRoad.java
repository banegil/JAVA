package simulator.model.objects;

import simulator.model.Weather;
import simulator.model.exceptions.NegativeNumber;

public class InterCityRoad extends Road{

	public InterCityRoad(String id, Junction srcJunc, Junction descJunc, int maxSpeed, int contLimit, int length,
			Weather weather) throws NegativeNumber {
		super(id, srcJunc, descJunc, maxSpeed, contLimit, length, weather);
		// TODO Auto-generated constructor stub
	}

	@Override
	void reduceTotalContamination() {
		int x = 0;
		if(tiempo.toString().equals("SUNNY")) x = 2;
		else if(tiempo.toString().equals("CLOUDY")) x = 3;
		else if(tiempo.toString().equals("RAINY")) x = 10;
		else if(tiempo.toString().equals("WINDY")) x = 15;
		else if(tiempo.toString().equals("STORM")) x = 20;
		contTotal *= ((100.0 - x) / 100.0);
	}

	@Override
	void updateSpeedLimit() {
		if(contTotal > limiteCont) maxSpeedVehicle = maxSpeed = (int) (ms * 0.5);
		else maxSpeedVehicle = ms;
	}

	@Override
	int calculateVehicleSpeed(Vehicle vehiculo) throws NegativeNumber {
		if(tiempo.toString().equals("STORM")) maxSpeedVehicle = (int) (maxSpeed * 0.8);
		return maxSpeedVehicle;
	}

}
