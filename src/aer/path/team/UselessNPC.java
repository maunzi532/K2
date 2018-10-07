package aer.path.team;

import aer.path.pather.*;
import aer.path.schedule.*;
import aer.path.takeable.*;

public class UselessNPC implements NPC_Control
{
	@Override
	public PathAction path(Pather xec)
	{
		return null;
	}

	@Override
	public TakeableAction interrupt(Pather xec, TargetData targetData)
	{
		return null;
	}

	@Override
	public Reaction reaction(TargetData targetData)
	{
		return targetData.reactionOptions().get(0);
	}

	@Override
	public AIValue aiValue(Pather pather, ActionResource resource)
	{
		return null;
	}
}