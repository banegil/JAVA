package simulator.factories;

import org.json.JSONObject;

import simulator.model.FallingToCenterGravity;
import simulator.model.GravityLaws;

public class FallingToCenterGravityBuilder extends Builder<GravityLaws>
{
	public FallingToCenterGravityBuilder()
	{
		this.typeTag = "ftcg";
		this.desc = "All bodies fall to the center";
	}
	public GravityLaws createTheInstance(JSONObject info) 
	{
		FallingToCenterGravity T = null;
		if (info.getString("type").equals("ftcg"))
			T = new FallingToCenterGravity();
		return T;		
	}
}
