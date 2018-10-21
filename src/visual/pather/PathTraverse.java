package visual.pather;

import aer.locate.*;
import aer.path.pather.*;
import aer.path.takeable.*;
import aer.relocatable.*;
import aer.resource2.resource.*;
import java.util.*;
import java.util.stream.*;
import visual.hud.*;
import visual.map.*;

public class PathTraverse
{
	private VisHUD visHUD;
	private final List<PathAction> possibleActions;
	public PathAction currentAction;
	public Pather pather;
	private R_Relocatable res1;
	public HexDirection turn;
	public HexLocation loc;
	public Relocatable object;
	private int choiceNum;
	private List choiceOptions;
	public boolean esc = false;
	public boolean visualUpdateRequired = false;

	public PathTraverse(Pather pather, VisHUD visHUD)
	{
		this.visHUD = visHUD;
		this.pather = pather;
		possibleActions = pather.getPossibleActionPaths();
		setCurrentAction(possibleActions.get(0));
	}

	private void setCurrentAction(PathAction currentAction)
	{
		this.currentAction = currentAction;
		if(currentAction.deducted instanceof R_Relocatable)
			res1 = (R_Relocatable) currentAction.deducted;
		updateChoiceOptions();
	}

	public PathAction exec(Input1 input1, HexLocation tLoc, Relocatable tObject, boolean autoTake)
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
				if(choiceNum >= 0 && choiceOptions.size() > choiceNum)
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
					return currentAction;
				}
				else if(choiceNum >= 0 && choiceOptions.size() > choiceNum)
				{
					chReset();
					pathIn((TakeableAction) choiceOptions.get(choiceNum));
					if(canEnd())
					{
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
					esc = true;
				}
				break;
			case ESCAPE:
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
					turn = HexDirection.minus(turn, 1);
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
					turn = HexDirection.plus(turn, 1);
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

	public Map<Relocatable, List<TActionObject>> objects()
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
		if(currentAction != null && currentAction.deducted instanceof Resource_AP_MP)
		{
			visHUD.updateText(1, "AP", String.valueOf(((Resource_AP_MP) currentAction.deducted).dActionPoints()));
			visHUD.updateText(1, "MP", String.valueOf(((Resource_AP_MP) currentAction.deducted).dMovementPoints()));
			visHUD.updateText(1, "Path", steps(currentAction));
		}
		else
		{
			visHUD.updateText(1, "AP", "");
			visHUD.updateText(1, "MP", "");
			visHUD.updateText(1, "Path", "");
		}
		visHUD.updateText(1, "Take", !chCheck() && canEnd() ? "Press enter key\nto take this Path" : "");
		visHUD.updateText(1, "Target", object != null ? "Actions targeting object:" :
				loc != null ? "Actions targeting location:" :
						turn != null ? "Actions targeting direction:" : "Actions:");
		if(choiceOptions.isEmpty())
			visHUD.updateText(1, "Options", "None");
		else
		{
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < choiceOptions.size(); i++)
				sb.append(i == choiceNum ? "> " : "| ").append(choiceOptions.get(i).getClass().getSimpleName()).append("\n");
			visHUD.updateText(1, "Options", sb.toString());
		}
		visHUD.changeMode(1);
	}

	private static String steps(PathAction pathAction)
	{
		List<TakeableAction> actions = new ArrayList<>();
		PathAction.pathToList(pathAction, actions);
		StringBuilder sb = new StringBuilder();
		for(TakeableAction action : actions)
			sb.append("-> ").append(action.getClass().getSimpleName()).append("\n");
		return sb.toString();
	}
}