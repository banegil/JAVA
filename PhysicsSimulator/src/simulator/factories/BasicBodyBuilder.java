package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body>
{
	public BasicBodyBuilder()
	{
		this.typeTag = "basic";
		this.desc = "Physical body";
	}
	public Body createTheInstance(JSONObject info) 
	{
		Body T = null;
		if (info.getString("type").equals("basic"))
		{
			JSONObject data = info.getJSONObject("data");
			Vector v = new Vector(jsonArrayToDoubleArray(data.getJSONArray("vel")));
			Vector p = new Vector(jsonArrayToDoubleArray(data.getJSONArray("pos")));
			if (v.dim() != p.dim())
				throw new IllegalArgumentException();
			Vector a = new Vector(v.dim());
			double m =  data.getDouble("mass");
			if (m <= 0)
				throw new IllegalArgumentException();
			T = new Body (data.getString("id"), v, a, p, m);
		}
		return T;
	}	
	public JSONObject createData()
	{
		JSONObject data = new JSONObject();
		data.put("id", "basic id");
		data.put("pos", "pos vector");
		data.put("vel", "velocity vector");
		data.put("mass", "mass");
		return data;		
	}
}
