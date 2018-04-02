package aer.resource1;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;

public class MountAction implements APAction, TActionObject
{
	private final CostTable costs;
	private final HexDirection from;
	private final HexObject mount;

	public MountAction(CostTable costs, HexDirection from, HexObject mount)
	{
		this.costs = costs;
		this.from = from;
		this.mount = mount;
	}

	public MountAction(CostTable costs, HexObject mount)
	{
		this.costs = costs;
		this.mount = mount;
		from = null;
	}

	@Override
	public int cost()
	{
		return costs.mountCost + (from == null ? 0 : HexDirection.turnCost(from, mount.getDirection()) * costs.tcm);
	}

	@Override
	public int mCost()
	{
		return costs.mountCostM + (from == null ? 0 : HexDirection.turnCost(from, mount.getDirection()) * costs.tcmM);
	}

	@Override
	public HexObject mounting()
	{
		return mount;
	}

	@Override
	public HexDirection lookDirection()
	{
		return from == null ? null : mount.getDirection();
	}

	@Override
	public AirState airState()
	{
		return AirState.MOUNT;
	}

	@Override
	public HexObject object()
	{
		return mount;
	}
}