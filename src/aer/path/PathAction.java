package aer.path;

import aer.path.takeable.*;
import java.util.*;

public class PathAction
{
	public final Pather pather;
	public final TakeableAction action;
	public final PathAction previous;
	public final ActionResource deducted;
	public final ArrayList<PathAction> next = new ArrayList<>();

	public PathAction(Pather pather, ActionResource resource, TakeableAction action, PathAction previous)
	{
		this.pather = pather;
		this.action = action;
		this.previous = previous;
		deducted = resource.deduct(action);
	}

	public PathAction(Pather pather, ActionResource resource, TakeableAction action)
	{
		this.pather = pather;
		this.action = action;
		this.previous = null;
		deducted = resource.deduct(action);
	}

	public static void pathToList(PathAction pathAction, List<TakeableAction> actions1)
	{
		if(pathAction.previous != null)
			pathToList(pathAction.previous, actions1);
		actions1.add(pathAction.action);
	}
}