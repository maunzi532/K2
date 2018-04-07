package aer.path.team;

import aer.path.*;
import aer.path.takeable.*;
import java.util.*;

public interface TherathicHex
{
	void linkTo(HexPather pather);

	List<HexItem> activeItems(ItemGetType type, TargetData targetData);

	TakeableAction startAction(ItemGetType type);

	ActionResource actionResource();

	boolean drawPhase();

	PathAction endPhase();

	int teamSide();

	boolean playerControlled();

	NPC_Control npcControl();

	enum ItemGetType
	{
		ACTION,
		INTERRUPT,
		END
	}
}