package simulator.model;

public enum Weather {
	SUNNY, CLOUDY, RAINY, WINDY, STORM;
	
	public static Weather tiempo(String w){
		if(w.equals("SUNNY")) return SUNNY;
		if(w.equals("CLOUDY")) return CLOUDY;
		if(w.equals("RAINY")) return RAINY;
		if(w.equals("WINDY")) return WINDY;
		if(w.equals("STORM")) return STORM;
		return null;
	}
}
