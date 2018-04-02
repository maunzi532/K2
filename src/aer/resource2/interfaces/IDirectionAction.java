package aer.resource2.interfaces;

import aer.*;

public interface IDirectionAction
{
	int mCost();

	default HexDirection lookDirection()
	{
		return null;
	}
}