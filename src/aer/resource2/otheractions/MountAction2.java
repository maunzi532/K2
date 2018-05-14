package aer.resource2.otheractions;

import aer.*;
import aer.commands.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicTypes.*;

public class MountAction2 implements TActionObject, IMountAction, IAPAction, IMainAction, IThAP
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
	public boolean executeEnd(HexPather xec0, TherathicHex xec1, E_AP_MP xec2)
	{
		if(xec2.useAPMP(this, this, E_AP_MP.Use.REAL))
		{
			if(from != null)
			{
				xec0.setDirection(mount.getDirection());
				CTurn.issueCommand(xec0);
			}
			xec0.setMount(mount);
			CMove.issueCommand(xec0);
			return false;
		}
		return true;
	}
}