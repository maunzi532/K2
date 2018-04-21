package aer.resource2.movement;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicTypes.*;

public class FallAction2 implements TActionOther, IMovementAction, IAPAction, IThAP
{
	private final CostTable costs;
	private final boolean forced;
	private final IHexMap map;
	private final HexLocation fallFrom;
	private final AirState fromState;
	private final HexLocation fallTo;

	public FallAction2(CostTable costs, boolean forced, IHexMap map, HexLocation fallFrom, AirState fromState)
	{
		this.costs = costs;
		this.forced = forced;
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
	public boolean executeEnd(HexPather xec0, TherathicHex xec1, E_AP_MP xec2)
	{
		if(xec2.useAPMP(this, this, E_AP_MP.rd(forced)))
		{
			xec0.setLoc(fallTo);
			xec0.setAirState(AirState.DOWN2);
			return false;
		}
		System.out.println("Not enough AP or MP");
		return true;
	}
}