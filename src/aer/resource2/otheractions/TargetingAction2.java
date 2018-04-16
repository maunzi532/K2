package aer.resource2.otheractions;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.interfaces.*;
import java.util.*;

public class TargetingAction2 implements TActionObject, ITargetedAction, IAPAction, IMainAction
{
	private final CostTable costs;
	private final HexLocation start;
	private final HexDirection from;
	private final HexObject target;
	private final HexDirection lookDir;

	public TargetingAction2(CostTable costs, HexLocation start, HexDirection from, HexObject target)
	{
		this.costs = costs;
		this.start = start;
		this.from = from;
		this.target = target;
		lookDir = HexLocation.direction(start, target.getLoc());
	}

	public TargetingAction2(CostTable costs, HexLocation start, HexObject target)
	{
		this.costs = costs;
		this.start = start;
		from = null;
		this.target = target;
		lookDir = null;
	}

	@Override
	public int cost()
	{
		return lookDir != null ? HexDirection.turnCost(from, lookDir) * costs.tcm : 0;
	}

	@Override
	public int mCost()
	{
		return lookDir != null ? HexDirection.turnCost(from, lookDir) * costs.tcmM : 0;
	}

	@Override
	public HexDirection lookDirection()
	{
		return lookDir;
	}

	@Override
	public HexObject object()
	{
		return target;
	}

	@Override
	public HexObject target()
	{
		return target;
	}

	@Override
	public boolean executeStart(HexPather xec)
	{
		return false;
	}

	@Override
	public List<HexPather> targets(HexPather xec)
	{
		if(target instanceof HexPather)
			return Collections.singletonList((HexPather) target);
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<Reaction> targetOptions(HexPather xec, HexPather target)
	{
		return Collections.singletonList(new Reaction("Wugu", 0, true));
	}

	@Override
	public List<Integer> interruptTeamNumbers(HexPather xec, HexPather target)
	{
		return Collections.singletonList(target.getTherathicHex().teamSide());
	}

	@Override
	public boolean executeOn(HexPather xec, HexPather target, Reaction chosen)
	{
		return chosen == null || chosen.code != 0;
	}

	@Override
	public boolean executeEnd(HexPather xec)
	{
		return false;
	}
}