package aer.resource1;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;

public class TurnAction implements APAction, TActionDirection
{
	private final CostTable costs;
	private final HexDirection from;
	private final HexDirection to;

	public TurnAction(CostTable costs, HexDirection from, HexDirection to)
	{
		this.costs = costs;
		this.from = from;
		this.to = to;
	}

	@Override
	public int cost()
	{
		return HexDirection.turnCost(from, to) * costs.tcm;
	}

	@Override
	public int mCost()
	{
		return HexDirection.turnCost(from, to) * costs.tcmM;
	}

	@Override
	public HexDirection lookDirection()
	{
		return to;
	}

	@Override
	public boolean dizzy()
	{
		return true;
	}

	@Override
	public HexDirection direction()
	{
		return to;
	}
}