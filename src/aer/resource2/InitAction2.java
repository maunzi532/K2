package aer.resource2;

import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.interfaces.*;

public class InitAction2 implements TActionOther, IAPAction
{
	private final CostTable costs;

	public InitAction2(CostTable costs)
	{
		this.costs = costs;
	}

	@Override
	public int cost()
	{
		return costs.init;
	}

	@Override
	public boolean executeEnd(HexPather xec)
	{
		return false;
	}
}