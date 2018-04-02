package aer.resource2.interfaces;

import aer.*;

public interface IMountAction extends IAirStateAction
{
	default HexObject mounting()
	{
		return null;
	}

	default boolean dismounting()
	{
		return false;
	}
}