package aer.resource2.movement;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.interfaces.*;
import java.util.*;

public class LandingAction2 implements TActionOther, IAirStateAction, IAPAction
{
	private final CostTable costs;

	public LandingAction2(CostTable costs)
	{
		this.costs = costs;
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
	public List<String> targetOptions(HexPather xec, HexPather target)
	{
		return null;
	}

	@Override
	public boolean executeOn(HexPather xec, HexPather target, String chosenOption)
	{
		return false;
	}

	@Override
	public boolean executeEnd(HexPather xec)
	{
		return false;
	}
}