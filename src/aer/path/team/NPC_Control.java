package aer.path.team;

import aer.path.*;
import aer.path.takeable.*;

public interface NPC_Control
{
	PathAction path(HexPather xec);

	TakeableAction interrupt(HexPather xec, TargetData targetData);

	Reaction reaction(TargetData targetData);
}