package aer.relocatable.mount;

import aer.path.pather.*;
import aer.path.team.*;
import aer.relocatable.*;
import aer.resource2.therathicType.*;

public class MTypeAlliedMount extends MType
{
	private TX_CP_AlliedMount tx;
	private int maxW;

	public MTypeAlliedMount(MountSlotInfo[] info, int weight, int cMin, int cMax,
			int slowEffect, int energyReduction1, int maxW)
	{
		super(info, weight, cMin, cMax, slowEffect, 0, energyReduction1);
		this.maxW = maxW;
	}

	@Override
	public void setMain(Relocatable main)
	{
		super.setMain(main);
		if(main instanceof Pather && ((Pather) main).getTherathic() instanceof TX_CP_AlliedMount)
			tx = (TX_CP_AlliedMount) ((Pather) main).getTherathic();
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
	public void update()
	{
		tx.mountSlotUpdateInfo();
	}
}