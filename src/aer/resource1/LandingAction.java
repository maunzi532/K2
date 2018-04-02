package aer.resource1;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;

public class LandingAction implements APAction, TActionOther
{
	private final CostTable costs;

	public LandingAction(CostTable costs)
	{
		this.costs = costs;
	}

	@Override
	public int cost()
	{
		return costs.landingCost;
	}

	@Override
	public int mCost()
	{
		return costs.landingCostM;
	}

	@Override
	public AirState airState()
	{
		return AirState.FLOOR;
	}
}