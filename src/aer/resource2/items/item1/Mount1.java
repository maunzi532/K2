package aer.resource2.items.item1;

import aer.*;
import aer.commands.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicType.*;

public class Mount1 implements TActionObject, IMountAction, IAPAction, IMainAction, IThAP
{
	private final CostTable costs;
	private final HexDirection from;
	private final Relocatable mount;
	private final int slot;

	public Mount1(CostTable costs, HexDirection from, Relocatable mount, int slot)
	{
		this.costs = costs;
		this.from = from;
		this.mount = mount;
		this.slot = slot;
	}

	public Mount1(CostTable costs, Relocatable mount, int slot)
	{
		this.costs = costs;
		from = null;
		this.mount = mount;
		this.slot = slot;
	}

	@Override
	public int cost()
	{
		return costs.mountCost;
	}

	@Override
	public int mCost()
	{
		return costs.mountCostM + costs.turnCost(from, mount.getDirection());
	}

	@Override
	public HexDirection lookDirection()
	{
		return from == null ? null : mount.getDirection();
	}

	@Override
	public Relocatable object()
	{
		return mount;
	}

	@Override
	public Relocatable mounting()
	{
		return mount;
	}

	@Override
	public AirState airState()
	{
		return AirState.MOUNT;
	}

	@Override
	public boolean end()
	{
		return from != null;
	}

	@Override
	public boolean executeEnd(Pather xec0, Therathic xec1, E_AP_MP xec2)
	{
		if(xec2.useAPMP(this, this, E_AP_MP.Use.REAL))
		{
			if(from != null)
			{
				xec0.setDirection(mount.getDirection(), null);
			}
			xec0.setMountedTo(mount.id, slot, new AC());
			return false;
		}
		return true;
	}
}