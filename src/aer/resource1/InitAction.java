package aer.resource1;

import aer.path.*;
import aer.path.takeable.*;

public class InitAction implements APAction, TActionOther
{
	private final CostTable costs;

	public InitAction(CostTable costs)
	{
		this.costs = costs;
	}

	@Override
	public int cost()
	{
		return costs.init;
	}

	@Override
	public int mCost()
	{
		return costs.initM;
	}
}