package aer.resource1;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;

public class DismountAction implements APAction, TActionOther
{
	private final CostTable costs;
	private final HexObject mount;
	private final AirState airState;

	public DismountAction(CostTable costs, HexObject mount, AirState airState)
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
	public boolean dismounting()
	{
		return true;
	}

	@Override
	public HexLocation movesTo()
	{
		return mount.getLoc();
	}

	@Override
	public AirState airState()
	{
		return airState;
	}
}