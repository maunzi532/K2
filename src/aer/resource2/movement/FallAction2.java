package aer.resource2.movement;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicTypes.*;

public class FallAction2 implements TActionOther, IMovementAction, IAPAction
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
	public boolean executeEnd(HexPather xec)
	{
		if(xec.getTherathicHex() instanceof EActionPoints)
		{
			EActionPoints eap = ((EActionPoints) xec.getTherathicHex());
			if(forced)
				eap.drainAPMP(this, this);
			else if(eap.useAPMP(this, this, true))
				return true;
			xec.setLoc(fallTo);
			xec.setAirState(AirState.DOWN2);
			return false;
		}
		return true;
	}
}