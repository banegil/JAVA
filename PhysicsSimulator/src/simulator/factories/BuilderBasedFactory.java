package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T>
{
	private List<Builder<T>> listOfBuilders;
	
	public BuilderBasedFactory(List<Builder<T>> builders)
	{
		this.listOfBuilders = builders;
	}

	public T createInstance(JSONObject info) 
	{
		T aux = null;
		int i = 0;
		while (i < this.listOfBuilders.size() && (aux == null))
		{
			aux =  this.listOfBuilders.get(i).createInstance(info);
			i++;
		}
		if (aux == null)
			throw new IllegalArgumentException();			
		return aux;
	}

	public List<JSONObject> getInfo() 
	{
		ArrayList<JSONObject> info = new ArrayList<JSONObject>();
		for(Builder<T> b : this.listOfBuilders)
			info.add(b.getBuilderInfo());
		return info;
	}

}
