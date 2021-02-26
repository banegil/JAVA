package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NoGravity;

public class NoGravityBuilder extends Builder<GravityLaws>
{
	public NoGravityBuilder()
	{
		this.typeTag = "ng";
		this.desc = "No gravity";
	}
	 public GravityLaws createTheInstance(JSONObject info) 
	 {
		 NoGravity T = null;
		 if (info.getString("type").equals("ng"))
			 T = new NoGravity();
		 return T;
	 }
}


