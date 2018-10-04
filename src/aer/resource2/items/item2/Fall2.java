package aer.resource2.items.item2;

import aer.commands.*;
import aer.locate.*;
import aer.map.*;
import aer.path.pather.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicType.*;

public class Fall2 implements TActionOther, IMovementAction, IAPAction, IThAP
{
	private final CostTable costs;
	private final boolean forced;
	private final ITiledMap map;
	private final HexLocation fallFrom;
	private final AirState fromState;
	private final HexLocation fallTo;

	public Fall2(CostTable costs, boolean forced, ITiledMap map, HexLocation fallFrom, AirState fromState)
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
		return costs.fallCost();
	}

	@Override
	public int mCost()
	{
		return costs.fallCostM();
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
	public boolean executeEnd(Pather xec0, Therathic xec1, E_AP_MP xec2)
	{
		if(xec2.useAPMP(this, this, E_AP_MP.rd(forced)))
		{
			xec0.setLoc(fallTo, null);
			xec0.setAirState(AirState.DOWN2, new AC());
			return false;
		}
		System.out.println("Not enough AP or MP");
		return true;
	}
}