package aer.path.team;

import aer.path.*;
import aer.path.takeable.*;
import java.io.*;

public interface NPC_Control extends Serializable
{
	PathAction path(Pather xec);

	TakeableAction interrupt(Pather xec, TargetData targetData);

	Reaction reaction(TargetData targetData);
}