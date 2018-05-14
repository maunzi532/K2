package aer.path.team;

import aer.path.*;
import aer.path.takeable.*;
import java.util.*;
import java.util.stream.*;

public interface TherathicHex
{
	void linkTo(HexPather pather);

	HexPather pather();

	List<HexItem> activeItems();

	List<HexItem> interruptItems(TargetData targetData);

	List<EndHexItem> endItems();

	TakeableAction startAction();

	ActionResource actionResource();

	boolean drawPhase();

	PathAction endPhase();

	int teamSide();

	boolean playerControlled();

	NPC_Control npcControl();

	default List<PathAction> possibleActivePaths()
	{
		ArrayList<PathAction> possiblePaths = new ArrayList<>();
		PathAction start = new PathAction(pather(), actionResource(), startAction());
		if(!start.deducted.okay())
			return possiblePaths;
		possiblePaths.add(start);
		for(int i = 0; i < possiblePaths.size(); i++)
		{
			if(i > 5000)
			{
				possiblePaths.forEach(e -> System.out.println(e.deducted));
				throw new RuntimeException("Pathcount > 5000");
			}
			PathAction currentPath = possiblePaths.get(i);
			for(HexItem item : activeItems())
			{
				currentPath.next.addAll(item.takeableActions(currentPath).stream()
						.map(action -> new PathAction(pather(), currentPath.deducted, action, currentPath))
						.filter(path -> path.deducted.okay()).collect(Collectors.toList()));
			}
			possiblePaths.addAll(currentPath.next);
		}
		return possiblePaths;
	}

	default List<PathAction> possibleInterrupts(TargetData targetData)
	{
		ArrayList<TakeableAction> takeableActions = new ArrayList<>();
		ActionResource actionResource = actionResource();
		for(HexItem item : interruptItems(targetData))
		{
			item.interrupts(targetData).stream().filter(e -> actionResource.deduct(e).okay()).forEach(takeableActions::add);
		}
		return takeableActions.stream().map(e -> new PathAction(pather(), actionResource(), e)).collect(Collectors.toList());
	}

	default PathAction endPath()
	{
		List<EndHexItem> endItems = endItems();
		PathAction pathAction = null;
		ActionResource actionResource = actionResource();
		for(EndHexItem endHexItem : endItems)
		{
			while(true)
			{
				TakeableAction endAction = endHexItem.endAction(actionResource, this);
				if(endAction != null)
				{
					if(pathAction == null)
						pathAction = new PathAction(pather(), actionResource, endAction);
					else
						pathAction = new PathAction(pather(), actionResource, endAction, pathAction);
					actionResource = pathAction.deducted;
				}
				else
					break;
			}
		}
		return pathAction;
	}
}