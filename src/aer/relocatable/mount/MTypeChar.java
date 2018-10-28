package aer.relocatable.mount;

import aer.relocatable.*;
import aer.resource3.*;

public class MTypeChar extends MType
{
	private TX_AP_Transform tx;

	public MTypeChar(Relocatable main, MountSlotInfo[] info, int weight, int cMin, int cMax, int slowEffect,
			int energyReduction0, int energyReduction1)
	{
		super(main, info, weight, cMin, cMax, slowEffect, energyReduction0, energyReduction1);
	}

	@Override
	public boolean canTransport(Relocatable target)
	{
		return false;
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
}