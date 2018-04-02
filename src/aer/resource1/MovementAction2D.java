package aer.resource1;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;

public class MovementAction2D implements APAction, TActionLocation
{
	private final CostTable costs;
	private final IHexMap map;
	private final HexLocation start;
	private final HexLocation end;
	private final HexDirection from;
	private final HexDirection mvDir;

	public MovementAction2D(CostTable costs, HexDirection from, IHexMap map, HexLocation start, HexLocation end)
	{
		this.costs = costs;
		this.from = from;
		this.map = map;
		this.start = start;
		this.end = end;
		mvDir = HexLocation.direction(start, end);
	}

	public boolean possible()
	{
		return map.getTile(end).type == MapTileType.FLOOR;
	}

	@Override
	public int cost()
	{
		return costs.initMove + HexDirection.turnCost(from, mvDir) * costs.tcm + HexLocation.xdzDifference(start, end) * costs.moveCost;
	}

	@Override
	public int mCost()
	{
		return costs.initMoveM + HexDirection.turnCost(from, mvDir) * costs.tcmM + HexLocation.xdzDifference(start, end) * costs.moveCostM;
	}

	@Override
	public HexLocation movesTo()
	{
		return end;
	}

	@Override
	public HexDirection lookDirection()
	{
		return mvDir;
	}

	@Override
	public HexLocation location()
	{
		return end;
	}

	@Override
	public boolean executeStart(HexPather xec)
	{
		if(xec.getAirState() != AirState.FLOOR)
			return true;
		return false;
	}
}