package visual.pather;

import aer.*;
import aer.path.*;
import aer.path.takeable.*;
import aer.resource2.*;
import aer.resource2.resourceTypes.*;
import java.util.*;
import java.util.stream.*;
import visual.map.*;

public class PathTraverse
{
	private static final HexDirection eins = new HexDirection(1);

	public VisHUD visHUD;
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
	public boolean visualUpdateRequired = false;

	public PathTraverse(List<PathAction> possibleActions, HexPather pather, boolean initial, VisHUD visHUD)
	{
		this.visHUD = visHUD;
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
		visualUpdateRequired = false;
		if(input1 == null)
			return null;
		switch(input1)
		{
			case TARGET:
				if(tObject != null && tObject != object)
				{
					chReset();
					if(tObject != pather)
						object = tObject;
					updateChoiceOptions();
					visualUpdateRequired = true;
				}
				else if(tLoc != null && !tLoc.equals(res1.dLocation()) && !tLoc.equals(loc))
				{
					chReset();
					loc = tLoc;
					updateChoiceOptions();
					visualUpdateRequired = true;
				}
				break;
			case CHOOSE:
				if(choiceNum >= 0 && choiceOptions != null && choiceOptions.size() > choiceNum)
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
				if(!chCheck() && canEnd())
				{
					/*System.out.println("Chosen path:");
					System.out.print(steps(currentAction));*/
					return currentAction;
				}
				else if(choiceNum >= 0 && choiceOptions != null && choiceOptions.size() > choiceNum)
				{
					chReset();
					pathIn((TakeableAction) choiceOptions.get(choiceNum));
					if(canEnd())
					{
						/*System.out.println("Chosen path:");
						System.out.print(steps(currentAction));*/
						return currentAction;
					}
				}
				break;
			case BACK:
				if(chCheck())
				{
					chReset();
					updateChoiceOptions();
				}
				else if(currentAction != null && currentAction.previous != null)
				{
					setCurrentAction(currentAction.previous);
					visualUpdateRequired = true;
				}
				else
				{
					//System.out.println("Exiting");
					esc = true;
				}
				break;
			case ESCAPE:
				//System.out.println("Exiting");
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
				visualUpdateRequired = true;
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
				visualUpdateRequired = true;
				break;
		}
		return null;
	}

	private boolean chCheck()
	{
		return !(object == null && loc == null && turn == null);
	}

	private void chReset()
	{
		if(chCheck())
			visualUpdateRequired = true;
		object = null;
		loc = null;
		turn = null;
	}

	public void updateChoiceOptions()
	{
		if(object != null)
		{
			/*System.out.println(object.id);
			if(!objects().keySet().isEmpty())
				System.out.println(objects().keySet().iterator().next().id);*/
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
		visualUpdateRequired = true;
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
		/*System.out.print(steps(currentAction));
		if(!chCheck() && canEnd())
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
		}*/

		visHUD.updateText(HUDMode.ACTION, "AP", String.valueOf(((BasicAPResource2) currentAction.deducted).dActionPoints()));
		visHUD.updateText(HUDMode.ACTION, "MP", String.valueOf(((BasicAPResource2) currentAction.deducted).dMovementPoints()));
		visHUD.updateText(HUDMode.ACTION, "Path", steps(currentAction));
		visHUD.updateText(HUDMode.ACTION, "Take", !chCheck() && canEnd() ? "Press enter key to take this Path" : "");
		visHUD.updateText(HUDMode.ACTION, "Target", object != null ? "Actions targeting object:" :
				loc != null ? "Actions targeting location:" : turn != null ? "Actions targeting direction:" : "Actions:");
		if(choiceOptions.isEmpty())
			visHUD.updateText(HUDMode.ACTION, "Options", "None");
		else
		{
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < choiceOptions.size(); i++)
				sb.append(i == choiceNum ? "> " : "| ").append(choiceOptions.get(i).getClass().getSimpleName()).append("\n");
			visHUD.updateText(HUDMode.ACTION, "Options", sb.toString());
		}
		visHUD.changeMode(HUDMode.ACTION);
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