package aer.resource2.movement;

import aer.*;
import aer.commands.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicTypes.*;

public class TurnAction2 implements TActionDirection, IDirectionAction, IMainAction, IAPAction, IThAP
{
	private final CostTable costs;
	private final HexDirection from;
	private final HexDirection to;

	public TurnAction2(CostTable costs, HexDirection from, HexDirection to)
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
	public HexDirection direction()
	{
		return to;
	}

	@Override
	public boolean executeEnd(HexPather xec0, TherathicHex xec1, E_AP_MP xec2)
	{
		if(xec2.useAPMP(this, this, E_AP_MP.Use.REAL))
		{
			xec0.setDirection(to);
			CTurn.issueCommand(xec0);
			return false;
		}
		return true;
	}
}