package aer.resource2.otheractions;

import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicTypes.*;

public class InitAction2 implements TActionOther, IAPAction, IThAP
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
	public boolean executeEnd(HexPather xec0, TherathicHex xec1, E_AP_MP xec2)
	{
		return !xec2.useAP(this, E_AP_MP.Use.REAL);
	}
}