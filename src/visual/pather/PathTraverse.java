package visual.pather;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.resourceTypes.*;
import java.util.*;
import java.util.stream.*;
import visual.map.*;

public class PathTraverse
{
	private static final HexDirection eins = new HexDirection(1);

	public final List<PathAction> possibleActions;
	public PathAction currentAction;
	public HexPather pather;
	public RBasicData res1;
	public HexDirection turn;
	public HexLocation loc;
	public HexObject object;
	public int choiceNum;
	public List choiceOptions;
	public boolean esc = false;

	public PathTraverse(List<PathAction> possibleActions, HexPather pather, boolean initial)
	{
		this.possibleActions = possibleActions;
		this.pather = pather;
		if(initial)
			setCurrentAction(possibleActions.get(0));
		else
			setCurrentAction(null);
	}

	public void setCurrentAction(PathAction currentAction)
	{
		this.currentAction = currentAction;
		if(currentAction != null && currentAction.deducted instanceof RBasicData)
			res1 = (RBasicData) currentAction.deducted;
		updateChoiceOptions();
	}

	public PathAction exec(Input1 input1, HexLocation tLoc, HexObject tObject, boolean autoTake)
	{
		if(input1 == null)
			return null;
		switch(input1)
		{
			case CHOOSE:
				if(tObject != null && tObject != object)
				{
					chReset();
					if(tObject != pather)
						object = tObject;
					updateChoiceOptions();
				}
				else if(tLoc != null && !tLoc.equals(res1.dLocation()) && !tLoc.equals(loc))
				{
					chReset();
					loc = tLoc;
					updateChoiceOptions();
				}
				else if(choiceNum >= 0 && choiceOptions != null && choiceOptions.size() > choiceNum)
				{
					chReset();
					pathIn((TakeableAction) choiceOptions.get(choiceNum));
					if(autoTake)
					{
						if(canEnd())
							return currentAction;
						else
							esc = true;
					}
				}
				break;
			case CHANGE:
				choiceNum++;
				if(choiceNum >= choiceOptions.size())
					choiceNum = 0;
				if(choiceOptions.isEmpty())
					choiceNum = -1;
				showChoiceOptions();
				break;
			case ACCEPT:
				if(canEnd())
				{
					System.out.println("Chosen path:");
					System.out.print(steps(currentAction));
					return currentAction;
				}
				break;
			case BACK:
				if(currentAction != null && currentAction.previous != null)
				{
					chReset();
					setCurrentAction(currentAction.previous);
				}
				else
				{
					System.out.println("Exiting");
					esc = true;
				}
				break;
			case ESCAPE:
				System.out.println("Exiting");
				esc = true;
				break;
			case MINUSD:
				object = null;
				loc = null;
				if(turn == null)
					turn = new HexDirection(11);
				else if(turn.r == 1)
					turn = null;
				else
					turn = HexDirection.minus(turn, eins);
				updateChoiceOptions();
				break;
			case PLUSD:
				object = null;
				loc = null;
				if(turn == null)
					turn = new HexDirection(1);
				else if(turn.r == 11)
					turn = null;
				else
					turn = HexDirection.plus(turn, eins);
				updateChoiceOptions();
				break;
		}
		return null;
	}

	private void chReset()
	{
		object = null;
		loc = null;
		turn = null;
	}

	public void updateChoiceOptions()
	{
		if(object != null)
		{
			System.out.println(object.id);
			if(!objects().keySet().isEmpty())
				System.out.println(objects().keySet().iterator().next().id);
			choiceOptions = objects().get(object);
		}
		else if(loc != null)
			choiceOptions = locations().get(loc);
		else if(turn != null)
			choiceOptions = directions().get(HexDirection.plus(res1.dDirection(), turn));
		else
			choiceOptions = others();
		choiceNum = 0;
		if(choiceOptions == null)
			choiceOptions = Collections.EMPTY_LIST;
		if(choiceOptions.isEmpty())
			choiceNum = -1;
		showChoiceOptions();
	}

	public List<PathAction> nextActions()
	{
		if(currentAction == null)
			return possibleActions;
		else
			return currentAction.next;
	}

	public boolean canEnd()
	{
		return currentAction != null && currentAction.action.canBeFinalAction();
	}

	public void pathIn(TakeableAction action)
	{
		setCurrentAction(nextActions().stream().filter(e -> e.action == action).findFirst().orElse(currentAction));
		//System.out.println("WUGU" + currentAction.action.getClass().getSimpleName());
	}

	public Map<HexDirection, List<TActionDirection>> directions()
	{
		return nextActions().stream().filter(e -> e.action instanceof TActionDirection).map(e -> (TActionDirection) e.action).collect(
				Collectors.groupingBy(TActionDirection::direction));
	}

	public Map<HexLocation, List<TActionLocation>> locations()
	{
		return nextActions().stream().filter(e -> e.action instanceof TActionLocation).map(e -> (TActionLocation) e.action).collect(
				Collectors.groupingBy(TActionLocation::location));
	}

	public Map<HexObject, List<TActionObject>> objects()
	{
		return nextActions().stream().filter(e -> e.action instanceof TActionObject).map(e -> (TActionObject) e.action).collect(
				Collectors.groupingBy(TActionObject::object));
	}

	public List<TActionOther> others()
	{
		return nextActions().stream().filter(e -> e.action instanceof TActionOther).map(e -> (TActionOther) e.action).collect(Collectors.toList());
	}

	private void showChoiceOptions()
	{
		System.out.print(steps(currentAction));
		if(canEnd())
			System.out.println("> enter key to take this path");
		System.out.println(object != null ? "Actions targeting object:" :
				loc != null ? "Actions targeting location:" :
						turn != null ? "Actions targeting direction:" :
								"Actions:");
		if(choiceOptions.isEmpty())
			System.out.println("None");
		else
		{
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < choiceOptions.size(); i++)
				sb.append(i == choiceNum ? "> " : "| ").append(choiceOptions.get(i).getClass().getSimpleName()).append("\n");
			System.out.print(sb.toString());
		}
	}

	public static String steps(PathAction pathAction)
	{
		if(pathAction == null)
			return "";
		List<TakeableAction> actions = new ArrayList<>();
		PathAction.pathToList(pathAction, actions);
		StringBuilder sb = new StringBuilder();
		for(TakeableAction action : actions)
			sb.append("-> ").append(action.getClass().getSimpleName()).append("\n");
		return sb.toString();
	}
}