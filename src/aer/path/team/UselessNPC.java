package aer.path.team;

import aer.path.*;
import aer.path.takeable.*;

public class UselessNPC implements NPC_Control
{
	@Override
	public PathAction path(HexPather xec)
	{
		return null;
	}

	@Override
	public TakeableAction interrupt(HexPather xec, TargetData targetData)
	{
		return null;
	}

	@Override
	public Reaction reaction(TargetData targetData)
	{
		return targetData.reactionOptions().get(0);
	}
}