package aer.relocatable.mount;

import aer.path.pather.*;
import aer.relocatable.*;
import aer.resource3.*;

public class MTypeChar extends MType
{
	private TX_AP_Transform tx;

	public MTypeChar(MountSlotInfo info, int weight, int cMin, int cMax, int slowEffect, int energyReduction0)
	{
		super(new MountSlotInfo[]{info}, weight, cMin, cMax, slowEffect, energyReduction0, 0);
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
		if(main.getMountSlotAt(0) != null)
			return false;
		if(target instanceof Pather && ((Pather) target).getTherathic().teamSide() != tx.teamSide())
			return false;
		return target.getMType().weight() <= cMax;
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