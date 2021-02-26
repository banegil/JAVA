package simulator.model;

import simulator.misc.Vector;

public class MassLosingBody extends Body
{
	protected double lossFactor;
	protected double lossFrequency;
	protected double c;
	
	public MassLosingBody(String id, Vector v, Vector a, Vector p, double m, double lossFactor, double lossFrequency) 
	{
		super(id, v, a, p, m);
		this.lossFactor = lossFactor;
		this.lossFrequency = lossFrequency;
		this.c = 0.0;
	}
	void move(double t)
	{
		this.p = this.p.plus(this.v.scale(t).plus(this.a.scale(0.5*t*t)));
		this.v = this.v.plus(this.a.scale(t));
		this.c += t;
		if(c >= this.lossFrequency)
		{
			this.m *= 1-this.lossFactor;
			this.c = 0.0;
		}		
	}
}
