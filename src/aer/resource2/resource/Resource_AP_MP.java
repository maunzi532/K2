package aer.resource2.resource;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.interfaces.*;
import java.util.*;

public class Resource_AP_MP implements ActionResource, R_AP_MP, R_Relocatable, R_FallData
{
	private final boolean end;
	private final int actionPoints;
	private final int movePoints;
	private final HexDirection direction;
	private final AirState airState;
	private final int requiredFall;
	private final HexLocation location;
	private final int freelyMovingBlocked;
	private final Relocatable mountedTo;
	private final int mountedToSlot;
	private final boolean hasMD;
	private final boolean error;

	public Resource_AP_MP(int actionPoints, int movePoints, HexDirection direction, AirState airState,
			int requiredFall, HexLocation location, Relocatable mountedTo, int mountedToSlot)
	{
		this.actionPoints = actionPoints;
		this.movePoints = movePoints;
		this.direction = direction;
		this.airState = airState;
		this.requiredFall = requiredFall;
		this.location = location;
		freelyMovingBlocked = 0;
		this.mountedTo = mountedTo;
		this.mountedToSlot = mountedToSlot;
		hasMD = false;
		end = false;
		error = actionPoints < 0 || movePoints < 0;
	}

	public Resource_AP_MP(boolean end, int actionPoints, int movePoints, HexDirection direction, AirState airState,
			int requiredFall, HexLocation location, int freelyMovingBlocked,
			Relocatable mountedTo, int mountedToSlot, boolean hasMD, boolean error)
	{
		this.end = end;
		this.actionPoints = actionPoints;
		this.movePoints = movePoints;
		this.direction = direction;
		this.airState = airState;
		this.requiredFall = requiredFall;
		this.location = location;
		this.freelyMovingBlocked = freelyMovingBlocked;
		this.mountedTo = mountedTo;
		this.mountedToSlot = mountedToSlot;
		this.hasMD = hasMD;
		this.error = error || actionPoints < 0 || movePoints < 0;
	}

	@Override
	public boolean okay()
	{
		return !error;
	}

	@Override
	public ActionResource deduct(TakeableAction action)
	{
		boolean end1 = end;
		int actionPoints1 = actionPoints;
		HexDirection direction1 = direction;
		int movePoints1 = movePoints;
		AirState airState1 = airState;
		int requiredFall1 = requiredFall;
		HexLocation location1 = location;
		int freelyMovingBlocked1 = freelyMovingBlocked == 1 ? 2 : freelyMovingBlocked;
		Relocatable mountedTo1 = mountedTo;
		int mountedToSlot1 = mountedToSlot;
		boolean hasMD1 = hasMD;
		boolean error1 = error || end;
		if(action instanceof IMainAction && ((IMainAction) action).end())
			end1 = true;
		if(action instanceof IAPAction)
			actionPoints1 = actionPoints - ((IAPAction) action).cost();
		if(action instanceof IDirectionAction)
		{
			movePoints1 = movePoints - ((IDirectionAction) action).mCost();
			direction1 = Optional.ofNullable(((IDirectionAction) action).lookDirection()).orElse(direction);
		}
		if(action instanceof IAirStateAction)
		{
			airState1 = Optional.ofNullable(((IAirStateAction) action).airState()).orElse(airState);
			int fallReduction = ((IAirStateAction) action).reqFallReduction();
			requiredFall1 = requiredFall - fallReduction;
			if(fallReduction < 0 || requiredFall1 < 0)
				requiredFall1 = 0;
		}
		if(action instanceof IMovementAction)
		{
			location1 = Optional.ofNullable(((IMovementAction) action).movesTo()).orElse(location);
			boolean freelyMoving = ((IMovementAction) action).freelyMovingN() != null;
			if(freelyMoving)
			{
				if(freelyMovingBlocked == 0)
					freelyMovingBlocked1 = 1;
				if(freelyMovingBlocked == 2)
					error1 = true;
			}
		}
		if(action instanceof IMountAction)
		{
			Relocatable mounting1 = ((IMountAction) action).mounting();
			int mountingToSlot1 = ((IMountAction) action).mountingToSlot();
			boolean block = false;
			if(mounting1 != null)
			{
				mountedTo1 = mounting1;
				mountedToSlot1 = mountingToSlot1;
				block = true;
			}
			if(((IMountAction) action).dismounting())
			{
				mountedTo1 = null;
				mountedToSlot1 = 0;
				block = true;
			}
			if(block)
			{
				if(hasMD1)
					error1 = true;
				else
					hasMD1 = true;
			}
		}
		if(mountedTo1 != null && !mountedTo1.getMountSlotInfo(mountedToSlot1).allowRotating && !direction1.equals(mountedTo1.getDirection()))
			error1 = true;
		return new Resource_AP_MP(end1, actionPoints1, movePoints1, direction1,
				airState1, requiredFall1, location1, freelyMovingBlocked1, mountedTo1, mountedToSlot1, hasMD1, error1);
	}

	@Override
	public int dActionPoints()
	{
		return actionPoints;
	}

	@Override
	public int dMovementPoints()
	{
		return movePoints;
	}

	@Override
	public HexLocation dLocation()
	{
		return location;
	}

	@Override
	public HexDirection dDirection()
	{
		return direction;
	}

	@Override
	public AirState dAirState()
	{
		return airState;
	}

	@Override
	public Relocatable dMount()
	{
		return mountedTo;
	}

	@Override
	public boolean dMDUsed()
	{
		return hasMD;
	}

	@Override
	public boolean dEnd()
	{
		return end;
	}

	@Override
	public int dRequiredFall()
	{
		return requiredFall;
	}

	@Override
	public String toString()
	{
		return "BasicAPResource2{" +
				"location=" + location +
				", direction=" + direction +
				", airState=" + airState +
				", mountedTo=" + mountedTo +
				", requiredFall=" + requiredFall +
				", actionPoints=" + actionPoints +
				", movePoints=" + movePoints +
				", hasMD=" + hasMD +
				", end=" + end +
				", error=" + error +
				'}';
	}
}