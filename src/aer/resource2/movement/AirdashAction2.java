package aer.resource2.movement;

import aer.*;
import aer.commands.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.path.team.*;
import aer.resource2.interfaces.*;
import aer.resource2.therathicTypes.*;

public class AirdashAction2 implements TActionLocation, IMovementAction, IAPAction, IThAP
{
	private final CostTable costs;
	private final IHexMap map;
	private final HexLocation start;
	private final HexLocation end1;
	private final HexDirection direction;
	private final AirState airState;
	private final HexLocation end2;

	public AirdashAction2(CostTable costs, IHexMap map, HexLocation start, HexLocation end1,
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
	public HexLocation location()
	{
		return end2;
	}

	@Override
	public int reqFallReduction()
	{
		return 1;
	}

	/*@Override
	public boolean executeEnd(HexPather xec)
	{
		if(xec.getTherathicHex() instanceof E_AP_MP &&
				((E_AP_MP) xec.getTherathicHex()).useAPMP(this, this, E_AP_MP.Use.REAL))
		{
			xec.setLoc(end2);
			xec.setAirState(airState);
			return false;
		}
		return true;
	}*/

	@Override
	public boolean executeEnd(HexPather xec0, TherathicHex xec1, E_AP_MP xec2)
	{
		if(xec2.useAPMP(this, this, E_AP_MP.Use.REAL))
		{
			xec0.setLoc(end2);
			xec0.setAirState(airState);
			CMove.issueCommand(xec0);
			return false;
		}
		return true;
	}
}