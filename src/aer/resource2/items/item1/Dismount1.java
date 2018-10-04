package aer.resource2.items.item1;

import aer.commands.*;
import aer.locate.*;
import aer.path.pather.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.relocatable.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicType.*;

public class Dismount1 implements TActionOther, IMountAction, IAPAction, IThAP
{
	private final CostTable costs;
	private final Relocatable mount;
	private final AirState airState;

	public Dismount1(CostTable costs, Relocatable mount, AirState airState)
	{
		this.costs = costs;
		this.mount = mount;
		this.airState = airState;
	}

	@Override
	public int cost()
	{
		return costs.dismountCost();
	}

	@Override
	public int mCost()
	{
		return costs.dismountCostM();
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
	public boolean executeEnd(Pather xec0, Therathic xec1, E_AP_MP xec2)
	{
		if(xec2.useAPMP(this, this, E_AP_MP.Use.REAL))
		{
			xec0.dismount(airState, new AC());
			return false;
		}
		return true;
	}
}