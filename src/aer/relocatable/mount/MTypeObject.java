package aer.relocatable.mount;

import aer.relocatable.*;

public class MTypeObject extends MType
{
	public MTypeObject()
	{
		super(new MountSlotInfo[0], 0, 0, 0,
				0, 0, 0);
	}

	public MTypeObject(int weight)
	{
		super(new MountSlotInfo[0], weight, 0, 0,
				0, 0, 0);
	}

	@Override
	public boolean canTransport(Relocatable target)
	{
		return false;
	}

	@Override
	public boolean moveLink()
	{
		return false;
	}

	@Override
	public boolean energyLink()
	{
		return false;
	}
}