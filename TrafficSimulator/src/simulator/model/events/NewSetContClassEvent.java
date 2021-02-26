package simulator.model.events;

import java.util.List;

import simulator.misc.Pair;
import simulator.model.RoadMap;
import simulator.model.exceptions.ContaminationClass;

public class NewSetContClassEvent extends Event{
	List<Pair<String,Integer>> cs;
	
	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) {
		super(time);
		if(cs == null ) throw new IllegalArgumentException("cs no puede ser null");
		this.cs = cs;
	}

	@Override
	public
	void execute(RoadMap map) throws ContaminationClass  {
		for(Pair<String,Integer> c: cs) {
			if(map.getVechicle(c.getFirst()) == null) throw new IllegalArgumentException("El vehiculo "+c.getFirst()+" no existe");
			map.getVechicle(c.getFirst()).setContClass(c.getSecond());
		}
	}
	
	@Override
	public String toString() {
		return "(New SetCont: " + "[" + cs.get(0).getFirst() +","+ cs.get(0).getSecond() + "])";
	}
}
