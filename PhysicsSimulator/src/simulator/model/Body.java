package simulator.model;

import simulator.misc.Vector;

public class Body 
{
	protected String id;
	protected Vector v;
	protected Vector a;
	protected Vector p;
	protected double m;
	public Body (String id, Vector v, Vector a, Vector p, double m)
	{
		this.id = id;
		this.v = v;
		this.a = a;
		this.p = p;
		this.m = m;
	}
	public String getId()
	{
		return this.id;
	}
	double getMass()
	{
		return this.m;
	}
	public Vector getPosition()
	{
		Vector copyOfPosition = new Vector(this.p);
		return copyOfPosition;
	}
	public Vector getVelocity()
	{
		Vector copyOfVelocity = new Vector(this.v);
		return copyOfVelocity;
	}
	public Vector getAcceleration()
	{
		Vector copyOfAcceleration = new Vector(this.a);
		return copyOfAcceleration;
	}
	void setVelocity(Vector v)
	{
		Vector newVelocity = new Vector(v);
		this.v = newVelocity;
	}
	void setAcceleration(Vector a)
	{
		Vector newAcceleration = new Vector(a);
		this.a = newAcceleration;
	}
	void setPosition(Vector p)
	{
		Vector newPosition = new Vector(p);
		this.p = newPosition;
	}
	void move(double t)
	{
		this.p = this.p.plus(this.v.scale(t).plus(this.a.scale(0.5*t*t)));
		this.v = this.v.plus(this.a.scale(t));
	}
	public String toString()
	{
		String info = "";
		info += "{\"id\": \"" + this.id + "\", \"mass\": " + this.m + ", \"pos\": " + this.p.toString() + ", \"vel\": " + this.v.toString() + ", \"acc\": " + this.a.toString() + "}";
		return info;
	}
}
