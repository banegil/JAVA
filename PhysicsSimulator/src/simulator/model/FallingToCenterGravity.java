package simulator.model;

import java.util.*;

import simulator.misc.Vector;

public class FallingToCenterGravity implements GravityLaws
{
	public static final double g = 9.81;
	public FallingToCenterGravity()
	{}
	public void apply(List<Body> bodies)
	{
		for (Body b: bodies)
		{
			Vector v = new Vector(b.getPosition().direction());
			b.setAcceleration(v.scale(-g));
		}
	}
}
