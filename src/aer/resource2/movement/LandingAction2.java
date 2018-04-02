package aer.resource2.movement;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicTypes.*;
import java.util.*;

public class LandingAction2 implements TActionOther, IAirStateAction, IAPAction
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
	public boolean executeEnd(HexPather xec)
	{
		if(xec.getTherathicHex() instanceof EActionPoints)
		{
			EActionPoints eap = ((EActionPoints) xec.getTherathicHex());
			if(forced)
				eap.drainAPMP(this, this);
			else if(eap.useAPMP(this, this, true))
				return true;
			xec.setAirState(AirState.FLOOR);
			return false;
		}
		return true;
	}
}