package aer.resource2.items.item3;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicType.*;
import java.util.*;

public class BaseTargeting3 implements TActionObject, ITargetedAction, IAPAction, IMainAction, IThAP
{
	private final CostTable costs;
	private final HexLocation start;
	private final HexDirection from;
	private final Relocatable target;
	private final HexDirection lookDir;

	public BaseTargeting3(CostTable costs, HexLocation start, HexDirection from, Relocatable target)
	{
		this.costs = costs;
		this.start = start;
		this.from = from;
		this.target = target;
		lookDir = HexLocation.direction(start, target.getLoc());
	}

	public BaseTargeting3(CostTable costs, HexLocation start, Relocatable target)
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
		return 0;
	}

	@Override
	public int mCost()
	{
		return costs.turnCost(from, lookDir);
	}

	@Override
	public HexDirection lookDirection()
	{
		return lookDir;
	}

	@Override
	public Relocatable object()
	{
		return target;
	}

	@Override
	public Relocatable target()
	{
		return target;
	}

	@Override
	public boolean executeStart(Pather xec0, Therathic xec1, E_AP_MP xec2)
	{
		return false;
	}

	@Override
	public List<Pather> targets(Pather xec0, Therathic xec1)
	{
		if(target instanceof Pather)
			return Collections.singletonList((Pather) target);
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<Reaction> targetOptions(Therathic xec1, Therathic target1, E_AP_MP target2)
	{
		return Arrays.asList(new Reaction("Wugu0", 0, true),
				new Reaction("Wugu1", 1, true));
	}

	@Override
	public List<Integer> interruptTeamNumbers(Therathic xec1, Therathic target1)
	{
		return Collections.singletonList(target1.teamSide());
	}

	@Override
	public boolean executeOn(Therathic xec1, E_AP_MP xec2, Therathic target1, E_AP_MP target2, Reaction chosen)
	{
		return chosen == null || chosen.code != 0;
	}

	@Override
	public boolean executeEnd(Pather xec0, Therathic xec1, E_AP_MP xec2)
	{
		return false;
	}
}