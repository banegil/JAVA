package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Builder<T> 
{
	protected String typeTag;
	protected String desc;
	
	public Builder()
	{
	}
	public T createInstance(JSONObject info) 
	{
		T aux = null;
		if(info.getString("type").equals(this.typeTag))
			aux = createTheInstance(info);
		return aux;
	}
	public double[] jsonArrayToDoubleArray(JSONArray jsonArray)
	{
		double[] doubleArray = new double[jsonArray.length()];
		for(int i = 0; i < jsonArray.length(); i++)
			doubleArray[i] = jsonArray.getDouble(i);
		return doubleArray;
	}
	public JSONObject getBuilderInfo()
	{
		JSONObject builderInfo = new JSONObject();
		builderInfo.put("type", this.typeTag);
		builderInfo.put("desc", this.desc);
		builderInfo.put("data", createData());
		return builderInfo;
		
	}
	public JSONObject createData()
	{
		return new JSONObject();  //for the Gravity Laws (no data)
	}
	public abstract T createTheInstance(JSONObject json);
	
}
