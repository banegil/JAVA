package simulator.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator 
{
	private List<Body> listOfBodies;
	private double realTime;
	private double time;
	private GravityLaws gravityLawToApply;
	
	public PhysicsSimulator(GravityLaws gl, double d)
	{
		if(gl == null)
			throw new IllegalArgumentException();
		this.gravityLawToApply = gl;
		if (d <= 0)
			throw new IllegalArgumentException();
		this.time = d;
		this.realTime = 0.0;
		this.gravityLawToApply = gl;
		this.listOfBodies = new ArrayList<Body>();
	}
	public void addBody(Body b)
	{
		for (Body exsistingB : listOfBodies)
		{
			if(b.getId().equals(exsistingB.getId()))
				throw new IllegalArgumentException();
		}
		listOfBodies.add(b);
	}
	public void advance()
	{
		this.gravityLawToApply.apply(this.listOfBodies);
		for(Body b : this.listOfBodies)
			b.move(this.time);
		this.realTime += this.time;
	}
	public String toString()
	{
		JSONObject simState = new JSONObject();
		JSONArray bodies = new JSONArray();
		simState.put("time", this.realTime);
		for(Body b : this.listOfBodies)
		{
			JSONObject aux = new JSONObject(b.toString());
			bodies.put(aux);
		}			
		simState.put("bodies", bodies);
		return simState.toString();
	}
}
