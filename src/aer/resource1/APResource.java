package aer.resource1;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import java.util.*;

public class APResource implements ActionResource
{
	public final HexLocation location;
	public final HexDirection direction;
	public final AirState airState;
	public final HexObject mount;
	public final int requiredFall;
	public final int actionPoints;
	public final int movePoints;
	public final boolean hasMounted;
	public final boolean hasDismounted;
	public final boolean dizzy;
	public final boolean error;

	public APResource(HexLocation location, HexDirection direction, AirState airState, HexObject mount, int requiredFall, int actionPoints, int movePoints)
	{
		this.location = location;
		this.direction = direction;
		this.airState = airState;
		this.mount = mount;
		this.requiredFall = requiredFall;
		this.actionPoints = actionPoints;
		this.movePoints = movePoints;
		hasMounted = false;
		hasDismounted = false;
		dizzy = false;
		error = actionPoints < 0 || movePoints < 0;
	}

	public APResource(HexLocation location, HexDirection direction, AirState airState, HexObject mount, int requiredFall, int actionPoints, int movePoints,
			boolean hasMounted, boolean hasDismounted, boolean dizzy, boolean error)
	{
		this.location = location;
		this.direction = direction;
		this.airState = airState;
		this.mount = mount;
		this.requiredFall = requiredFall;
		this.actionPoints = actionPoints;
		this.movePoints = movePoints;
		this.hasMounted = hasMounted;
		this.hasDismounted = hasDismounted;
		this.dizzy = dizzy;
		this.error = error || actionPoints < 0 || movePoints < 0;
	}

	@Override
	public boolean okay()
	{
		return !error;
	}

	@Override
	public APResource deduct(TakeableAction action)
	{
		APAction action1 = (APAction) action;
		Optional<HexLocation> newLoc = Optional.ofNullable(action1.movesTo());
		Optional<HexDirection> newDir = Optional.ofNullable(action1.lookDirection());
		Optional<AirState> newAir = Optional.ofNullable(action1.airState());
		int newFall = requiredFall - action1.falls();
		int newAP = actionPoints - action1.cost();
		int newMP = movePoints - action1.mCost();
		Optional<HexObject> mounting = Optional.ofNullable(action1.mounting());
		boolean dismounting = action1.dismounting();
		return new APResource(newLoc.orElse(location), newDir.orElse(direction), newAir.orElse(airState),
				dismounting ? null : mounting.orElse(mount), newFall, newAP, newMP, hasMounted || mounting.isPresent(),
				hasDismounted || dismounting, dizzy || action1.dizzy(),
				(hasMounted && mounting.isPresent()) || (hasDismounted && dismounting) || (dizzy && action1.dizzy()));
	}

	@Override
	public String toString()
	{
		return "APResource{" +
				"location=" + location +
				", direction=" + direction +
				", airState=" + airState +
				", mount=" + mount +
				", requiredFall=" + requiredFall +
				", actionPoints=" + actionPoints +
				", movePoints=" + movePoints +
				", hasMounted=" + hasMounted +
				", hasDismounted=" + hasDismounted +
				", dizzy=" + dizzy +
				", error=" + error +
				'}';
	}
}