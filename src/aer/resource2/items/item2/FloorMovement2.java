package aer.resource2.items.item2;

import aer.*;
import aer.commands.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicType.*;
import java.util.*;

public class FloorMovement2 implements TActionLocation, IMovementAction, IAPAction, IThAP
{
	private final CostTable costs;
	private final ITiledMap map;
	private final HexLocation start;
	private final HexLocation end;
	private final HexDirection from;
	private final HexDirection mvDir;
	private final List<HexLocation> fMovingA;
	private final List<HexLocation> fMovingN;

	public FloorMovement2(CostTable costs, HexDirection from, ITiledMap map, HexLocation start, HexLocation end,
			List<HexLocation> fMovingA, List<HexLocation> fMovingN)
	{
		this.costs = costs;
		this.from = from;
		this.map = map;
		this.start = start;
		this.end = end;
		mvDir = HexLocation.direction(start, end);
		this.fMovingA = fMovingA;
		this.fMovingN = fMovingN;
	}

	public boolean possible()
	{
		return (fMovingA == null || !fMovingA.contains(end)) && map.getTile(end).type == MapTileType.FLOOR;
	}

	@Override
	public int cost()
	{
		return costs.initMove;
	}

	@Override
	public int mCost()
	{
		return costs.moveCost(start, end) + costs.turnCost(from, mvDir);
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
	public HexLocation movesTo()
	{
		return end;
	}

	@Override
	public List<HexLocation> freelyMovingN()
	{
		return fMovingN;
	}

	@Override
	public boolean executeEnd(Pather xec0, Therathic xec1, E_AP_MP xec2)
	{
		if(xec2.useAPMP(this, this, E_AP_MP.Use.REAL))
		{
			xec0.setDirection(mvDir);
			CTurn.issueCommand(xec0);
			xec0.setLoc(end);
			CMove.issueCommand(xec0);
			return false;
		}
		return true;
	}
}