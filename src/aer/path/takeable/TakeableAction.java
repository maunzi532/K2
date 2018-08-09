package aer.path.takeable;

import aer.path.*;
import java.util.*;

public interface TakeableAction
{
	default boolean canBeFinalAction()
	{
		return true;
	}

	default boolean executeStart(Pather xec)
	{
		return false;
	}

	default List<Pather> targets(Pather xec)
	{
		return Collections.EMPTY_LIST;
	}

	default List<Reaction> targetOptions(Pather xec, Pather target)
	{
		return null;
	}

	default List<Integer> interruptTeamNumbers(Pather xec, Pather target)
	{
		return Collections.singletonList(target.getTherathic().teamSide());
	}

	default boolean executeOn(Pather xec, Pather target, Reaction chosen)
	{
		return false;
	}

	default boolean executeEnd(Pather xec)
	{
		return false;
	}
}