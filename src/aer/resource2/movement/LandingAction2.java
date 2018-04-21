package aer.resource2.movement;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicTypes.*;
import java.util.*;

public class LandingAction2 implements TActionOther, IAirStateAction, IAPAction, IThAP
{
	private final CostTable costs;
	private final boolean forced;

	public LandingAction2(CostTable costs, boolean forced)
	{
		this.costs = costs;
		this.forced = forced;
	}

	@Override
	public int cost()
	{
		return costs.landingCost;
	}

	@Override
	public int mCost()
	{
		return costs.landingCostM;
	}

	@Override
	public AirState airState()
	{
		return AirState.FLOOR;
	}

	@Override
	public boolean executeStart(HexPather xec)
	{
		return false;
	}

	@Override
	public List<HexPather> targets(HexPather xec)
	{
		return null;
	}

	@Override
	public List<Reaction> targetOptions(HexPather xec, HexPather target)
	{
		return null;
	}

	@Override
	public boolean executeOn(HexPather xec, HexPather target, Reaction chosen)
	{
		return false;
	}

	@Override
	public boolean executeEnd(HexPather xec0, TherathicHex xec1, E_AP_MP xec2)
	{
		if(xec2.useAPMP(this, this, E_AP_MP.rd(forced)))
		{
			xec0.setAirState(AirState.FLOOR);
			return false;
		}
		System.out.println("Not enough AP or MP");
		return true;
	}
}