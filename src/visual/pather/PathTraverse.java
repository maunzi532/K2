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
	public RBasicData res1;
	public HexDirection turn;
	public HexLocation loc;
	public HexObject object;
	public int choiceNum;
	public List choiceOptions;
	public boolean esc = false;

	public PathTraverse(List<PathAction> possibleActions)
	{
		this.possibleActions = possibleActions;
		setCurrentAction(possibleActions.get(0));
	}

	public void setCurrentAction(PathAction currentAction)
	{
		this.currentAction = currentAction;
		if(currentAction.deducted instanceof RBasicData)
			res1 = (RBasicData) currentAction.deducted;
		updateChoiceOptions();
	}

	public PathAction exec(Input1 input1, HexLocation tLoc, HexObject tObject)
	{
		if(input1 == null)
			return null;
		switch(input1)
		{
			case CHOOSE:
				if(tObject != null && tObject != currentAction.pather && tObject != object)
				{
					object = tObject;
					updateChoiceOptions();
				}
				else if(tLoc != null && !tLoc.equals(res1.dLocation()) && !tLoc.equals(loc))
				{
					loc = tLoc;
					updateChoiceOptions();
				}
				else if(choiceNum >= 0 && choiceOptions != null && choiceOptions.size() > choiceNum)
				{
					pathIn((TakeableAction) choiceOptions.get(choiceNum));
					updateChoiceOptions();
				}
				break;
			case CHANGE:
				choiceNum++;
				if(choiceNum >= choiceOptions.size())
					choiceNum = 0;
				if(choiceOptions.isEmpty())
					choiceNum = -1;
				break;
			case ACCEPT:
				return currentAction;
			case BACK:
				if(currentAction.previous != null)
					setCurrentAction(currentAction.previous);
				else
					esc = true;
				break;
			case ESCAPE:
				esc = true;
				break;
			case MINUSD:
				if(turn == null)
					turn = new HexDirection(11);
				else if(turn.r == 1)
					turn = null;
				else
					turn = HexDirection.minus(turn, eins);
				updateChoiceOptions();
				break;
			case PLUSD:
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

	public void updateChoiceOptions()
	{
		if(object != null)
			choiceOptions = objects().get(object);
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
		System.out.println(choiceOptions);
	}

	public void pathIn(TakeableAction action)
	{
		setCurrentAction(currentAction.next.stream().filter(e -> e.action == action).findFirst().orElse(currentAction));
		System.out.println("WUGU" + currentAction.action.getClass().getSimpleName());
	}

	public Map<HexDirection, List<TActionDirection>> directions()
	{
		return currentAction.next.stream().filter(e -> e instanceof TActionDirection).map(e -> (TActionDirection) e).collect(
				Collectors.groupingBy(TActionDirection::direction));
	}

	public Map<HexLocation, List<TActionLocation>> locations()
	{
		return currentAction.next.stream().filter(e -> e.action instanceof TActionLocation).map(e -> (TActionLocation) e.action).collect(
				Collectors.groupingBy(TActionLocation::location));
	}

	public Map<HexObject, List<TActionObject>> objects()
	{
		return currentAction.next.stream().filter(e -> e instanceof TActionObject).map(e -> (TActionObject) e).collect(
				Collectors.groupingBy(TActionObject::object));
	}

	public List<TActionOther> others()
	{
		return currentAction.next.stream().filter(e -> e instanceof TActionOther).map(e -> (TActionOther) e).collect(Collectors.toList());
	}
}