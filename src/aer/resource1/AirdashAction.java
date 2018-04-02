package aer.resource1;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;

public class AirdashAction implements APAction, TActionLocation
{
	private final CostTable costs;
	private final IHexMap map;
	private final HexLocation start;
	private final HexLocation end1;
	private final HexDirection direction;
	private final AirState airState;
	private final HexLocation end2;

	public AirdashAction(CostTable costs, IHexMap map, HexLocation start, HexLocation end1,
			HexDirection direction, AirState airState)
	{
		this.costs = costs;
		this.map = map;
		this.start = start;
		this.end1 = end1;
		this.direction = direction;
		this.airState = airState;
		end2 = new HexLocation(end1, 0, 0, airState.fall, 0);
	}

	public boolean possible()
	{
		return map.getTile(end2).type != MapTileType.BLOCKED;
	}

	@Override
	public int cost()
	{
		return costs.initAD + HexLocation.xdzDifference(start, end1) * costs.adMove;
	}

	@Override
	public int mCost()
	{
		return costs.initADM + HexLocation.xdzDifference(start, end1) * costs.adMoveM;
	}

	@Override
	public HexLocation movesTo()
	{
		return end2;
	}

	@Override
	public AirState airState()
	{
		return AirState.DOWN;
	}

	@Override
	public int falls()
	{
		return 1;
	}

	@Override
	public HexLocation location()
	{
		return end2;
	}
}