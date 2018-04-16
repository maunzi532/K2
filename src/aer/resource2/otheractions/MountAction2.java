package aer.resource2.otheractions;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicTypes.*;

public class MountAction2 implements TActionObject, IMountAction, IAPAction, IMainAction
{
	private final CostTable costs;
	private final HexDirection from;
	private final HexObject mount;

	public MountAction2(CostTable costs, HexDirection from, HexObject mount)
	{
		this.costs = costs;
		this.from = from;
		this.mount = mount;
	}

	public MountAction2(CostTable costs, HexObject mount)
	{
		this.costs = costs;
		from = null;
		this.mount = mount;
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
	public HexDirection lookDirection()
	{
		return from == null ? null : mount.getDirection();
	}

	@Override
	public HexObject object()
	{
		return mount;
	}

	@Override
	public HexObject mounting()
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
	public boolean executeEnd(HexPather xec)
	{
		if(xec.getTherathicHex() instanceof EActionPoints &&
				((EActionPoints) xec.getTherathicHex()).useAPMP(this, this, true))
		{
			if(from != null)
				xec.setDirection(mount.getDirection());
			xec.setMount(mount);
			return false;
		}
		return true;
	}
}