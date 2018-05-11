package aer.path;

import aer.path.takeable.*;
import aer.path.team.*;

public interface EndHexItem extends HexItem
{
	TakeableAction endAction(ActionResource resource, TherathicHex therathicHex);
}