package simulator.model;

import java.util.List;
import java.lang.Math;
import simulator.misc.Vector;

public class NewtonUniversalGravitation implements GravityLaws
{
	public static final double G = 6.67E-11;
	public void NewtonianUniversalGravitation()
	{}
	public void apply(List<Body> bodies)
	{
		Vector sumForces;
		for (int i = 0; i < bodies.size(); i++)
		{
			sumForces = new Vector(bodies.get(i).getAcceleration().dim());
			sumForces = sumForces.scale(0);
			for (int j = 0; j < bodies.size(); j++)
			{
				if(i != j)
				{
					sumForces = sumForces.plus(forceBetweenTwoBodies(bodies.get(i), bodies.get(j)));
				}				
			}
			bodies.get(i).setAcceleration(sumForces.scale(1/bodies.get(i).getMass()));
		}
	}
	
	public Vector forceBetweenTwoBodies(Body i, Body j)
	{
		double forceMagnitude;
		Vector forceDir = j.getPosition().minus(i.getPosition()).direction();
		forceMagnitude = G * i.getMass() * j.getMass() / Math.pow((j.getPosition().distanceTo(i.getPosition())), 2);
		return forceDir.scale(forceMagnitude);		
	}
}
