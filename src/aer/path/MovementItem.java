package aer.path;

import aer.*;
import aer.path.takeable.*;
import java.util.*;

public abstract class MovementItem implements HexItem
{
	@Override
	public List<TakeableAction> takeableActions(PathAction pathAction)
	{
		IHexMap map = pathAction.pather.map;
		HexLocation loc = pathAction.pather.getLoc();
		return null;
	}
}