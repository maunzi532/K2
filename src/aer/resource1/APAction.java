package aer.resource1;

import aer.*;
import aer.path.takeable.*;

public interface APAction extends TakeableAction
{
	int cost();

	int mCost();

	default HexObject mounting()
	{
		return null;
	}

	default boolean dismounting()
	{
		return false;
	}

	default HexLocation movesTo()
	{
		return null;
	}

	default HexDirection lookDirection()
	{
		return null;
	}

	default AirState airState()
	{
		return null;
	}

	default boolean dizzy()
	{
		return false;
	}

	default int falls()
	{
		return 0;
	}
}