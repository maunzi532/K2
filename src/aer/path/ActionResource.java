package aer.path;

import aer.path.takeable.*;

public interface ActionResource
{
	boolean okay();

	ActionResource deduct(TakeableAction action);
}