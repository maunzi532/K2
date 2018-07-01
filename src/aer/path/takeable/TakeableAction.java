package aer.path.takeable;

import aer.path.*;
import java.util.*;

public interface TakeableAction
{
	default boolean canBeFinalAction()
	{
		return true;
	}

	default boolean executeStart(HexPather xec)
	{
		return false;
	}

	default List<HexPather> targets(HexPather xec)
	{
		return Collections.EMPTY_LIST;
	}

	default List<Reaction> targetOptions(HexPather xec, HexPather target)
	{
		return null;
	}

	default List<Integer> interruptTeamNumbers(HexPather xec, HexPather target)
	{
		return Collections.singletonList(target.getTherathicHex().teamSide());
	}

	default boolean executeOn(HexPather xec, HexPather target, Reaction chosen)
	{
		return false;
	}

	default boolean executeEnd(HexPather xec)
	{
		return false;
	}
}