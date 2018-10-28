package aer.relocatable.mount;

import aer.relocatable.*;
import aer.resource2.therathicType.*;

public class MTypeAlliedMount extends MType
{
	private TX_CP_AlliedMount tx_cp;
	private int weight;

	@Override
	public boolean canTransport(Relocatable target)
	{
		return false;
	}

	@Override
	public int weight()
	{
		return weight;
	}

	@Override
	public int cMin()
	{
		return 0;
	}

	@Override
	public int cMax()
	{
		return 0;
	}

	@Override
	public int slowEffect()
	{
		return 0;
	}

	@Override
	public int energyReduction0()
	{
		return 0;
	}

	@Override
	public int energyReduction1()
	{
		return 0;
	}

	@Override
	public boolean moveLink()
	{
		return true;
	}

	@Override
	public boolean energyLink()
	{
		return false;
	}

	@Override
	public void update()
	{
		tx_cp.mountSlotUpdateInfo();
	}
}