package aer.relocatable.mount;

import aer.relocatable.*;

public abstract class MType
{
	private Relocatable main;
	private MountSlotInfo[] info = new MountSlotInfo[0];

	public Relocatable main()
	{
		return main;
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

	public abstract int weight();

	public abstract int cMin();

	public abstract int cMax();

	public abstract int slowEffect();

	public abstract int energyReduction0();

	public abstract int energyReduction1();

	public abstract boolean moveLink();

	public abstract boolean energyLink();

	public abstract void update();
}