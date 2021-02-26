package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Vector;
import simulator.model.Body;
import simulator.model.MassLosingBody;

public class MassLosingBodyBuilder extends Builder<Body>
{
	public MassLosingBodyBuilder()
	{
		this.typeTag = "mlb";
		this.desc = "Body that loses mass when moving";
	}
	public MassLosingBody createTheInstance(JSONObject info) 
	{
		MassLosingBody T = null;
		if (info.getString("type").equals("mlb"))
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
			double freq = data.getDouble("freq");
			double factor = data.getDouble("factor");
			if ((freq <= 0) || (factor > 1) || (factor <= 0)) 
				throw new IllegalArgumentException();
			T = new MassLosingBody (data.getString("id"), v, a, p, m, factor, freq);
		}
		return T;
	}		
	public JSONObject createData()
	{
		JSONObject data = new JSONObject();
		data.put("id", "mlb id");
		data.put("pos", "pos vector");
		data.put("vel", "velocity vector");
		data.put("mass", "mass");
		data.put("freq", "frequency of loss");
		data.put("factor", "factor of loss");
		return data;		
	}
}
