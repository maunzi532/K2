package aer.resource2.interfaces;

import aer.locate.*;

public interface IDirectionAction
{
	int mCost();

	default boolean extraCostM()
	{
		return false;
	}

	default HexDirection lookDirection()
	{
		return null;
	}
}