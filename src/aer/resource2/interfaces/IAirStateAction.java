package aer.resource2.interfaces;

import aer.locate.*;

public interface IAirStateAction extends IDirectionAction
{
	default AirState airState()
	{
		return null;
	}

	default int reqFallReduction()
	{
		return 0;
	}
}