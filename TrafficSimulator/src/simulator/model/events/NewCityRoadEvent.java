package simulator.model.events;

import simulator.model.RoadMap;
import simulator.model.Weather;
import simulator.model.exceptions.NegativeNumber;
import simulator.model.objects.CityRoad;

public class NewCityRoadEvent extends NewRoadEvent{
	int time, length, co2Limit, maxSpeed;
	String id, srcJun, destJunc;
	Weather weather;
	
	public NewCityRoadEvent(int time, String id, String srcJun, String
		destJunc, int length, int co2Limit, int maxSpeed, Weather weather){
		super(time);
		this.time = time;
		this.length = length;
		this.co2Limit = co2Limit;
		this.maxSpeed = maxSpeed;
		this.id = id;
		this.srcJun = srcJun;
		this.destJunc = destJunc;
		this.weather = weather;
		}

	@Override
	public
	void execute(RoadMap map) throws NegativeNumber {
		if(map.getJunction(srcJun) == null || map.getJunction(destJunc) == null) throw new IllegalArgumentException("Hay al menos un cruce que no existe");
		map.addRoad(new CityRoad(id, map.getJunction(srcJun), map.getJunction(destJunc), maxSpeed, co2Limit, length, weather));
	}
	
	@Override
	public String toString() {
	return "New CityRoad '" + id + "'";
	}
}
