package aer.path.team;

import aer.path.*;
import aer.path.takeable.*;
import java.util.*;

public interface NPC_Control
{
	PathAction path(HexPather xec);

	TakeableAction interrupt(HexPather xec, TakeableAction action1, HexPather a1xec, HexPather target);

	Reaction reaction(HexPather xec, TakeableAction action1, HexPather a1xec, List<Reaction> reactions);
}