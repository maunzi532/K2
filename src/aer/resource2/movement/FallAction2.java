package aer.resource2.movement;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.interfaces.*;

public class FallAction2 implements TActionOther, IMovementAction, IAPAction
{
	private final CostTable costs;
	private final IHexMap map;
	private final HexLocation fallFrom;
	private final AirState fromState;
	private final HexLocation fallTo;

	public FallAction2(CostTable costs, IHexMap map, HexLocation fallFrom, AirState fromState)
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
	public int reqFallReduction()
	{
		return 1;
	}

	@Override
	public boolean executeEnd(HexPather xec)
	{
		return false;
	}
}