package aer.resource2.interfaces;

import aer.*;

public interface ITargetedAction extends IDirectionAction
{
	default Relocatable target()
	{
		return null;
	}
}