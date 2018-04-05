package aer.resource2.movement;

import aer.*;
import aer.commands.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicTypes.*;
import java.util.*;

public class FloorMovementAction2 implements TActionLocation, IMovementAction, IAPAction
{
	private final CostTable costs;
	private final IHexMap map;
	private final HexLocation start;
	private final HexLocation end;
	private final HexDirection from;
	private final HexDirection mvDir;
	private final List<HexLocation> fMovingA;
	private final List<HexLocation> fMovingN;

	public FloorMovementAction2(CostTable costs, HexDirection from, IHexMap map, HexLocation start, HexLocation end,
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
		if(fMovingA != null && fMovingA.contains(end))
			return false;
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
	public boolean executeEnd(HexPather xec)
	{
		if(xec.getTherathicHex() instanceof EActionPoints &&
				((EActionPoints) xec.getTherathicHex()).useAPMP(this, this, true))
		{
			xec.setDirection(mvDir);
			xec.addCommand(new CTurn(mvDir));
			xec.setLoc(end);
			xec.addCommand(new CMove(end));
			return false;
		}
		return true;
	}
}