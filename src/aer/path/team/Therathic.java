package aer.path.team;

import aer.path.pather.*;
import aer.path.schedule.*;
import aer.path.takeable.*;
import aer.relocatable.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

public interface Therathic extends Serializable
{
	void linkTo(Pather pather);

	Pather pather();

	MountSlotInfo[] mountSlotInfo();

	List<PatherItem> activeItems();

	List<PatherItem> interruptItems(TargetData targetData);

	List<EndPatherItem> endItems();

	TakeableAction startAction();

	void setUsedFirstPath();

	void setUsedFirstMovement();

	void setMountThisTurnUsed();

	ActionResource actionResource();

	AIValue aiValue();

	boolean drawPhase();

	PathAction endPhase();

	int teamSide();

	boolean playerControlled();

	default void mountSlotUpdateInfo(){}

	NPC_Control npcControl();

	default List<PathAction> possibleActivePaths()
	{
		ArrayList<PathAction> possiblePaths = new ArrayList<>();
		PathAction start = new PathAction(pather(), actionResource(), aiValue(), startAction());
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
			for(PatherItem item : activeItems())
			{
				currentPath.next.addAll(item.takeableActions(currentPath).stream()
						.map(action -> new PathAction(pather(), currentPath.deducted, currentPath.aiValue, action, currentPath))
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
		for(PatherItem item : interruptItems(targetData))
		{
			item.interrupts(targetData).stream().filter(e -> actionResource.deduct(e).okay()).forEach(takeableActions::add);
		}
		return takeableActions.stream().map(e -> new PathAction(pather(), actionResource(), aiValue(), e)).collect(Collectors.toList());
	}

	default PathAction endPath()
	{
		List<EndPatherItem> endItems = endItems();
		PathAction pathAction = null;
		ActionResource actionResource = actionResource();
		for(EndPatherItem endHexItem : endItems)
		{
			while(true)
			{
				TakeableAction endAction = endHexItem.endAction(actionResource, this);
				if(endAction != null)
				{
					if(pathAction == null)
						pathAction = new PathAction(pather(), actionResource, null, endAction);
					else
						pathAction = new PathAction(pather(), actionResource, null, endAction, pathAction);
					actionResource = pathAction.deducted;
				}
				else
					break;
			}
		}
		return pathAction;
	}
}