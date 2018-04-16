package aer.resource2.otheractions;

import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicTypes.*;

public class InitAction2 implements TActionOther, IAPAction
{
	private final CostTable costs;

	public InitAction2(CostTable costs)
	{
		this.costs = costs;
	}

	@Override
	public boolean canBeFinalAction()
	{
		return false;
	}

	@Override
	public int cost()
	{
		return costs.init;
	}

	@Override
	public boolean executeEnd(HexPather xec)
	{
		return !(xec.getTherathicHex() instanceof EActionPoints &&
				((EActionPoints) xec.getTherathicHex()).useAP(this, true));
	}
}