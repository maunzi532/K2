package aer.relocatable.mount;

import aer.relocatable.*;

public abstract class MType
{
	protected Relocatable main;
	protected MountSlotInfo[] info;
	protected int weight;
	protected int cMin;
	protected int cMax;
	protected int slowEffect;
	protected int energyReduction0;
	protected int energyReduction1;

	public MType(MountSlotInfo[] info, int weight, int cMin, int cMax, int slowEffect,
			int energyReduction0, int energyReduction1)
	{
		this.info = info;
		this.weight = weight;
		this.cMin = cMin;
		this.cMax = cMax;
		this.slowEffect = slowEffect;
		this.energyReduction0 = energyReduction0;
		this.energyReduction1 = energyReduction1;
	}

	public void setMain(Relocatable main)
	{
		this.main = main;
	}

	public MountSlotInfo[] info()
	{
		return info;
	}

	public int seats()
	{
		return info.length;
	}

	public abstract boolean canTransport(Relocatable target);

	public int weight()
	{
		return weight;
	}

	public int cMin()
	{
		return cMin;
	}

	public int cMax()
	{
		return cMax;
	}

	public int slowEffect()
	{
		return slowEffect;
	}

	public int energyReduction0()
	{
		return energyReduction0;
	}

	public int energyReduction1()
	{
		return energyReduction1;
	}

	public abstract boolean moveLink();

	public abstract boolean energyLink();

	public boolean actionsBlocked()
	{
		return false;
	}

	public void update(){}
}