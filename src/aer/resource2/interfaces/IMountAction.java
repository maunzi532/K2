package aer.resource2.interfaces;

import aer.*;

public interface IMountAction extends IAirStateAction
{
	default Relocatable mounting()
	{
		return null;
	}

	default int mountingToSlot()
	{
		return 0;
	}

	default boolean dismounting()
	{
		return false;
	}
}