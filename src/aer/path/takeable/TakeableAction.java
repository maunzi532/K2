package aer.path.takeable;

import aer.path.*;
import java.util.*;

public interface TakeableAction
{
	default boolean executeStart(HexPather xec)
	{
		return false;
	}

	default List<HexPather> targets(HexPather xec)
	{
		return Collections.EMPTY_LIST;
	}

	default List<String> targetOptions(HexPather xec, HexPather target)
	{
		throw new RuntimeException();
	}

	default boolean executeOn(HexPather xec, HexPather target, String chosenOption)
	{
		return false;
	}

	default boolean executeEnd(HexPather xec)
	{
		return false;
	}
}