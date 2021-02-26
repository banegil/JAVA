package simulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.PhysicsSimulator;

public class Controller 
{
	private PhysicsSimulator physicsSimulator;
	private Factory<Body> factoryOfBodies;
	public Controller(PhysicsSimulator ps, Factory<Body> fb)
	{
		this.physicsSimulator = ps;
		this.factoryOfBodies = fb;
	}
	
	public void run (int n, OutputStream out)
	{
		JSONObject simulatorStates = new JSONObject();
		JSONArray arrayOfStates = new JSONArray();
		JSONObject state = new JSONObject(this.physicsSimulator.toString());
		arrayOfStates.put(state);
		for(int i = 0; i < n; i++)
		{
			this.physicsSimulator.advance();
			state = new JSONObject(this.physicsSimulator.toString());
			arrayOfStates.put(state);
		}
		simulatorStates.put("states", arrayOfStates);
		try {
			out.write(simulatorStates.toString().getBytes());
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
			
	}
	public void loadBodies(InputStream in)
	{
		JSONObject jsonInupt = new JSONObject(new JSONTokener(in));
		JSONArray arrayOfBodies;
		JSONObject bb;
		Body b;
		arrayOfBodies = jsonInupt.getJSONArray("bodies");
		for(int i = 0; i < arrayOfBodies.length(); i++)
		{
			bb = arrayOfBodies.getJSONObject(i);
			b = this.factoryOfBodies.createInstance(bb);
			this.physicsSimulator.addBody(b);
		}
	}
}
