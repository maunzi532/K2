package aer.resource2.movement;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicTypes.*;

public class TurnAction2 implements TActionDirection, IDirectionAction, IMainAction, IAPAction
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
	public boolean executeEnd(HexPather xec)
	{
		if(xec.getTherathicHex() instanceof EActionPoints &&
				((EActionPoints) xec.getTherathicHex()).useAPMP(this, this, true))
		{
			xec.setDirection(to);
			return false;
		}
		return true;
	}
}