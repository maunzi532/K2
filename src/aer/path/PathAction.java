package aer.path;

import aer.path.takeable.*;
import java.util.*;

public class PathAction
{
	public final HexPather pather;
	public final TakeableAction action;
	public final PathAction previous;
	public final ActionResource deducted;
	public final ArrayList<PathAction> next = new ArrayList<>();

	public PathAction(HexPather pather, ActionResource resource, TakeableAction action, PathAction previous)
	{
		this.pather = pather;
		this.action = action;
		this.previous = previous;
		deducted = resource.deduct(action);
	}

	public PathAction(HexPather pather, ActionResource resource, TakeableAction action)
	{
		this.pather = pather;
		this.action = action;
		this.previous = null;
		deducted = resource.deduct(action);
	}
}