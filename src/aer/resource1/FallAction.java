package aer.resource1;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;

public class FallAction implements APAction, TActionOther
{
	private final CostTable costs;
	private final IHexMap map;
	private final HexLocation fallFrom;
	private final AirState fromState;
	private final HexLocation fallTo;

	public FallAction(CostTable costs, IHexMap map, HexLocation fallFrom, AirState fromState)
	{
		this.costs = costs;
		this.map = map;
		this.fallFrom = fallFrom;
		this.fromState = fromState;
		fallTo = new HexLocation(fallFrom, 0, 0, fromState.fall, 0);
	}

	@Override
	public int cost()
	{
		return costs.fallCost;
	}

	@Override
	public int mCost()
	{
		return costs.fallCostM;
	}

	@Override
	public HexLocation movesTo()
	{
		return fallTo;
	}

	@Override
	public AirState airState()
	{
		return AirState.DOWN2;
	}

	@Override
	public int falls()
	{
		return 1;
	}
}