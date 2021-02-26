package simulator.factories;

import org.json.JSONObject;

import simulator.model.GravityLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<GravityLaws>
{
	public NewtonUniversalGravitationBuilder()
	{
		this.typeTag = "nlug";
		this.desc = "All bodies attract to each other";
	}
	public GravityLaws createTheInstance(JSONObject info) 
	{
		NewtonUniversalGravitation T = null;
		if(info.getString("type").equals("nlug"))
			T = new NewtonUniversalGravitation();
		return T;		
	}
}
