package aer.resource2;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.interfaces.*;

public class DismountAction2 implements TActionOther, IMountAction, IAPAction
{
	private final CostTable costs;
	private final HexObject mount;
	private final AirState airState;

	public DismountAction2(CostTable costs, HexObject mount, AirState airState)
	{
		this.costs = costs;
		this.mount = mount;
		this.airState = airState;
	}

	@Override
	public int cost()
	{
		return costs.dismountCost;
	}

	@Override
	public int mCost()
	{
		return costs.dismountCostM;
	}

	@Override
	public AirState airState()
	{
		return airState;
	}

	@Override
	public int reqFallReduction()
	{
		return -1;
	}

	@Override
	public boolean dismounting()
	{
		return true;
	}

	@Override
	public boolean executeEnd(HexPather xec)
	{
		return false;
	}
}