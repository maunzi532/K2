package aer.path.team;

import aer.path.*;
import aer.path.takeable.*;

public interface NPC_Control
{
	PathAction path(Pather xec);

	TakeableAction interrupt(Pather xec, TargetData targetData);

	Reaction reaction(TargetData targetData);
}