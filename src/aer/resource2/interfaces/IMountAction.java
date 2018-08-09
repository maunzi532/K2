package aer.resource2.interfaces;

import aer.*;

public interface IMountAction extends IAirStateAction
{
	default Relocatable mounting()
	{
		return null;
	}

	default boolean dismounting()
	{
		return false;
	}
}