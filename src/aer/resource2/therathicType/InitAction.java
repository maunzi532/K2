package aer.resource2.therathicType;

import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.interfaces.*;

public class InitAction implements TActionOther, IAPAction, IThAP
{
	private final CostTable costs;
	private final boolean extraCost;

	public InitAction(CostTable costs, boolean extraCost)
	{
		this.costs = costs;
		this.extraCost = extraCost;
	}

	@Override
	public boolean canBeFinalAction()
	{
		return false;
	}

	@Override
	public int cost()
	{
		return extraCost ? costs.init() : 0;
	}

	@Override
	public boolean extraCost()
	{
		return true;
	}

	@Override
	public boolean executeEnd(Pather xec0, Therathic xec1, E_AP_MP xec2)
	{
		if(!xec2.useAP(this, E_AP_MP.Use.REAL))
			return true;
		xec1.setUsedFirstPath();
		return false;
	}
}