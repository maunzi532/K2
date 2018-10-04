package aer.resource2.items.item2;

import aer.commands.*;
import aer.locate.*;
import aer.locate.pathseeker.*;
import aer.map.*;
import aer.map.maptiles.*;
import aer.path.pather.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicType.*;
import java.util.*;

public class FloorMovement2 implements TActionLocation, IMovementAction, IAPAction, IThAP
{
	private final CostTable costs;
	private final boolean extraCost;
	private final ITiledMap map;
	private final HexLocation start;
	private final HexLocation end;
	private final HexDirection from;
	private final HexDirection mvDir;
	private final List<HexLocation> fMovingA;
	private final List<HexLocation> fMovingN;
	private final PathSeeker seeker;

	public FloorMovement2(CostTable costs, boolean extraCost, HexDirection from, ITiledMap map, HexLocation start,
			HexLocation end, List<HexLocation> fMovingA, List<HexLocation> fMovingN)
	{
		this.costs = costs;
		this.extraCost = extraCost;
		this.from = from;
		this.map = map;
		this.start = start;
		this.end = end;
		mvDir = HexLocation.direction(start, end);
		this.fMovingA = fMovingA;
		this.fMovingN = fMovingN;
		if(fMovingA == null || !fMovingA.contains(end))
			seeker = new PathSeeker(map, start, end, e -> e.type == MapTileType.FLOOR ? 1 : -1);
		else
			seeker = null;
	}

	public boolean possible()
	{
		return (fMovingA == null || !fMovingA.contains(end)) && seeker.len >= 0;
	}

	@Override
	public int cost()
	{
		return costs.initMove();
	}

	@Override
	public int mCost()
	{
		return (extraCost ? costs.initMoveM() : 0) + (seeker != null ? costs.moveCostM() * seeker.len : 0) + costs.turnCost(from, mvDir);
	}

	@Override
	public boolean extraCost()
	{
		return true;
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
			xec1.setUsedFirstMovement();
			xec0.setDirection(mvDir, new AC());
			xec0.setLoc(end, new AC());
			return false;
		}
		return true;
	}
}