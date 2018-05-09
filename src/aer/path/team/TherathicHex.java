package aer.path.team;

import aer.path.*;
import aer.path.takeable.*;
import java.util.*;

public interface TherathicHex
{
	void linkTo(HexPather pather);

	HexPather pather();

	List<HexItem> activeItems(ItemGetType type, TargetData targetData);

	TakeableAction startAction(ItemGetType type);

	ActionResource actionResource();

	boolean drawPhase();

	PathAction endPhase();

	int teamSide();

	boolean playerControlled();

	NPC_Control npcControl();

	/*static ArrayList<PathAction> calculatePossiblePaths(ItemGetType type, TargetData targetData, TherathicHex therathicHex, List<HexItem> items)
	{
		ArrayList<PathAction> possiblePaths = new ArrayList<>();
		PathAction start = new PathAction(therathicHex.pather(), therathicHex.actionResource(), therathicHex.startAction(type));
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
			for(HexItem item : items)
			{
				currentPath.next.addAll(item.takeableActions(currentPath).stream()
						.map(action -> new PathAction(therathicHex.pather(), currentPath.deducted, action, currentPath))
						.filter(path -> path.deducted.okay()).collect(Collectors.toList()));
			}
			possiblePaths.addAll(currentPath.next);
		}
		return possiblePaths;
	}*/

	/*static ArrayList<PathAction> calculateEndPath(TherathicHex therathicHex, HexItem item)
	{
		ArrayList<PathAction> possiblePaths = new ArrayList<>();
		PathAction start = new PathAction(therathicHex.pather(), therathicHex.actionResource(), therathicHex.startAction(ItemGetType.END));
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
			currentPath.next.addAll(item.takeableActions(currentPath).stream()
					.map(action -> new PathAction(therathicHex.pather(), currentPath.deducted, action, currentPath))
					.filter(path -> path.deducted.okay()).collect(Collectors.toList()));
			possiblePaths.addAll(currentPath.next);
		}
		return possiblePaths;
	}*/
}