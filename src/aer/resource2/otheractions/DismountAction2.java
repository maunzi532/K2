package aer.resource2.otheractions;

import aer.*;
import aer.commands.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicTypes.*;

public class DismountAction2 implements TActionOther, IMountAction, IAPAction, IThAP
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
	public boolean executeEnd(HexPather xec0, TherathicHex xec1, E_AP_MP xec2)
	{
		if(xec2.useAPMP(this, this, E_AP_MP.Use.REAL))
		{
			xec0.setMount(null);
			xec0.setAirState(airState);
			CMove.issueCommand(xec0);
			return false;
		}
		return true;
	}
}