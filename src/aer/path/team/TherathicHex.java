package aer.path.team;

import aer.path.*;
import aer.path.takeable.*;
import java.util.*;

public interface TherathicHex
{
	void linkTo(HexPather pather);

	List<HexItem> activeItems(ItemGetType type, HexPather toDef);

	TakeableAction startAction(ItemGetType type, HexPather toDef);

	ActionResource actionResource();

	void drawPhase();

	int teamSide();

	boolean playerControlled();

	enum ItemGetType
	{
		ACTION,
		INTERRUPT,
		END
	}
}