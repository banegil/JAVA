package simulator.model.objects;

import simulator.model.Weather;
import simulator.model.exceptions.NegativeNumber;

public class CityRoad extends Road{

	public CityRoad(String id, Junction srcJunc, Junction descJunc, int maxSpeed, int contLimit, int length,
			Weather weather) throws NegativeNumber {
		super(id, srcJunc, descJunc, maxSpeed, contLimit, length, weather);
		// TODO Auto-generated constructor stub
	}

	@Override
	void reduceTotalContamination() {
		if(tiempo.toString().equals("WINDY") ||tiempo.toString().equals("STORM")) contTotal -= 10;
		else contTotal -= 2;
		if(contTotal < 0) contTotal = 0;
	}

	@Override
	void updateSpeedLimit() {
		maxSpeedVehicle = maxSpeed;
	}

	@Override
	int calculateVehicleSpeed(Vehicle vehiculo) throws NegativeNumber {
		return maxSpeedVehicle = (int) (((11.0 - vehiculo.getContClass()) / 11.0) * maxSpeed);
	}

}
