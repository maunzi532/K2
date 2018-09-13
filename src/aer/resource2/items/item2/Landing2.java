package aer.resource2.items.item2;

import aer.*;
import aer.commands.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicType.*;
import java.util.*;

public class Landing2 implements TActionOther, IAirStateAction, IAPAction, IThAP
{
	private final CostTable costs;
	private final boolean forced;

	public Landing2(CostTable costs, boolean forced)
	{
		this.costs = costs;
		this.forced = forced;
	}

	@Override
	public int cost()
	{
		return costs.landingCost();
	}

	@Override
	public int mCost()
	{
		return costs.landingCostM();
	}

	@Override
	public AirState airState()
	{
		return AirState.FLOOR;
	}

	@Override
	public boolean executeStart(Pather xec)
	{
		return false;
	}

	@Override
	public List<Pather> targets(Pather xec)
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<Reaction> targetOptions(Pather xec, Pather target)
	{
		return null;
	}

	@Override
	public boolean executeOn(Pather xec, Pather target, Reaction chosen)
	{
		return false;
	}

	@Override
	public boolean executeEnd(Pather xec0, Therathic xec1, E_AP_MP xec2)
	{
		if(xec2.useAPMP(this, this, E_AP_MP.rd(forced)))
		{
			xec0.setAirState(AirState.FLOOR, new AC());
			return false;
		}
		System.out.println("Not enough AP or MP");
		return true;
	}
}