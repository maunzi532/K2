package aer.path;

import aer.path.takeable.*;

public interface EndHexItem extends HexItem
{
	TakeableAction endAction(ActionResource resource);
}