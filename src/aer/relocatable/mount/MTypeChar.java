package aer.relocatable.mount;

import aer.path.pather.*;
import aer.path.team.*;
import aer.relocatable.*;
import aer.resource3.*;

public class MTypeChar extends MType
{
	private TX_AP_Transform tx;
	private int maxW;

	public MTypeChar(MountSlotInfo[] info, int weight, int cMin, int cMax, int slowEffect, int maxW)
	{
		super(info, weight, cMin, cMax, slowEffect, 30, 0);
		this.maxW = maxW;
	}

	@Override
	public void setMain(Relocatable main)
	{
		super.setMain(main);
		if(main instanceof Pather && ((Pather) main).getTherathic() instanceof TX_AP_Transform)
			tx = (TX_AP_Transform) ((Pather) main).getTherathic();
		else
			throw new IllegalArgumentException();
	}

	@Override
	public boolean canTransport(Relocatable target)
	{
		if(target instanceof Pather)
		{
			Therathic t0 = ((Pather) target).getTherathic();
			if(t0.teamSide() != tx.teamSide())
				return false;
		}
		return target.getMType().weight() <= maxW;
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
	public boolean actionsBlocked()
	{
		return true;
	}
}