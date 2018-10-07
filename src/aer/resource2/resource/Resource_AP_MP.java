package aer.resource2.resource;

import aer.locate.*;
import aer.path.pather.*;
import aer.path.takeable.*;
import aer.relocatable.*;
import aer.resource2.interfaces.*;
import java.util.*;

public class Resource_AP_MP implements ActionResource, R_AP_MP, R_Relocatable, R_FallData
{
	private final boolean end;
	private final int actionPoints;
	private final int movePoints;
	private final int requiresExtraAP;
	private final int requiresExtraMP;
	private final HexDirection direction;
	private final AirState airState;
	private final int requiredFall;
	private final HexLocation location;
	private final int freelyMovingBlocked;
	private final Relocatable mountedTo;
	private final int mountedToSlot;
	private final boolean mountedThisTurn;
	private final boolean error;

	public Resource_AP_MP(int actionPoints, int movePoints, int requiresExtraAP, int requiresExtraMP,
			HexDirection direction, AirState airState,
			int requiredFall, HexLocation location, Relocatable mountedTo, int mountedToSlot, boolean mountedThisTurn)
	{
		end = false;
		this.actionPoints = actionPoints;
		this.movePoints = movePoints;
		this.requiresExtraAP = requiresExtraAP;
		this.requiresExtraMP = requiresExtraMP;
		this.direction = direction;
		this.airState = airState;
		this.requiredFall = requiredFall;
		this.location = location;
		freelyMovingBlocked = 0;
		this.mountedTo = mountedTo;
		this.mountedToSlot = mountedToSlot;
		this.mountedThisTurn = mountedThisTurn;
		error = actionPoints < 0 || movePoints < 0;
	}

	public Resource_AP_MP(boolean end, int actionPoints, int movePoints, int requiresExtraAP,
			int requiresExtraMP, HexDirection direction, AirState airState,
			int requiredFall, HexLocation location, int freelyMovingBlocked,
			Relocatable mountedTo, int mountedToSlot, boolean mountedThisTurn, boolean error)
	{
		this.end = end;
		this.actionPoints = actionPoints;
		this.movePoints = movePoints;
		this.requiresExtraAP = requiresExtraAP;
		this.requiresExtraMP = requiresExtraMP;
		this.direction = direction;
		this.airState = airState;
		this.requiredFall = requiredFall;
		this.location = location;
		this.freelyMovingBlocked = freelyMovingBlocked;
		this.mountedTo = mountedTo;
		this.mountedToSlot = mountedToSlot;
		this.mountedThisTurn = mountedThisTurn;
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
		int movePoints1 = movePoints;
		int extraAP1 = requiresExtraAP;
		int extraMP1 = requiresExtraMP;
		HexDirection direction1 = direction;
		AirState airState1 = airState;
		int requiredFall1 = requiredFall;
		HexLocation location1 = location;
		int freelyMovingBlocked1 = freelyMovingBlocked == 1 ? 2 : freelyMovingBlocked;
		Relocatable mountedTo1 = mountedTo;
		int mountedToSlot1 = mountedToSlot;
		boolean hasMT1 = mountedThisTurn;
		boolean error1 = error || end;
		if(action instanceof IMainAction && ((IMainAction) action).end())
			end1 = true;
		if(action instanceof IAPAction)
		{
			actionPoints1 = actionPoints - ((IAPAction) action).cost();
			if(((IAPAction) action).extraCost())
				extraAP1 = -1;
		}
		if(action instanceof IDirectionAction)
		{
			movePoints1 = movePoints - ((IDirectionAction) action).mCost();
			if(((IDirectionAction) action).extraCostM())
				extraMP1 = -1;
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
			if(hasMT1)
				error1 = true;
			Relocatable mounting1 = ((IMountAction) action).mounting();
			int mountingToSlot1 = ((IMountAction) action).mountingToSlot();
			if(mounting1 != null)
			{
				mountedTo1 = mounting1;
				mountedToSlot1 = mountingToSlot1;
				hasMT1 = true;
			}
			if(((IMountAction) action).dismounting())
			{
				mountedTo1 = null;
				mountedToSlot1 = 0;
			}
		}
		if(mountedTo1 != null && !mountedTo1.getMountSlotInfo(mountedToSlot1).allowRotating && !direction1.equals(mountedTo1.getDirection()))
			error1 = true;
		return new Resource_AP_MP(end1, actionPoints1, movePoints1, extraAP1, extraMP1, direction1,
				airState1, requiredFall1, location1, freelyMovingBlocked1, mountedTo1, mountedToSlot1, hasMT1, error1);
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
	public int dRequiresExtraAP()
	{
		return requiresExtraAP;
	}

	@Override
	public int dRequiresExtraMP()
	{
		return requiresExtraMP;
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
	public boolean dMTTUsed()
	{
		return mountedThisTurn;
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
		return "Resource_AP_MP{" +
				"end=" + end +
				", actionPoints=" + actionPoints +
				", movePoints=" + movePoints +
				", requiresExtraAP=" + requiresExtraAP +
				", requiresExtraMP=" + requiresExtraMP +
				", direction=" + direction +
				", airState=" + airState +
				", requiredFall=" + requiredFall +
				", location=" + location +
				", freelyMovingBlocked=" + freelyMovingBlocked +
				", mountedTo=" + mountedTo +
				", mountedToSlot=" + mountedToSlot +
				", mountedThisTurn=" + mountedThisTurn +
				", error=" + error +
				'}';
	}
}